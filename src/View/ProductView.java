package View;

import java.util.List;
import java.util.Scanner;

import Controller.ProductController;
import Model.Product;

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
            System.out.println("\n========== PRODUCT MANAGEMENT ==========");
            System.out.println("1. Add New Product");
            System.out.println("2. Update Product");
            System.out.println("3. Remove Product");
            System.out.println("4. View All Products");
            System.out.println("5. Search Products");
            System.out.println("0. Back");
            System.out.print("Choose: ");

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
                    System.out.println("Invalid choice!");
            }

        } while (!choice.equals("0"));
    }

    // ================= ADD PRODUCT =================

    private void handleAddProduct() {

        System.out.println("\n===== ADD PRODUCT =====");

        String id = validateProductID("Enter Product ID: ");

        String name = validateName("Enter Product Name: ");

        String category = validateCategory("Enter Category: ");

        double price = validatePrice("Enter Price: ");

        int stock = validateStock("Enter Stock Quantity: ");

        Product product = new Product(id, name, category, price, stock);

        String result = productController.handleAddNewProduct(product);

        System.out.println(result);
    }

    // ================= UPDATE PRODUCT =================

    private void handleUpdateProduct() {

        System.out.println("\n===== UPDATE PRODUCT =====");

        String id = validateProductIDForUpdateAndDelete("Enter Product ID: ");

        System.out.print("Enter New Name (leave blank to skip): ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter New Category (leave blank to skip): ");
        String category = scanner.nextLine().trim();

        double price = -1;

        while (true) {

            System.out.print("Enter New Price (leave blank to skip): ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                break;
            }

            try {

                price = Double.parseDouble(input);

                if (price <= 0) {
                    System.out.println("Price must be greater than 0.");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid price.");
            }
        }

        int stock = -1;

        while (true) {

            System.out.print("Enter New Stock Quantity (leave blank to skip): ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                break;
            }

            try {

                stock = Integer.parseInt(input);

                if (stock < 0) {
                    System.out.println("Stock cannot be negative.");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid stock.");
            }
        }

        Product updatedProduct = new Product(id, name, category, price, stock);

        String result = productController.handleUpdateProduct(id, updatedProduct);

        System.out.println(result);
    }
        // ================= REMOVE PRODUCT =================

    private void handleRemoveProduct() {

        System.out.println("\n===== REMOVE PRODUCT =====");

        String id = validateProductIDForUpdateAndDelete("Enter Product ID: ");

        String result = productController.handleDeleteProduct(id);

        System.out.println(result);
    }

    // ================= VIEW PRODUCT =================

    private void handleViewAllProducts() {

        System.out.println("\n===== PRODUCT LIST =====");

        List<Product> products = productController.handleGetAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        System.out.println("+-----+--------------+---------------------------+-----------------+----------+--------+");
        System.out.printf("| %-3s | %-12s | %-25s | %-15s | %-8s | %-6s |\n",
                "No", "Product ID", "Name", "Category", "Price", "Stock");
        System.out.println("+-----+--------------+---------------------------+-----------------+----------+--------+");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            String name = product.getName();
            if (name.length() > 25) {
                name = name.substring(0, 22) + "...";
            }

            String category = product.getCategory();
            if (category.length() > 15) {
                category = category.substring(0, 12) + "...";
            }

            System.out.printf("| %-3d | %-12s | %-25s | %-15s | %-8.2f | %-6d |\n",
                    i + 1,
                    product.getProductID(),
                    name,
                    category,
                    product.getPrice(),
                    product.getStockQuantity());
        }

        System.out.println("+-----+--------------+---------------------------+-----------------+----------+--------+");
    }

    // ================= SEARCH PRODUCT =================

    private void handleSearchProducts() {

        System.out.println("\n===== SEARCH PRODUCT =====");

        System.out.print("Enter keyword: ");

        String keyword = scanner.nextLine().trim();

        List<Product> products =
                productController.handleSearchProductsByNameOrCategory(keyword);

        if (products.isEmpty()) {
            System.out.println("No matching product found.");
            return;
        }

        for (Product product : products) {
            System.out.println(product.displayProductInfo());
        }
    }

    // ================= VALIDATE PRODUCT ID =================

    private String validateProductID(String message) {

        while (true) {

            System.out.print(message);

            String id = scanner.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("Product ID cannot be empty.");
                continue;
            }

            if (!id.matches("[A-Za-z0-9]+")) {
                System.out.println("Product ID only contains letters and numbers.");
                continue;
            }

            if (productController.isProductIdExistsIncludingDeleted(id)) {
                System.out.println("Product ID already exists.");
                continue;
            }

            return id;
        }
    }

    private String validateProductIDForUpdateAndDelete(String message) {

        while (true) {

            System.out.print(message);

            String id = scanner.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("Product ID cannot be empty.");
                continue;
            }

            if (!id.matches("[A-Za-z0-9]+")) {
                System.out.println("Product ID only contains letters and numbers.");
                continue;
            }

            if (productController.handleGetProductById(id) == null) {
                System.out.println("Product ID does not exist or has been deleted.");
                continue;
            }

            return id;
        }
    }

    // ================= VALIDATE NAME =================

    private String validateName(String message) {

        while (true) {

            System.out.print(message);

            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Product name cannot be empty.");
                continue;
            }

            if (name.length() > 50) {
                System.out.println("Maximum 50 characters.");
                continue;
            }

            return name;
        }
    }

    // ================= VALIDATE CATEGORY =================

    private String validateCategory(String message) {

        while (true) {

            System.out.print(message);

            String category = scanner.nextLine().trim();

            if (category.isEmpty()) {
                System.out.println("Category cannot be empty.");
                continue;
            }

            if (category.length() > 50) {
                System.out.println("Maximum 50 characters.");
                continue;
            }

            return category;
        }
    }

    // ================= VALIDATE PRICE =================

    private double validatePrice(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                double price = Double.parseDouble(input);

                if (price <= 0) {
                    System.out.println("Price must be greater than 0.");
                    continue;
                }

                return price;

            } catch (NumberFormatException e) {
                System.out.println("Invalid price.");
            }
        }
    }

    // ================= VALIDATE STOCK =================

    private int validateStock(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                int stock = Integer.parseInt(input);

                if (stock < 0) {
                    System.out.println("Stock quantity cannot be negative.");
                    continue;
                }

                return stock;

            } catch (NumberFormatException e) {
                System.out.println("Invalid stock quantity.");
            }
        }
    }
}