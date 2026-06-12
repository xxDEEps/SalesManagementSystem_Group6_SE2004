package View;

import java.util.Scanner;

import Controller.CustomerController;

public class CustomerView {
    private final CustomerController customerController;
    private final Scanner scanner;

    public CustomerView(CustomerController customerController, Scanner scanner) {
        this.customerController = customerController;
        this.scanner = scanner;
    }

    public void displayMenu() {
        String choice;
        do {
            System.out.println("\n--- CUSTOMER MANAGEMENT ---");
            System.out.println("1. Add New Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. Remove Customer");
            System.out.println("4. View All Customers");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddCustomer();
                    break;
                case "2":
                    handleUpdateCustomer();
                    break;
                case "3":
                    handleRemoveCustomer();
                    break;
                case "4":
                    handleViewAllCustomers();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        } while (!choice.equals("0"));
    }

    private void handleAddCustomer() {
        System.out.println("\n--- ADD NEW CUSTOMER ---");
        System.out.print("Is this a VIP Customer? (yes/no): ");
        String isVip = scanner.nextLine();
        
        System.out.print("Enter Customer ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        String discountRate = "0";
        if (isVip.equalsIgnoreCase("yes")) {
            System.out.print("Enter VIP Discount Rate (e.g., 0.1 for 10%): ");
            discountRate = scanner.nextLine();
        }

        
    }

    private void handleUpdateCustomer() {
        System.out.println("\n--- UPDATE CUSTOMER ---");
        System.out.print("Enter Customer ID to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter New Name (leave blank to skip): ");
        String name = scanner.nextLine();
        System.out.print("Enter New Phone (leave blank to skip): ");
        String phone = scanner.nextLine();
        System.out.print("Enter New Address (leave blank to skip): ");
        String address = scanner.nextLine();

        
    }

    private void handleRemoveCustomer() {
        System.out.println("\n--- REMOVE CUSTOMER ---");
        System.out.print("Enter Customer ID to remove: ");
        String id = scanner.nextLine();

    }

    private void handleViewAllCustomers() {
        System.out.println("\n--- CUSTOMER LIST ---");
        
    }
}
