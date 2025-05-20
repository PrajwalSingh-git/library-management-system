package dbms30;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HoldRequestsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterComboBox;
    private JButton approveBtn, cancelBtn, refreshBtn;

    public HoldRequestsPanel() {
        setLayout(new BorderLayout());

        // Top panel for filter and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Filter dropdown
        filterComboBox = new JComboBox<>(new String[]{"All", "Pending", "Approved", "Cancelled"});
        filterComboBox.addActionListener(e -> loadHoldRequests());
        topPanel.add(new JLabel("Filter by Status:"));
        topPanel.add(filterComboBox);

        // Approve and Cancel buttons
        approveBtn = new JButton("Approve");
        cancelBtn = new JButton("Cancel");
        refreshBtn = new JButton("Refresh");

        topPanel.add(approveBtn);
        topPanel.add(cancelBtn);
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Hold ID", "Book Title", "Member Name", "Request Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        approveBtn.addActionListener(e -> updateSelectedHoldStatus("Approved"));
        cancelBtn.addActionListener(e -> updateSelectedHoldStatus("Cancelled"));
        refreshBtn.addActionListener(e -> loadHoldRequests());

        loadHoldRequests();
    }

    private void loadHoldRequests() {
        String selectedStatus = (String) filterComboBox.getSelectedItem();
        String sql = "SELECT hr.id, b.title, m.name, hr.request_date, hr.status " +
                     "FROM hold_requests hr " +
                     "JOIN books b ON hr.book_id = b.id " +
                     "JOIN members m ON hr.member_id = m.id ";

        if (!"All".equals(selectedStatus)) {
            sql += " WHERE hr.status = ? ";
        }
        sql += " ORDER BY hr.request_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!"All".equals(selectedStatus)) {
                ps.setString(1, selectedStatus);
            }
            try (ResultSet rs = ps.executeQuery()) {
                tableModel.setRowCount(0);
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getDate("request_date"),
                        rs.getString("status")
                    };
                    tableModel.addRow(row);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading hold requests:\n" + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedHoldStatus(String newStatus) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a hold request first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int holdId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

        if (currentStatus.equals(newStatus)) {
            JOptionPane.showMessageDialog(this, "This request is already " + newStatus + ".", "No Change", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to change status to '" + newStatus + "'?",
            "Confirm Status Change",
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "UPDATE hold_requests SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, holdId);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Status updated successfully.");
                loadHoldRequests();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
