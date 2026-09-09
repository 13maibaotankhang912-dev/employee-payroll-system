package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import utils.EmployeeRole;
import validation.EmployeeValidator;

public class EmployeeFileHelper implements IFileReadWrite<Employee> {

    private static final String DEFAULT_FILE_NAME = "src/file/employees.txt";

    private Path resolveDataPath() {
        String[] candidates = {
            DEFAULT_FILE_NAME,
            "file/employees.txt",
            "employees.txt"
        };
        for (String candidate : candidates) {
            Path path = Paths.get(candidate);
            if (Files.exists(path)) {
                return path;
            }
        }
        return Paths.get(DEFAULT_FILE_NAME);
    }

    @Override
    public List<Employee> read() throws Exception {
        List<Employee> list = new ArrayList<>();
        Path path = resolveDataPath();
        if (!Files.exists(path)) return list;

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty()) continue;
                try {
                    Employee emp = convertToEmployee(line, list);
                    list.add(emp);
                } catch (Exception ex) {
                    System.out.println("[WARNING] Skipped invalid data at line " + lineNum
                            + " (" + ex.getMessage() + "): " + line.trim());
                }
            }
        }
        return list;
    }

    @Override
    public boolean write(List<Employee> list) throws Exception {
        Path path = resolveDataPath();
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                for (Employee emp : list) {
                    // Validate lại trước khi ghi để tránh lưu dữ liệu không hợp lệ ra file.
                    EmployeeValidator.validateEmployee(emp);
                    bw.write(convertToString(emp));
                    bw.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("[ERROR] Cannot write to file: " + e.getMessage());
            return false;
        }
    }

    private String convertToString(Employee emp) {
        return String.format("%s, %s, %s, %.0f, %d, %.0f, %s",
                emp.getCode(),
                emp.getName(),
                EmployeeRole.getStringRole(emp.getRole()),
                emp.getBaseSalary(),
                emp.getWorkingDays(),
                emp.getBonus(),
                emp.getStatus() ? "active" : "inactive");
    }

    private Employee convertToEmployee(String str, List<Employee> currentList) throws Exception {
        String[] p = str.split(",", -1);
        if (p.length != 7) {
            throw new Exception("wrong field count (" + p.length + "/7)");
        }

        String code = p[0].trim().toUpperCase();
        String name = p[1].trim();
        String roleStr = p[2].trim();
        String baseSalStr = p[3].trim();
        String workDaysStr = p[4].trim();
        String bonusStr = p[5].trim();
        String statusStr = p[6].trim();

        EmployeeRole.Role role = EmployeeValidator.parseRole(roleStr);

        double baseSal;
        try {
            baseSal = Double.parseDouble(baseSalStr);
        } catch (NumberFormatException e) {
            throw new Exception("invalid Base Salary '" + baseSalStr + "'");
        }

        int workDays;
        try {
            workDays = Integer.parseInt(workDaysStr);
        } catch (NumberFormatException e) {
            throw new Exception("invalid Working Days '" + workDaysStr + "'");
        }

        double bonus;
        try {
            bonus = Double.parseDouble(bonusStr);
        } catch (NumberFormatException e) {
            throw new Exception("invalid Bonus '" + bonusStr + "'");
        }

        boolean status = EmployeeValidator.parseStatus(statusStr);

        Employee employee = new Employee(code, name, role, baseSal, workDays, bonus, status);
        EmployeeValidator.validateNewEmployee(employee, currentList);
        return employee;
    }
}
