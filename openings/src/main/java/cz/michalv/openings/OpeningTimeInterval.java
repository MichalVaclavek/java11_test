package cz.michalv.openings;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

/**
 * Basic opening time interval: contains start: time From, opening time
 * end: time to, closing time
 */
public class OpeningTimeInterval implements Comparable<OpeningTimeInterval> {

    public static final String CLOSED = "ZAVŘENO";

    private final LocalTime start;
    private final LocalTime end;

    private final LocalTime difference;

    /**
     * @param start the beginning of the period
     * @param end the end of the period; must not precede start
     * @throws IllegalArgumentException if start is after end
     * @throws NullPointerException if start or end is null
     * @throws java.time.format.DateTimeParseException if start or end cannot be parsed
     */
    private OpeningTimeInterval(String start, String end) {
        this.start = LocalTime.parse(start);
        this.end = LocalTime.parse(end);
        if (this.start.isAfter(this.end))
            throw new IllegalArgumentException(this.start + " after " + this.end);
        difference = diff();
    }

    public static OpeningTimeInterval of(String start, String end) {
        return new  OpeningTimeInterval(start, end);
    }

    public String start() {
        return this.start.toString();
    }

    public String end() {
        return this.end.toString();
    }

    public LocalTime startTime() {
        return this.start;
    }

    public LocalTime endTime() {
        return this.end;
    }

    /**
     * Difference betwenn end and start as LocalTime
     * @return
     */
    private LocalTime diff() {
        return this.end.minusHours(this.start.getHour()).minusMinutes(this.start.getMinute());
    }

    public LocalTime differenceTime() {
        return difference;
    }

    public String difference() {
        return difference.toString();
    }

    /**
     * Check if current time is within this time interval specified by start and end
     * @return
     */
    public boolean isNowInInterval() {
        var now = LocalTime.now();
        return start.isBefore(now) && end.isAfter(now);
    }

    /**
     * Check if given time is within this time interval specified by start and end
     * @return
     */
    public boolean isTimeInInterval(LocalTime time) {
        return start.isBefore(time) && end.isAfter(time);
    }

    /**
     * Checks if intervals overlaps
     * @return
     */
    public boolean intervalOverlaps(OpeningTimeInterval interval) {
        return (isTimeInInterval(interval.startTime()) || isTimeInInterval(interval.endTime()))
                || (interval.isTimeInInterval(this.startTime()) || interval.isTimeInInterval(this.endTime()));
    }


    @Override
    public String toString() {
        return start() + " - " + end();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpeningTimeInterval interval = (OpeningTimeInterval) o;
        return start.equals(interval.start) && end.equals(interval.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public int compareTo(OpeningTimeInterval o) {
        Comparator<OpeningTimeInterval> intervalComparator = Comparator.comparing(OpeningTimeInterval::startTime);
        return intervalComparator.compare(this, o);
    }
}
