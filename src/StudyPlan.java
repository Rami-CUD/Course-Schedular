import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// StudyPlan class represents a study plan for a specific major
public class StudyPlan {
    // Major name associated with the study plan
    private String majorName;
    
    // List of courses included in the study plan
    private List<Course> courses;

    // Constructor to initialize a StudyPlan with a major name and a list of courses
    public StudyPlan(String majorName, List<Course> courses) {
        this.majorName = majorName;
        this.courses = courses;
    }

    // Getter for the major name
    public String getMajorName() {
        return majorName;
    }

    // Getter for the list of courses
    public List<Course> getCourses() {
        return courses;
    }

    // Getter for an array of course names 
    public String[] getCourseNames() {
        String[] output = new String[courses.size()+1];
        output[0] = " ";
        for (int i = 0; i < output.length-1; i++) {
            output[i+1] = courses.get(i).getCourseName();
        }
        return output;
    }

    // Method to save the study plan to a file specified by the provided pathString
    public void save(String pathString) throws IOException {
        // Create a FileWriter to write to the specified file path
        FileWriter writer = new FileWriter(pathString, true);
        
        // Write the string representation of the study plan to the file
        writer.write(this.toString());
        
        // Close the FileWriter
        writer.close();
    }

    // Method to generate a string representation of the study plan
    public String toString() {
        // Initialize the output string with the major name
        String output = majorName + ":";
        
        // Iterate over each course in the list and append its details to the output string
        for (Course course : courses) {
            String courseID = course.getCourseID();
            String courseName = course.getCourseName();
            output = output + courseID + "," + courseName + ";";
        }
        
        // Add a newline character at the end of the string
        output = output + "\n";
        
        // Return the final output string
        return output;
    }
}