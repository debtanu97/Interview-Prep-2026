package machine.coding.repository;


import machine.coding.model.Task;

import java.util.*;

public class InMemoryTaskRepository implements TaskRepository {

    // taskId -> Task
    private final Map<String, Task> store = new HashMap<>();

    @Override
    public void save(Task task) {
        store.put(task.getTaskId(), task);
    }

    @Override
    public Optional<Task> findById(String taskId) {
        return Optional.ofNullable(store.get(taskId));
    }

    @Override
    public Collection<Task> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    @Override
    public void delete(String taskId) {
        store.remove(taskId);
    }

    @Override
    public boolean exists(String taskId) {
        return store.containsKey(taskId);
    }
}
