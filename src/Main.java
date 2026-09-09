import java.util.Scanner;
import view.EmployeeView;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmployeeView empView = new EmployeeView(sc);

        boolean running = true;
        while (running) {
            try {
                int choice = empView.showMainMenu();
                switch (choice) {
                    case 1:
                        empView.reloadFromFile();
                        break;
                    case 2:
                        empView.addEmployee();
                        break;
                    case 3:
                        empView.updateEmployee();
                        break;
                    case 4:
                        empView.removeEmployee();
                        break;
                    case 5:
                        empView.searchEmployee();
                        break;
                    case 6:
                        empView.calculatePayroll();
                        break;
                    case 7:
                        empView.displayAllEmployees();
                        break;
                    case 8:
                        empView.saveToFile();
                        break;
                    case 9:
                        running = empView.quit();
                        break;
                    default:
                        empView.showMessage("  [!] Invalid option. Please choose from 1 to 9.");
                }
            } catch (Exception e) {
                empView.showMessage("  [!] Unexpected error: " + e.getMessage());
            }
        }

        sc.close();
    }
}
