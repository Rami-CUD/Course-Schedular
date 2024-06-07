import java.util.ArrayList;
import java.util.List;

// StudyPlanParser class is responsible for parsing study plan information
public class StudyPlanParser {

    // Method to parse a study plan from an array of lines
    public StudyPlan parseStudyPlan(String[] linesList) {
        // Find the major from the array of lines
        String major = findMajor(linesList);
        
        // List to store parsed courses
        List<Course> coursesList = new ArrayList<Course>();
        
        // List to track encountered course IDs to avoid duplicates
        List<String> courseIDs = new ArrayList<String>();
        
        // Iterate over each line in the array
        for (String line : linesList) {
            // Split the line into parts based on tab character
            String[] parts = line.split("\t");
            
            // Iterate over each part in the line
            for (int i = 0; i < parts.length; i++) {
                // Check if the current part is a valid course code
                if (isCourseCode(parts[i])) {
                    // Construct the complete course ID by combining the text part with the number part
                    String courseID = parts[i] + parts[i + 1];
                    
                    // Ignore duplicates
                    if (courseIDs.contains(courseID)) {
                        continue;
                    }
                    
                    // Add the course ID to the list to track duplicates
                    courseIDs.add(courseID);
                    
                    // Extract course name from the next part
                    String courseName = parts[i + 2];
                    
                    // Create a new Course object and add it to the list
                    Course course = new Course(courseID, courseName);
                    coursesList.add(course);
                    
                    // Increment the index to skip the next two parts as they are already processed
                    i += 2;
                }
            }
        }
        
        // Return a new StudyPlan object with the parsed major and courses
        return new StudyPlan(major, coursesList);
    }

    // Helper method to check if a string is a valid course code
    private Boolean isCourseCode(String s) {
        return s.matches("[A-Z]{3}"); // Regular expression that checks if the string consists of three captial letters
    }

    // Helper method to find the major from the array of lines
    private String findMajor(String[] list) {
        // Iterate over each line in the array
        for (String line : list) {
            // Get the first word of the line
            String firstWord = line.split(" ")[0];
            
            // Check if the first word indicates a major (Bachelor or Master)
            if (firstWord.equals("Bachelor") || firstWord.equals("Master")) {
                return line;
            }
        }
        
        // If no major is found, return an empty string
        return "";
    }
}
