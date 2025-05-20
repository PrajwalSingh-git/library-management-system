package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TrackOverduePanel extends JPanel {

    private JTextArea overdueArea;

    public TrackOverduePanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34)); // dark background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Title Label
        JLabel titleLabel = new JLabel("Overdue Books");
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

        // Overdue TextArea inside ScrollPane
        overdueArea = new JTextArea();
        overdueArea.setEditable(false);
        overdueArea.setBackground(new Color(45, 45, 45));
        overdueArea.setForeground(Color.WHITE);
        overdueArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(overdueArea);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Load overdue data from DB
        loadOverdueData();
    }

    private void loadOverdueData() {
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
                sb.append(String.format("Lending ID: %d, Book: %s, Member: %s, Due: %s%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getDate("due_date").toString()));
            }
            overdueArea.setText(sb.toString());

        } catch (SQLException e) {
            overdueArea.setText("Error fetching overdue books.\n" + e.getMessage());
        }
    }
}
