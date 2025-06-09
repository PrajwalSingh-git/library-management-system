

---

# 📚 Library Management System

### 🚀 Java Swing + MySQL Integrated Desktop Application..

---

## 🔍 Overview

The **Library Management System** is a powerful desktop application built using **Java Swing** for the user interface and **MySQL** for database operations. Designed for educational institutions and public libraries, it streamlines the process of managing books, users, and transactions with a focus on usability and role-based access.

This system supports multiple user types—**Admin**, **Librarian**, and **User**—each with tailored functionality and access rights.

---

## 🎯 Core Features

### 🔐 Role-Based Authentication

* Secure login system with distinct dashboards for Admins, Librarians, and Users.

### 🛠️ Admin Panel

* Manage book inventory and member records (add/edit/delete).
* Access system-wide settings and generate reports.

### 📚 Librarian Panel

* Issue and return books with due date tracking.
* Handle hold/reservation requests.
* Monitor fines for overdue books.

### 👤 User Dashboard

* View borrowed books and due dates.
* Place and manage book reservations.
* Pay fines and view payment history.

### 💾 Embedded MySQL Integration

* Direct JDBC-based database interaction.
* Eliminates the need for external ORM tools.

### 🔁 Full CRUD Operations

* Seamless Create, Read, Update, and Delete functionality for books and members.

### 🖥️ Intuitive User Interface

* Responsive and easy-to-navigate GUI built entirely with Java Swing.

---

## 🛠️ Tech Stack

| Layer        | Technology                       |
| ------------ | -------------------------------- |
| **Frontend** | Java Swing                       |
| **Backend**  | Java                             |
| **Database** | MySQL (JDBC)                     |
| **IDE**      | IntelliJ / Eclipse (recommended) |

---

## ⚙️ Getting Started

### ✅ Prerequisites

* Java JDK 8 or higher
* MySQL Server installed and running
* MySQL Connector/J (JDBC driver)

### 📦 Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/PrajwalSingh-git/library-management-system.git
   cd library-management-system
   ```

2. **Set Up MySQL Database**

   * Create a database named `library_db`.
   * Import the `schema.sql` file to initialize required tables.

3. **Configure Database Credentials**

   * Open the `DatabaseConnection` Java class.
   * Update the `DB_URL`, `username`, and `password` with your MySQL details.

4. **Run the Application**

   * Compile and run the main Java class (e.g., `Main.java`).
   * Log in with default or registered credentials.

---

## 🤝 Contribution Guidelines

Contributions are welcome! 🚀
To contribute:

* Fork this repository.
* Create a feature branch.
* Commit your changes.
* Open a Pull Request with a brief description.

You can also open [issues](https://github.com/PrajwalSingh-git/library-management-system/issues) for bugs or enhancement ideas.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
Feel free to use, modify, and distribute it with attribution.

