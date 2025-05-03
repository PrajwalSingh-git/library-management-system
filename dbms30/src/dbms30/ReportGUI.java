package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReportGUI extends JFrame {
    private JTextArea reportArea;
    private JButton refreshButton;

    public ReportGUI() {
        setTitle("Library Reports");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        refreshButton = new JButton("Generate Report");

        refreshButton.addActionListener(e -> generateReport());

        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        generateReport();  // Auto-generate on open

        setVisible(true);
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

            ResultSet rsOverdue = stmt.executeQuery("SELECT COUNT(*) FROM lendings WHERE return_date IS NULL AND due_date < CURDATE()");
            if (rsOverdue.next()) sb.append("Overdue Books: ").append(rsOverdue.getInt(1)).append("\n");

            ResultSet rsFines = stmt.executeQuery("SELECT SUM(fine) FROM lendings");
            if (rsFines.next()) sb.append("Total Fines Collected: ₹").append(rsFines.getDouble(1)).append("\n");

            reportArea.setText(sb.toString());

        } catch (SQLException e) {
            reportArea.setText("Error generating report:\n" + e.getMessage());
        }
    }
}
