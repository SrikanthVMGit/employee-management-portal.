package org.example;

import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        EmployeeService service = new EmployeeService();
        SwingUtilities.invokeLater(EmployeeAppletUI::new);
        // Add employee
        Employee emp = new Employee("John Doe", "john@example.com", "Engineering",
                Arrays.asList("Java", "MongoDB"), new Date());
        service.addEmployee(emp);

        // Update employee's department
        Map<String, Object> updates = new HashMap<>();
        updates.put("department", "Research");
        service.updateEmployee("john@example.com", updates);

        // Search by name
        service.searchByName("john");

        // Filter by department
        service.filterByDepartment("Research");

        // Search by skill
        service.searchBySkill("MongoDB");

        // Filter by joining date
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JANUARY, 1);
        Date from = cal.getTime();
        cal.set(2023, Calendar.DECEMBER, 31);
        Date to = cal.getTime();
        service.filterByJoiningDate(from, to);

        // Pagination example
        service.paginateAndSort(1, 5, "name", true);

        // Department stats
        service.departmentStats();

        // Delete example
        // service.deleteByEmail("john@example.com");
    }
}
