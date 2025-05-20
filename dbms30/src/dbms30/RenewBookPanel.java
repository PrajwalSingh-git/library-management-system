package dbms30;

import javax.swing.*;
import java.awt.*;

public class RenewBookPanel extends JPanel {

    private JTextField lendingIdField;
    private JButton renewBtn;

    public RenewBookPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));  // Optional: match dashboard background or set your preferred bg

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Renew Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);  // For dark background visibility
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // span two columns for title
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Reset gridwidth for other components
        gbc.gridwidth = 1;

        // Lending ID Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel lendingIdLabel = new JLabel("Lending ID:");
        lendingIdLabel.setForeground(Color.WHITE);
        add(lendingIdLabel, gbc);

        // Lending ID Field
        gbc.gridx = 1;
        lendingIdField = new JTextField(15);
        add(lendingIdField, gbc);

        // Renew Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        renewBtn = new JButton("Renew Book");
        add(renewBtn, gbc);

        // Button Action
        renewBtn.addActionListener(e -> {
            try {
                int lendingId = Integer.parseInt(lendingIdField.getText().trim());
                boolean success = DatabaseConnection.renewBook(lendingId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Book renewed successfully.");
                    lendingIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Renewal failed. Check Lending ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Lending ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Renew Book");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(350, 200);
            frame.setLocationRelativeTo(null);
            frame.add(new RenewBookPanel());
            frame.setVisible(true);
        });
    }
}
