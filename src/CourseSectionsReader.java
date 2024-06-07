import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* CourseSectionsReader extends FileReader to read and process course section information from a file
File is assumed to already be processed use the CourseOfferingsParser */
public class CourseSectionsReader extends FileReader {

    // Constructor that initalizes the filePath property of the parent class
    public CourseSectionsReader(Path filePath) {
        super(filePath);
    }

    // Method to read all course sections from the file and return them as a map
    public Map<String, CourseSection[]> returnAllSections() throws IOException {
        Map<String, CourseSection[]> output = new HashMap<>();
        // Get all lines from the file
        List<String> allLines = getAllLines();
        for (String line : allLines) {
            // Split the line into course code and section information
            String[] splitLine = line.split(":");
            String courseCode = splitLine[0];
            // Convert the section information string to an array of CourseSection objects
            CourseSection[] sections = returnSectionsArrayFromString(splitLine[1]);
            // Put the course code and corresponding sections into the output map
            output.put(courseCode, sections);
        }
        return output;
    }

    // Private helper method to convert a section information string to an array of CourseSection objects
    private CourseSection[] returnSectionsArrayFromString(String line) {
        List<CourseSection> outputList = new ArrayList<>();
        // Split the multiple sections in the string into individual section strings
        String[] sectionStrings = line.split(";");
        for (String sectionString : sectionStrings) {
            // Split each section string into its parts (course code, name, section, timings)
            String[] sectionParts = sectionString.split(",");
            String courseCode = sectionParts[0];
            String courseName = sectionParts[1];
            String courseSection = sectionParts[2];
            List<TimeRange> timeRanges = new ArrayList<>();
            // Could have more than one TimeRange so we splice the parts array and
            // loop over the remaining elements, converting them to TimeRange one by one
            String[] timingStrings = Arrays.copyOfRange(sectionParts, 3, sectionParts.length);
            for (String timingString : timingStrings) {
                timeRanges.add(TimeRange.parseToTimeRange(timingString));
            }
            // Convert the list of time ranges to an array and create a new CourseSection object
            TimeRange[] timeRangesArray = timeRanges.toArray(new TimeRange[0]);
            outputList.add(new CourseSection(courseCode, courseName, courseSection, timeRangesArray));
        }
        // Convert the list of CourseSection objects to an array and return
        return outputList.toArray(new CourseSection[0]);
    }
}