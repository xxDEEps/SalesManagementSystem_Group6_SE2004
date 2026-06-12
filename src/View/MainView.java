package View;

import java.util.Scanner;

public class MainView {
    private final ProductView productView;
    private final CustomerView customerView;
    private final SalesTransactionView salesTransactionView;
    private final ReportView reportView;
    private final Scanner scanner;

    public MainView(ProductView productView, CustomerView customerView, SalesTransactionView salesTransactionView, ReportView reportView, Scanner scanner) {
        this.productView = productView;
        this.customerView = customerView;
        this.salesTransactionView = salesTransactionView;
        this.reportView = reportView;
        this.scanner = scanner;
    }

    public void displayMainMenu() {
        String choice;
        do {
            System.out.println("\n=================================");
            System.out.println("     SALES MANAGEMENT SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Customers");
            System.out.println("3. Manage Sales Transactions");
            System.out.println("4. Reports");
            System.out.println("0. Exit System");
            System.out.print("Please choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    productView.displayMenu();
                    break;
                case "2":
                    customerView.displayMenu();
                    break;
                case "3":
                    salesTransactionView.displayMenu();
                    break;
                case "4":
                    reportView.displayMenu();
                    break;
                case "0":
                    System.out.println("\nExiting system... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (!choice.equals("0"));
    }
}
