# Library Management System with Embedded MySQL Integration
<br> group member-
Prajwal singh 
Somya Awasthi
Katyani Shah
Tanishq yadav <br>

## 📚 Project Overview

This is a **desktop-based Library Management System** built using **Java Swing** for the GUI and **MySQL** for backend storage. The system is designed to streamline everyday library operations like managing books, members, lending, returns, and fine payments. It supports multiple user roles—**Admin**, **Librarian**, and **User**—each with their own dashboard and access to specific features.

## 🎯 Features

- 🔐 **Role-Based Authentication**
  - Admin, Librarian, and User roles with secure login and dashboard redirection

- 🛠️ **Admin Functionalities**
  - Add, edit, and delete books and members
  - View system reports and manage settings

- 📚 **Librarian Functionalities**
  - Issue, return, and books
  - Track due dates, manage overdue fines, and view hold request

- 👤 **User Functionalities**
  - View borrowed books
  - Check and pay fines
  - hold book(like reservation)
  - View payment history

- 💾 **Embedded MySQL Integration**
  - All database interactions are embedded directly in Java classes
  - Fast and efficient execution without the need for an external ORM

- 🔄 **Full CRUD Support**
  - Create, Read, Update, and Delete operations on books and member records

- 🖥️ **User-Friendly Interface**
  - Java Swing-based GUI with intuitive layout and controls

## 🛠️ Tech Stack

- **Frontend**: Java Swing
- **Backend**: Java
- **Database**: MySQL (Embedded within the Java code)
- **IDE**: IntelliJ IDEA / Eclipse (recommended)

## 🚀 Getting Started

### Prerequisites

- Java JDK 8 or higher
- MySQL Server
- MySQL Connector/J (JDBC Driver)

### Setup Instructions

1. **Clone the repository**
   ``bash
   git clone https://github.com/PrajwalSingh-git/library-management-system.git
   cd library-management-system
2. **Set up the MySQL database**

  ``Create a database named library_db

  ``Import the provided SQL schema (schema.sql) to create necessary tables

3. **Update Database Configuration**

  ``In the DatabaseConnection class, update your MySQL username, password, and DB_URL

4. **Run the Application**

  ``Compile and run the main Java class (e.g., Main.java)

  ``Login using default credentials or create new users via Admin


🙌 Contributing
Contributions are welcome! Feel free to open issues or submit pull requests for improvements or bug fixes.

📃 License
This project is open-source.
