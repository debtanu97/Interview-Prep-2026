package machine.coding.stats;

/**
 * Aggregated statistics for a given time period.
 */
public class TaskStatistics {

    private final int totalAdded;
    private final int totalCompleted;
    private final int totalRemoved;
    private final int totalSpilledOver;

    public TaskStatistics(int totalAdded, int totalCompleted, int totalRemoved, int totalSpilledOver) {
        this.totalAdded       = totalAdded;
        this.totalCompleted   = totalCompleted;
        this.totalRemoved     = totalRemoved;
        this.totalSpilledOver = totalSpilledOver;
    }

    public int getTotalAdded() {
        return totalAdded;
    }
    public int getTotalCompleted() {
        return totalCompleted;
    }

    public int getTotalRemoved() {
        return totalRemoved;
    }

    public int getTotalSpilledOver() {
        return totalSpilledOver;
    }

    @Override
    public String toString() {
        return String.format(
            "Statistics { added=%d, completed=%d, removed=%d, spilledOver=%d }",
            totalAdded, totalCompleted, totalRemoved, totalSpilledOver
        );
    }
}
