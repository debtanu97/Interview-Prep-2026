package machine.coding.service;


import machine.coding.criteria.TaskCriteria;
import machine.coding.log.ActivityLogEntry;
import machine.coding.model.Task;
import machine.coding.model.TimePeriod;
import machine.coding.stats.TaskStatistics;

import java.util.List;
import java.util.Optional;

public interface TodoService {

    void addTask(Task task);

    Optional<Task> getTask(String taskId);

    void modifyTask(Task updatedTask);

    void removeTask(String taskId);

    void completeTask(String taskId);

    /**
     * List tasks matching the given criteria, sorted as specified.
     * Automatically promotes FUTURE tasks to PENDING if their visibleFrom
     * date has arrived.
     */
    List<Task> listTasks(TaskCriteria criteria);

    /**
     * Get statistics for the given time period (or all time if null).
     */
    TaskStatistics getStatistics(Optional<TimePeriod> timePeriod);

    /**
     * Get the activity log for the given time period (or all time if null).
     */
    List<ActivityLogEntry> getActivityLog(Optional<TimePeriod> timePeriod);
}
