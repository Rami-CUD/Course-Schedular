import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class ManualSearchableDropdownExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Searchable Dropdown Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();

            List<String> itemList = new ArrayList<>();
            itemList.add("Apple");
            itemList.add("Banana");
            itemList.add("Orange");
            itemList.add("Grapes");
            itemList.add("Pineapple");

            JComboBox<String> searchableComboBox = new JComboBox<>(itemList.toArray(new String[0]));

            JTextField searchField = new JTextField(15);
            searchField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {}

                @Override
                public void keyPressed(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {
                    String searchText = searchField.getText().toLowerCase();
                    filterComboBoxItems(searchableComboBox, itemList, searchText);
                }
            });

            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(e -> {
                String selectedValue = (String) searchableComboBox.getSelectedItem();
                JOptionPane.showMessageDialog(frame, "Selected item: " + selectedValue);
            });

            panel.add(searchField);
            panel.add(searchableComboBox);
            panel.add(submitButton);

            frame.getContentPane().add(panel);
            frame.setSize(new Dimension(300, 150));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void filterComboBoxItems(JComboBox<String> comboBox, List<String> items, String searchText) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String item : items) {
            if (item.toLowerCase().contains(searchText)) {
                model.addElement(item);
            }
        }
        comboBox.setModel(model);
    }
}
