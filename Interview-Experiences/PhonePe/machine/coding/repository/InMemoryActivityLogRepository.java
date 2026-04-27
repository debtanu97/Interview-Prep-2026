package machine.coding.repository;

import machine.coding.log.ActivityLogEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryActivityLogRepository implements ActivityLogRepository {

    private final List<ActivityLogEntry> log = new ArrayList<>();

    @Override
    public void append(ActivityLogEntry entry) {
        log.add(entry);
    }

    @Override
    public List<ActivityLogEntry> findAll() {
        return Collections.unmodifiableList(log);
    }
}
