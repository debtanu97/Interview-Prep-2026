package service;

import criteria.TaskCriteria;
import enums.ActivityType;
import enums.TaskStatus;
import log.ActivityLogEntry;
import model.Task;
import model.TimePeriod;
import repository.ActivityLogRepository;
import repository.TaskRepository;
import stats.TaskStatistics;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Design assumptions:
 * - Task promotion (FUTURE -> PENDING) is lazy: done on read (listTasks / getTask).
 * - Statistics for "spilledOver" count tasks that are PENDING/FUTURE with a
 *   deadline in the past at query time (or within the given period).
 * - The repository stores tasks irrespective of status; filtering is in-service.
 * - Activity log entries are written for every mutation.
 *
 */
public class TodoServiceImpl implements TodoService {

    private final TaskRepository taskRepository;
    private final ActivityLogRepository activityLogRepository;

    public TodoServiceImpl(TaskRepository taskRepository,
                           ActivityLogRepository activityLogRepository) {
        this.taskRepository      = taskRepository;
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) {
            throw new IllegalArgumentException("Task must not be null");
        }

        if (taskRepository.exists(task.getTaskId())) {
            throw new IllegalStateException("Task with id '" + task.getTaskId() + "' already exists");
        }

        taskRepository.save(task);

        log(task.getTaskId(), task.getUserId(), ActivityType.ADDED,
            "Task added: '" + task.getTitle() + "' [status=" + task.getStatus() + "]");
    }

    @Override
    public Optional<Task> getTask(String taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        taskOpt.ifPresent(this::promoteIfDue);
        return taskOpt;
    }

    @Override
    public void modifyTask(Task updatedTask) {
        if (Objects.isNull(updatedTask)) {
            throw new IllegalArgumentException("Task must not be null");
        }

        Task existing = taskRepository.findById(updatedTask.getTaskId())
            .orElseThrow(() -> new IllegalStateException(
                "Task '" + updatedTask.getTaskId() + "' not found"));

        assertMutable(existing);

        // Apply all mutable fields from updatedTask onto the stored entity
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setPriority(updatedTask.getPriority());
        existing.setDeadline(updatedTask.getDeadline());
        existing.setVisibleFrom(updatedTask.getVisibleFrom());
        existing.setTags(updatedTask.getTags());

        // If visibleFrom changed, re-evaluate status
        if (Objects.nonNull(updatedTask.getVisibleFrom())
                && updatedTask.getVisibleFrom().isAfter(LocalDate.now())) {
            existing.setStatus(TaskStatus.FUTURE);
        } else if (Objects.equals(existing.getStatus(), TaskStatus.FUTURE)) {
            existing.setStatus(TaskStatus.PENDING);
        }

        taskRepository.save(existing);
        log(existing.getTaskId(), existing.getUserId(), ActivityType.MODIFIED,
            "Task modified: '" + existing.getTitle() + "'");
    }

    @Override
    public void removeTask(String taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalStateException("Task '" + taskId + "' not found"));

        task.setStatus(TaskStatus.REMOVED);
        taskRepository.save(task); // keep for stats; mark as removed
        log(taskId, task.getUserId(), ActivityType.REMOVED,
            "Task removed: '" + task.getTitle() + "'");
    }

    @Override
    public void completeTask(String taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalStateException("Task '" + taskId + "' not found"));

        assertMutable(task);

        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
        log(taskId, task.getUserId(), ActivityType.COMPLETED,
            "Task completed: '" + task.getTitle() + "'");
    }


    @Override
    public List<Task> listTasks(TaskCriteria criteria) {
        // Promote all due FUTURE tasks first (lazy promotion)
        taskRepository.findAll().forEach(this::promoteIfDue);

        return taskRepository.findAll().stream()
            .filter(task -> matchesCriteria(task, criteria))
            .sorted(buildComparator(criteria))
            .collect(Collectors.toList());
    }


    @Override
    public TaskStatistics getStatistics(Optional<TimePeriod> timePeriod) {
        List<ActivityLogEntry> entries = filteredLog(timePeriod);

        int added     = countByType(entries, ActivityType.ADDED);
        int completed = countByType(entries, ActivityType.COMPLETED);
        int removed   = countByType(entries, ActivityType.REMOVED);

        int spilledOver = computeSpilledOver(entries);

        return new TaskStatistics(added, completed, removed, spilledOver);
    }


    @Override
    public List<ActivityLogEntry> getActivityLog(Optional<TimePeriod> timePeriod) {
        return filteredLog(timePeriod);
    }


    /**
     * Lazy update of task if FUTURE task visibleFrom date becomes past date
     */
    private void promoteIfDue(Task task) {
        if (task.getStatus() == TaskStatus.FUTURE
                && (task.getVisibleFrom() == null
                    || !task.getVisibleFrom().isAfter(LocalDate.now()))) {
            task.setStatus(TaskStatus.PENDING);
            taskRepository.save(task);
            log(task.getTaskId(), task.getUserId(), ActivityType.MODIFIED,
                "Task auto-promoted from FUTURE to PENDING: '" + task.getTitle() + "'");
        }
    }

    private void assertMutable(Task task) {
        if (Objects.equals(task.getStatus(), TaskStatus.COMPLETED)) {
            throw new IllegalStateException(
                "Task '" + task.getTaskId() + "' is already completed");
        }

        if (task.getStatus() == TaskStatus.REMOVED) {
            throw new IllegalStateException(
                "Task '" + task.getTaskId() + "' has been removed");
        }
    }

    private boolean matchesCriteria(Task task, TaskCriteria c) {
        // userId filter
        if (Objects.nonNull(c.getUserId()) && !c.getUserId().equals(task.getUserId())) {
            return false;
        }

        // status filter — if no statuses specified, show only PENDING
        if (c.getStatuses().isEmpty()) {
            if (task.getStatus() != TaskStatus.PENDING) return false;
        } else {
            if (!c.getStatuses().contains(task.getStatus())) return false;
        }

        // tag filter: task must contain ALL requested tags
        if (!c.getTags().isEmpty() && !task.getTags().containsAll(c.getTags())) {
            return false;
        }

        // minPriority filter (ordinal comparison: LOW=0, MEDIUM=1, HIGH=2)
        if (Objects.nonNull(c.getMinPriority())
                && task.getPriority().ordinal() < c.getMinPriority().ordinal()) {
            return false;
        }

        // deadline range filter
        if (Objects.nonNull(c.getDeadlineFrom()) && task.getDeadline() != null
                && task.getDeadline().isBefore(c.getDeadlineFrom())) {
            return false;
        }
        if (Objects.nonNull(c.getDeadlineTo()) && task.getDeadline() != null
                && task.getDeadline().isAfter(c.getDeadlineTo())) {
            return false;
        }

        // visibleOnDate: include FUTURE tasks visible on that date
        if (Objects.nonNull(c.getVisibleOnDate()) && task.getStatus() == TaskStatus.FUTURE) {
            return Objects.isNull(task.getVisibleFrom())
                    || !task.getVisibleFrom().isAfter(c.getVisibleOnDate());
        }

        return true;
    }

    /** Build a Comparator from the sort specification in criteria. */
    private Comparator<Task> buildComparator(TaskCriteria criteria) {
        Comparator<Task> comparator = switch (criteria.getSortBy()) {
            case DEADLINE ->
                // Tasks with no deadline go last
                    Comparator.comparing(Task::getDeadline,
                            Comparator.nullsLast(Comparator.naturalOrder()));
            case PRIORITY -> {
                // Higher ordinal = higher priority; descending by default for priority
                comparator = Comparator.comparingInt(t -> t.getPriority().ordinal());
                yield comparator.reversed();
            }
            case TITLE -> Comparator.comparing(Task::getTitle,
                    String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(Task::getCreatedAt);
        };

        return criteria.isAscending() ? comparator : comparator.reversed();
    }

    private void log(String taskId, String userId, ActivityType type, String desc) {
        activityLogRepository.append(new ActivityLogEntry(taskId, userId, type, desc));
    }

    private List<ActivityLogEntry> filteredLog(Optional<TimePeriod> timePeriod) {
        List<ActivityLogEntry> all = activityLogRepository.findAll();
        if (timePeriod.isEmpty()) {
            return new ArrayList<>(all);
        }

        TimePeriod period = timePeriod.get();
        return all.stream()
            .filter(e -> period.contains(e.getTimestamp()))
            .collect(Collectors.toList());
    }

    private int countByType(List<ActivityLogEntry> entries, ActivityType type) {
        return (int) entries.stream()
            .filter(e -> e.getActivityType() == type)
            .count();
    }

    /**
     * Spilled over = tasks ADDED in the observation period,
     * still exist in a non-completed state, AND their deadline has passed.

     * Assumption: We look at ADDED log entries within the period, then check
     * the current live status of those tasks.
     */
    private int computeSpilledOver(List<ActivityLogEntry> addedEntries) {
        List<String> addedTaskIds = addedEntries.stream()
            .filter(e -> e.getActivityType() == ActivityType.ADDED)
            .map(ActivityLogEntry::getTaskId)
            .toList();

        int count = 0;
        LocalDate today = LocalDate.now();
        for (String taskId : addedTaskIds) {
            Optional<Task> taskOpt = taskRepository.findById(taskId);
            if (taskOpt.isPresent()) {
                Task t = taskOpt.get();
                boolean notDone = (Objects.equals(t.getStatus(), TaskStatus.PENDING))
                        || Objects.equals(t.getStatus(), TaskStatus.FUTURE);
                boolean deadlinePassed = Objects.nonNull(t.getDeadline())
                    && t.getDeadline().isBefore(today);

                if (notDone && deadlinePassed) count++;
            }
        }
        return count;
    }
}
