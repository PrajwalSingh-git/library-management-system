package dbms30;

import javax.swing.*;
import java.awt.*;

public class ReturnBookPanel extends JPanel {

    private JTextField lendingIdField;
    private JButton returnBtn;

    public ReturnBookPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34)); // Optional: dark background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Return Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // span two columns
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;

        // Lending ID Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel lendingIdLabel = new JLabel("Lending ID:");
        lendingIdLabel.setForeground(Color.WHITE);
        add(lendingIdLabel, gbc);

        // Lending ID Field
        gbc.gridx = 1;
        lendingIdField = new JTextField(15);
        add(lendingIdField, gbc);

        // Return Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        returnBtn = new JButton("Return Book");
        add(returnBtn, gbc);

        // Button Action
        returnBtn.addActionListener(e -> {
            try {
                int lendingId = Integer.parseInt(lendingIdField.getText().trim());
                boolean success = DatabaseConnection.returnBook(lendingId); // assuming method for returning book
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Book returned successfully.");
                    lendingIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to return book. Check Lending ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Lending ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
