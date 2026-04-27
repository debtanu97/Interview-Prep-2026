package machine.coding.repository;

import machine.coding.log.ActivityLogEntry;

import java.util.List;

public interface ActivityLogRepository {

    void append(ActivityLogEntry entry);

    List<ActivityLogEntry> findAll();
}
