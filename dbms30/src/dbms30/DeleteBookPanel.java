package dbms30;

import javax.swing.*;
import java.awt.*;

public class DeleteBookPanel extends JPanel {
    private JTextField idField;
    private JButton deleteButton;

    public DeleteBookPanel() {
        setBackground(new Color(34, 34, 34));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("             Delete Book");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel idLabel = new JLabel("Enter Book ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(idLabel, gbc);

        idField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(idField, gbc);

        deleteButton = new JButton("Delete");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(deleteButton, gbc);

        deleteButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Book ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookId;
            try {
                bookId = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Book ID must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (DatabaseConnection.deleteBook(bookId)) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                    idField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete book.");
                }
            }
        });
    }
}
