package machine.coding.repository;


import machine.coding.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    void save(Task task);

    Optional<Task> findById(String taskId);

    Collection<Task> findAll();

    void delete(String taskId);

    boolean exists(String taskId);
}
