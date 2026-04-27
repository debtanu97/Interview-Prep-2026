package criteria;

import enums.Priority;
import enums.SortField;
import enums.TaskStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TaskCriteria {

    private final String userId;
    private final Set<TaskStatus> statuses;
    private final Set<String> tags;
    private final Priority minPriority;
    private final LocalDate deadlineFrom;
    private final LocalDate deadlineTo;
    private final LocalDate visibleOnDate;

    private final SortField sortBy;
    private final boolean ascending;

    private TaskCriteria(Builder builder) {
        this.userId        = builder.userId;
        this.statuses      = Collections.unmodifiableSet(builder.statuses);
        this.tags          = Collections.unmodifiableSet(builder.tags);
        this.minPriority   = builder.minPriority;
        this.deadlineFrom  = builder.deadlineFrom;
        this.deadlineTo    = builder.deadlineTo;
        this.visibleOnDate = builder.visibleOnDate;
        this.sortBy        = builder.sortBy;
        this.ascending     = builder.ascending;
    }

    public String getUserId() {
        return userId;
    }

    public Set<TaskStatus> getStatuses() {
        return statuses;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Priority getMinPriority() {
        return minPriority;

    }

    public LocalDate getDeadlineFrom() {
        return deadlineFrom;
    }

    public LocalDate getDeadlineTo() {
        return deadlineTo;
    }

    public LocalDate getVisibleOnDate() {
        return visibleOnDate;
    }

    public SortField getSortBy() {
        return sortBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public static class Builder {
        private String userId;
        private Set<TaskStatus> statuses = EnumSet.noneOf(TaskStatus.class);
        private Set<String> tags = new HashSet<>();
        private Priority minPriority;
        private LocalDate deadlineFrom;
        private LocalDate deadlineTo;
        private LocalDate visibleOnDate;
        private SortField sortBy = SortField.CREATED_AT;
        private boolean ascending = true;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.statuses.add(status);
            return this;
        }

        public Builder statuses(Set<TaskStatus> statuses) {
            this.statuses.addAll(statuses);
            return this;
        }

        public Builder tag(String tag) {
            if (tag != null) this.tags.add(tag.trim().toLowerCase());
            return this;
        }

        public Builder tags(Set<String> tags) {
            if (tags != null) tags.forEach(this::tag);
            return this;
        }

        public Builder minPriority(Priority minPriority) {
            this.minPriority = minPriority;
            return this;
        }

        public Builder deadlineFrom(LocalDate deadlineFrom) {
            this.deadlineFrom = deadlineFrom;
            return this;
        }

        public Builder deadlineTo(LocalDate deadlineTo) {
            this.deadlineTo = deadlineTo;
            return this;
        }

        /** Include FUTURE tasks whose visibleFrom is on or before this date. */
        public Builder visibleOnDate(LocalDate date) {
            this.visibleOnDate = date;
            return this;
        }

        public Builder sortBy(SortField sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public Builder ascending(boolean ascending) {
            this.ascending = ascending;
            return this;
        }

        public TaskCriteria build() {
            return new TaskCriteria(this);
        }
    }
}
