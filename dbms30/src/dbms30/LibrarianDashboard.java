package dbms30;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibrarianDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    public LibrarianDashboard() {
        setTitle("Librarian Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(8, 1, 0, 10));
        sidebar.setBackground(new Color(45, 45, 45));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("Librarian Panel", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sidebar.add(title);

        // Buttons
        JButton issueBookBtn = createSidebarButton("Issue Book");
        JButton returnBookBtn = createSidebarButton("Return Book");
        JButton renewBookBtn = createSidebarButton("Renew Book");
        JButton trackOverdueBtn = createSidebarButton("Track Overdue");
        JButton viewFinesBtn = createSidebarButton("View Fines");
        JButton viewHoldsBtn = createSidebarButton("View Hold Requests");
        JButton logoutBtn = createSidebarButton("Logout");

        sidebar.add(issueBookBtn);
        sidebar.add(returnBookBtn);
        sidebar.add(renewBookBtn);
        sidebar.add(trackOverdueBtn);
        sidebar.add(viewFinesBtn);
        sidebar.add(viewHoldsBtn);
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // Main Content with background
        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);
        mainContentPanel.setBackground(new Color(34, 34, 34));

        mainContentPanel.add(createPlaceholderPanel("Welcome, Librarian!"), "default");
        mainContentPanel.add(new IssueBookPanel(), "issue");
        mainContentPanel.add(new ReturnBookPanel(), "return");
        mainContentPanel.add(new RenewBookPanel(), "renew");
        mainContentPanel.add(new TrackOverduePanel(), "overdue");
        mainContentPanel.add(new ViewFinesPanel(), "fines");
        mainContentPanel.add(new HoldRequestsPanel(), "holds");

        add(mainContentPanel, BorderLayout.CENTER);

        // Button Actions
        issueBookBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "issue"));
        returnBookBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "return"));
        renewBookBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "renew"));
        trackOverdueBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "overdue"));
        viewFinesBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "fines"));
        viewHoldsBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "holds"));
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
        JPanel panel = new JPanel() {
            private final Image backgroundImage = new ImageIcon(getClass().getResource("/icons/librar.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        label.setOpaque(false);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
