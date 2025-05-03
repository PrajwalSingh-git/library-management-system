package dbms30;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddBookGUI extends JFrame {
    private JTextField titleField, authorField, isbnField;
    private JButton addButton, cancelButton;

    public AddBookGUI() {
        setTitle("Add Book");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Labels and fields
        add(new JLabel("Title:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField();
        add(authorField);

        add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        add(isbnField);

        // Buttons panel (to keep them aligned at the bottom)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton = new JButton("Add Book");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseConnection.addBook(title, author, isbn);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                titleField.setText("");
                authorField.setText("");
                isbnField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    public static void main(String[] args) {
        new AddBookGUI();
    }
}
