import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


// The main application for the Schedule Creator
public class MainApplication extends JFrame {

    // List to store currently selected courses
    private List<Course> currentSelectedCourses = new ArrayList<>();

    // Current StudyPlan and Course information
    private StudyPlan currentStudyPlan;
    private Course[] currentCourses;
    private Course currentCourse;

    // Components for the user interface
    private JTextField selectedCoursesTextField;
    private String selectedProgram = new String();
    private String selectedCourse;
    private JComboBox<String> programDropdown;
    private JComboBox<String> courseDropdown;
    boolean firstRun = true; // Flag for the first instance the user runs the app

    // Constructs the main application window
    public MainApplication() {
        setTitle("Scheduler Creator Menu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create various components to be used in the interface
        programDropdown = new JComboBox<>(readProgramsFromFile());
        courseDropdown = new JComboBox<>();
        JTextField courseFilter = new JTextField(15);
        JTextField dayFilterTextField = new JTextField();
        JButton importProgramButton = new JButton("Import Program");
        JButton importSectionButton = new JButton("Import Section");
        JButton generateSchedulesButton = new JButton("Generate Schedules");
        JButton refreshButton = new JButton("Refresh");
        JButton addCourseButton = new JButton("Add Course");
        JButton clearButton = new JButton("Clear");
        selectedCoursesTextField = new JTextField();
        selectedCoursesTextField.setEditable(false);

        // Main panel
        JPanel panel = new JPanel(new GridLayout(11, 1));
        Border padding = BorderFactory.createEmptyBorder(25, 15, 25, 15);
        panel.setBorder(padding);

        // Sub-panels
        JPanel programPanel = new JPanel(new GridLayout(1, 2));
        programPanel.add(new JLabel("Select Program:"));
        programPanel.add(importProgramButton);

        JPanel coursePanel = new JPanel(new GridLayout(1, 3));
        coursePanel.add(new JLabel("Select Course:"));
        coursePanel.add(importSectionButton);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(addCourseButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);
        
        JPanel dayFilterPanel = new JPanel(new GridLayout(1, 2));
        dayFilterPanel.add(new JLabel("Day Filter:"));
        dayFilterPanel.add(dayFilterTextField);
        
        
        // Add components and sub-panels to main panel
        panel.add(programPanel);
        panel.add(programDropdown);
        panel.add(coursePanel);
        panel.add(courseFilter);
        panel.add(courseDropdown);
        panel.add(new JLabel("Selected Courses:"));
        panel.add(selectedCoursesTextField);
        panel.add(buttonPanel);
        panel.add(dayFilterPanel);
        panel.add(new JPanel()); // Filler panel
        panel.add(generateSchedulesButton);
        
        // Add action listeners for each button and combo box
        // Instantiate their respective import windows
        importProgramButton.addActionListener(e -> new ImportProgramWindow());
        importSectionButton.addActionListener(e -> new ImportSectionWindow());

        // Adds a course to the selected courses text field and handles two common errors
        addCourseButton.addActionListener(e -> {
            if (currentCourse == null) {
                new ErrorWindow("No course selected!");
                return;
            }
            if (currentSelectedCourses.contains(currentCourse)) {                
                new ErrorWindow("Course already added!");
                return;
            }
            currentSelectedCourses.add(currentCourse);
            updateSelectedCoursesTextField();
        });

        // Refreshes the drop downs in case of new data
        refreshButton.addActionListener(e -> {
            updateProgramDropdown();
            if (!selectedProgram.isEmpty()) {
                updateCourseDropdown();
            }
        });

        // Clears the selected courses text field
        clearButton.addActionListener(e -> {
            currentSelectedCourses.clear();
            updateSelectedCoursesTextField();
        });

        // Sets the user's selection as the selected program
        programDropdown.addActionListener(e -> {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            selectedProgram = (String) source.getSelectedItem();
            if (!selectedProgram.isEmpty()) {
                updateCourseDropdown();
            }
        });

        courseFilter.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = courseFilter.getText().toLowerCase();
                filterComboBoxItems(courseDropdown, currentCourses, searchText);
            }
        });

        // Sets the user's selection as the selected course
        courseDropdown.addActionListener(e -> {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            selectedCourse = (String) source.getSelectedItem();
            for (Course course : currentCourses) {
                if (course.getCourseName().equals(selectedCourse)) {
                    currentCourse = course;
                    break;
                }
            }
        });

        // Generates schedules when the generate schedule button is pressed
        generateSchedulesButton.addActionListener(e -> {
            // Check if no courses are selected
            if (currentSelectedCourses.isEmpty()) {
                new ErrorWindow("Courses must be selected!");
                return;
            }

            // Create instances for ScheduleMaker and CourseSectionsReader
            ScheduleMaker scheduleMaker = new ScheduleMaker();
            CourseSectionsReader sectionsReader = new CourseSectionsReader(Paths.get("CourseSections.txt"));
    
            // List to store CourseSection arrays for each selected course
            List<CourseSection[]> sectionsToBeProcessed = new ArrayList<>();

            try {
                // Read all sections from the file and store in a map
                Map<String, CourseSection[]> sectionsMap = sectionsReader.returnAllSections();

                // Process each selected course
                for (Course course : currentSelectedCourses) {
                    // Retrieve sections for the current course from the map
                    CourseSection[] currentSections = sectionsMap.get(course.getCourseID());

                    // Check if sections exist for the current course
                    if (currentSections == null) {
                        new ErrorWindow("Sections for course " + course.getCourseID() + " does not exist. Make sure to import them!");
                        return;
                    }

                    // Add the sections to the list for processing
                    sectionsToBeProcessed.add(currentSections);
                }
            } catch (IOException error) {
                // Handle IO exception by displaying an error window
                new ErrorWindow(error.getMessage());
            }

            // Generate all possible schedules based on the selected courses and sections
            List<Schedule> schedulesList = scheduleMaker.generateAllPossibleSchedules(sectionsToBeProcessed);

            try {
                // Parse the day filter from the user input
                int dayFilter = Integer.valueOf(dayFilterTextField.getText());

                // Display the schedules in a new ScheduleWindow
                new ScheduleWindow(schedulesList, dayFilter);
            } catch (NumberFormatException error) {
                // Handle the case where the day filter is not a valid integer
                new ErrorWindow("Day filter cannot be empty!");
            }
        });


        // Set the layout and make the window visible
        add(panel);
        setVisible(true);
    }


    // Updates the program dropdown, course dropdown, and selected courses text field
    private void updateProgramDropdown() {
        String[] updatedPrograms = readProgramsFromFile();
        programDropdown.setModel(new DefaultComboBoxModel<>(updatedPrograms));
    }

    private void updateCourseDropdown() {
        String[] updatedCourses = readCoursesFromFile();
        courseDropdown.setModel(new DefaultComboBoxModel<>(updatedCourses));
    }

    private void updateSelectedCoursesTextField() {
        List<String> selectedCourseIDs = new ArrayList<>();
        for (Course course : currentSelectedCourses) {
            selectedCourseIDs.add(course.getCourseID());
        }
        selectedCoursesTextField.setText(String.join(", ", selectedCourseIDs));
    }

    // Reads the list of programs from the file "StudyPlans.txt"
    public String[] readProgramsFromFile() {
        Set<String> programs = new HashSet<>();

        // Open a BufferedReader to read lines from the "StudyPlans.txt" file
        try (BufferedReader reader = new BufferedReader(new FileReader("StudyPlans.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                // Split the line into two parts, then take the first part which is always the major
                String[] parts = line.split(":");
                if (parts.length > 0) {
                    programs.add(parts[0].trim());
                }
            }
        } 
        // This error will always occur during initialization, since the file does not exist
        catch (IOException e) {
            if (firstRun) {  
                firstRun = false;
            } else {
                new ErrorWindow(e.getMessage());
            }
        }

        // Adding an empty string to represent a blank field
        programs.add("");
        return programs.toArray(new String[0]);
    }

    private static void filterComboBoxItems(JComboBox<String> comboBox, Course[] items, String searchText) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Course item : items) {
            String courseName = item.getCourseName();
            if (courseName.toLowerCase().contains(searchText)) {
                model.addElement(courseName);
            }
        }
        comboBox.setModel(model);
    }
    
    /* Reads courses from the "StudyPlans.txt" file for the selected program
    Updates the currentStudyPlan and currentCourses fields accordingly */
    public String[] readCoursesFromFile() {
        // Create a StudyPlanReader instance with the path to "StudyPlans.txt"
        StudyPlanReader reader = new StudyPlanReader(Paths.get("StudyPlans.txt"));
    
        try {
            // Attempt to return the study plan for the selected program
            StudyPlan studyPlan = reader.returnStudyPlan(selectedProgram);
    
            // Update the currentStudyPlan and currentCourses fields
            currentStudyPlan = studyPlan;
            currentCourses = studyPlan.getCourses().toArray(new Course[0]);
    
            // Get and return the course names from the study plan
            String[] courseNames = studyPlan.getCourseNames();
            return courseNames;
        } catch (IOException | MajorNotFoundException e) {
            // Handle IO or MajorNotFoundException by displaying an error window
            new ErrorWindow(e.getMessage());
        }
    
        // Return null in case of an exception
        return null;
    }

    // Main method to start the application using the SwingUtilities.invokeLater method
    public static void main(String[] args) {
        // Invoke the MainApplication constructor
        SwingUtilities.invokeLater(MainApplication::new);
    }
}