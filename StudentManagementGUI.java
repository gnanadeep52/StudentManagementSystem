import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementGUI extends JFrame {
    private JTextField idField, nameField, ageField, searchField;
    private DefaultTableModel tableModel;

    public StudentManagementGUI() {
        setTitle("Student Management System with DB");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 4));
        idField = new JTextField(); nameField = new JTextField(); ageField = new JTextField();
        JButton addButton = new JButton("Add Student");

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(addButton);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // Search/Delete
        JPanel bottomPanel = new JPanel();
        searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");

        bottomPanel.add(new JLabel("ID:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(deleteButton);

        // Add to frame
        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Create table if not exists
        createTable();
        loadStudentsFromDB();

        // Add student
        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int age;

            try {
                age = Integer.parseInt(ageField.getText().trim());

                try (Connection conn = connect();
                     PreparedStatement ps = conn.prepareStatement("INSERT INTO students (id, name, age) VALUES (?, ?, ?)")) {
                    ps.setString(1, id);
                    ps.setString(2, name);
                    ps.setInt(3, age);
                    ps.executeUpdate();
                    tableModel.addRow(new Object[]{id, name, age});
                    idField.setText(""); nameField.setText(""); ageField.setText("");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Search student
        searchButton.addActionListener(e -> {
            String id = searchField.getText().trim();
            try (Connection conn = connect();
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE id = ?")) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Found: " +
                            rs.getString("name") + ", Age: " + rs.getInt("age"));
                } else {
                    JOptionPane.showMessageDialog(this, "Not found.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Delete student
        deleteButton.addActionListener(e -> {
            String id = searchField.getText().trim();
            try (Connection conn = connect();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {
                ps.setString(1, id);
                int deleted = ps.executeUpdate();
                if (deleted > 0) {
                    removeRowById(id);
                    JOptionPane.showMessageDialog(this, "Deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Not found.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    // DB connection
    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:students.db");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage());
            return null;
        }
    }

    // Create table
    private void createTable() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS students (id TEXT PRIMARY KEY, name TEXT, age INTEGER)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Table error: " + e.getMessage());
        }
    }

    // Load all students to table on startup
    private void loadStudentsFromDB() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("age")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load error: " + e.getMessage());
        }
    }

    // Remove row from table
    private void removeRowById(String id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(id)) {
                tableModel.removeRow(i);
                return;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementGUI::new);
    }
}
