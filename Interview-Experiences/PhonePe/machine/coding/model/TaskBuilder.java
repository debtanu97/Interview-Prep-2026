package machine.coding.model;



import machine.coding.enums.Priority;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class TaskBuilder {

    private String taskId     = UUID.randomUUID().toString();
    private String userId;
    private String title;
    private String description = "";
    private Priority priority  = Priority.MEDIUM;
    private LocalDate deadline;
    private LocalDate visibleFrom;
    private Set<String> tags  = new HashSet<>();

    public TaskBuilder taskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public TaskBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public TaskBuilder title(String title) {
        this.title = title;
        return this;
    }

    public TaskBuilder description(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public TaskBuilder deadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public TaskBuilder visibleFrom(LocalDate visibleFrom) {
        this.visibleFrom = visibleFrom;
        return this;
    }

    public TaskBuilder tag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public TaskBuilder tags(Set<String> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public Task build() {
        if (Objects.isNull(userId))
            throw new IllegalStateException("userId is required");
        if (Objects.isNull(title))
            throw new IllegalStateException("title is required");

        return new Task(taskId, userId, title, description, priority,
                        deadline, visibleFrom, tags);
    }
}
