package view;

import java.util.List;
import java.util.Scanner;
import manager.EmployeeController;
import model.Employee;
import utils.EmployeeRole;
import validation.EmployeeValidator;

public class EmployeeView extends View {

    private final EmployeeController controller;
    private boolean isChanged = false;

    public EmployeeView(Scanner sc) {
        super(sc);
        this.controller = new EmployeeController();
    }

    public int showMainMenu() {
        String[] options = {
            "Load employee data from file (reload)",
            "Add a new employee",
            "Update employee information",
            "Remove an employee by ID",
            "Search employees by attribute",
            "Calculate monthly payroll",
            "Display employee list",
            "Save data to file",
            "Quit"
        };
        Menu.showMenu("EMPLOYEE PAYROLL MANAGEMENT SYSTEM", options);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void reloadFromFile() {
        System.out.println("\n--- Reload Data From File ---");
        if (controller.loadFromFile()) {
            isChanged = false;
            showMessage("  [OK] Data reloaded from file.");
            displayAllEmployees();
        } else {
            showMessage("  [!] Failed to reload data.");
        }
    }

    public void addEmployee() {
        System.out.println("\n--- Add New Employee ---");

        String code;
        while (true) {
            code = readString("Enter Employee ID (format E001): ").toUpperCase();
            try {
                EmployeeValidator.validateIdFormat(code);
                if (controller.isExistCode(code)) {
                    showMessage("  [!] ID \"" + code + "\" already exists. Please enter a different ID.");
                } else {
                    break;
                }
            } catch (Exception e) {
                showMessage("  [!] " + e.getMessage());
            }
        }

        String name = readString("Enter Employee Name: ");
        EmployeeRole.Role role = readRole();
        double baseSalary = readDouble("Enter Base Salary (> 0): ", 0.01);
        int workingDays = readInt("Enter Working Days (" + EmployeeValidator.MIN_WORKING_DAYS
                        + "-" + EmployeeValidator.MAX_WORKING_DAYS + "): ",
                EmployeeValidator.MIN_WORKING_DAYS, EmployeeValidator.MAX_WORKING_DAYS);
        double bonus = readDouble("Enter Bonus (>= 0): ", 0);
        boolean status = confirm("Is this employee currently active?");

        Employee emp = new Employee(code, name, role, baseSalary, workingDays, bonus, status);

        try {
            controller.addEmployee(emp);
            isChanged = true;
            showMessage("  [OK] Employee \"" + name + "\" added successfully.");
        } catch (Exception e) {
            showMessage("  [!] " + e.getMessage());
        }
    }

    public void updateEmployee() {
        System.out.println("\n--- Update Employee ---");
        String code = readString("Enter Employee ID to update: ").toUpperCase();
        Employee emp = controller.findByCode(code);
        if (emp == null) {
            showMessage("  [!] Employee with ID \"" + code + "\" not found.");
            return;
        }

        System.out.println("  Current: " + emp);
        System.out.println("  (Press Enter to keep current value)\n");

        System.out.print("  New Name [" + emp.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) emp.setName(name);

        System.out.print("  New Role [" + EmployeeRole.getStringRole(emp.getRole()) + "] (leave blank to keep): ");
        String roleInput = sc.nextLine().trim();
        if (!roleInput.isEmpty()) {
            EmployeeRole.Role newRole = EmployeeRole.getRole(roleInput);
            if (newRole != null) {
                emp.setRole(newRole);
            } else {
                showMessage("  [!] Invalid role. Role unchanged.");
            }
        }

        double newSalary = readDoubleOptional(
                "  New Base Salary [" + emp.getBaseSalary() + "]: ",
                emp.getBaseSalary(), 0.01);
        emp.setBaseSalary(newSalary);

        int newDays = readIntOptional(
                "  New Working Days [" + emp.getWorkingDays() + "] ("
                        + EmployeeValidator.MIN_WORKING_DAYS + "-" + EmployeeValidator.MAX_WORKING_DAYS + "): ",
                emp.getWorkingDays(), EmployeeValidator.MIN_WORKING_DAYS, EmployeeValidator.MAX_WORKING_DAYS);
        emp.setWorkingDays(newDays);

        double newBonus = readDoubleOptional(
                "  New Bonus [" + emp.getBonus() + "]: ",
                emp.getBonus(), 0);
        emp.setBonus(newBonus);

        String currentStatus = emp.getStatus() ? "active" : "inactive";
        System.out.print("  New Status [" + currentStatus + "] (active/inactive, Enter to keep): ");
        String statusInput = sc.nextLine().trim().toLowerCase();
        if (statusInput.equals("active")) {
            emp.setStatus(true);
        } else if (statusInput.equals("inactive")) {
            emp.setStatus(false);
        } else if (!statusInput.isEmpty()) {
            showMessage("  [!] Invalid status. Status unchanged.");
        }

        if (controller.updateEmployee(emp)) {
            isChanged = true;
            showMessage("  [OK] Employee updated successfully.");
        } else {
            showMessage("  [!] Update failed.");
        }
    }

    public void removeEmployee() {
        System.out.println("\n--- Remove Employee ---");
        String code = readString("Enter Employee ID to remove: ").toUpperCase();
        Employee emp = controller.findByCode(code);
        if (emp == null) {
            showMessage("  [!] Employee with ID \"" + code + "\" not found.");
            return;
        }

        System.out.println("  Found: " + emp);
        if (confirm("Are you sure you want to remove this employee?")) {
            controller.deleteEmployee(code);
            isChanged = true;
            showMessage("  [OK] Employee \"" + code + "\" removed.");
        } else {
            showMessage("  [INFO] Remove cancelled.");
        }
    }

    public void searchEmployee() {
        System.out.println("\n--- Search Employee ---");
        String[] options = {"Search by ID", "Search by Name", "Search by Role", "Search by Status", "Search all columns"};
        Menu.showMenu("Search By", options);

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            showMessage("  [!] Invalid choice.");
            return;
        }

        List<Employee> result;
        switch (choice) {
            case 1:
                String id = readString("Enter Employee ID: ").toUpperCase();
                Employee found = controller.findByCode(id);
                if (found == null) {
                    showMessage("  [!] No employee found with ID \"" + id + "\".");
                } else {
                    printHeader();
                    System.out.println("  " + found);
                }
                return;

            case 2:
                String name = readString("Enter name (or partial name): ");
                result = controller.searchByName(name);
                break;

            case 3:
                showMessage("  Roles: Developer | Tester | Manager | HR");
                String roleStr = readString("Enter role: ");
                result = controller.searchByRole(roleStr);
                break;

            case 4:
                boolean active = confirm("Search for active employees?");
                result = controller.searchByStatus(active);
                break;

            case 5:
                String query = readString("Enter keyword: ");
                result = controller.searchByAttribute("all", query);
                break;

            default:
                showMessage("  [!] Invalid choice.");
                return;
        }

        if (result.isEmpty()) {
            showMessage("  [!] No employees found.");
        } else {
            printHeader();
            for (Employee e : result) System.out.println("  " + e);
            showMessage("  Total found: " + result.size());
        }
    }

