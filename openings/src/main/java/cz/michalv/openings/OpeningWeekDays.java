package cz.michalv.openings;

import java.util.*;

public class OpeningWeekDays {

    /**
     * Every day must be unique
     */
    private final SortedSet<OpeningDay> openings = new TreeSet<>();

    /**
     * Builder for OpeningWeekDays
     */
    public static class Builder {

        private final Set<OpeningDay.Builder> openingDayBuilders;

        public Builder() {
            openingDayBuilders = new HashSet<>();
        }

        public OpeningDay.Builder addOpeningDays(DayOfWeek day) {
            var newOpeningDayBuilder = new OpeningDay.Builder(this, day);
            openingDayBuilders.add(newOpeningDayBuilder);
            return newOpeningDayBuilder;
        }

        public OpeningDay.Builder addOpeningDays(DayOfWeek... days) {
            var newOpeningDayBuilder = new OpeningDay.Builder(this, days);
            openingDayBuilders.add(newOpeningDayBuilder);
            return newOpeningDayBuilder;
        }

        public OpeningWeekDays build() {
            return new OpeningWeekDays(this);
        }

    }


    /**
     * If the same day already entered, remove it, and replace by newly entered day
     *
     * @param builder
     */
    private OpeningWeekDays(OpeningWeekDays.Builder builder) {

        for (OpeningDay.Builder odBuilder : builder.openingDayBuilders) {
            OpeningDay odb = odBuilder.build();
            openings.add(odb);
        }
    }


    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder();
        openings.forEach(od -> retVal.append(od).append("\r\n"));
        return retVal.toString();
    }
}
