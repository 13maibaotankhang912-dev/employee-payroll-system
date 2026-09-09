package validation;

import java.util.List;
import model.Employee;
import utils.EmployeeRole;


public class EmployeeValidator {

    private static final String ID_PATTERN = "E\\d{3}";

    public static final int MIN_WORKING_DAYS = 0;
    public static final int MAX_WORKING_DAYS = 26;

    private EmployeeValidator() {
    }

    public static boolean isValidIdFormat(String code) {
        return code != null && code.trim().toUpperCase().matches(ID_PATTERN);
    }

    public static void validateIdFormat(String code) throws Exception {
        if (!isValidIdFormat(code)) {
            throw new Exception("Employee ID must be in format E followed by 3 digits (e.g., E001).");
        }
    }

    public static void validateName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Employee name cannot be empty.");
        }
        if (name.indexOf(',') >= 0 || name.indexOf('\n') >= 0 || name.indexOf('\r') >= 0) {
            throw new Exception("Employee name cannot contain comma or newline characters "
                    + "(reserved as field separator in the data file).");
        }
    }

    public static EmployeeRole.Role parseRole(String roleText) throws Exception {
        EmployeeRole.Role role = EmployeeRole.getRole(roleText);
        if (role == null) {
            throw new Exception("Role must be Developer, Tester, Manager or HR.");
        }
        return role;
    }

    public static void validateBaseSalary(double baseSalary) throws Exception {
        if (baseSalary <= 0) {
            throw new Exception("Base salary must be a positive number.");
        }
    }

    public static void validateWorkingDays(int workingDays) throws Exception {
        if (workingDays < MIN_WORKING_DAYS || workingDays > MAX_WORKING_DAYS) {
            throw new Exception("Working days must be from " + MIN_WORKING_DAYS
                    + " to " + MAX_WORKING_DAYS + ".");
        }
    }

    public static void validateBonus(double bonus) throws Exception {
        if (bonus < 0) {
            throw new Exception("Bonus must be greater than or equal to 0.");
        }
    }

    public static boolean parseStatus(String statusText) throws Exception {
        if (statusText == null) {
            throw new Exception("Status must be active or inactive.");
        }
        String clean = statusText.trim().toLowerCase();
        if (clean.equals("active")) return true;
        if (clean.equals("inactive")) return false;
        throw new Exception("Status must be active or inactive.");
    }

    public static boolean isDuplicateId(String code, List<Employee> employees) {
        if (code == null || employees == null) return false;
        for (Employee e : employees) {
            if (e != null && e.getCode() != null && e.getCode().equalsIgnoreCase(code.trim())) {
                return true;
            }
        }
        return false;
    }

    public static void validateEmployee(Employee employee) throws Exception {
        if (employee == null) {
            throw new Exception("Employee object cannot be null.");
        }
        validateIdFormat(employee.getCode());
        validateName(employee.getName());
        if (employee.getRole() == null) {
            throw new Exception("Employee role is invalid.");
        }
        validateBaseSalary(employee.getBaseSalary());
        validateWorkingDays(employee.getWorkingDays());
        validateBonus(employee.getBonus());
    }

    public static void validateNewEmployee(Employee employee, List<Employee> employees) throws Exception {
        validateEmployee(employee);
        if (isDuplicateId(employee.getCode(), employees)) {
            throw new Exception("Employee ID \"" + employee.getCode() + "\" already exists.");
        }
    }
}
