package dbms30;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(9, 1, 0, 10));
        sidebar.setBackground(new Color(45, 45, 45));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("Admin Panel", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sidebar.add(title);

        // Buttons
        JButton addBookBtn = createSidebarButton("Add Book");
        JButton editSearchBookBtn = createSidebarButton("Edit/Search Book");
        JButton deleteBookBtn = createSidebarButton("Delete Book");
        JButton addMemberBtn = createSidebarButton("Manage Memberships");
        JButton reportBtn = createSidebarButton("Generate Reports");
        JButton settingsBtn = createSidebarButton("System Settings");
        JButton logoutBtn = createSidebarButton("Logout");

        sidebar.add(addBookBtn);
        sidebar.add(editSearchBookBtn);
        sidebar.add(deleteBookBtn);
        sidebar.add(addMemberBtn);
        sidebar.add(reportBtn);
        sidebar.add(settingsBtn);
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // Main Content
        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);
        mainContentPanel.setBackground(new Color(34, 34, 34));

        mainContentPanel.add(createPlaceholderPanel("Welcome, Admin!"), "default");
        mainContentPanel.add(new AddBookPanel(), "addBook");
        mainContentPanel.add(new EditSearchBookPanel(), "editSearch");
        mainContentPanel.add(new DeleteBookPanel(), "deleteBook");
        mainContentPanel.add(new AddMemberPanel(), "addmember");
        mainContentPanel.add(new ReportPanel(), "report");
        mainContentPanel.add(new SettingsPanel(), "settings");

        add(mainContentPanel, BorderLayout.CENTER);

        // Button actions
        addBookBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "addBook"));
        editSearchBookBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "editSearch"));
        deleteBookBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "deleteBook"));
        addMemberBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "addmember"));
        reportBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "report"));
        settingsBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "settings"));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        cardLayout.show(mainContentPanel, "default");
        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(64, 64, 64));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(85, 85, 85)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(34, 34, 34));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }
}