package dbms30;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SettingsPanel extends JPanel {
    private JTextField lendingPeriodField, fineRateField;
    private JButton saveButton, resetButton;

    public SettingsPanel() {
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Input Fields
        formPanel.add(new JLabel("Lending Period (days):"));
        lendingPeriodField = new JTextField();
        formPanel.add(lendingPeriodField);

        formPanel.add(new JLabel("Fine Per Day (₹):"));
        fineRateField = new JTextField();
        formPanel.add(fineRateField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        saveButton = new JButton("Save Settings");
        resetButton = new JButton("Reset to Defaults");
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadSettings();

        saveButton.addActionListener(e -> saveSettingsToDatabase());
        resetButton.addActionListener(e -> resetToDefaults());
    }

    private void loadSettings() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT lending_days, fine_per_day FROM settings WHERE id=1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lendingPeriodField.setText(String.valueOf(rs.getInt("lending_days")));
                fineRateField.setText(String.valueOf(rs.getDouble("fine_per_day")));
            } else {
                resetToDefaults();  // In case the settings row is missing
            }
        } catch (SQLException e) {
            resetToDefaults(); // If DB unavailable, show defaults
        }
    }

    private void saveSettingsToDatabase() {
        String lendingText = lendingPeriodField.getText().trim();
        String fineText = fineRateField.getText().trim();

        if (lendingText.isEmpty() || fineText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields must be filled.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int days = Integer.parseInt(lendingText);
            double fine = Double.parseDouble(fineText);

            if (days <= 0 || fine < 0) {
                JOptionPane.showMessageDialog(this, "Lending days must be > 0 and fine must be ≥ 0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO settings (id, lending_days, fine_per_day) VALUES (1, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE lending_days=?, fine_per_day=?"
                );
                ps.setInt(1, days);
                ps.setDouble(2, fine);
                ps.setInt(3, days);
                ps.setDouble(4, fine);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Settings updated successfully!");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving settings: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetToDefaults() {
        lendingPeriodField.setText("14");
        fineRateField.setText("2.5");
    }
}
