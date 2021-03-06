package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private AccountService accountService;
    private TransferService transferService;
    private UserService userService;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService = new AccountService(API_BASE_URL, currentUser);
            transferService = new TransferService(API_BASE_URL, currentUser);
            userService = new UserService(API_BASE_URL, currentUser);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        try {
            consoleService.printBalance(accountService.getBalance());
        } catch (NullPointerException e) {
            System.out.println("Account not found.");
        }
	}

	private void viewTransferHistory() {
            consoleService.printTransfers(transferService.listUserTransferStrings(), currentUser.getUser().getUsername());
            Long transferId = consoleService.promptForTransferId();
            System.out.println(transferService.transferDetails(transferId));
    }

	private void viewPendingRequests() {
        System.out.println("Feature coming soon!");
		
	}

	private void sendBucks() {
		consoleService.printUsers(userService.listAllUsers());
        Long userId = consoleService.promptForUserId();
        double amount = consoleService.promptForAmount();

        BasicTransferObject transfer = new BasicTransferObject(userId, amount);
        transferService.sendTEBucks(transfer);
        viewCurrentBalance();
		
	}

	private void requestBucks() {
        System.out.println("Feature coming soon!");
		
	}

}
