import criteria.TaskCriteria;
import enums.Priority;
import enums.SortField;
import enums.TaskStatus;
import log.ActivityLogEntry;
import model.Task;
import model.TaskBuilder;
import model.TimePeriod;
import repository.ActivityLogRepository;
import repository.InMemoryActivityLogRepository;
import repository.InMemoryTaskRepository;
import repository.TaskRepository;
import service.TodoService;
import service.TodoServiceImpl;
import stats.TaskStatistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Main {
    private static TodoService todoService;

    public static void main(String[] args) {
        TaskRepository taskRepo       = new InMemoryTaskRepository();
        ActivityLogRepository logRepo = new InMemoryActivityLogRepository();
        todoService = new TodoServiceImpl(taskRepo, logRepo);

        separator("SCENARIO 1: Alice adds tasks");

        Task aliceTask1 = new TaskBuilder()
                .taskId("T001")
                .userId("alice")
                .title("Prepare quarterly report")
                .description("Q3 financial summary for management")
                .priority(Priority.HIGH)
                .deadline(LocalDate.now().plusDays(3))
                .tag("work")
                .tag("finance")
                .build();

        Task aliceTask2 = new TaskBuilder()
                .taskId("T002")
                .userId("alice")
                .title("Buy groceries")
                .description("Milk, eggs, bread")
                .priority(Priority.LOW)
                .deadline(LocalDate.now().plusDays(1))
                .tag("personal")
                .build();

        Task aliceTask3 = new TaskBuilder()
                .taskId("T003")
                .userId("alice")
                .title("Schedule dentist appointment")
                .priority(Priority.MEDIUM)
                .tag("health")
                .tag("personal")
                .build();

        todoService.addTask(aliceTask1);
        todoService.addTask(aliceTask2);
        todoService.addTask(aliceTask3);
        print("Added 3 tasks for Alice.");

        separator("SCENARIO 2: Bob adds tasks");

        Task bobTask1 = new TaskBuilder()
                .taskId("T004")
                .userId("bob")
                .title("Code review for PR #42")
                .priority(Priority.HIGH)
                .deadline(LocalDate.now().plusDays(2))
                .tag("work")
                .tag("engineering")
                .build();

        Task bobTask2 = new TaskBuilder()
                .taskId("T005")
                .userId("bob")
                .title("Read 'Clean Code' chapter 5")
                .priority(Priority.LOW)
                .tag("learning")
                .build();

        todoService.addTask(bobTask1);
        todoService.addTask(bobTask2);
        print("Added 2 tasks for Bob.");

        separator("SCENARIO 3: Alice adds a future-scheduled task");

        Task futureTask = new TaskBuilder()
                .taskId("T006")
                .userId("alice")
                .title("Prepare annual review slides")
                .description("Visible 2 weeks from now")
                .priority(Priority.HIGH)
                .visibleFrom(LocalDate.now().plusDays(14))
                .deadline(LocalDate.now().plusDays(20))
                .tag("work")
                .build();

        todoService.addTask(futureTask);
        print("Added future task T006 (visible from: " + futureTask.getVisibleFrom() + ")");

        separator("SCENARIO 4: List Alice's active PENDING tasks (default sort: CREATED_AT)");

        TaskCriteria aliceActiveCriteria = new TaskCriteria.Builder()
                .userId("alice")
                .status(TaskStatus.PENDING)
                .build();

        printTaskList(todoService.listTasks(aliceActiveCriteria));

        separator("SCENARIO 5: Query Alice's FUTURE tasks");

        TaskCriteria aliceFutureCriteria = new TaskCriteria.Builder()
                .userId("alice")
                .status(TaskStatus.FUTURE)
                .build();

        printTaskList(todoService.listTasks(aliceFutureCriteria));

        separator("SCENARIO 6: List Alice's tasks tagged 'personal'");

        TaskCriteria tagCriteria = new TaskCriteria.Builder()
                .userId("alice")
                .status(TaskStatus.PENDING)
                .tag("personal")
                .build();

        printTaskList(todoService.listTasks(tagCriteria));

        separator("SCENARIO 7: List ALL users' HIGH priority tasks, sorted by deadline ASC");

        TaskCriteria highPriorityCriteria = new TaskCriteria.Builder()
                .status(TaskStatus.PENDING)
                .minPriority(Priority.HIGH)
                .sortBy(SortField.DEADLINE)
                .ascending(true)
                .build();

        printTaskList(todoService.listTasks(highPriorityCriteria));

        separator("SCENARIO 8: Alice modifies T002 (update title + bump priority)");

        Task modifiedT002 = new TaskBuilder()
                .taskId("T002")
                .userId("alice")
                .title("Buy groceries + household items")
                .description("Milk, eggs, bread, soap")
                .priority(Priority.MEDIUM)
                .deadline(LocalDate.now().plusDays(2)) // extended by 1 day
                .tag("personal")
                .build();

        todoService.modifyTask(modifiedT002);
        print("Modified T002.");
        todoService.getTask("T002").ifPresent(t -> print("Updated task: " + t));

        separator("SCENARIO 9: Bob completes T004 (code review)");

        todoService.completeTask("T004");
        print("T004 marked as completed.");

        TaskCriteria bobActiveCriteria = new TaskCriteria.Builder()
                .userId("bob")
                .status(TaskStatus.PENDING)
                .build();
        print("Bob's active tasks after completion:");
        printTaskList(todoService.listTasks(bobActiveCriteria));

        separator("SCENARIO 10: Alice removes T003 (dentist appointment)");

        todoService.removeTask("T003");
        print("T003 removed.");

        print("Alice's active tasks after removal:");
        printTaskList(todoService.listTasks(aliceActiveCriteria));

        separator("SCENARIO 11: Tasks with deadline in the next 2 days");

        TaskCriteria deadlineCriteria = new TaskCriteria.Builder()
                .status(TaskStatus.PENDING)
                .deadlineTo(LocalDate.now().plusDays(2))
                .sortBy(SortField.DEADLINE)
                .build();

        printTaskList(todoService.listTasks(deadlineCriteria));

        separator("SCENARIO 12: getTask(T001)");

        todoService.getTask("T001").ifPresentOrElse(
                t -> print("Found: " + t),
                ()  -> print("Task T001 not found.")
        );

        separator("SCENARIO 13: Full activity log (all time)");

        List<ActivityLogEntry> allLogs = todoService.getActivityLog(Optional.empty());
        allLogs.forEach(entry -> print("  " + entry));

        separator("SCENARIO 14: Statistics (all time)");

        TaskStatistics stats = todoService.getStatistics(Optional.empty());
        print("  " + stats);

        separator("SCENARIO 15: Activity log — last 10 seconds (narrow window demo)");

        TimePeriod recentWindow = new TimePeriod(
                LocalDateTime.now().minusSeconds(10),
                LocalDateTime.now()
        );
        List<ActivityLogEntry> recentLogs = todoService.getActivityLog(Optional.of(recentWindow));
        print("  Entries in last 10 seconds: " + recentLogs.size());
        recentLogs.forEach(entry -> print("    " + entry));
    }

    private static void separator(String label) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + label);
        System.out.println("=".repeat(60));
    }

    private static void print(String message) {
        System.out.println("  " + message);
    }

    private static void printTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("  [No tasks match the criteria]");
            return;
        }
        tasks.forEach(t -> System.out.printf(
                "  %-6s | %-8s | %-35s | %-9s | %-8s | deadline=%-12s | tags=%s%n",
                t.getTaskId(),
                t.getUserId(),
                t.getTitle(),
                t.getStatus(),
                t.getPriority(),
                t.getDeadline() != null ? t.getDeadline().toString() : "none",
                t.getTags()
        ));
    }
}