    public void calculatePayroll() {
        System.out.println("\n--- Monthly Payroll Report (Active Employees Only) ---");
        List<Employee> activeList = controller.getActiveEmployees();

        if (activeList.isEmpty()) {
            showMessage("  [!] No active employees found.");
            return;
        }

        printHeader();
        for (Employee e : activeList) {
            System.out.printf("  %-6s | %-20s | %12.2f%n",
                    e.getCode(), e.getName(), e.calSalary());
        }
        System.out.println("  " + repeatChar('-', 50));
        System.out.printf("  %-29s | %12.2f%n", "TOTAL PAYROLL", controller.getTotalPayroll());
        System.out.println("  Formula: Total Salary = Base Salary x Working Days + Bonus");
    }

    public void displayAllEmployees() {
        System.out.println("\n--- Employee List ---");
        List<Employee> list = controller.getAllEmployee();

        if (list.isEmpty()) {
            showMessage("  [!] No employee records found.");
            return;
        }

        printHeader();
        for (Employee e : list) System.out.println("  " + e);
        showMessage("  Total: " + list.size() + " employee(s).");
    }

    public void saveToFile() {
        System.out.println("\n--- Save to File ---");
        if (controller.saveToFile()) {
            isChanged = false;
            showMessage("  [OK] Data saved to employees.txt successfully.");
        } else {
            showMessage("  [!] Failed to save data.");
        }
    }

    public boolean quit() {
        System.out.println("\n--- Quit ---");
        if (isChanged && confirm("You have unsaved changes. Save before exiting?")) {
            saveToFile();
        }
        showMessage("  Goodbye!");
        return false;
    }

    private EmployeeRole.Role readRole() {
        String[] roles = EmployeeRole.getListRole();
        System.out.println("  Roles: ");
        for (int i = 0; i < roles.length; i++) {
            System.out.println("    " + (i + 1) + ". " + roles[i]);
        }
        while (true) {
            int choice = readInt("  Select role [1-" + roles.length + "]: ", 1, roles.length);
            EmployeeRole.Role role = EmployeeRole.getRole(roles[choice - 1]);
            if (role != null) return role;
        }
    }

    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }

    private void printHeader() {
        System.out.println("  " + repeatChar('-', 75));
        System.out.printf("  %-6s | %-20s | %-10s | %10s | %4s | %8s | %s%n",
                "ID", "Name", "Role", "BaseSalary", "Days", "Bonus", "Status");
        System.out.println("  " + repeatChar('-', 75));
    }

    public EmployeeController getController() {
        return controller;
    }
}
