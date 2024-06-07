import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ErrorWindow extends JFrame {
    
    // Constructs an ErrorWindow for displaying error messages
    public ErrorWindow(String errorMessage) {
        // Set the title and size of the window
        setTitle("Error!");
        setSize(500, 100);

        // Closing the window does not close the whole program
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Create a 2x1 panel, then apply an empty border around it
        JPanel panel = new JPanel(new GridLayout(2, 1));
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(padding);
        
        // Create error label components and a button
        JLabel errorLabel = new JLabel("Error: " + errorMessage);
        JButton backButton = new JButton("Back");
        
        // Create a 1x3 panel, then insert the button in the central grid
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(new JPanel());
        buttonPanel.add(backButton);
        buttonPanel.add(new JPanel());

        // Add the error label with the error mssage and the 1x3 panel
        panel.add(errorLabel);
        panel.add(buttonPanel);
        
        // Dispose of the window using the back button
        backButton.addActionListener(e -> super.dispose());

        // Add the main panel to the frame
        add(panel);

        /* Since this is an error window and the user must be prompted to
        by it, set the window to always stay on top of other windows */
        setAlwaysOnTop(true);

        // Make the window visible
        setVisible(true);
    }
}