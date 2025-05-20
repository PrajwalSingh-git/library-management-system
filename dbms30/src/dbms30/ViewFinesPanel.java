package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewFinesPanel extends JPanel {

    private JTextArea fineArea;

    public ViewFinesPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Title Label
        JLabel titleLabel = new JLabel("View Fines");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.gridwidth = 1;
        titleGbc.insets = new Insets(10, 10, 5, 10);
        titleGbc.fill = GridBagConstraints.HORIZONTAL;
        titleGbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, titleGbc);

        // Fine TextArea inside ScrollPane
        fineArea = new JTextArea();
        fineArea.setEditable(false);
        fineArea.setBackground(new Color(45, 45, 45));
        fineArea.setForeground(Color.WHITE);
        fineArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(fineArea);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        add(scrollPane, gbc);

        loadFineData();
    }

    private void loadFineData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT lendings.id, books.title, members.name, lendings.due_date, " +
                         "DATEDIFF(CURDATE(), lendings.due_date) AS overdue_days, " +
                         "DATEDIFF(CURDATE(), lendings.due_date) * 5 AS fine " +
                         "FROM lendings " +
                         "JOIN books ON lendings.book_id = books.id " +
                         "JOIN members ON lendings.member_id = members.id " +
                         "WHERE lendings.return_date IS NULL AND lendings.due_date < CURDATE()";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder("Fines for Overdue Books:\n\n");
            while (rs.next()) {
                sb.append(String.format("Lending ID: %d, Book: %s, Member: %s, Due: %s, Overdue Days: %d, Fine: ₹%d%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getDate("due_date").toString(),
                        rs.getInt("overdue_days"),
                        rs.getInt("fine")));
            }

            fineArea.setText(sb.toString());

        } catch (SQLException e) {
            fineArea.setText("Error retrieving fines.\n" + e.getMessage());
        }
    }
}
