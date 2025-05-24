package dbms30;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.security.SecureRandom;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Load MySQL driver and get connection
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Validate login for admin or librarian
    public static boolean validateLogin(String username, String password, String userType) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, userType);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get member ID by username
    public static int getMemberIdByUsername(String username) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT id FROM members WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }

    // View borrowed books by member ID
    public static ArrayList<String> getBorrowedBooks(int memberId) {
        ArrayList<String> borrowedBooks = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT books.title, lendings.issue_date, lendings.due_date FROM lendings " +
                         "JOIN books ON lendings.book_id = books.id WHERE lendings.member_id = ? AND lendings.return_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                borrowedBooks.add(rs.getString("title") + " (Issued: " + rs.getDate("issue_date") + ", Due: " + rs.getDate("due_date") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }
 // Search book by ID - returns Book object or null if not found
    public static Book searchBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("genre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update book info - returns true if update was successful
    public static boolean updateBook(int bookId, String title, String author, String isbn, String genre) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, genre = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, isbn);
            ps.setString(4, genre);
            ps.setInt(5, bookId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete book by ID - returns true if delete was successful
    public static boolean deleteBook(int bookId) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean placeHoldRequest(int memberId, int bookId) {
        // First check if book is available
        if (!getAvailableBooksForHold().contains(bookId + " - ")) {
            return false;
        }
        
        String sql = "INSERT INTO hold_requests (member_id, book_id, request_date, status) " +
                     "VALUES (?, ?, CURDATE(), 'Pending')";
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean isBookAvailableForHoldByIsbn(String isbn) {
        String sql = "SELECT b.id FROM books b " +
                     "LEFT JOIN lendings l ON b.id = l.book_id AND l.return_date IS NULL " +
                     "WHERE b.isbn = ? " +
                     "GROUP BY b.id " +
                     "HAVING COUNT(l.id) = 0";
        
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean placeHoldRequest(int memberId, String isbn) {
        Book book = searchBookByIsbn(isbn);
        if (book == null) {
            return false;
        }
        return placeHoldRequest(memberId, book.getId());
    }

    public static ArrayList<String> getAvailableBooksForHold() {
        ArrayList<String> books = new ArrayList<>();
        String sql = "SELECT b.id, b.title FROM books b " +
                     "LEFT JOIN lendings l ON b.id = l.book_id AND l.return_date IS NULL " +
                     "GROUP BY b.id, b.title " +
                     "HAVING COUNT(l.id) = 0"; // Books not currently lent out
        
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                books.add(id + " - " + title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }


    public static boolean addBook(String title, String author, String isbn, String genre) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO books (title, author, isbn, genre) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, isbn);
            stmt.setString(4, genre);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean returnBook(int lendingId) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE lendings SET return_date = CURDATE() WHERE id = ? AND return_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lendingId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;  // return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static boolean addMember(String name, String username, String email, String membershipType) {
        String checkUserSQL = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertMemberSQL = "INSERT INTO members (name, username, email, membership_type) VALUES (?, ?, ?, ?)";
        String insertUserSQL = "INSERT INTO users (username, password, role) VALUES (?, ?, 'User')";

        try (Connection conn = getConnection()) {
            // Check if username already exists in users table
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSQL)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, "Username already exists. Choose a different one.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            // Generate random password
            String randomPassword = generateRandomPassword(8);

            conn.setAutoCommit(false);
            try (
                PreparedStatement psMember = conn.prepareStatement(insertMemberSQL);
                PreparedStatement psUser = conn.prepareStatement(insertUserSQL)
            ) {
                // Insert into members table
                psMember.setString(1, name);
                psMember.setString(2, username);
                psMember.setString(3, email);
                psMember.setString(4, membershipType);
                psMember.executeUpdate();

                // Insert into users table
                psUser.setString(1, username);
                psUser.setString(2, randomPassword);
                psUser.executeUpdate();

                conn.commit();

                JOptionPane.showMessageDialog(null, "Member added successfully!\nGenerated password: " + randomPassword, "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public static boolean renewBook(int lendingId) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE lendings SET due_date = DATE_ADD(due_date, INTERVAL 7 DAY) WHERE id = ? AND return_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lendingId);

            // Execute the update query
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // If the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if any error occurs
        }
    }

    public static boolean issueBook(int bookId, int memberId) {
        try (Connection conn = getConnection()) {
            // Check if book is already issued
            String checkSql = "SELECT * FROM lendings WHERE book_id = ? AND return_date IS NULL";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, bookId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                return false;  // Book is already issued
            }

            // Check for pending hold requests
            String holdCheckSql = "SELECT * FROM hold_requests WHERE book_id = ? AND status = 'Approved'";
            PreparedStatement holdCheckPs = conn.prepareStatement(holdCheckSql);
            holdCheckPs.setInt(1, bookId);
            ResultSet holdRs = holdCheckPs.executeQuery();

            if (holdRs.next()) {
                return false;  // Book is on hold for someone
            }

            // Issue the book
            String sql = "INSERT INTO lendings (book_id, member_id, issue_date, due_date) " +
                         "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean hasPendingHoldRequests(int bookId) {
        String sql = "SELECT COUNT(*) FROM hold_requests WHERE book_id = ? AND status = 'Pending'";
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // View fines for a member
    public static double getFines(int memberId) {
        double totalFines = 0;
        double totalPaid = 0;

        try (Connection conn = getConnection()) {
            // 1. Get total fine from overdue books (₹5/day)
            String fineQuery = "SELECT SUM(DATEDIFF(CURDATE(), due_date) * 5) AS total_fine " +
                               "FROM lendings WHERE member_id = ? AND return_date IS NULL AND due_date < CURDATE()";
            PreparedStatement fineStmt = conn.prepareStatement(fineQuery);
            fineStmt.setInt(1, memberId);
            ResultSet fineRs = fineStmt.executeQuery();
            if (fineRs.next()) {
                totalFines = fineRs.getDouble("total_fine");
            }

            // 2. Get total amount paid
            String paidQuery = "SELECT SUM(amount_paid) AS total_paid FROM payments WHERE member_id = ?";
            PreparedStatement paidStmt = conn.prepareStatement(paidQuery);
            paidStmt.setInt(1, memberId);
            ResultSet paidRs = paidStmt.executeQuery();
            if (paidRs.next()) {
                totalPaid = paidRs.getDouble("total_paid");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        double outstanding = totalFines - totalPaid;
        return Math.max(outstanding, 0); // Never return negative fine
    }


    // Pay fines for a member
    public static boolean payFines(int memberId, double amount) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO payments (member_id, amount_paid) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            ps.setDouble(2, amount);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get payment history for a member
    public static ArrayList<String> getPaymentHistory(int memberId) {
        ArrayList<String> payments = new ArrayList<>();
        try (Connection conn = getConnection()) {
        	String sql = "SELECT amount_paid, payment_date FROM payments WHERE member_id = ?";
        	PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            payments.add("Amount: " + rs.getDouble("amount_paid") + " | Date: " + rs.getTimestamp("payment_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
}
