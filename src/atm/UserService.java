package atm;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Datastore store;

    public UserService(Datastore store) {
        this.store = store;
    }

    // ===== Auth =====
    public boolean authenticate(int accountNumber, int pin) {
        Account a = store.getAccount(accountNumber);
        return a != null && a.getPin() == pin;
    }

    // ===== Sign up =====
    public int signUp(String name, int pin) {
        return store.createAccount(name, pin);
    }

    // ===== Info =====
    public Map<String, Object> getInfo(int accountNumber, int pin) {
        Map<String, Object> out = new HashMap<>();
        if (!authenticate(accountNumber, pin)) return out;
        Account a = store.getAccount(accountNumber);
        out.put("accountName", a.getName());
        out.put("accountNumber", a.getAccountNumber());
        out.put("balance", a.getBalance());
        return out;
    }

    public double getBalance(int accountNumber, int pin) {
        if (!authenticate(accountNumber, pin)) return -1;
        return store.getAccount(accountNumber).getBalance();
    }

    // ===== Money Ops =====
    public boolean deposit(int accountNumber, int pin, double amount) {
        if (!authenticate(accountNumber, pin)) return false;
        return store.deposit(accountNumber, amount);
    }

    public boolean withdraw(int accountNumber, int pin, double amount) {
        if (!authenticate(accountNumber, pin)) return false;
        return store.withdraw(accountNumber, amount);
    }

    public boolean deleteOwnAccount(int accountNumber, int pin) {
        if (!authenticate(accountNumber, pin)) return false;
        return store.deleteAccount(accountNumber);
    }

    public boolean changePin(int accountNumber, int pin, int newPin) {
        if (!authenticate(accountNumber, pin)) return false;
        return store.setPin(accountNumber, newPin);
    }

    public boolean changeName(int accountNumber, int pin, String newName) {
        if (!authenticate(accountNumber, pin)) return false;
        return store.setName(accountNumber, newName);
    }
}
