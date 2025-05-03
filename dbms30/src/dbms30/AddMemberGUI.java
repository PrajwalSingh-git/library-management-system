package dbms30;

import javax.swing.*;
import java.awt.*;

public class AddMemberGUI extends JFrame {
    private JTextField nameField;
    private JTextField usernameField;
    private JComboBox<String> membershipTypeBox;
    private JButton addButton, cancelButton;

    public AddMemberGUI() {
        setTitle("Add Member");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Row 2: Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // Row 3: Membership Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Membership Type:"), gbc);

        gbc.gridx = 1;
        membershipTypeBox = new JComboBox<>(new String[]{"Gold", "Silver", "Platinum"});
        add(membershipTypeBox, gbc);

        // Row 4: Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        addButton = new JButton("Add Member");
        cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        // Button Actions
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String membershipType = (String) membershipTypeBox.getSelectedItem();

            if (name.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseConnection.addMember(name, username, membershipType);
            if (success) {
                JOptionPane.showMessageDialog(this, "Member added successfully!");
                nameField.setText("");
                usernameField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add member.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    public static void main(String[] args) {
        new AddMemberGUI();
    }
}
