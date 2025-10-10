package atm;

import java.util.Scanner;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println(BLUE + "╔══════════════════════════════╗" + RESET);
            System.out.println(BLUE + "║        ATM SIMULATION        ║" + RESET);
            System.out.println(BLUE + "╠══════════════════════════════╣" + RESET);

            System.out.println(GREEN + "║  [1] User Login              ║" + RESET);
            System.out.println(YELLOW + "║  [2] Admin Login             ║" + RESET);
            System.out.println(RED + "║  [0] Exit                    ║" + RESET);

            System.out.print(CYAN + "╚═══► Enter option here: " + RESET);

        }
    }
}