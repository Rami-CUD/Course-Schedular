import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Schedule class represents a schedule containing CourseSection objects
public class Schedule {
    // List to store CourseSection objects in the schedule
    private List<CourseSection> scheduleSections;
    
    // Set to store unique days of the week represented in the schedule
    private Set<DayOfWeek> days = new HashSet<>();

    // Getter for the scheduleSections list
    public List<CourseSection> getScheduleSections() {
        return scheduleSections;
    }

    // Method to calculate and update the set of unique days in the schedule
    public void calculateDays() {
        // Clear the existing set of days
        days.clear();
        
        // Iterate over each CourseSection in the schedule
        for (CourseSection section : scheduleSections) {
            // Retrieve the time ranges for the section
            MultipleTimeRange timeRanges = section.getTiming();
            
            // Iterate over each TimeRange in the section and add its day to the set
            for (TimeRange timeRange : timeRanges.getTimeRanges()) {
                days.add(timeRange.getDayOfWeek());
            }
        }
    }

    // Getter for the set of unique days in the schedule
    public Set<DayOfWeek> getDays() {
        // Calculate and return the set of days
        calculateDays();
        return days;
    }

    // Default constructor initializes an empty schedule
    public Schedule() {
        scheduleSections = new ArrayList<CourseSection>();
        days = new HashSet<>();
    }

    // Constructor that initializes the schedule with a list of CourseSection objects
    public Schedule(List<CourseSection> sections) {
        this.scheduleSections = sections;
        calculateDays();
    }

    // Method to add a CourseSection to the schedule, checking for conflicts
    public void addToSchedule(CourseSection section) {
        // Check if the provided section conflicts with the existing schedule
        if (sectionConflictsWithSchedule(section)) {
            throw new IllegalArgumentException("The provided section conflicts with the schedule.");
        }
        
        // If no conflicts, add the section to the schedule
        scheduleSections.add(section);
    }

    // Method to check if a CourseSection conflicts with the existing schedule
    public boolean sectionConflictsWithSchedule(CourseSection section) {
        // Iterate over each CourseSection in the schedule
        for (CourseSection scheduleSection : scheduleSections) {
            // Check if the provided section overlaps with any section in the schedule
            if (section.overlapsWith(scheduleSection)) {
                return true;
            }
        }
        // If no conflicts found, return false
        return false;
    }

    // Method to generate a string representation of the schedule
    public String toString() {
        String output = "Schedule:\n";
        // Iterate over each CourseSection in the schedule and append its string representation
        for (CourseSection courseSection : scheduleSections) {
            output = output + courseSection.toString() + "\n";
        }
        return output;
    }
}
