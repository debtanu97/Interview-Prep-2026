package machine.coding.log;



import machine.coding.enums.ActivityType;

import java.time.LocalDateTime;

public class ActivityLogEntry {

    private final String taskId;
    private final String userId;
    private final ActivityType activityType;
    private final String description;
    private final LocalDateTime timestamp;

    public ActivityLogEntry(String taskId,
                            String userId,
                            ActivityType activityType,
                            String description) {
        this.taskId       = taskId;
        this.userId       = userId;
        this.activityType = activityType;
        this.description  = description;
        this.timestamp    = LocalDateTime.now();
    }

    public String getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | User: %s | Task: %s | %s",
            timestamp, activityType, userId, taskId, description);
    }
}
