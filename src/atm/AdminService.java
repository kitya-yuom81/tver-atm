package atm;



import java.util.Map;

public class AdminService {
    private final Datastore store;

    private final String ADMIN_USER = "admin";
    private final String ADMIN_PASS = "admin123";

    public AdminService(Datastore store) {
        this.store = store;
    }

    public boolean adminLogin(String username, String password) {
        return ADMIN_USER.equals(username) && ADMIN_PASS.equals(password);
    }

    public Map<Integer, Account> listAllAccounts() {
        return store.getAllAccountsSnapshot();
    }

    public Account findAccount(int accountNumber) {
        return store.getAccount(accountNumber);
    }

    public boolean adminDeposit(int accountNumber, double amount) {
        return store.deposit(accountNumber, amount);
    }

    public boolean adminWithdraw(int accountNumber, double amount) {
        return store.withdraw(accountNumber, amount);
    }

    public boolean resetPin(int accountNumber, int newPin) {
        return store.setPin(accountNumber, newPin);
    }

    public boolean renameAccount(int accountNumber, String newName) {
        return store.setName(accountNumber, newName);
    }

    public boolean deleteAccount(int accountNumber) {
        return store.deleteAccount(accountNumber);
    }
}
