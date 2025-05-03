package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewFinesGUI extends JFrame {

    public ViewFinesGUI() {
        setTitle("View Fines");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JTextArea fineArea = new JTextArea();
        fineArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(fineArea);
        add(scrollPane);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT lendings.id, books.title, members.name, lendings.due_date, " +
                         "DATEDIFF(CURDATE(), lendings.due_date) AS overdue_days, " +
                         "DATEDIFF(CURDATE(), lendings.due_date) * 5 AS fine " +  // Assume ₹5/day fine
                         "FROM lendings " +
                         "JOIN books ON lendings.book_id = books.id " +
                         "JOIN members ON lendings.member_id = members.id " +
                         "WHERE lendings.return_date IS NULL AND lendings.due_date < CURDATE()";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder("Fines for Overdue Books:\n\n");
            while (rs.next()) {
                sb.append("Lending ID: ").append(rs.getInt("id"))
                  .append(", Book: ").append(rs.getString("title"))
                  .append(", Member: ").append(rs.getString("name"))
                  .append(", Due: ").append(rs.getDate("due_date"))
                  .append(", Overdue Days: ").append(rs.getInt("overdue_days"))
                  .append(", Fine: ₹").append(rs.getInt("fine")).append("\n");
            }

            fineArea.setText(sb.toString());

        } catch (SQLException e) {
            fineArea.setText("Error retrieving fines.\n" + e.getMessage());
        }

        setVisible(true);
    }
}
