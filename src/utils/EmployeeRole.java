package utils;

public class EmployeeRole {

    public enum Role {
        DEVELOPER,
        TESTER,
        MANAGER,
        HR
    }

    public static String getStringRole(EmployeeRole.Role role) {
        if (role == null) return "Unknown";
        switch (role) {
            case DEVELOPER: return "Developer";
            case TESTER:    return "Tester";
            case MANAGER:   return "Manager";
            case HR:        return "HR";
            default:        return "";
        }
    }

    public static EmployeeRole.Role getRole(String role) {
        if (role == null) return null;
        switch (role.trim()) {
            case "Developer": return Role.DEVELOPER;
            case "Tester":    return Role.TESTER;
            case "Manager":   return Role.MANAGER;
            case "HR":        return Role.HR;
            default:          return null;
        }
    }

    public static String[] getListRole() {
        return new String[]{"Developer", "Tester", "Manager", "HR"};
    }
}
