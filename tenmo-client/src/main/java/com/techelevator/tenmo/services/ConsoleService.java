package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printUsers(User[] users) {
        System.out.println("-------------------------------------------\n" +
                "Users\n" +
                "ID          Name\n" +
                "-------------------------------------------");
        for (User user: users) {
            System.out.println(user.getId() + " :   " + user.getUsername());
        }
        System.out.println("-------------------------------------------");
    }

    public Long promptForUserId() {
        System.out.println("");
        System.out.println("Enter ID of user you are sending to (0 to cancel):");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return Long.parseLong(input);
    }

    public double promptForAmount() {
        System.out.println("");
        System.out.println("Enter amount:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return Double.parseDouble(input);
    }


    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public Long promptForTransferId () {
        System.out.println("");
        System.out.println("Please enter transfer ID to view details (0 to cancel): ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return Long.parseLong(input);
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void printBalance(double balance) {
        System.out.println("Your current balance is: $" + String.format("%.2f",balance));
    }

    public void printTransfers(Transfer[] transfers) {
        System.out.println("-------------------------------------------\n" +
                "Transfers\n" +
                "ID          From/To                 Amount\n" +
                "-------------------------------------------");

        for(Transfer transfer : transfers) {
                System.out.println(transfer.getTransferId() + "        To: " + transfer.getAccountTo() +
                        "                 $" + String.format("%.2f", transfer.getAmount()));
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
