package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class TimePeriod {

    private final LocalDateTime from;
    private final LocalDateTime to;

    public TimePeriod(LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) || Objects.isNull(to)) {
            throw new IllegalArgumentException("TimePeriod bounds must not be null");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' must not be after 'to'");
        }
        this.from = from;
        this.to   = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }
    public LocalDateTime getTo() {
        return to;
    }

    public boolean contains(LocalDateTime timestamp) {
        return !timestamp.isBefore(from) && !timestamp.isAfter(to);
    }

    @Override
    public String toString() {
        return String.format("TimePeriod[%s -> %s]", from, to);
    }
}
