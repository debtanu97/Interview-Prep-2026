package machine.coding.model;



import machine.coding.enums.Priority;
import machine.coding.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Assumptions:
 * - A task belongs to exactly one user.
 * - Tags are case-insensitive strings (stored lowercase).
 * - visibleFrom determines when a "future" task appears in the active list.
 *   If null, the task is immediately visible.
 * - Once status becomes COMPLETED or REMOVED, it no longer appears in active queries.
 */
public class Task {

    private final String taskId;
    private final String userId;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDate deadline;         // Optional deadline
    private LocalDate visibleFrom;      // Optional future visibility date
    private Set<String> tags;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String taskId,
                String userId,
                String title,
                String description,
                Priority priority,
                LocalDate deadline,
                LocalDate visibleFrom,
                Set<String> tags) {
        this.taskId    = taskId;
        this.userId    = userId;
        this.title     = title;
        this.description = description;
        this.priority  = priority;
        this.deadline  = deadline;
        this.visibleFrom = visibleFrom;
        this.tags      = normalizeTags(tags);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Decide initial status based on visibleFrom
        if (visibleFrom != null && visibleFrom.isAfter(LocalDate.now())) {
            this.status = TaskStatus.FUTURE;
        } else {
            this.status = TaskStatus.PENDING;
        }
    }

    public String getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public LocalDate getVisibleFrom() {
        return visibleFrom;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public void setTitle(String title) {
        this.title = title;
        touch();
    }

    public void setDescription(String description) {
        this.description = description;
        touch();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        touch();
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        touch();
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
        touch();
    }

    public void setVisibleFrom(LocalDate visibleFrom) {
        this.visibleFrom = visibleFrom;
        touch();
    }

    public void setTags(Set<String> tags) {
        this.tags = normalizeTags(tags);
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    private Set<String> normalizeTags(Set<String> raw) {
        if (raw == null) return new HashSet<>();
        Set<String> normalized = new HashSet<>();
        for (String tag : raw) {
            if (tag != null && !tag.isBlank()) {
                normalized.add(tag.trim().toLowerCase());
            }
        }
        return normalized;
    }

    @Override
    public String toString() {
        return String.format(
            "Task{id='%s', user='%s', title='%s', status=%s, priority=%s, deadline=%s, visibleFrom=%s, tags=%s}",
            taskId, userId, title, status, priority, deadline, visibleFrom, tags
        );
    }
}
