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
        System.out.println("Tip: type 'cancel' at any prompt to abort this action.");

        String customerType = validateCustomerType("Choose type (1-3): ");
        if (customerType == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String id      = validateCustomerID("Enter Customer ID: ");
        if (id == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String name    = validateName("Enter Full Name: ");
        if (name == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String phone   = validatePhone("Enter Phone Number: ");
        if (phone == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String address = validateAddress("Enter Address: ");
        if (address == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Customer customer;
        if (customerType.equals("2")) {
            double discountRate = validateDiscountRate("Enter VIP Discount Rate (e.g., 0.1 for 10%): ");
            if (Double.isNaN(discountRate)) {
                System.out.println("Operation cancelled.");
                return;
            }
            customer = new VIPCustomer(id, name, phone, address, discountRate);
        } else if (customerType.equals("3")) {
            String companyName = validateCompanyName("Enter Company Name: ");
            if (companyName == null) {
                System.out.println("Operation cancelled.");
                return;
            }

            String taxID = validateTaxID("Enter Tax ID: ");
            if (taxID == null) {
                System.out.println("Operation cancelled.");
                return;
            }

            double negotiatedDiscountRate = validateDiscountRate("Enter Negotiated Discount Rate (e.g., 0.1 for 10%): ");
            if (Double.isNaN(negotiatedDiscountRate)) {
                System.out.println("Operation cancelled.");
                return;
            }

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
        System.out.println("Tip: type 'cancel' at any prompt to abort this action.");

        Customer existingCustomer = null;
        while (true) {
            String id = validateCustomerIDForUpdate("Enter Customer ID to update: ");
            if (id == null) {
                System.out.println("Operation cancelled.");
                return;
            }
            existingCustomer = customerController.handleGetById(id);
            if (existingCustomer != null) break;
            System.out.println(">> Error: Customer ID not found! Please try again.");
        }

        // Name
        String name;
        while (true) {
            String input = readInput("Enter New Name (leave blank to skip): ");
            if (input == null) {
                System.out.println("Operation cancelled.");
                return;
            }
            if (input.isEmpty()) { name = existingCustomer.getName(); break; }
            if (input.length() > 50) { System.out.println("Name must not exceed 50 characters."); continue; }
            name = input;
            break;
        }

        // Phone
        String phone;
        while (true) {
            String input = readInput("Enter New Phone (leave blank to skip): ");
            if (input == null) {
                System.out.println("Operation cancelled.");
                return;
            }
            if (input.isEmpty()) { phone = existingCustomer.getPhone(); break; }
            if (!input.matches("0[0-9]{9}")) { System.out.println("Phone must start with 0 and have exactly 10 digits."); continue; }
            phone = input;
            break;
        }

        // Address
        String address;
        while (true) {
            String input = readInput("Enter New Address (leave blank to skip): ");
            if (input == null) {
                System.out.println("Operation cancelled.");
                return;
            }
            if (input.isEmpty()) { address = existingCustomer.getAddress(); break; }
            if (input.length() > 100) { System.out.println("Address must not exceed 100 characters."); continue; }
            address = input;
            break;
        }

        Customer updatedCustomer;

        if (existingCustomer instanceof CorporateCustomer) {
            CorporateCustomer existing = (CorporateCustomer) existingCustomer;

            String companyName;
            while (true) {
                String input = readInput("Enter New Company Name (leave blank to skip): ");
                if (input == null) {
                    System.out.println("Operation cancelled.");
                    return;
                }
                if (input.isEmpty()) { companyName = existing.getCompanyName(); break; }
                if (input.length() > 100) { System.out.println("Company name must not exceed 100 characters."); continue; }
                companyName = input;
                break;
            }

            String taxID;
            while (true) {
                String input = readInput("Enter New Tax ID (leave blank to skip): ");
                if (input == null) {
                    System.out.println("Operation cancelled.");
                    return;
                }
                if (input.isEmpty()) { taxID = existing.getTaxID(); break; }
                if (!input.matches("[A-Za-z0-9]+")) { System.out.println("Tax ID must only contain letters and numbers."); continue; }
                taxID = input;
                break;
            }

            double negotiatedDiscountRate;
            while (true) {
                String input = readInput("Enter New Negotiated Discount Rate (leave blank to skip): ");
                if (input == null) {
                    System.out.println("Operation cancelled.");
                    return;
                }
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
                String input = readInput("Enter New VIP Discount Rate (leave blank to skip): ");
                if (input == null) {
                    System.out.println("Operation cancelled.");
                    return;
                }
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
        System.out.println("Tip: type 'cancel' at any prompt to abort this action.");

        String id = validateCustomerIDForDelete("Enter Customer ID to remove: ");
        if (id == null) {
            System.out.println("Operation cancelled.");
            return;
        }

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

    private String readInput(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("cancel")) {
            return null;
        }
        return input;
    }

    private boolean isCancelled(String input) {
        return input == null;
    }

    // ================= VALIDATE CUSTOMER TYPE =================

    private String validateCustomerType(String message) {
        while (true) {
            String type = readInput(message);
            if (isCancelled(type)) return null;
            if (type.equals("1") || type.equals("2") || type.equals("3")) return type;
            System.out.println("Please enter 1, 2, or 3.");
        }
    }

    // ================= VALIDATE CUSTOMER ID =================

    private String validateCustomerID(String message) {
        while (true) {
            String id = readInput(message);
            if (isCancelled(id)) return null;
            if (id.isEmpty()) { System.out.println("Customer ID cannot be empty."); continue; }
            if (!id.matches("[A-Za-z0-9]+")) { System.out.println("Customer ID must only contain letters and numbers."); continue; }
            if (customerController.isCustomerIdExistsIncludingDeleted(id)) {
                System.out.println("Customer ID already exists.");
                continue;
            }
            return id;
        }
    }

    private String validateCustomerIDForUpdate(String message) {
        while (true) {
            String id = readInput(message);
            if (isCancelled(id)) return null;
            if (id.isEmpty()) { System.out.println("Customer ID cannot be empty."); continue; }
            if (!id.matches("[A-Za-z0-9]+")) { System.out.println("Customer ID must only contain letters and numbers."); continue; }
            if (customerController.handleGetById(id) == null) {
                System.out.println("Customer ID does not exist or has been deleted.");
                continue;
            }
            return id;
        }
    }

    private String validateCustomerIDForDelete(String message) {
        while (true) {
            String id = readInput(message);
            if (isCancelled(id)) return null;
            if (id.isEmpty()) { System.out.println("Customer ID cannot be empty."); continue; }
            if (!id.matches("[A-Za-z0-9]+")) { System.out.println("Customer ID must only contain letters and numbers."); continue; }
            if (customerController.handleGetById(id) == null) {
                System.out.println("Customer ID does not exist or has been deleted.");
                continue;
            }
            return id;
        }
    }

    // ================= VALIDATE NAME =================

    private String validateName(String message) {
        while (true) {
            String name = readInput(message);
            if (isCancelled(name)) return null;
            if (name.isEmpty()) { System.out.println("Name cannot be empty."); continue; }
            if (name.length() > 50) { System.out.println("Name must not exceed 50 characters."); continue; }
            return name;
        }
    }

    // ================= VALIDATE PHONE =================

    private String validatePhone(String message) {
        while (true) {
            String phone = readInput(message);
            if (isCancelled(phone)) return null;
            if (phone.isEmpty()) { System.out.println("Phone number cannot be empty."); continue; }
            if (!phone.matches("0[0-9]{9}")) { System.out.println("Phone must start with 0 and have exactly 10 digits."); continue; }
            return phone;
        }
    }

    // ================= VALIDATE ADDRESS =================

    private String validateAddress(String message) {
        while (true) {
            String address = readInput(message);
            if (isCancelled(address)) return null;
            if (address.isEmpty()) { System.out.println("Address cannot be empty."); continue; }
            if (address.length() > 100) { System.out.println("Address must not exceed 100 characters."); continue; }
            return address;
        }
    }

    // ================= VALIDATE COMPANY NAME =================

    private String validateCompanyName(String message) {
        while (true) {
            String companyName = readInput(message);
            if (isCancelled(companyName)) return null;
            if (companyName.isEmpty()) { System.out.println("Company name cannot be empty."); continue; }
            if (companyName.length() > 100) { System.out.println("Company name must not exceed 100 characters."); continue; }
            return companyName;
        }
    }

    // ================= VALIDATE TAX ID =================

    private String validateTaxID(String message) {
        while (true) {
            String taxID = readInput(message);
            if (isCancelled(taxID)) return null;
            if (taxID.isEmpty()) { System.out.println("Tax ID cannot be empty."); continue; }
            if (!taxID.matches("[A-Za-z0-9]+")) { System.out.println("Tax ID must only contain letters and numbers."); continue; }
            return taxID;
        }
    }

    // ================= VALIDATE DISCOUNT RATE =================

    private double validateDiscountRate(String message) {
        while (true) {
            String input = readInput(message);
            if (input == null) return Double.NaN;
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