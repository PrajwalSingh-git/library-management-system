package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TrackOverdueGUI extends JFrame {

    public TrackOverdueGUI() {
        setTitle("Overdue Books");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JTextArea overdueArea = new JTextArea();
        overdueArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(overdueArea);
        add(scrollPane);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT lendings.id, books.title, members.name, lendings.due_date " +
                         "FROM lendings " +
                         "JOIN books ON lendings.book_id = books.id " +
                         "JOIN members ON lendings.member_id = members.id " +
                         "WHERE lendings.return_date IS NULL AND lendings.due_date < CURDATE()";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder("Overdue Books:\n\n");
            while (rs.next()) {
                sb.append("Lending ID: ").append(rs.getInt("id"))
                  .append(", Book: ").append(rs.getString("title"))
                  .append(", Member: ").append(rs.getString("name"))
                  .append(", Due: ").append(rs.getDate("due_date")).append("\n");
            }

            overdueArea.setText(sb.toString());

        } catch (SQLException e) {
            overdueArea.setText("Error fetching overdue books.\n" + e.getMessage());
        }

        setVisible(true);
    }
}
