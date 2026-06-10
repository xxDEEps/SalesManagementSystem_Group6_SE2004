package View;

public class MenuGenerator {
    
    public static void displayMainMenu() {
        System.out.println("=== Sales Management System ===");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Customers");
        System.out.println("3. Manage Orders");
        System.out.println("4. Exit");
        System.out.println("Please select an option: ");
    }

    public static void displayProductMenu() {
        System.out.println("=== Product Management ===");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product Stock");
        System.out.println("3. Display All Products");
        System.out.println("4. Back to Main Menu");
        System.out.println("Please select an option: ");
    }

    public static void displayCustomerMenu() {
        System.out.println("=== Customer Management ===");
        System.out.println("1. Add Customer");
        System.out.println("2. Display All Customers");
        System.out.println("3. Back to Main Menu");
        System.out.println("Please select an option: ");
    }

    public static void displayOrderMenu() {
        System.out.println("=== Order Management ===");
        System.out.println("1. Create Order");
        System.out.println("2. Display All Orders");
        System.out.println("3. Back to Main Menu");
        System.out.println("Please select an option: ");
    }




}
