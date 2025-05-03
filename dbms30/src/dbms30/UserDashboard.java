package dbms30;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UserDashboard extends JFrame {
    private String username;
    private int memberId;

    public UserDashboard(String username) {
        this.username = username;
        this.memberId = DatabaseConnection.getMemberIdByUsername(username);

        if (memberId == -1) {
            JOptionPane.showMessageDialog(this, "User not found in members table.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Close the window
            return;
        }

        setTitle("User Dashboard - " + username);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel layout
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Buttons for options
        JButton viewBooksBtn = new JButton("View Borrowed Books");
        JButton viewFinesBtn = new JButton("Check Fines");
        JButton payFinesBtn = new JButton("Pay Fines");
        JButton paymentHistoryBtn = new JButton("Payment History");
        JButton logoutBtn = new JButton("Logout");

        // Add to panel
        panel.add(viewBooksBtn);
        panel.add(viewFinesBtn);
        panel.add(payFinesBtn);
        panel.add(paymentHistoryBtn);
        panel.add(logoutBtn);

        // Add panel
        add(panel);

        // Button listeners
        viewBooksBtn.addActionListener(e -> showBorrowedBooks());
        viewFinesBtn.addActionListener(e -> showFines());
        payFinesBtn.addActionListener(e -> payFines());
        paymentHistoryBtn.addActionListener(e -> showPaymentHistory());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        setVisible(true);
    }

    private void showBorrowedBooks() {
        ArrayList<String> books = DatabaseConnection.getBorrowedBooks(memberId);
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books currently borrowed.");
        } else {
            JOptionPane.showMessageDialog(this, String.join("\n", books), "Borrowed Books", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showFines() {
        double fines = DatabaseConnection.getFines(memberId);
        JOptionPane.showMessageDialog(this, "Total outstanding fines: ₹" + String.format("%.2f", fines), "Fines", JOptionPane.INFORMATION_MESSAGE);
    }

    private void payFines() {
        double fines = DatabaseConnection.getFines(memberId);
        if (fines <= 0) {
            JOptionPane.showMessageDialog(this, "No outstanding fines to pay.");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Enter amount to pay (max ₹" + fines + "):");
        if (input != null) {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0 || amount > fines) {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean success = DatabaseConnection.payFines(memberId, amount);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment successful! Amount paid: ₹" + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        }
    }

    private void showPaymentHistory() {
        ArrayList<String> history = DatabaseConnection.getPaymentHistory(memberId);
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No payment history found.");
        } else {
            JOptionPane.showMessageDialog(this, String.join("\n", history), "Payment History", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
