import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class ImportSectionWindow extends JFrame {

    // Constructs an ImportSection for importing course section text
    public ImportSectionWindow() {
        // Set the title and size of the window
        setTitle("Import Section");
        setSize(400, 200);

        // Closing the window does not close the whole program
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Create various components
        JTextArea importTextArea = new JTextArea();
        JButton importButton = new JButton("Import");
        JButton clearButton = new JButton("Clear");
        JLabel importStatusLabel = new JLabel();

        // Create a 3x1 panel, then apply an empty border around it
        JPanel panel = new JPanel(new GridLayout(3, 1));
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(padding);

        // Add the components to the layout
        panel.add(new JLabel("Paste your section text:"));
        panel.add(importTextArea);
        panel.add(importButton);
        panel.add(clearButton);
        panel.add(importStatusLabel);

        /* Add action listeners
        This listener for the import button grabs the text from the text field, then stores it
        in a variable, then parses it as a list of sections of a course
        Empty fields and IOExceptions are handled */ 
        importButton.addActionListener(e -> {
            String textToImport = importTextArea.getText();
            if (!textToImport.isEmpty()) {
                String[] linesList = textToImport.split("\n");
                CourseOfferingsParser parser = new CourseOfferingsParser();
                try {
                    parser.parseAndSendTo(linesList, "CourseSections.txt");
                    importStatusLabel.setText("Import success!");
                } catch (IOException e1) {
                    new ErrorWindow(e1.getMessage());
                    importStatusLabel.setText("Import failed!");
                }
                importTextArea.setText("");
            } else {
                importStatusLabel.setText("Text field is empty!");
            }
        });

        // Clears the contents of the text field and label
        clearButton.addActionListener(e -> {
            importTextArea.setText("");
            importStatusLabel.setText("");
        });

        // Set the layout and make the window visible
        add(panel);
        setVisible(true);
    }
}