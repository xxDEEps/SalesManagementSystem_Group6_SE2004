package View;

import java.util.Scanner;
import java.util.List;
import Controller.CustomerController;
import Model.Customer;
import Model.VIPCustomer;

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

    // ================= ADD CUSTOMER =================

    private void handleAddCustomer() {
        System.out.println("\n--- ADD NEW CUSTOMER ---");
        System.out.println("Select Customer Type:");
        System.out.println("1. Regular Customer");
        System.out.println("2. VIP Customer");

        String customerType = validateCustomerType("Choose type (1-2): ");

        String id      = validateCustomerID("Enter Customer ID: ");
        String name    = validateName("Enter Full Name: ");
        String phone   = validatePhone("Enter Phone Number: ");
        String address = validateAddress("Enter Address: ");

        Customer customer;
        if (customerType.equals("2")) {
            double discountRate = validateDiscountRate("Enter VIP Discount Rate (e.g., 0.1 for 10%): ");
            customer = new VIPCustomer(id, name, phone, address, discountRate);
        } else {
            customer = new Customer(id, name, phone, address);
        }

        if (customerController.handleAdd(customer)) {
            System.out.println(">> Added customer successfully!");
        } else {
            System.out.println(">> Error: Customer ID already exists!");
        }
    }

    // ================= UPDATE CUSTOMER =================

    private void handleUpdateCustomer() {
        System.out.println("\n--- UPDATE CUSTOMER ---");

        String id = validateCustomerID("Enter Customer ID to update: ");

        List<Customer> currentList = customerController.handleView();
        Customer existingCustomer = null;
        for (Customer c : currentList) {
            if (c.getCustomerID().equalsIgnoreCase(id)) {
                existingCustomer = c;
                break;
            }
        }

        if (existingCustomer == null) {
            System.out.println(">> Error: Customer ID not found!");
            return;
        }

        // Name
        String name;
        while (true) {
            System.out.print("Enter New Name (leave blank to skip): ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) { name = existingCustomer.getName(); break; }
            if (name.length() > 50) { System.out.println("Name must not exceed 50 characters."); continue; }
            break;
        }

        // Phone
        String phone;
        while (true) {
            System.out.print("Enter New Phone (leave blank to skip): ");
            phone = scanner.nextLine().trim();
            if (phone.isEmpty()) { phone = existingCustomer.getPhone(); break; }
            if (!phone.matches("0[0-9]{9}")) { System.out.println("Phone must start with 0 and have exactly 10 digits."); continue; }
            break;
        }

        // Address
        String address;
        while (true) {
            System.out.print("Enter New Address (leave blank to skip): ");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) { address = existingCustomer.getAddress(); break; }
            if (address.length() > 100) { System.out.println("Address must not exceed 100 characters."); continue; }
            break;
        }

        Customer updatedCustomer;
        if (existingCustomer instanceof VIPCustomer) {
            double discountRate;
            while (true) {
                System.out.print("Enter New VIP Discount Rate (leave blank to skip): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) { discountRate = ((VIPCustomer) existingCustomer).getDiscountRate(); break; }
                try {
                    discountRate = Double.parseDouble(input);
                    if (discountRate < 0 || discountRate > 1) { System.out.println("Discount rate must be between 0 and 1."); continue; }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid discount rate.");
                }
            }
            updatedCustomer = new VIPCustomer(id, name, phone, address, discountRate);
        } else {
            updatedCustomer = new Customer(id, name, phone, address);
        }

        if (customerController.handleUpdate(id, updatedCustomer)) {
            System.out.println(">> Customer updated successfully!");
        } else {
            System.out.println(">> Update failed!");
        }
    }

    // ================= REMOVE CUSTOMER =================

    private void handleRemoveCustomer() {
        System.out.println("\n--- REMOVE CUSTOMER ---");

        String id = validateCustomerID("Enter Customer ID to remove: ");

        if (customerController.handleDelete(id)) {
            System.out.println(">> Customer removed successfully (Soft Deleted)!");
        } else {
            System.out.println(">> Error: Customer ID not found or already removed!");
        }
    }

    // ================= VIEW ALL CUSTOMERS =================

    private void handleViewAllCustomers() {
        System.out.println("\n--- CUSTOMER LIST ---");
        List<Customer> customers = customerController.handleView();

        if (customers.isEmpty()) {
            System.out.println("No active customers found.");
        } else {
            for (Customer c : customers) {
                System.out.println(c.displayCustomerInfo());
            }
        }
    }

    // ================= VALIDATE CUSTOMER TYPE =================

    private String validateCustomerType(String message) {
        while (true) {
            System.out.print(message);
            String type = scanner.nextLine().trim();
            if (type.equals("1") || type.equals("2")) return type;
            System.out.println("Please enter 1 or 2.");
        }
    }

    // ================= VALIDATE CUSTOMER ID =================

    private String validateCustomerID(String message) {
        while (true) {
            System.out.print(message);
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) { System.out.println("Customer ID cannot be empty."); continue; }
            if (!id.matches("[A-Za-z0-9]+")) { System.out.println("Customer ID must only contain letters and numbers."); continue; }
            return id;
        }
    }

    // ================= VALIDATE NAME =================

    private String validateName(String message) {
        while (true) {
            System.out.print(message);
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) { System.out.println("Name cannot be empty."); continue; }
            if (name.length() > 50) { System.out.println("Name must not exceed 50 characters."); continue; }
            return name;
        }
    }

    // ================= VALIDATE PHONE =================

    private String validatePhone(String message) {
        while (true) {
            System.out.print(message);
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) { System.out.println("Phone number cannot be empty."); continue; }
            if (!phone.matches("0[0-9]{9}")) { System.out.println("Phone must start with 0 and have exactly 10 digits."); continue; }
            return phone;
        }
    }

    // ================= VALIDATE ADDRESS =================

    private String validateAddress(String message) {
        while (true) {
            System.out.print(message);
            String address = scanner.nextLine().trim();
            if (address.isEmpty()) { System.out.println("Address cannot be empty."); continue; }
            if (address.length() > 100) { System.out.println("Address must not exceed 100 characters."); continue; }
            return address;
        }
    }

    // ================= VALIDATE DISCOUNT RATE =================

    private double validateDiscountRate(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) { System.out.println("Discount rate cannot be empty."); continue; }
            try {
                double rate = Double.parseDouble(input);
                if (rate < 0 || rate > 1) { System.out.println("Discount rate must be between 0 and 1."); continue; }
                return rate;
            } catch (NumberFormatException e) {
                System.out.println("Invalid discount rate. Please enter a number (e.g., 0.1).");
            }
        }
    }
}
