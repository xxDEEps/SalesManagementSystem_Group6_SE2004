package View;

import java.util.Scanner;
import java.util.List;
import Controller.CustomerController;
import Model.Customer;
import Model.VIPCustomer;
import Model.CorporateCustomer;

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
                case "1": handleAddCustomer();      break;
                case "2": handleUpdateCustomer();   break;
                case "3": handleRemoveCustomer();   break;
                case "4": handleViewAllCustomers(); break;
                case "0": break;
                default:  System.out.println("Invalid option!");
            }
        } while (!choice.equals("0"));
    }

    // ================= ADD CUSTOMER =================

    private void handleAddCustomer() {
        System.out.println("\n--- ADD NEW CUSTOMER ---");
        System.out.println("Select Customer Type:");
        System.out.println("1. Regular Customer");
        System.out.println("2. VIP Customer");
        System.out.println("3. Corporate Customer");

        String customerType = validateCustomerType("Choose type (1-3): ");

        String id      = validateCustomerID("Enter Customer ID: ");
        String name    = validateName("Enter Full Name: ");
        String phone   = validatePhone("Enter Phone Number: ");
        String address = validateAddress("Enter Address: ");

        Customer customer;
        if (customerType.equals("2")) {
            double discountRate = validateDiscountRate("Enter VIP Discount Rate (e.g., 0.1 for 10%): ");
            customer = new VIPCustomer(id, name, phone, address, discountRate);
        } else if (customerType.equals("3")) {
            String companyName = validateCompanyName("Enter Company Name: ");
            String taxID       = validateTaxID("Enter Tax ID: ");
            double negotiatedDiscountRate = validateDiscountRate("Enter Negotiated Discount Rate (e.g., 0.1 for 10%): ");
            customer = new CorporateCustomer(id, name, phone, address, companyName, taxID, negotiatedDiscountRate);
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

        Customer existingCustomer = null;
        while (true) {
            String id = validateCustomerID("Enter Customer ID to update: ");
            existingCustomer = customerController.handleGetById(id);
            if (existingCustomer != null) break;
            System.out.println(">> Error: Customer ID not found! Please try again.");
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

        if (existingCustomer instanceof CorporateCustomer) {
            CorporateCustomer existing = (CorporateCustomer) existingCustomer;

            String companyName;
            while (true) {
                System.out.print("Enter New Company Name (leave blank to skip): ");
                companyName = scanner.nextLine().trim();
                if (companyName.isEmpty()) { companyName = existing.getCompanyName(); break; }
                if (companyName.length() > 100) { System.out.println("Company name must not exceed 100 characters."); continue; }
                break;
            }

            String taxID;
            while (true) {
                System.out.print("Enter New Tax ID (leave blank to skip): ");
                taxID = scanner.nextLine().trim();
                if (taxID.isEmpty()) { taxID = existing.getTaxID(); break; }
                if (!taxID.matches("[A-Za-z0-9]+")) { System.out.println("Tax ID must only contain letters and numbers."); continue; }
                break;
            }

            double negotiatedDiscountRate;
            while (true) {
                System.out.print("Enter New Negotiated Discount Rate (leave blank to skip): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) { negotiatedDiscountRate = existing.getNegotiatedDiscountRate(); break; }
                try {
                    negotiatedDiscountRate = Double.parseDouble(input);
                    if (negotiatedDiscountRate < 0 || negotiatedDiscountRate > 1) { System.out.println("Discount rate must be between 0 and 1."); continue; }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid discount rate.");
                }
            }

            updatedCustomer = new CorporateCustomer(existingCustomer.getCustomerID(), name, phone, address, companyName, taxID, negotiatedDiscountRate);

        } else if (existingCustomer instanceof VIPCustomer) {
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
            updatedCustomer = new VIPCustomer(existingCustomer.getCustomerID(), name, phone, address, discountRate);

        } else {
            updatedCustomer = new Customer(existingCustomer.getCustomerID(), name, phone, address);
        }

        if (customerController.handleUpdate(existingCustomer.getCustomerID(), updatedCustomer)) {
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
            if (type.equals("1") || type.equals("2") || type.equals("3")) return type;
            System.out.println("Please enter 1, 2, or 3.");
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

    // ================= VALIDATE COMPANY NAME =================

    private String validateCompanyName(String message) {
        while (true) {
            System.out.print(message);
            String companyName = scanner.nextLine().trim();
            if (companyName.isEmpty()) { System.out.println("Company name cannot be empty."); continue; }
            if (companyName.length() > 100) { System.out.println("Company name must not exceed 100 characters."); continue; }
            return companyName;
        }
    }

    // ================= VALIDATE TAX ID =================

    private String validateTaxID(String message) {
        while (true) {
            System.out.print(message);
            String taxID = scanner.nextLine().trim();
            if (taxID.isEmpty()) { System.out.println("Tax ID cannot be empty."); continue; }
            if (!taxID.matches("[A-Za-z0-9]+")) { System.out.println("Tax ID must only contain letters and numbers."); continue; }
            return taxID;
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