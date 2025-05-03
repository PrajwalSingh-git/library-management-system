-- Reset and create database
DROP DATABASE IF EXISTS library_db;
CREATE DATABASE library_db;
USE library_db;

-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role ENUM('Admin', 'Librarian', 'User') NOT NULL
);

-- Books table
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100),
    isbn VARCHAR(20) NOT NULL UNIQUE
);

-- Members table
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    membership_type VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE
);

-- Settings table
CREATE TABLE settings (
    id INT PRIMARY KEY,
    lending_days INT NOT NULL,
    fine_per_day DOUBLE NOT NULL
);

-- Lendings table
CREATE TABLE lendings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE DEFAULT NULL,
    fine DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Hold requests table
CREATE TABLE hold_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    request_date DATE NOT NULL,
    status ENUM('Pending', 'Approved', 'Cancelled') DEFAULT 'Pending',
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Payments table
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    amount_paid DECIMAL(10,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Indexes for performance
CREATE INDEX idx_member_book ON lendings(member_id, book_id);
CREATE INDEX idx_payment_member ON payments(member_id);

-- Initial data
INSERT INTO users (username, password, role) VALUES
('admin1', 'admin123', 'Admin'),
('librarian1', 'lib123', 'Librarian'),
('user1', 'user123', 'User'),
('user2', 'password', 'User');

INSERT INTO books (title, author, isbn) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565'),
('To Kill a Mockingbird', 'Harper Lee', '9780060935467'),
('1984', 'George Orwell', '9780451524935'),
('Pride and Prejudice', 'Jane Austen', '9780141439518'),
('Moby Dick', 'Herman Melville', '9781503280786');

INSERT INTO members (name, membership_type, username) VALUES
('Alice Johnson', 'Gold', 'user1'),
('Bob Smith', 'Silver', 'user2'),
('Charlie Davis', 'Platinum', NULL),
('Diana Evans', 'Gold', NULL);

INSERT INTO settings (id, lending_days, fine_per_day) VALUES
(1, 14, 2.5)
ON DUPLICATE KEY UPDATE lending_days = VALUES(lending_days), fine_per_day = VALUES(fine_per_day);

INSERT INTO lendings (member_id, book_id, issue_date, due_date, return_date, fine) VALUES
(1, 1, '2025-04-01', '2025-04-15', '2025-04-14', 0.00),
(2, 2, '2025-04-10', '2025-04-24', '2025-04-28', 10.00),
(3, 3, '2025-04-05', '2025-04-19', NULL, NULL),
(4, 4, '2025-04-12', '2025-04-26', '2025-04-25', 0.00);

INSERT INTO hold_requests (member_id, book_id, request_date, status) VALUES
(1, 3, '2025-04-18', 'Pending'),
(2, 4, '2025-04-20', 'Approved'),
(3, 1, '2025-04-22', 'Cancelled'),
(4, 2, '2025-04-21', 'Pending');
