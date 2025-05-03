package dbms30;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;

    public LoginGUI() {
        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window

        // Panel for form
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("User Type:"));
        userTypeCombo = new JComboBox<>(new String[]{"Admin", "Librarian", "User"});
        panel.add(userTypeCombo);

        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        JButton exitButton = new JButton("Exit");
        panel.add(exitButton);

        add(panel);

        // Action Listeners
        loginButton.addActionListener(e -> performLogin());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (DatabaseConnection.validateLogin(username, password, userType)) {
            JOptionPane.showMessageDialog(this, "Login successful as " + userType + "!");
            switch (userType) {
                case "Admin":
                    new AdminDashboard(); // Ensure this class is fully defined
                    break;
                case "Librarian":
                    new LibrarianDashboard(); // Ensure this class is fully defined
                    break;
                case "User":
                    new UserDashboard(username); // Ensure this class is fully defined
                    break;
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to run the interface
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
