import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.DayOfWeek;

public class CourseOfferingsParser {

    // Map to convert characters representing weekdays to DayOfWeek enum
    private static final Map<Character, DayOfWeek> WEEKDAYS;
    static {
        WEEKDAYS = new HashMap<>();
        WEEKDAYS.put('M', DayOfWeek.MONDAY);
        WEEKDAYS.put('T', DayOfWeek.TUESDAY);
        WEEKDAYS.put('W', DayOfWeek.WEDNESDAY);
        WEEKDAYS.put('R', DayOfWeek.THURSDAY);
        WEEKDAYS.put('F', DayOfWeek.FRIDAY);

    }

    /* Stores the given course section info in a CourseSection object then adds it to the map
    If the course ID already exists in the map, simply add the section
    Otherwise, add a new key and attach the new section in a new list */
    private void addCurrentSectionToMap(Map<String, List<CourseSection>> map, String code, String name, String section, List<TimeRange> timings) {
        TimeRange[] timingsArray = timings.toArray(new TimeRange[0]);
        timings.clear(); // reset the timings list
        CourseSection courseSection = new CourseSection(code, name, section, timingsArray);
        if (!map.containsKey(code)) {
            List<CourseSection> secs = new ArrayList<CourseSection>();
            secs.add(courseSection);
            map.put(code, secs);
        } else {
            map.get(code).add(courseSection);
        }
    }

    // Main method for parsing input lines and writing to an output file
    public void parseAndSendTo(String[] inputLines, String outputPathString) throws IOException {
        String currentCourseCode = "";
        String currentCourseName = "";
        String currentSection = "";
        List<TimeRange> currentTimings = new ArrayList<TimeRange>();
        Map<String, List<CourseSection>> courseToSectionsMap = new HashMap<>();
        for (int i = 0; i < inputLines.length; i++) {
            int codeLengthOffset = returnCodeLengthOffset(inputLines[i]);
            boolean startsWithCourseCode = codeLengthOffset >= 0; 
            if (startsWithCourseCode) {
                if (!currentCourseCode.isEmpty() && !currentTimings.isEmpty()) {
                    addCurrentSectionToMap(courseToSectionsMap, currentCourseCode, currentCourseName, currentSection, currentTimings);
                }
                String[] splitLine = inputLines[i].split("\t");
                currentCourseCode = splitLine[0].substring(0, 6 + codeLengthOffset);
                currentSection = splitLine[0].substring(6 + codeLengthOffset);
                currentCourseName = splitLine[1];
            }

            if (inputLines[i].contains(":")) {
                String[] splitLine = inputLines[i].split("\t");
                char[] days = splitLine[2].toCharArray();
                LocalTime startTime = parseStringToLocalTime(splitLine[4]);
                LocalTime endTime = parseStringToLocalTime(splitLine[5]);
                for (char dayChar : days) {
                    if (dayChar == 'N' || dayChar == '\\' || dayChar == 'A') {
                        continue;
                    } 
                    DayOfWeek day = WEEKDAYS.get(dayChar);
                    TimeRange t = new TimeRange(startTime, endTime, day);
                    currentTimings.add(t);
                }
            }
        }
        addCurrentSectionToMap(courseToSectionsMap, currentCourseCode, currentCourseName, currentSection, currentTimings);
        FileWriter writer = new FileWriter(outputPathString, true);
        CourseSectionsReader reader = new CourseSectionsReader(Paths.get(outputPathString));
        Map<String, CourseSection[]> savedSections = reader.returnAllSections();
        for (Map.Entry<String, List<CourseSection>> courseAndSection : courseToSectionsMap.entrySet()) {
            // To avoid duplication, if sections already exist in the file it won't be added again
            if (savedSections.containsKey(courseAndSection.getKey())) {
                continue;
            }
            String line = courseAndSection.getKey() + ":";
            for (CourseSection section : courseAndSection.getValue()) {
                line = line + section.toString() + ";";
            }
            writer.write(line + "\n");
        }
        writer.close();
    }

    // Method to parse a string representation of time to LocalTime object
    private LocalTime parseStringToLocalTime(String s) {
        String[] splitString = s.split(":");
        int hour = Integer.parseInt(splitString[0]);
        int minutes = Integer.parseInt(splitString[1]);
        if (splitString[2].contains("PM") && hour != 12) {
            hour += 12;
        }
        return LocalTime.of(hour, minutes);
    }

    // Checks if a given String with three Capital Letters and 3 numbers
    private int returnCodeLengthOffset(String line) {
        boolean isThreeLetterCode = line.substring(0, 6).matches("^[A-Z]{2,}[0-9]{3}$"); 
        boolean isFourLetterCode = line.substring(0, 7).matches("^[A-Z]{2,}[0-9]{3}$"); 
        if (isThreeLetterCode){
            return 0;
        }
        else if (isFourLetterCode) {
            return 1;
        }
        return -1;
    }

}