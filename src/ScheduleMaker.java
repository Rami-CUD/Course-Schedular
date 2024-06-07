import java.util.ArrayList;
import java.util.List;

// ScheduleMaker class generates all possible schedules from a list of CourseSection arrays
public class ScheduleMaker {

    // Method to generate all possible schedules from a list of CourseSection arrays
    public List<Schedule> generateAllPossibleSchedules(List<CourseSection[]> allSections) {
        // List to store all generated schedules
        List<Schedule> allSchedules = new ArrayList<Schedule>();
        
        // Create an initial empty schedule
        Schedule initialSchedule = new Schedule();
        
        // Generate all possible schedules recursively
        generateCombinations(allSections, allSchedules, 0, initialSchedule);
        
        // Return the list of generated schedules
        return allSchedules;
    }

    // Recursive method using backtracking algorithim to generate combinations of CourseSection arrays as Schedules
    public void generateCombinations(List<CourseSection[]> allSections, List<Schedule> outputArray, int currentArrayIndex, Schedule currentSchedule) {

        // Base case: If we have processed all CourseSection arrays, add the current schedule to the output list
        if (currentArrayIndex == allSections.size()) {
            outputArray.add(new Schedule(new ArrayList<CourseSection>(currentSchedule.getScheduleSections())));
            // Reset the current schedule for the next iteration
            currentSchedule = new Schedule();
            return;
        }

        // Get the CourseSection array at the current index
        CourseSection[] currentSections = allSections.get(currentArrayIndex);

        // Iterate over each CourseSection in the current array
        for (CourseSection courseSection : currentSections) {
            try {
                // Try to add the current CourseSection to the current schedule
                currentSchedule.addToSchedule(courseSection);
                
                // Recursively generate combinations for the next CourseSection array
                generateCombinations(allSections, outputArray, currentArrayIndex + 1, currentSchedule);
                
                // Remove the last added CourseSection to backtrack and explore other combinations
                currentSchedule.getScheduleSections().remove(currentArrayIndex);
            } catch (IllegalArgumentException e) {
                // If adding the CourseSection causes a conflict, continue with the next CourseSection
                continue;
            }
        }
    }
}
