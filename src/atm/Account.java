package atm;

public class Account {
    private final int accountNumber;
    private String name;
    private int pin;
    private double balance;

    public Account(int accountNumber, String name, int pin) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pin = pin;
        this.balance = 0.0;
    }

    public int getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getPin() { return pin; }
    public double getBalance() { return balance; }

    public void setName(String name) { this.name = name; }
    public void setPin(int pin) { this.pin = pin; }
    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public String toString() {
        return String.format(
                "Account{number=%d, name='%s', balance=$%.2f}",
                accountNumber, name, balance
        );
    }
}
