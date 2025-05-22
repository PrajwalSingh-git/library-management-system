package dbms30;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JToggleButton viewPasswordToggle; // Added toggle button

    public LoginGUI() {
        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(510, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(34, 34, 34)); // Dark background

        // Title Panel with Logo
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(34, 34, 34));

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/logo.png"));
            Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            System.out.println("Logo not found. Skipping...");
        }

        JLabel titleLabel = new JLabel("Library Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        titlePanel.add(logoLabel, BorderLayout.NORTH);
        titlePanel.add(titleLabel, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        formPanel.setBackground(new Color(34, 34, 34));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        userTypeCombo = new JComboBox<>(new String[]{"Admin", "Librarian", "User"});

        formPanel.add(createLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(createLabel("Password:"));

        // Password panel with password field and view toggle
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(new Color(34, 34, 34));
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        viewPasswordToggle = new JToggleButton("Show");
        viewPasswordToggle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        viewPasswordToggle.setFocusPainted(false);
        viewPasswordToggle.setBackground(new Color(64, 64, 64));
        viewPasswordToggle.setForeground(Color.WHITE);
        viewPasswordToggle.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        viewPasswordToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Toggle action for password visibility
        viewPasswordToggle.addActionListener(e -> {
            if (viewPasswordToggle.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
                viewPasswordToggle.setText("Hide");
            } else {
                passwordField.setEchoChar('•'); // Hide password
                viewPasswordToggle.setText("Show");
            }
        });

        passwordPanel.add(viewPasswordToggle, BorderLayout.EAST);
        formPanel.add(passwordPanel);

        formPanel.add(createLabel("User Type:"));
        formPanel.add(userTypeCombo);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(34, 34, 34));
        JButton loginButton = createButton("Login");
        JButton exitButton = createButton("Exit");

        loginButton.addActionListener(e -> performLogin());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(64, 64, 64));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(85, 85, 85));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(64, 64, 64));
            }
        });
        return button;
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (DatabaseConnection.validateLogin(username, password, userType)) {
            JOptionPane.showMessageDialog(this, "Login successful as " + userType + "!");
            dispose();

            switch (userType) {
                case "Admin":
                    new AdminDashboard();
                    break;
                case "Librarian":
                    new LibrarianDashboard();
                    break;
                case "User":
                    new UserDashboard(username);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
