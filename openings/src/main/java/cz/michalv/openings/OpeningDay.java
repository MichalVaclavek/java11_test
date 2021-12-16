package cz.michalv.openings;


import java.util.*;

/**
 * Represents one or more days of week with opening time intervals.
 * If more days, than they have same opening TimeHours intervals
 */
public class OpeningDay implements Comparable<OpeningDay> {

    /**
     * More days of week with same opening TimeHours intervals
     */
    private final SortedSet<DayOfWeek> daysOfWeek; // when the same Opening hours apply to more days

    /**
     * Opening TimeHours intervals belonging either to one {@code dayOfWeek} or
     * to set of {@code daysOfWeek}
     */
    private final SortedSet<OpeningTimeInterval> openingTimeIntervals;

    /**
     * Builder for OpeningDay
     */
    public static class Builder {

        private final OpeningWeekDays.Builder parentBuilder;

        private SortedSet<DayOfWeek> daysOfWeek; // when the same Opening hours apply to more days

        private final SortedSet<OpeningTimeInterval> openingTimeIntervals = new TreeSet<>();

        public Builder(OpeningWeekDays.Builder parentBuilder) {
            super();
            this.parentBuilder = parentBuilder;
        }

        public Builder(OpeningWeekDays.Builder parentBuilder, DayOfWeek... daysOfWeek) {
            this(parentBuilder);
            this.daysOfWeek = new TreeSet<>(Arrays.asList(daysOfWeek));
        }

        public OpeningDay.Builder addOpeningTimeInterval(OpeningTimeInterval timeInterval) {
            // Checks if builder.openingTimeIntervals are not overlapping already entered interval
            // If so, ignores it
            if (this.openingTimeIntervals.stream().noneMatch(ti -> ti.intervalOverlaps(timeInterval))) {
                this.openingTimeIntervals.add(timeInterval);
            }
            return this;
        }

        public OpeningDay.Builder setClosed() {
            // Checks if builder.openingTimeIntervals are not overlapping already entered interval
            // If so, ignores it
            openingTimeIntervals.clear();
            return this;
        }


        public OpeningDay build() {
            return new OpeningDay(this);
        }

        public OpeningWeekDays.Builder and() {
            return parentBuilder;
        }

    }

    /** OpeningDay class **/

    private OpeningDay(Builder builder) {
        this.daysOfWeek = builder.daysOfWeek;
        this.openingTimeIntervals = builder.openingTimeIntervals;
    }


    public SortedSet<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Checks. if the input OpeningDay attributes {@code dayOfWeek} or {@code daysOfWeek}
     * are included in this OpeningDay instance.
     *
     * @param day
     * @return
     */
    public boolean isAlreadyIncluded(OpeningDay day) {
        if (this.daysOfWeek != null) {
            return daysOfWeek.stream().anyMatch(dayOfWeek1 -> (day.getDaysOfWeek() != null && day.getDaysOfWeek().contains(dayOfWeek1)));
        }

        return false;
    }

    @Override
    public int compareTo(OpeningDay o) {
        if (this.daysOfWeek != null && o.daysOfWeek != null) {
            return this.daysOfWeek.first().compareTo(o.daysOfWeek.first());
        }
        return 0;
    }

    @Override
    public String toString() {
        if (daysOfWeek != null) {
            return daysOfWeek + ": "  + (openingTimeIntervals.isEmpty() ? OpeningTimeInterval.CLOSED : openingTimeIntervals);
        }
        return "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpeningDay that = (OpeningDay) o;
        return isAlreadyIncluded(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(daysOfWeek, openingTimeIntervals);
    }
}
