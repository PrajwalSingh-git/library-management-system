package dbms30;

import javax.swing.*;
import java.awt.*;

public class LibrarianDashboard extends JFrame {

    public LibrarianDashboard() {
        setTitle("Librarian Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton issueBookBtn = new JButton("Issue Book");
        JButton returnBookBtn = new JButton("Return Book");
        JButton trackOverdueBtn = new JButton("Track Overdue Books");
        JButton renewBookBtn = new JButton("Renew Book");
        JButton viewFinesBtn = new JButton("View Fines");
        JButton logoutBtn = new JButton("Logout");

        panel.add(issueBookBtn);
        panel.add(returnBookBtn);
        panel.add(trackOverdueBtn);
        panel.add(renewBookBtn);
        panel.add(viewFinesBtn);
        panel.add(logoutBtn);

        issueBookBtn.addActionListener(e -> new IssueBookGUI());
        returnBookBtn.addActionListener(e -> new RenewBookGUI());
        trackOverdueBtn.addActionListener(e -> new TrackOverdueGUI());
        renewBookBtn.addActionListener(e -> new RenewBookGUI());
        viewFinesBtn.addActionListener(e -> new ViewFinesGUI());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        add(panel);
        setVisible(true);
    }
}
