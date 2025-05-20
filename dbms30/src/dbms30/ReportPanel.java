package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReportPanel extends JPanel {
    private JTextArea reportArea;
    private JButton refreshButton;

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 34, 34));

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setBackground(new Color(24, 24, 24));
        reportArea.setForeground(Color.WHITE);
        reportArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        refreshButton = new JButton("Generate Report");
        refreshButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(e -> generateReport());

        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        generateReport();
    }

    private void generateReport() {
        StringBuilder sb = new StringBuilder();

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();

            ResultSet rsBooks = stmt.executeQuery("SELECT COUNT(*) FROM books");
            if (rsBooks.next()) sb.append("Total Books: ").append(rsBooks.getInt(1)).append("\n");

            ResultSet rsMembers = stmt.executeQuery("SELECT COUNT(*) FROM members");
            if (rsMembers.next()) sb.append("Total Members: ").append(rsMembers.getInt(1)).append("\n");

            ResultSet rsLendings = stmt.executeQuery("SELECT COUNT(*) FROM lendings");
            if (rsLendings.next()) sb.append("Total Lendings: ").append(rsLendings.getInt(1)).append("\n");

            ResultSet rsOverdue = stmt.executeQuery(
                "SELECT COUNT(*) FROM lendings WHERE return_date IS NULL AND due_date < CURDATE()");
            if (rsOverdue.next()) sb.append("Overdue Books: ").append(rsOverdue.getInt(1)).append("\n");

            ResultSet rsFines = stmt.executeQuery("SELECT SUM(fine) FROM lendings");
            if (rsFines.next()) {
                double fines = rsFines.getDouble(1);
                sb.append("Total Fines Collected: ₹").append(String.format("%.2f", fines)).append("\n");
            }

            reportArea.setText(sb.toString());

        } catch (SQLException e) {
            reportArea.setText("Error generating report:\n" + e.getMessage());
        }
    }
}
