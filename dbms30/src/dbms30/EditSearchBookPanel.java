package dbms30;

import javax.swing.*;
import java.awt.*;

public class EditSearchBookPanel extends JPanel {
    private JTextField searchIsbnField, idField, titleField, authorField, isbnField, genreField;
    private JButton searchButton, updateButton;

    public EditSearchBookPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 34, 34));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // Extra row for search
        formPanel.setBackground(new Color(34, 34, 34));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Row 1: Search by ISBN
        JLabel searchIsbnLabel = new JLabel("Search by ISBN:");
        searchIsbnLabel.setForeground(Color.WHITE);
        formPanel.add(searchIsbnLabel);
        searchIsbnField = new JTextField();
        formPanel.add(searchIsbnField);

        // Row 2: Book ID (read-only)
        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setForeground(Color.WHITE);
        formPanel.add(idLabel);
        idField = new JTextField();
        idField.setEditable(false); // ID should not be editable
        formPanel.add(idField);

        // Row 3: Title
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.WHITE);
        formPanel.add(titleLabel);
        titleField = new JTextField();
        formPanel.add(titleField);

        // Row 4: Author
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        formPanel.add(authorLabel);
        authorField = new JTextField();
        formPanel.add(authorField);

        // Row 5: ISBN
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setForeground(Color.WHITE);
        formPanel.add(isbnLabel);
        isbnField = new JTextField();
        formPanel.add(isbnField);

        // Row 6: Genre
        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setForeground(Color.WHITE);
        formPanel.add(genreLabel);
        genreField = new JTextField();
        formPanel.add(genreField);

        // Row 7: Buttons
        searchButton = new JButton("Search");
        updateButton = new JButton("Update");
        formPanel.add(searchButton);
        formPanel.add(updateButton);

        add(formPanel, BorderLayout.CENTER);

        // Button Actions
        searchButton.addActionListener(e -> {
            String isbn = searchIsbnField.getText().trim();
            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter ISBN to search.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = DatabaseConnection.searchBookByIsbn(isbn);
            if (book != null) {
                idField.setText(String.valueOf(book.getId()));
                titleField.setText(book.getTitle());
                authorField.setText(book.getAuthor());
                isbnField.setText(book.getIsbn());
                genreField.setText(book.getGenre());
            } else {
                JOptionPane.showMessageDialog(this, "Book not found.");
                clearFieldsExceptSearch();
            }
        });

        updateButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String genre = genreField.getText().trim();

            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No book selected to update.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void clearFieldsExceptSearch() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        genreField.setText("");
    }
}
