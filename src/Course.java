// Represents a basic Course with a course ID and course name
public class Course {
    private String courseID;
    private String courseName;

    // Constructor to initialize a Course with a given course ID and course name
    public Course(String courseID, String courseName) {
        this.courseID = courseID;
        this.courseName = courseName;
    }

    // Getter method to retrieve the course name
    public String getCourseName() {
        return courseName;
    }

    // Getter method to retrieve the course ID
    public String getCourseID() {
        return courseID;
    }

    // Override the toString method to provide a string representation of the Course
    public String toString() {
        return "Course: " + courseID + ", " + courseName;
    }
}

// Represents a specific section of a Course with additional timing information
class CourseSection extends Course {
    private String section;
    private MultipleTimeRange timing;

    // Constructor for CourseSection with timing specified as a MultipleTimeRange
    public CourseSection(String courseID, String courseName, String section, MultipleTimeRange timing) {
        super(courseID, courseName);
        this.section = section;
        this.timing = timing;
    }

    // Constructor for CourseSection with timing specified as an array of TimeRange
    public CourseSection(String courseID, String courseName, String section, TimeRange[] timing) {
        super(courseID, courseName);
        this.section = section;
        // Create a MultipleTimeRange from the array of TimeRange
        this.timing = new MultipleTimeRange(timing);
    }

    // Getter method to retrieve the section
    public String getSection() {
        return section;
    }

    // Getter method to retrieve the timing information
    public MultipleTimeRange getTiming() {
        return timing;
    }

    // Override the toString method to provide a string representation of the CourseSection
    public String toString() {
        return getCourseID() + "," + getCourseName() + "," + section + "," + timing.toString();
    }

    // Method to check if the timing of this CourseSection overlaps with another CourseSection
    public boolean overlapsWith(CourseSection other) {
        return this.timing.overlapsWith(other.timing);
    }
}
