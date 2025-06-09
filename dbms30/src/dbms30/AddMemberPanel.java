package dbms30;

import javax.swing.*;
import java.awt.*;

public class AddMemberPanel extends JPanel {
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JComboBox<String> membershipTypeBox;
    private JButton addButton, cancelButton;

    public AddMemberPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Add New Member");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Name
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        // Membership Type
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel membershipLabel = new JLabel("Membership Type:");
        membershipLabel.setForeground(Color.WHITE);
        add(membershipLabel, gbc);

        gbc.gridx = 1;
        membershipTypeBox = new JComboBox<>(new String[]{"Gold", "Silver", "Platinum"});
        add(membershipTypeBox, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        addButton = new JButton("Add Member");
        cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(34, 34, 34));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        // Add Button Action
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String membershipType = (String) membershipTypeBox.getSelectedItem();

            if (name.isEmpty() || username.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, username, and email cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseConnection.addMember(name, username, email, membershipType);
            if (success) {
                JOptionPane.showMessageDialog(this, "Member added successfully!");
                nameField.setText("");
                usernameField.setText("");
                emailField.setText("");
                membershipTypeBox.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add member.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel Button Action
        cancelButton.addActionListener(e -> {
            nameField.setText("");
            usernameField.setText("");
            emailField.setText("");
            membershipTypeBox.setSelectedIndex(0);
        });
    }
}
