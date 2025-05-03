package dbms30;

import javax.swing.*;
import java.awt.*;

public class ReturnBookGUI extends JFrame {

    private JTextField lendingIdField;
    private JButton returnBtn;

    public ReturnBookGUI() {
        setTitle("Return Book");
        setSize(300, 150);
        setLayout(new GridLayout(2, 2, 10, 10));
        setLocationRelativeTo(null);

        add(new JLabel("Lending ID:"));
        lendingIdField = new JTextField();
        add(lendingIdField);

        returnBtn = new JButton("Return Book");
        add(returnBtn);

        returnBtn.addActionListener(e -> {
            int lendingId = Integer.parseInt(lendingIdField.getText());

            boolean success = DatabaseConnection.renewBook(lendingId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book returned successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
