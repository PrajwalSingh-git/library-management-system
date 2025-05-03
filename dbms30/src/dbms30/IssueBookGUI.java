package dbms30;

import javax.swing.*;
import java.awt.*;

public class IssueBookGUI extends JFrame {

    private JTextField bookIdField, memberIdField;
    private JButton issueBtn;

    public IssueBookGUI() {
        setTitle("Issue Book");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Book ID Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Book ID:"), gbc);

        gbc.gridx = 1;
        bookIdField = new JTextField(15);
        add(bookIdField, gbc);

        // Member ID Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Member ID:"), gbc);

        gbc.gridx = 1;
        memberIdField = new JTextField(15);
        add(memberIdField, gbc);

        // Issue Button
        gbc.gridx = 0;
        gbc.gridy = 2;
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

        setVisible(true);
    }

    public static void main(String[] args) {
        new IssueBookGUI();  // Launch IssueBookGUI
    }
}
