# 🎓 Student Management System

A simple desktop application built with **Java Swing** and **SQLite** that allows users to manage student records (Add, View, Search, and Delete) with a graphical user interface.

---

## 💻 Features

- 🖼️ User-friendly GUI built using Java Swing
- 🗃️ Persistent storage using SQLite
- 🔍 Search student records by ID
- ➕ Add, view, and delete student entries
- 💾 Real-time updates with JDBC

---

## 🛠️ Tech Stack

- Java
- Swing (for GUI)
- SQLite (for database)
- JDBC (for database connection)

---

## 🚀 How to Run

1. **Download or clone** the project.

2. Make sure you have **Java installed** (JDK 8 or above).

3. Download the [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc) and place the `.jar` file (e.g., `sqlite-jdbc-3.49.1.0.jar`) in the project folder.

4. Open a terminal in the project directory and run:

   ### 🧑‍💻 Compile
   ```bash
   javac -cp ".;sqlite-jdbc-3.49.1.0.jar" *.java


  java -cp ".;sqlite-jdbc-3.49.1.0.jar" StudentManagementGUI


