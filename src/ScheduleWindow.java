import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleWindow extends JFrame {
    
    // Array to store time intervals for the timetable
    private static final LocalTime[] TIMINGS = generateTimings();

    // Formatter for displaying time in a specific format
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");


    // Generates an array of time intervals from 9:00 AM to 9:00 PM with 30-minute intervals
    private static LocalTime[] generateTimings() {
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(21, 00);
        int intervals = (int) startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30 + 1;

        LocalTime[] timings = new LocalTime[intervals];
        for (int i = 0; i < intervals; i++) {
            timings[i] = startTime.plusMinutes(i * 30);
        }
        return timings;
    }

    // Constructs a ScheduleWindow based on the provided schedules and the day filter 
    public ScheduleWindow(List<Schedule> schedulesList, int dayFilter) {
        // Set title and dispose window, not exit on close
        setTitle("Schedule Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a tabbed pane to display timetables
        JTabbedPane tabbedPane = new JTabbedPane();
        int id = 0;

        boolean tablesExist = false;  // Flag to check if any tables exist

        // Create and add tabs for each timetable
        for (Schedule schedule : schedulesList) {
            if (schedule.getDays().size() == dayFilter) {
                id += 1;
                CourseSection[] sectionsArray = schedule.getScheduleSections().toArray(new CourseSection[0]);
                String[][] timetableData = generateTimetable(sectionsArray);

                // Check if any non-empty tables exist
                if (hasNonEmptyTable(timetableData)) {
                    tablesExist = true;
                    tabbedPane.addTab("Schedule " + id, createSchedulePanel(timetableData));
                }
            }
        } 
        
        // Wrap the JTabbedPane in a JScrollPane to make tabs scrollable
        JScrollPane scrollableTabs = new JScrollPane(tabbedPane);
        
        // Check if any tables were generated and if not, then show a message dialog
        if (!tablesExist) {
            JOptionPane.showMessageDialog(this, "No matching tables found.", "No Tables", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Add the scrollable pane to the frame
            add(scrollableTabs);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    /* Creates a JPanel containing a JTable displaying timetable data, with column headers as
    days and row headers as preset timings */
    private JPanel createSchedulePanel(String[][] data) {
        String[] columnHeaders = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnHeaders);
        JTable timetableTable = new JTable(tableModel);

        timetableTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
        JScrollPane scrollPane = new JScrollPane(timetableTable);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Helper method that checks if a table has non-empty cells
    private boolean hasNonEmptyTable(String[][] data) {
        for (String[] row : data) {
            for (String cell : row) {
                if (cell != null && !cell.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Generates timetable data based on the provided CourseSection objects
    private String[][] generateTimetable(CourseSection[] sectionsList) {
        int rows = TIMINGS.length - 1;
        int cols = 6;
        String[][] data = new String[rows][cols];

        // Populate the first column with time intervals
        for (int i = 0; i < rows; i++) {
            data[i][0] = TIMINGS[i].format(TIME_FORMATTER);
        }
        
        // Fill in the timetable data based on CourseSection timing
        for (CourseSection section : sectionsList) {
            for (int i = 0; i < cols; i++) {
                for (TimeRange timerange : section.getTiming().getTimeRanges()) {
                    if (timerange.getDayOfWeek().getValue() == i) {
                        for (int j = 0; j < rows; j++) {
                            if (timerange.getEnd().isAfter(TIMINGS[j]) && timerange.getStart().isBefore(TIMINGS[j+1])) {
                                data[j][i] = section.getSection() + " - " + section.getCourseName();
                                
                            }
                        }
                    }
                }
            }
        }
        return data;
    }
}