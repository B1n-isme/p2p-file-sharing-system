import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class chaythu {
    private JFrame frame;
    private JPanel panel;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField placeOfBirthField;
    private JButton submitButton;

    public chaythu() {
        // Create frame
        frame = new JFrame("User Information Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Create panel and set layout
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2)); // 4 rows, 2 columns

        // Create and add components
        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Place of Birth:"));
        placeOfBirthField = new JTextField();
        panel.add(placeOfBirthField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        panel.add(submitButton);

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }

    private class SubmitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Clear the panel
            panel.removeAll();
            panel.setLayout(new GridLayout(1, 3)); // 1 row, 3 columns

            // Create new buttons
            JButton option1 = new JButton("Option 1");
            JButton option2 = new JButton("Option 2");
            JButton option3 = new JButton("Option 3");

            // Add new buttons to panel
            panel.add(option1);
            panel.add(option2);
            panel.add(option3);

            // Refresh the panel
            panel.revalidate();
            panel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new chaythu();
            }
        });
    }
}
