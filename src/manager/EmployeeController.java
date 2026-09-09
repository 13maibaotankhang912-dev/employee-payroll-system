package manager;

import file.EmployeeFileHelper;
import file.IFileReadWrite;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import utils.EmployeeRole;
import validation.EmployeeValidator;

public class EmployeeController {

    private final List<Employee> empList = new ArrayList<>();
    private final IFileReadWrite<Employee> fileHelper = new EmployeeFileHelper();

    public EmployeeController() {
        loadFromFile();
    }

    public List<Employee> getAllEmployee() {
        return empList;
    }

    public void addEmployee(Employee employee) throws Exception {
        EmployeeValidator.validateNewEmployee(employee, empList);
        empList.add(employee);
    }

    public boolean updateEmployee(Employee updated) {
        if (updated == null || updated.getCode() == null) return false;
        try {
            EmployeeValidator.validateEmployee(updated);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
        for (int i = 0; i < empList.size(); i++) {
            if (empList.get(i).getCode().equalsIgnoreCase(updated.getCode())) {
                empList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean deleteEmployee(String id) {
        Employee emp = findByCode(id);
        if (emp != null) {
            empList.remove(emp);
            return true;
        }
        return false;
    }

    public Employee findByCode(String code) {
        if (code == null) return null;
        for (Employee e : empList) {
            if (e.getCode().equalsIgnoreCase(code.trim())) return e;
        }
        return null;
    }

    public List<Employee> searchByName(String name) {
        return searchByAttribute("name", name);
    }

    public List<Employee> searchByRole(String roleStr) {
        return searchByAttribute("role", roleStr);
    }

    public List<Employee> searchByStatus(boolean active) {
        return searchByAttribute("status", active ? "active" : "inactive");
    }

    public List<Employee> searchByAttribute(String attribute, String query) {
        List<Employee> result = new ArrayList<>();
        if (attribute == null || query == null) return result;
        String cleanQuery = query.trim().toLowerCase();
        String cleanAttr = attribute.trim().toLowerCase();
        for (Employee e : empList) {
            if (isMatched(e, cleanAttr, cleanQuery)) result.add(e);
        }
        return result;
    }

    private boolean isMatched(Employee e, String attribute, String query) {
        String codeText = e.getCode().toLowerCase();
        String nameText = e.getName().toLowerCase();
        String roleText = EmployeeRole.getStringRole(e.getRole()).toLowerCase();
        String statusText = e.getStatus() ? "active" : "inactive";
        switch (attribute) {
            case "id":
            case "code":
                return codeText.equals(query);
            case "name":
                return nameText.contains(query);
            case "role":
                return roleText.equals(query);
            case "status":
                return statusText.equals(query);
            default:
                return codeText.equals(query) || nameText.contains(query)
                        || roleText.equals(query) || statusText.equals(query);
        }
    }

    public List<Employee> getActiveEmployees() {
        return searchByStatus(true);
    }

    public double getTotalPayroll() {
        double total = 0;
        for (Employee e : getActiveEmployees()) {
            total += e.calSalary();
        }
        return total;
    }

    public boolean saveToFile() {
        try {
            return fileHelper.write(empList);
        } catch (Exception e) {
            System.out.println("[ERROR] Cannot save: " + e.getMessage());
            return false;
        }
    }


    public boolean loadFromFile() {
        try {
            List<Employee> loaded = fileHelper.read();
            empList.clear();
            if (loaded != null) empList.addAll(loaded);
            return true;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to load data: " + e.getMessage());
            return false;
        }
    }

    public boolean isExistCode(String code) {
        return findByCode(code) != null;
    }
}
