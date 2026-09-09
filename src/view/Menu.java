package view;

public class Menu {

    public static void showMenu(String title, Object[] options) {
        System.out.println("\n========== " + title + " ==========");
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ". " + options[i]);
        }
        System.out.print("Your choice [1-" + options.length + "]: ");
    }
}
