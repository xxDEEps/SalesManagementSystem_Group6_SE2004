package View;

import java.util.List;
import java.util.Scanner;

import Controller.ProductController;

public class ProductView {
    private final ProductController productController;
    private final Scanner scanner;

    public ProductView(ProductController productController, Scanner scanner) {
        this.productController = productController;
        this.scanner = scanner;
    }

    public void displayMenu() {
        String choice;
        do {
            System.out.println("\n--- PRODUCT MANAGEMENT ---");
            System.out.println("1. Add New Product");
            System.out.println("2. Update Product");
            System.out.println("3. Remove Product");
            System.out.println("4. View All Products");
            System.out.println("5. Search Products");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddProduct();
                    break;
                case "2":
                    handleUpdateProduct();
                    break;
                case "3":
                    handleRemoveProduct();
                    break;
                case "4":
                    handleViewAllProducts();
                    break;
                case "5":
                    handleSearchProducts();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        } while (!choice.equals("0"));
    }

    private void handleAddProduct() {
        System.out.println("\n--- ADD NEW PRODUCT ---");
        String id = validateProductID("Enter Product ID: ");
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        System.out.print("Enter Price: ");
        String price = scanner.nextLine();
        System.out.print("Enter Stock Quantity: ");
        String stock = scanner.nextLine();

        String resultMsg = productController.handleAddNewProduct(new Model.Product(id, name, category, Double.parseDouble(price), Integer.parseInt(stock)));
        System.out.println(resultMsg);
    }

    private void handleUpdateProduct() {
        System.out.println("\n--- UPDATE PRODUCT ---");
        System.out.print("Enter Product ID to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter New Name (leave blank to skip): ");
        String name = scanner.nextLine();
        System.out.print("Enter New Category (leave blank to skip): ");
        String category = scanner.nextLine();
        System.out.print("Enter New Price (leave blank to skip): ");
        String price = scanner.nextLine();
        System.out.print("Enter New Stock Quantity (leave blank to skip): ");
        String stock = scanner.nextLine();

        Model.Product updatedProduct = new Model.Product(id, name, category, Double.parseDouble(price), Integer.parseInt(stock));
        String resultMsg = productController.handleUpdateProduct(id, updatedProduct);
        System.out.println(resultMsg);
    }

    private void handleRemoveProduct() {
        System.out.println("\n--- REMOVE PRODUCT ---");
        System.out.print("Enter Product ID to remove: ");
        String id = scanner.nextLine();

        String resultMsg = productController.handleDeleteProduct(id);
        System.out.println(resultMsg);
    }

    private void handleViewAllProducts() {
        System.out.println("\n--- PRODUCT LIST ---");
        List<Model.Product> products = productController.handleGetAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (Model.Product product : products) {
                System.out.println(product);
            }
        }
    }

    private void handleSearchProducts() {
        System.out.println("\n--- SEARCH PRODUCTS ---");
        System.out.print("Enter keyword (Name or Category): ");
        String keyword = scanner.nextLine();
        List<Model.Product> products = productController.handleSearchProductsByNameOrCategory(keyword);
        if (products.isEmpty()) {
            System.out.println("No products found matching the keyword.");
        } else {
            for (Model.Product product : products) {
                System.out.println(product);
            }
        }
    }

    private String validateProductID(String Message) {
        boolean isValid = false;
        String ProductID = null;
        while (!isValid) {
            System.out.print(Message);
            ProductID = scanner.nextLine().trim();
            if (ProductID.isEmpty()) {
                System.out.println("Product ID cannot be empty.");
            } else {
                isValid = true;
            }
        }
        return ProductID;
    }
}
