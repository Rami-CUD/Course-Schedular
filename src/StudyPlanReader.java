import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// StudyPlanReader class is responsible for processing StudyPlan objects from a file
// Assumes the file has already been parsed by the StudyPlanParser
public class StudyPlanReader extends FileReader {

    // Constructor to initialize StudyPlanReader with the file path
    public StudyPlanReader(Path filePath) {
        super(filePath);
    }

    // Method to return an array of all the StudyPlans in file
    public StudyPlan[] returnAllStudyPlans() throws IOException {
        // Retrieve all lines from the file
        List<String> allLines = getAllLines();
        
        // Create an array to store StudyPlan objects
        StudyPlan[] output = new StudyPlan[allLines.size()];
        
        // Iterate over each line and process it into a StudyPlan object
        for (int i = 0; i < output.length; i++) {
            String line = allLines.get(i);
            output[i] = processStringToStudyPlan(line);
        }
        
        // Return the array of StudyPlan objects
        return output;
    }

    // Method to process a string into a StudyPlan object
    private StudyPlan processStringToStudyPlan(String line) {
        // Split the line into major and courses
        String[] majorCoursesArray = line.split(":");
        String major = majorCoursesArray[0];
        
        // Split unprocessed courses into an array
        String[] unprocessedCoursesArray = majorCoursesArray[1].split(";");
        
        // List to store Course objects
        List<Course> coursesList = new ArrayList<Course>();
        
        // Iterate over unprocessed courses, split, and create Course objects
        for (String unprocessedCourseString : unprocessedCoursesArray) {
            String[] courseNameAndCode = unprocessedCourseString.split(",");
            Course course = new Course(courseNameAndCode[0], courseNameAndCode[1]);
            coursesList.add(course);
        }
        
        // Return a new StudyPlan object with the processed major and courses
        return new StudyPlan(major, coursesList);
    }

    // Method to return one StudyPlan object for a specific target major
    public StudyPlan returnStudyPlan(String targetMajor) throws IOException, MajorNotFoundException {
        // Retrieve all lines from the file
        List<String> allLines = getAllLines();
        
        // Iterate over each line to find the StudyPlan for the target major
        for (String line : allLines) {
            String[] majorCoursesArray = line.split(":");
            String currentMajor = majorCoursesArray[0];
            
            // If the current major matches the target major, return the corresponding StudyPlan
            if (currentMajor.equals(targetMajor)) {
                return processStringToStudyPlan(line);
            }
        }

        // If the loop completes without finding the major, throw MajorNotFoundException
        throw new MajorNotFoundException(targetMajor);
    }

}
