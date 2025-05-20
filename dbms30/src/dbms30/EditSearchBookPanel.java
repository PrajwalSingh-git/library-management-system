package dbms30;

import javax.swing.*;
import java.awt.*;

public class EditSearchBookPanel extends JPanel {
    private JTextField idField, titleField, authorField, isbnField, genreField;
    private JButton searchButton, updateButton;

    public EditSearchBookPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 34, 34));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(new Color(34, 34, 34));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setForeground(Color.WHITE);
        formPanel.add(idLabel);
        idField = new JTextField();
        formPanel.add(idField);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.WHITE);
        formPanel.add(titleLabel);
        titleField = new JTextField();
        formPanel.add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        formPanel.add(authorLabel);
        authorField = new JTextField();
        formPanel.add(authorField);

        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setForeground(Color.WHITE);
        formPanel.add(isbnLabel);
        isbnField = new JTextField();
        formPanel.add(isbnField);

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setForeground(Color.WHITE);
        formPanel.add(genreLabel);
        genreField = new JTextField();
        formPanel.add(genreField);

        searchButton = new JButton("Search");
        updateButton = new JButton("Update");

        formPanel.add(searchButton);
        formPanel.add(updateButton);

        add(formPanel, BorderLayout.CENTER);

        // Button Actions
        searchButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Book ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int id = Integer.parseInt(idText);
                Book book = DatabaseConnection.searchBookById(id);
                if (book != null) {
                    titleField.setText(book.getTitle());
                    authorField.setText(book.getAuthor());
                    isbnField.setText(book.getIsbn());
                    genreField.setText(book.getGenre());
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found.");
                    clearFieldsExceptId();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Book ID must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String genre = genreField.getText().trim();

            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Book ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                if (DatabaseConnection.updateBook(id, title, author, isbn, genre)) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Book ID must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void clearFieldsExceptId() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        genreField.setText("");
    }
}
