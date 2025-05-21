package dbms30;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UserDashboard extends JFrame {
    private String username;
    private int memberId;
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    public UserDashboard(String username) {
        this.username = username;
        this.memberId = DatabaseConnection.getMemberIdByUsername(username);

        if (memberId == -1) {
            JOptionPane.showMessageDialog(this, "User not found in members table.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("User Dashboard - " + username);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar setup
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 0, 10));
        sidebar.setBackground(new Color(45, 45, 45));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("User Panel", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sidebar.add(titleLabel);

        // Sidebar buttons
        JButton borrowedBooksBtn = createSidebarButton("View Borrowed Books");
        JButton checkFinesBtn = createSidebarButton("Check Fines");
        JButton holdBookBtn = createSidebarButton("Hold Book");
        JButton payFinesBtn = createSidebarButton("Pay Fines");
        JButton paymentHistoryBtn = createSidebarButton("Payment History");
        JButton logoutBtn = createSidebarButton("Logout");

        sidebar.add(borrowedBooksBtn);
        sidebar.add(checkFinesBtn);
        sidebar.add(holdBookBtn);
        sidebar.add(payFinesBtn);
        sidebar.add(paymentHistoryBtn);
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // Main content panel with CardLayout
        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);
        mainContentPanel.setBackground(new Color(34, 34, 34));

        // Adding content panels
        mainContentPanel.add(createPlaceholderPanel("Welcome, " + username + "!"), "default");
        mainContentPanel.add(createBorrowedBooksPanel(), "borrowedBooks");
        mainContentPanel.add(createFinesPanel(), "fines");
        mainContentPanel.add(createHoldBookPanel(), "holdBook");
        mainContentPanel.add(createPayFinesPanel(), "payFines");
        mainContentPanel.add(createPaymentHistoryPanel(), "paymentHistory");

        add(mainContentPanel, BorderLayout.CENTER);

        // Button actions to switch cards or logout
        borrowedBooksBtn.addActionListener(e -> {
            updateBorrowedBooksPanel();
            cardLayout.show(mainContentPanel, "borrowedBooks");
        });
        checkFinesBtn.addActionListener(e -> {
            updateFinesPanel();
            cardLayout.show(mainContentPanel, "fines");
        });
        holdBookBtn.addActionListener(e -> {
            updateHoldBookPanel();
            cardLayout.show(mainContentPanel, "holdBook");
        });
        payFinesBtn.addActionListener(e -> {
            updatePayFinesPanel();
            cardLayout.show(mainContentPanel, "payFines");
        });
        paymentHistoryBtn.addActionListener(e -> {
            updatePaymentHistoryPanel();
            cardLayout.show(mainContentPanel, "paymentHistory");
        });
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        cardLayout.show(mainContentPanel, "default");
        setVisible(true);
    }

    // Helper method for sidebar button style
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(64, 64, 64));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(85, 85, 85)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(85, 85, 85));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(64, 64, 64));
            }
        });

        return button;
    }

    private JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 34, 34));
        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }

    // Borrowed Books Panel and refresh method
    private JTextArea borrowedBooksArea;
    private JPanel createBorrowedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(34, 34, 34));
        borrowedBooksArea = new JTextArea();
        borrowedBooksArea.setEditable(false);
        borrowedBooksArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        borrowedBooksArea.setBackground(new Color(50, 50, 50));
        borrowedBooksArea.setForeground(Color.WHITE);
        borrowedBooksArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(borrowedBooksArea), BorderLayout.CENTER);
        return panel;
    }
    private void updateBorrowedBooksPanel() {
        ArrayList<String> books = DatabaseConnection.getBorrowedBooks(memberId);
        if (books.isEmpty()) {
            borrowedBooksArea.setText("No books currently borrowed.");
        } else {
            borrowedBooksArea.setText(String.join("\n", books));
        }
    }
    
 // Hold Book Panel
    private JComboBox<String> bookDropdown;
    private JPanel createHoldBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 34, 34));

        JLabel selectLabel = new JLabel("Select a book to hold:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        selectLabel.setForeground(Color.WHITE);

        bookDropdown = new JComboBox<>();
        JButton holdBtn = new JButton("Place Hold Request");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(selectLabel, gbc);
        gbc.gridx = 1;
        panel.add(bookDropdown, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(holdBtn, gbc);

        holdBtn.addActionListener(e -> {
            String selected = (String) bookDropdown.getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                int bookId = Integer.parseInt(selected.split(" - ")[0]);
                boolean success = DatabaseConnection.placeHoldRequest(memberId, bookId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Hold request placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to place hold request. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    private void updateHoldBookPanel() {
        bookDropdown.removeAllItems();
        ArrayList<String> availableBooks = DatabaseConnection.getAvailableBooksForHold();
        for (String book : availableBooks) {
            bookDropdown.addItem(book);
        }
    }


    // Fines Panel and refresh method
    private JLabel finesLabel;
    private JPanel createFinesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 34, 34));
        finesLabel = new JLabel();
        finesLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        finesLabel.setForeground(Color.WHITE);
        panel.add(finesLabel);
        return panel;
    }
    private void updateFinesPanel() {
        double fines = DatabaseConnection.getFines(memberId);
        finesLabel.setText("Total outstanding fines: ₹" + String.format("%.2f", fines));
    }

    // Pay Fines Panel
    private JPanel createPayFinesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 34, 34));

        JLabel prompt = new JLabel("Enter amount to pay:");
        prompt.setFont(new Font("Segoe UI", Font.BOLD, 16));
        prompt.setForeground(Color.WHITE);

        JTextField amountField = new JTextField(10);
        JButton payButton = new JButton("Pay");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(prompt, gbc);
        gbc.gridx = 1;
        panel.add(amountField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(payButton, gbc);

        payButton.addActionListener(e -> {
            double fines = DatabaseConnection.getFines(memberId);
            if (fines <= 0) {
                JOptionPane.showMessageDialog(this, "No outstanding fines to pay.");
                return;
            }

            String input = amountField.getText().trim();
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0 || amount > fines) {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount (max ₹" + fines + ").", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean success = DatabaseConnection.payFines(memberId, amount);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment successful! Amount paid: ₹" + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                    amountField.setText("");
                    updateFinesPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
    private void updatePayFinesPanel() {
        // No dynamic content to update currently
    }

    // Payment History Panel and refresh method
    private JTextArea paymentHistoryArea;
    private JPanel createPaymentHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(34, 34, 34));
        paymentHistoryArea = new JTextArea();
        paymentHistoryArea.setEditable(false);
        paymentHistoryArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        paymentHistoryArea.setBackground(new Color(50, 50, 50));
        paymentHistoryArea.setForeground(Color.WHITE);
        paymentHistoryArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(paymentHistoryArea), BorderLayout.CENTER);
        return panel;
    }
    private void updatePaymentHistoryPanel() {
        ArrayList<String> history = DatabaseConnection.getPaymentHistory(memberId);
        if (history.isEmpty()) {
            paymentHistoryArea.setText("No payment history found.");
        } else {
            paymentHistoryArea.setText(String.join("\n", history));
        }
    }
}
