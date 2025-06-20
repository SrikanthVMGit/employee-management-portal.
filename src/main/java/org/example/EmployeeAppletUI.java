package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class EmployeeAppletUI extends JFrame {
    private final EmployeeService service = new EmployeeService();

    // UI Components
    JTextField nameField = new JTextField(15);
    JTextField emailField = new JTextField(15);
    JTextField deptField = new JTextField(15);
    JTextField skillsField = new JTextField(15);
    JTextField dateField = new JTextField(15);  // Format: yyyy-MM-dd
    JTextArea outputArea = new JTextArea(10, 50);

    public EmployeeAppletUI() {
        setTitle("Employee Management Portal");
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Name:")); inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(emailField);
        inputPanel.add(new JLabel("Department:")); inputPanel.add(deptField);
        inputPanel.add(new JLabel("Skills (comma):")); inputPanel.add(skillsField);
        inputPanel.add(new JLabel("Joining Date (yyyy-MM-dd):")); inputPanel.add(dateField);

        // Buttons
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete by Email");
        JButton searchBtn = new JButton("Search by Name");
        JButton statsBtn = new JButton("Show Department Stats");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(statsBtn);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // Button Listeners
        addBtn.addActionListener(e -> addEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        searchBtn.addActionListener(e -> searchByName());
        statsBtn.addActionListener(e -> showStats());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addEmployee() {
        try {
            Employee emp = getEmployeeFromFields();
            service.addEmployee(emp);
            outputArea.append("✅ Added: " + emp.getEmail() + "\n");
            clearFields();
        } catch (Exception ex) {
            outputArea.append("❌ Error adding employee: " + ex.getMessage() + "\n");
        }
    }

    private void updateEmployee() {
        try {
            String email = emailField.getText();
            Map<String, Object> updates = new HashMap<>();
            if (!deptField.getText().isEmpty()) updates.put("department", deptField.getText());
            if (!skillsField.getText().isEmpty()) updates.put("skills", Arrays.asList(skillsField.getText().split(",")));
            service.updateEmployee(email, updates);
            outputArea.append("✅ Updated: " + email + "\n");
            clearFields();
        } catch (Exception ex) {
            outputArea.append("❌ Error updating: " + ex.getMessage() + "\n");
        }
    }

    private void deleteEmployee() {
        try {
            String email = emailField.getText();
            service.deleteByEmail(email);
            outputArea.append("✅ Deleted: " + email + "\n");
            clearFields();
        } catch (Exception ex) {
            outputArea.append("❌ Error deleting: " + ex.getMessage() + "\n");
        }
    }

    private void searchByName() {
        try {
            String name = nameField.getText();
            outputArea.append("🔍 Search Results for '" + name + "':\n");
            service.searchByName(name); // Logs to console
            clearFields();
        } catch (Exception ex) {
            outputArea.append("❌ Error searching: " + ex.getMessage() + "\n");
        }
    }

    private void showStats() {
        try {
            outputArea.append("📊 Department Stats:\n");
            service.departmentStats(); // Logs to console
        } catch (Exception ex) {
            outputArea.append("❌ Error showing stats: " + ex.getMessage() + "\n");
        }
    }

    private Employee getEmployeeFromFields() throws Exception {
        String name = nameField.getText();
        String email = emailField.getText();
        String dept = deptField.getText();
        List<String> skills = Arrays.asList(skillsField.getText().split(","));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
        return new Employee(name, email, dept, skills, date);
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        deptField.setText("");
        skillsField.setText("");
        dateField.setText("");
    }
}
