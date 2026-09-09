package model;

import java.util.Objects;
import utils.EmployeeRole;

public class Employee {

    private String code;
    private String name;
    private EmployeeRole.Role role;
    private double baseSalary;
    private int workingDays;
    private double bonus;
    private boolean status;

    public Employee(String code, String name, EmployeeRole.Role role,
            double baseSalary, int workingDays, double bonus, boolean status) {
        this.code = code;
        this.name = name;
        this.role = role;
        this.baseSalary = baseSalary;
        this.workingDays = workingDays;
        this.bonus = bonus;
        this.status = status;
    }

    public String getCode()         { return code; }
    public String getName()         { return name; }
    public EmployeeRole.Role getRole() { return role; }
    public double getBaseSalary()   { return baseSalary; }
    public int getWorkingDays()     { return workingDays; }
    public double getBonus()        { return bonus; }
    public boolean getStatus()      { return status; }

    public void setCode(String code)           { this.code = code; }
    public void setName(String name)           { this.name = name; }
    public void setRole(EmployeeRole.Role role){ this.role = role; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public void setWorkingDays(int workingDays)  { this.workingDays = workingDays; }
    public void setBonus(double bonus)           { this.bonus = bonus; }
    public void setStatus(boolean status)        { this.status = status; }

    public double calSalary() {
        return baseSalary * workingDays + bonus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Employee)) return false;
        Employee other = (Employee) obj;
        return code != null && code.equalsIgnoreCase(other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code == null ? null : code.toUpperCase());
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-20s | %-10s | %10.2f | %4d | %8.2f | %s",
                code, name, EmployeeRole.getStringRole(role),
                baseSalary, workingDays, bonus,
                status ? "active" : "inactive");
    }
}
