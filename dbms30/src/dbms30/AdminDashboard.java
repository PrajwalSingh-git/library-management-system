package dbms30;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton addBookBtn = new JButton("Add/Edit/Delete Books");
        JButton manageMembersBtn = new JButton("Manage Memberships");
        JButton reportBtn = new JButton("Generate Reports");
        JButton settingsBtn = new JButton("System Settings");
        JButton logoutBtn = new JButton("Logout");

        panel.add(addBookBtn);
        panel.add(manageMembersBtn);
        panel.add(reportBtn);
        panel.add(settingsBtn);
        panel.add(logoutBtn);

        add(panel);
        
        addBookBtn.addActionListener(e -> new AddBookGUI());
        manageMembersBtn.addActionListener(e -> new AddMemberGUI());
        reportBtn.addActionListener(e -> new ReportGUI());
        settingsBtn.addActionListener(e -> new SystemSettingsGUI());
        logoutBtn.addActionListener((ActionEvent e) -> {
            new LoginGUI();
            dispose();
        });

        setVisible(true);
    }
}
