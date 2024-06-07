import java.time.LocalTime;
import java.time.DayOfWeek;

// TimeRange class represents a range of time on a specific day of the week
public class TimeRange {
    private LocalTime start;
    private LocalTime end;
    private DayOfWeek dayOfWeek;

    // Constructor to initialize TimeRange with start time, end time, and day of the
    // week
    public TimeRange(LocalTime start, LocalTime end, DayOfWeek dayOfWeek) {
        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
    }

    // Getter method to retrieve start time
    public LocalTime getStart() {
        return start;
    }
    
    // Getter method to retrieve end time
    public LocalTime getEnd() {
        return end;
    }

    // Getter method to retrieve the day of the week
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    // Static method to parse a string into a TimeRange object
    public static TimeRange parseToTimeRange(String s) {
        // Split the input string into components
        String[] splitString = s.split(" ");
        String[] startTimeString = splitString[0].split("-");
        LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeString[0]), Integer.parseInt(startTimeString[1]));
        String[] endTimeString = splitString[1].split("-");
        LocalTime endTime = LocalTime.of(Integer.parseInt(endTimeString[0]), Integer.parseInt(endTimeString[1]));
        DayOfWeek day = DayOfWeek.of(Integer.parseInt(splitString[2]));

        // Return a new TimeRange object based on the parsed values
        return new TimeRange(startTime, endTime, day);
    }

    // Method to check if the current TimeRange overlaps with another TimeRange
    public boolean overlaps(TimeRange other) {
        // If they days are different, then there is no overlap
        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }

        // Check for actual time overlap
        return !this.end.isBefore(other.start) && !this.start.isAfter(other.end.minusSeconds(1));
        // We subtract one second so that cases where the end of one range is the same
        // as the start of another, it doesn't count as overlapping
        // Ex: Range1 is 12:00 to 1:30 and Range2 is 1:30 to 2:00, the method should
        // return false
    }

    // Method to convert the TimeRange object to a string
    public String toString() {
        String startHours = Integer.toString(start.getHour());
        String startMinutes = Integer.toString(start.getMinute());
        String endHours = Integer.toString(end.getHour());
        String endMinutes = Integer.toString(end.getMinute());
        String dayIntAsString = Integer.toString(dayOfWeek.getValue());
        return startHours + "-" + startMinutes + " " + endHours + "-" + endMinutes + " " + dayIntAsString;
    }
}

// MultipleTimeRange class represents a collection of TimeRange objects
class MultipleTimeRange {
    private TimeRange[] timeRanges;

    // Constructor to initialize MultipleTimeRange with an array of TimeRange
    // objects
    public MultipleTimeRange(TimeRange[] timeRanges) {
        this.timeRanges = timeRanges.clone(); // clone to ensure immutability
    }

    // Getter method to retrieve the array of TimeRange objects
    public TimeRange[] getTimeRanges() {
        return timeRanges;
    }

    // Method to check if the current MultipleTimeRange overlaps with another
    // MultipleTimeRange
    public boolean overlapsWith(MultipleTimeRange other) {
        for (TimeRange thisTimeRange : this.timeRanges) {
            for (TimeRange otherTimeRange : other.getTimeRanges()) {
                if (thisTimeRange.overlaps(otherTimeRange)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to convert the MultipleTimeRange object to a string
    public String toString() {
        String output = "";
        int lastIndex = timeRanges.length - 1;

        // Iterate over each TimeRange and append its string representation to the
        // output
        for (int i = 0; i < timeRanges.length; i++) {
            output = output + timeRanges[i].toString();
            if (i != lastIndex) {
                output = output + ","; // Add a comma if it's not the last TimeRange
            }
        }
        return output;
    }
}
