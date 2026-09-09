package view;

import java.util.Scanner;

public class View {

    protected final Scanner sc;

    public View(Scanner sc) {
        this.sc = sc;
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val < min || val > max) {
                    System.out.println("  [!] Please enter a number from " + min + " to " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Try again.");
            }
        }
    }

    public int readIntOptional(String prompt, int oldValue, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = sc.nextLine().trim();
                if (line.isEmpty()) return oldValue;
                int val = Integer.parseInt(line);
                if (val < min || val > max) {
                    System.out.println("  [!] Please enter a number from " + min + " to " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Try again.");
            }
        }
    }


    public double readDouble(String prompt, double min) {
        while (true) {
            try {
                System.out.print(prompt);
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val < min) {
                    System.out.println("  [!] Value must be >= " + min + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Try again.");
            }
        }
    }

    public double readDoubleOptional(String prompt, double oldValue, double min) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = sc.nextLine().trim();
                if (line.isEmpty()) return oldValue;
                double val = Double.parseDouble(line);
                if (val < min) {
                    System.out.println("  [!] Value must be >= " + min + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Try again.");
            }
        }
    }


    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = sc.nextLine().trim();
            if (!val.isEmpty()) return val;
            System.out.println("  [!] Input cannot be empty.");
        }
    }

    public String readStringOptional(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y")) return true;
            if (ans.equals("n")) return false;
            System.out.println("  [!] Please enter y or n.");
        }
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }
}
