package dbms30;

import javax.swing.*;
import java.awt.*;

public class EditSearchBookPanel extends JPanel {
    private JTextField searchIsbnField, idField, titleField, authorField, isbnField, genreField;
    private JButton searchButton, updateButton;

    public EditSearchBookPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 34, 34));
        
        // Title Bar
        JLabel titleBar = new JLabel("Edit / Search Book");
        titleBar.setOpaque(true);
        titleBar.setBackground(new Color(45, 45, 45));
        titleBar.setForeground(Color.WHITE);
        titleBar.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleBar.setHorizontalAlignment(SwingConstants.CENTER);
        titleBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleBar, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(34, 34, 34));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // Search ISBN
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel searchIsbnLabel = new JLabel("Search by ISBN:");
        searchIsbnLabel.setForeground(Color.WHITE);
        searchIsbnLabel.setFont(labelFont);
        formPanel.add(searchIsbnLabel, gbc);

        gbc.gridx = 1;
        searchIsbnField = new JTextField(20);
        formPanel.add(searchIsbnField, gbc);

        // Book ID (read-only)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        idField = new JTextField(20);
        idField.setEditable(false);
        idField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(idField, gbc);

        // Title
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(labelFont);
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        // Author
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        authorLabel.setFont(labelFont);
        formPanel.add(authorLabel, gbc);

        gbc.gridx = 1;
        authorField = new JTextField(20);
        formPanel.add(authorField, gbc);

        // ISBN
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setForeground(Color.WHITE);
        isbnLabel.setFont(labelFont);
        formPanel.add(isbnLabel, gbc);

        gbc.gridx = 1;
        isbnField = new JTextField(20);
        formPanel.add(isbnField, gbc);

        // Genre
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setFont(labelFont);
        formPanel.add(genreLabel, gbc);

        gbc.gridx = 1;
        genreField = new JTextField(20);
        formPanel.add(genreField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(34, 34, 34));

        searchButton = new JButton("Search");
        updateButton = new JButton("Update");

        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);

        add(buttonPanel, BorderLayout.SOUTH);

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
