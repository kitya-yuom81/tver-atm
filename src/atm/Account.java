package atm;

public class Account {
    public int AccNo;
    public int pin;
    public double balance;

    public Account(int accNo, int pin, double balance) {
        this.AccNo = accNo;
        this.pin = pin;
        this.balance = balance;
    }
}
