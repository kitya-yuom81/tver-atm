package atm;

import java.util.Scanner;

public class UserService {
    public static void login(Scanner in) {   // method + do-while
        int attempts = 0;
        boolean ok = false;
        int idx = -1;

        do {
            System.out.print("Account No: ");
            int acc = readInt(in);
            System.out.print("PIN: ");
            int pin = readInt(in);

            idx = DataStore.findByAccNo(acc);
            ok = (idx != -1 && DataStore.accounts[idx].pin == pin);
            if (!ok) {
                System.out.println("Invalid acc/pin.");
                attempts++;
            }
        } while (!ok && attempts < 3);

        if (ok) dashboard(in, idx);
    }
    public static void dashboard(Scanner in, int idx) {}
    public static int readInt(Scanner in) {}


}
