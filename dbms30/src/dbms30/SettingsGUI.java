package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SettingsGUI extends JFrame {
    private JTextField daysField, fineField;
    private JButton saveButton;

    public SettingsGUI() {
        setTitle("System Settings");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Lending Days:"));
        daysField = new JTextField();
        add(daysField);

        add(new JLabel("Fine Per Day (₹):"));
        fineField = new JTextField();
        add(fineField);

        saveButton = new JButton("Save Settings");
        add(saveButton);

        loadSettings();

        saveButton.addActionListener(e -> {
            try {
                int days = Integer.parseInt(daysField.getText().trim());
                double fine = Double.parseDouble(fineField.getText().trim());

                try (Connection conn = DatabaseConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO settings (id, lending_days, fine_per_day) VALUES (1, ?, ?) ON DUPLICATE KEY UPDATE lending_days=?, fine_per_day=?");
                    ps.setInt(1, days);
                    ps.setDouble(2, fine);
                    ps.setInt(3, days);
                    ps.setDouble(4, fine);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Settings updated successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input or error updating settings.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void loadSettings() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT lending_days, fine_per_day FROM settings WHERE id=1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                daysField.setText(String.valueOf(rs.getInt("lending_days")));
                fineField.setText(String.valueOf(rs.getDouble("fine_per_day")));
            }
        } catch (SQLException e) {
            daysField.setText("14");
            fineField.setText("2.5");
        }
    }
}
