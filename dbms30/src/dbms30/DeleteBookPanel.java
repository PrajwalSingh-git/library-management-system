package dbms30;

import javax.swing.*;
import java.awt.*;

public class DeleteBookPanel extends JPanel {
    private JTextField idField;
    private JButton deleteButton;

    public DeleteBookPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 34, 34));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(new Color(34, 34, 34));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel idLabel = new JLabel("Enter Book ID:");
        idLabel.setForeground(Color.WHITE);
        formPanel.add(idLabel);

        idField = new JTextField();
        formPanel.add(idField);

        deleteButton = new JButton("Delete");
        formPanel.add(new JLabel()); // empty placeholder
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.CENTER);

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
