package atm;


import org.w3c.dom.ls.LSOutput;

import java.util.Scanner;

public class AdminService {
    private static final int ADMIN_PIN = 1234;

    public static void login (Scanner in) {
        System.out.println("Enter admin pin: ");
        int pin = in.nextInt();
        if (pin != ADMIN_PIN) {
            System.out.println("Invalid admin pin");
            return;
        }
        dashboard(in);
    }

    private static void dashboard (Scanner in) {
        while (true) {
            System.out.println("\n-- Admin Dashboard --");
            System.out.println("1) List accounts");
            System.out.println("2) Create account");
            System.out.println("3) Delete account");
            System.out.println("4) Change PIN");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
        }
    }


}
