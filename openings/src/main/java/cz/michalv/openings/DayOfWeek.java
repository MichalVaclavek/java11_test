package cz.michalv.openings;

public enum DayOfWeek {

    Po ("Pondělí"),
    Ut("Úterý"),
    Str ("Středa"),
    Ct ("Čtvrtek"),
    Pa ("Pátek"),
    So ("Sobota"),
    Ne("Neděle");

    private String dayString;

    DayOfWeek(String dayString) {
        this.dayString = dayString;
    }

    @Override
    public String toString() {
        return dayString;
    }
}
