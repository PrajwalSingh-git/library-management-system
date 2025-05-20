package dbms30;

import javax.swing.*;
import java.awt.*;

public class IssueBookPanel extends JPanel {

    private JTextField bookIdField, memberIdField;
    private JButton issueBtn;

    public IssueBookPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34)); // Match dashboard background

        GridBagConstraints gbc = new GridBagConstraints();

        // Common insets and fill for all components after title
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Issue Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);  // Add color for visibility on dark background
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns for title
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Reset gridwidth for next components
        gbc.gridwidth = 1;

        // Book ID Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel bookLabel = new JLabel("Book ID:");
        bookLabel.setForeground(Color.WHITE);
        add(bookLabel, gbc);

        // Book ID Field
        gbc.gridx = 1;
        bookIdField = new JTextField(15);
        add(bookIdField, gbc);

        // Member ID Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel memberLabel = new JLabel("Member ID:");
        memberLabel.setForeground(Color.WHITE);
        add(memberLabel, gbc);

        // Member ID Field
        gbc.gridx = 1;
        memberIdField = new JTextField(15);
        add(memberIdField, gbc);

        // Issue Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        issueBtn = new JButton("Issue Book");
        add(issueBtn, gbc);

        // Action Listener
        issueBtn.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText().trim());
                int memberId = Integer.parseInt(memberIdField.getText().trim());

                if (bookId <= 0 || memberId <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter valid Book ID and Member ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = DatabaseConnection.issueBook(bookId, memberId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Book issued successfully.");
                    bookIdField.setText("");
                    memberIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to issue book. Check if book is already issued.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Book ID and Member ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
