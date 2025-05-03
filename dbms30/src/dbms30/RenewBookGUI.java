package dbms30;

import javax.swing.*;
import java.awt.*;

public class RenewBookGUI extends JFrame {

    private JTextField lendingIdField;
    private JButton renewBtn;

    public RenewBookGUI() {
        setTitle("Renew Book");
        setSize(350, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Lending ID Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Lending ID:"), gbc);

        // Lending ID Field
        gbc.gridx = 1;
        lendingIdField = new JTextField(15);
        add(lendingIdField, gbc);

        // Renew Button
        gbc.gridx = 0;
        gbc.gridy = 1;
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

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RenewBookGUI::new);
    }
}
