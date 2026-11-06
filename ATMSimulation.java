
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATMSimulation {

    static void withdraw(double balance, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                "Withdrawal amount cannot be negative"
            );
        }

        if (amount > balance) {
            throw new ArithmeticException(
                "Insufficient account balance"
            );
        }

        System.out.println("Withdrawal Successful");
        System.out.println("Remaining Balance: Rs " +
            (balance - amount));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            File cardReader = new File("card.txt");

            if (!cardReader.exists()) {
                throw new FileNotFoundException(
                    "Card reader file is missing"
                );
            }

            System.out.print("Enter Account Number: ");
            String account = sc.nextLine();

            System.out.print("Enter Withdrawal Amount: ");
            double amount = sc.nextDouble();

            double balance = 5000.0;

            System.out.println("Account Number: " + account);

            withdraw(balance, amount);

        } catch (InputMismatchException e) {
            System.out.println("Error: Enter a valid number");

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException |
                 ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());

        } finally {
            System.out.println("ATM Transaction Completed");
            sc.close();
        }
    }
}
