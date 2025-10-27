package atm;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Datastore {

    private final Map<Integer, Account> accounts = new HashMap<>();
    private int nextAccNo = 1001;

    public Datastore() {
        int demo = createAccount("Demo User", 1234);
        deposit(demo, 1000.00);
    }


    public synchronized int createAccount(String name, int pin) {
        if (name == null || name.isBlank()) return -1;
        if (pin < 0 || pin > 9999) return -1;

        int accNo = nextAccNo++;
        accounts.put(accNo, new Account(accNo, name.trim(), pin));
        return accNo;
    }

    public synchronized boolean deleteAccount(int accountNumber) {
        return accounts.remove(accountNumber) != null;
    }

    public synchronized Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public synchronized Map<Integer, Account> getAllAccountsSnapshot() {
        return Collections.unmodifiableMap(new HashMap<>(accounts));
    }


    public synchronized boolean deposit(int accountNumber, double amount) {
        if (amount <= 0) return false;
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        a.setBalance(a.getBalance() + amount);
        return true;
    }

    public synchronized boolean withdraw(int accountNumber, double amount) {
        if (amount <= 0) return false;
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        if (a.getBalance() < amount) return false;
        a.setBalance(a.getBalance() - amount);
        return true;
    }

    public synchronized boolean setPin(int accountNumber, int newPin) {
        if (newPin < 0 || newPin > 9999) return false;
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        a.setPin(newPin);
        return true;
    }

    public synchronized boolean setName(int accountNumber, String newName) {
        if (newName == null || newName.isBlank()) return false;
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        a.setName(newName.trim());
        return true;
    }
}
