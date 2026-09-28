
import java.util.Scanner;

// Represents the user's bank account holding the balance
class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        if (initialBalance >= 0) {
            this.balance = initialBalance;
        } else {
            this.balance = 0.0;
        }
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

// Represents the ATM machine interface and operation handler
class ATM {
    private final BankAccount account;
    private final Scanner scanner;

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    // Displays the user interface and handles the interaction loop
    public void start() {
        boolean exit = false;

        System.out.println("==========================================");
        System.out.println("          WELCOME TO THE ATM SYSTEM        ");
        System.out.println("==========================================");

        while (!exit) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid selection. Please enter a number from 1 to 4.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    handleDeposit();
                    break;
                case 3:
                    handleWithdrawal();
                    break;
                case 4:
                    System.out.println("\nThank you for using this ATM. Please take your card!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 4.");
            }
        }
    }

    private void checkBalance() {
        System.out.printf("%n[Balance Inquiry] Your current balance is: $%.2f%n", account.getBalance());
    }

    private void handleDeposit() {
        System.out.print("\nEnter amount to deposit: $");
        if (!scanner.hasNextDouble()) {
            System.out.println("Transaction Failed: Invalid numeric input.");
            scanner.next();
            return;
        }

        double amount = scanner.nextDouble();
        if (account.deposit(amount)) {
            System.out.printf("Success: $%.2f has been deposited.%n", amount);
            System.out.printf("Updated Balance: $%.2f%n", account.getBalance());
        } else {
            System.out.println("Transaction Failed: Deposit amount must be greater than zero.");
        }
    }

    private void handleWithdrawal() {
        System.out.print("\nEnter amount to withdraw: $");
        if (!scanner.hasNextDouble()) {
            System.out.println("Transaction Failed: Invalid numeric input.");
            scanner.next();
            return;
        }

        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Transaction Failed: Withdrawal amount must be greater than zero.");
        } else if (amount > account.getBalance()) {
            System.out.printf("Transaction Failed: Insufficient funds. Available balance: $%.2f%n", account.getBalance());
        } else {
            account.withdraw(amount);
            System.out.printf("Success: Please collect your cash of $%.2f.%n", amount);
            System.out.printf("Remaining Balance: $%.2f%n", account.getBalance());
        }
    }
}

// Main execution entry point
public class ATMInterface {
    public static void main(String[] args) {
        // Initialize an account with a starting balance of $1,000.00
        BankAccount userAccount = new BankAccount(1000.00);
        ATM atm = new ATM(userAccount);
        atm.start();
    }
}