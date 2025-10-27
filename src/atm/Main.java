package atm;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final Datastore datastore = new Datastore();
    private static final UserService userService = new UserService(datastore);
    private static final AdminService adminService = new AdminService(datastore);

    private static boolean running = true;
    private static Integer loggedInUserAccNum = null;

    public static void main(String[] args) {
        System.out.println("--- Welcome to the ATM Simulation System ---");
        while (running) {
            if (loggedInUserAccNum == null) {
                displayLoginMenu();
                handleLoginMenuInput();
            } else {
                displayUserMenu();
                handleUserMenuInput();
            }
        }
        System.out.println("\n--- Thank you for using the ATM System. Goodbye! ---");
        scanner.close();
    }

    // ===================== INPUT UTILS =====================
    private static int getIntInput() {
        try {
            if (scanner.hasNextInt()) {
                int val = scanner.nextInt();
                scanner.nextLine();
                return val;
            } else {
                System.out.println("[ERROR] Please enter a whole number.");
                scanner.nextLine();
                return -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] Invalid number.");
            scanner.nextLine();
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            if (scanner.hasNextDouble()) {
                double val = scanner.nextDouble();
                scanner.nextLine();
                return val;
            } else {
                System.out.println("[ERROR] Please enter a valid amount (e.g., 123.45).");
                scanner.nextLine();
                return -1.0;
            }
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] Invalid amount.");
            scanner.nextLine();
            return -1.0;
        }
    }

    private static int promptForPin() {
        System.out.print("Enter PIN: ");
        return getIntInput();
    }

    // ===================== LOGIN MENU ======================
    private static void displayLoginMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. User Login");
        System.out.println("2. Sign Up (Create Account)");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }

    private static void handleLoginMenuInput() {
        int choice = getIntInput();
        switch (choice) {
            case 1 -> loginUser();
            case 2 -> signUp();
            case 3 -> adminLoginMenu();
            case 4 -> running = false;
            case -1 -> { /* already messaged */ }
            default -> System.out.println("[ERROR] Choose 1-4.");
        }
    }

    private static void loginUser() {
        System.out.println("\n--- USER LOGIN ---");
        System.out.print("Account Number: ");
        int acc = getIntInput();
        if (acc == -1) return;
        int pin = promptForPin();
        if (pin == -1) return;

        if (userService.authenticate(acc, pin)) {
            loggedInUserAccNum = acc;
            System.out.println("[SUCCESS] Logged in!");
        } else {
            System.out.println("[FAILURE] Wrong account or PIN.");
        }
    }

    private static void signUp() {
        System.out.println("\n--- SIGN UP ---");
        System.out.print("Your Name: ");
        String name = scanner.nextLine();
        System.out.print("Choose PIN (0-9999): ");
        int pin = getIntInput();
        if (pin == -1) return;

        int newAcc = userService.signUp(name, pin);
        if (newAcc != -1) {
            System.out.println("[SUCCESS] Account created! Your Account Number is: " + newAcc);
            System.out.println("Use it to log in.");
        } else {
            System.out.println("[FAILURE] Could not create account. Check name/PIN.");
        }
    }

    // ===================== USER MENU =======================
    private static void displayUserMenu() {
        System.out.println("\n--- ATM OPERATIONS --- (User: " + loggedInUserAccNum + ")");
        System.out.println("1. Check Full Info (Name, Number, Balance)");
        System.out.println("2. Check Balance Only");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Change PIN");
        System.out.println("6. Change Account Name");
        System.out.println("7. Delete Account");
        System.out.println("8. Log Out");
        System.out.print("Enter your choice (1-8): ");
    }

    private static void handleUserMenuInput() {
        int choice = getIntInput();
        switch (choice) {
            case 1 -> userShowFullInfo();
            case 2 -> userShowBalance();
            case 3 -> userDeposit();
            case 4 -> userWithdraw();
            case 5 -> userChangePin();
            case 6 -> userChangeName();
            case 7 -> userDeleteAccount();
            case 8 -> userLogout();
            case -1 -> { }
            default -> System.out.println("[ERROR] Choose 1-8.");
        }
    }

    private static void userShowFullInfo() {
        int pin = promptForPin();
        if (pin == -1) return;
        Map<String, Object> info = userService.getInfo(loggedInUserAccNum, pin);
        if (info.isEmpty()) {
            System.out.println("[ERROR] Wrong PIN.");
            return;
        }
        System.out.println("\n--- ACCOUNT DETAILS ---");
        System.out.println("Name: " + info.get("accountName"));
        System.out.println("Account: " + info.get("accountNumber"));
        System.out.printf("Balance: $%.2f%n", (Double) info.get("balance"));
    }

    private static void userShowBalance() {
        int pin = promptForPin();
        if (pin == -1) return;
        double bal = userService.getBalance(loggedInUserAccNum, pin);
        if (bal == -1) {
            System.out.println("[ERROR] Wrong PIN.");
            return;
        }
        System.out.printf("Current Balance: $%.2f%n", bal);
    }

    private static void userDeposit() {
        int pin = promptForPin();
        if (pin == -1) return;
        System.out.print("Amount to deposit: $");
        double amt = getDoubleInput();
        if (amt <= 0) {
            System.out.println("[ERROR] Must be positive.");
            return;
        }
        if (userService.deposit(loggedInUserAccNum, pin, amt)) {
            double bal = userService.getBalance(loggedInUserAccNum, pin);
            System.out.printf("[SUCCESS] Deposited $%.2f. New balance: $%.2f%n", amt, bal);
        } else {
            System.out.println("[FAILURE] Deposit failed (PIN?).");
        }
    }

    private static void userWithdraw() {
        int pin = promptForPin();
        if (pin == -1) return;
        System.out.print("Amount to withdraw: $");
        double amt = getDoubleInput();
        if (amt <= 0) {
            System.out.println("[ERROR] Must be positive.");
            return;
        }
        if (userService.withdraw(loggedInUserAccNum, pin, amt)) {
            double bal = userService.getBalance(loggedInUserAccNum, pin);
            System.out.printf("[SUCCESS] Withdrew $%.2f. New balance: $%.2f%n", amt, bal);
        } else {
            System.out.println("[FAILURE] Withdrawal failed (funds/PIN?).");
        }
    }

    private static void userChangePin() {
        int oldPin = promptForPin();
        if (oldPin == -1) return;
        System.out.print("New PIN (0-9999): ");
        int newPin = getIntInput();
        if (newPin == -1) return;
        if (userService.changePin(loggedInUserAccNum, oldPin, newPin)) {
            System.out.println("[SUCCESS] PIN updated.");
        } else {
            System.out.println("[FAILURE] Could not update PIN (old PIN wrong?).");
        }
    }

    private static void userChangeName() {
        int pin = promptForPin();
        if (pin == -1) return;
        System.out.print("New Account Name: ");
        String newName = scanner.nextLine();
        if (userService.changeName(loggedInUserAccNum, pin, newName)) {
            System.out.println("[SUCCESS] Name updated.");
        } else {
            System.out.println("[FAILURE] Could not update name.");
        }
    }

    private static void userDeleteAccount() {
        int pin = promptForPin();
        if (pin == -1) return;
        System.out.print("Type 'YES' to confirm permanent deletion: ");
        String confirm = scanner.nextLine();
        if (!"YES".equalsIgnoreCase(confirm.trim())) {
            System.out.println("[INFO] Deletion cancelled.");
            return;
        }
        if (userService.deleteOwnAccount(loggedInUserAccNum, pin)) {
            System.out.println("[SUCCESS] Account deleted.");
            loggedInUserAccNum = null;
        } else {
            System.out.println("[FAILURE] Deletion failed (PIN?).");
        }
    }

    private static void userLogout() {
        System.out.println("[INFO] Logged out user " + loggedInUserAccNum + ".");
        loggedInUserAccNum = null;
    }

    // ===================== ADMIN MENU ======================
    private static void adminLoginMenu() {
        System.out.println("\n--- ADMIN LOGIN ---");
        System.out.print("Username: ");
        String u = scanner.nextLine();
        System.out.print("Password: ");
        String p = scanner.nextLine();

        if (!adminService.adminLogin(u, p)) {
            System.out.println("[FAILURE] Wrong admin credentials.");
            return;
        }
        boolean adminOn = true;
        while (adminOn) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. List All Accounts");
            System.out.println("2. Find Account by Number");
            System.out.println("3. Deposit to Account");
            System.out.println("4. Withdraw from Account");
            System.out.println("5. Reset Account PIN");
            System.out.println("6. Rename Account");
            System.out.println("7. Delete Account");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter your choice (1-8): ");
            int ch = getIntInput();
            switch (ch) {
                case 1 -> adminListAccounts();
                case 2 -> adminFindAccount();
                case 3 -> adminDepositToAccount();
                case 4 -> adminWithdrawFromAccount();
                case 5 -> adminResetPin();
                case 6 -> adminRenameAccount();
                case 7 -> adminDeleteAccount();
                case 8 -> adminOn = false;
                case -1 -> { }
                default -> System.out.println("[ERROR] Choose 1-8.");
            }
        }
    }

    private static void adminListAccounts() {
        var map = adminService.listAllAccounts();
        if (map.isEmpty()) {
            System.out.println("[INFO] No accounts in system.");
            return;
        }
        System.out.println("\n--- ALL ACCOUNTS ---");
        map.values().forEach(System.out::println);
    }

    private static void adminFindAccount() {
        System.out.print("Account Number: ");
        int acc = getIntInput();
        if (acc == -1) return;
        Account a = adminService.findAccount(acc);
        if (a == null) {
            System.out.println("[INFO] Account not found.");
        } else {
            System.out.println(a);
        }
    }

    private static void adminDepositToAccount() {
        System.out.print("Account Number: ");
        int acc = getIntInput();
        if (acc == -1) return;
        System.out.print("Amount to deposit: $");
        double amt = getDoubleInput();
        if (amt <= 0) { System.out.println("[ERROR] Must be positive."); return; }
        if (adminService.adminDeposit(acc, amt)) {
            System.out.println("[SUCCESS] Deposited.");
        } else {
            System.out.println("[FAILURE] Deposit failed (account?).");
        }
    }

    private static void adminWithdrawFromAccount() {
        System.out.print("Account Number: ");
        int acc = getIntInput();
        if (acc == -1) return;
        System.out.print("Amount to withdraw: $");
        double amt = getDoubleInput();
        if (amt <= 0) { System.out.println("[ERROR] Must be positive."); return; }
        if (adminService.adminWithdraw(acc, amt)) {
            System.out.println("[SUCCESS] Withdrawn.");
        } else {
            System.out.println("[FAILURE] Withdraw failed (funds/account?).");
        }
    }

    private static void adminResetPin() {
        System.out.print("Account Number: ");
        int acc = getIntInput();
        if (acc == -1) return;
        System.out.print("New PIN (0-9999): ");
        int newPin = getIntInput();
        if (newPin == -1) return;
        if (adminService.resetPin(acc, newPin)) {
            System.out.println("[SUCCESS] PIN reset.");
        } else {
            System.out.println("[FAILURE] Could not reset PIN (account?).");
        }
    }

    private static void adminRenameAccount() {
        System.out.print("Account Number: ");
        int acc = getIntInput();
        if (acc == -1) return;
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        if (adminService.renameAccount(acc, name)) {
            System.out.println("[SUCCESS] Name updated.");
        } else {
            System.out.println("[FAILURE] Could not update name (account?).");
        }
    }

    private static void adminDeleteAccount() {
        System.out.print("Account Number: ");
        int acc = getIntInput();
        if (acc == -1) return;
        System.out.print("Type 'YES' to confirm permanent deletion: ");
        String confirm = scanner.nextLine();
        if (!"YES".equalsIgnoreCase(confirm.trim())) {
            System.out.println("[INFO] Deletion cancelled.");
            return;
        }
        if (adminService.deleteAccount(acc)) {
            System.out.println("[SUCCESS] Account deleted.");
            // if deleting currently logged-in user, ensure state is consistent
            if (loggedInUserAccNum != null && loggedInUserAccNum == acc) {
                loggedInUserAccNum = null;
            }
        } else {
            System.out.println("[FAILURE] Delete failed (account?).");
        }
    }
}
