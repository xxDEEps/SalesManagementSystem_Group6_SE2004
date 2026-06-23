package View;

import java.util.Scanner;

import Controller.ReportController;

public class ReportView {

    private final ReportController reportController;
    private final Scanner scanner;

    public ReportView(ReportController reportController, Scanner scanner) {
        this.reportController = reportController;
        this.scanner = scanner;
    }

    public void displayMenu() {
        String choice;
        do {
            System.out.println("\n--- REPORTS & ANALYTICS ---");
            System.out.println("1. Daily/Monthly Sales Report");
            System.out.println("2. Best-Selling Products");
            System.out.println("3. Top Spending Customers");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showSalesReport();
                    break;

                case "2":
                    showBestSellingProducts();
                    break;

                case "3":
                    showTopCustomers();
                    break;

                case "0":
                    break;

                default:
                    System.out.println("Invalid option!");
            }
        } while (!choice.equals("0"));
    }

    private void showSalesReport() {
        System.out.print("Enter date (dd/MM/yyyy) or month (MM/yyyy): ");
        String period = scanner.nextLine().trim();

        double total = reportController.handleSalesReport(period);

        System.out.println("\n---------- SALES REPORT ----------");
        System.out.println("Period: " + period);
        System.out.println("Total Amount: " + String.format("%,.0f", total) + "$");
        System.out.println("----------------------------------");
        pressEnterToReturn();
    }

    private void showBestSellingProducts() {
        System.out.println("\n---------- BEST-SELLING PRODUCTS ----------");
        System.out.println(reportController.handleBestSellingProducts());
        System.out.println("-------------------------------------------");
        pressEnterToReturn();
    }

    private void showTopCustomers() {
        System.out.println("\n---------- TOP CUSTOMERS ----------");
        System.out.println(reportController.handleTopCustomers());
        System.out.println("-----------------------------------");
        pressEnterToReturn();
    }

    private void pressEnterToReturn() {
        System.out.print("Press ENTER to return...");
        scanner.nextLine();
    }
}
