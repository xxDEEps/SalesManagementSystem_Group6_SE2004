package View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Controller.SalesTransactionController;
import Model.Customer;
import Model.OrderDetail;
import Model.Product;
import Model.SalesTransaction;

public class SalesTransactionView {

    private final SalesTransactionController salesTransactionController;
    private final Scanner scanner;

    public SalesTransactionView(SalesTransactionController salesTransactionController, Scanner scanner) {
        this.salesTransactionController = salesTransactionController;
        this.scanner = scanner;
    }

    public void displayMenu() {
        String choice;
        do {
            System.out.println("\n--- SALES TRANSACTION MANAGEMENT ---");
            System.out.println("1. Create New Sales Transaction");
            System.out.println("2. View Sales Transaction History");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleCreateTransaction();
                    break;
                case "2":
                    handleViewTransactionHistory();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (!choice.equals("0"));
    }

    private void handleCreateTransaction() {
        System.out.println("\n--- CREATE NEW SALES TRANSACTION ---");
        List<OrderDetail> orderDetails = new ArrayList<>();
        String customerId = null;

        // CHECK CUSTOMER

        while (customerId == null) {
            customerId = validateCustomerInput();
            if (customerId != null && customerId.equals("cancel")) {
                System.out.println("Returning to menu...");
                return;
            }
            if (customerId == null) {
                System.out.println("Customer does not exist. Please try again. (Or type 'cancel' to return to menu)");
            }
        }
        Customer customer = salesTransactionController.getCustomerById(customerId);
        System.out.println("Creating transaction for customer: " + customer.getName() + " (ID: " + customer.getCustomerID() + ")");

        //CHECK PRODUCT
        System.out.println("\n--- ADD PRODUCTS TO TRANSACTION ---");
        while (true) {
           
            String productId = validateProductIDInput();
            if (productId == null) {
                System.out.println("Product does not exist or is deleted. Please try again.");
                continue;
            }
            if (productId.equalsIgnoreCase("done")) {
                break;
            }else if (productId.equalsIgnoreCase("cancel")) {
                System.out.println("Returning to menu...");
                return;
            }
            Product product = salesTransactionController.checkForExistingProduct(productId);
            System.out.println("Selected Product: " + product.getName() + " | Price: $" + product.getPrice() + " | Stock: " + product.getStockQuantity());
            int quantity = validateQuantityInput(productId);
            if (quantity == -1) {
                return; 
            }

            double priceAtPurchase = salesTransactionController.getProductPrice(productId);
            if (priceAtPurchase == -1) {
                System.out.println("Error retrieving product price. Please try again.");
                continue;
            }
            OrderDetail orderDetail = new OrderDetail(productId, quantity, priceAtPurchase);
            orderDetails.add(orderDetail);
            System.out.println("Product added successfully to cart.");
            System.out.println("Current Cart: ");
            System.out.println("====================================");
            displayCurrentCart(orderDetails);
        }

        if (orderDetails.isEmpty()) {
            System.out.println("Notification: Transaction canceled. Cart is empty.");
            return;
        }
        System.out.println("Bill details:");
        System.out.println(customer.getBillingInfo());
        displayCurrentCart(orderDetails);
        double totalAmount = Math.round(salesTransactionController.calculateTotalAmount(customerId, orderDetails) * 100.0) / 100.0;
        System.out.println("Total Amount: $" + totalAmount);
        
        //CONFIRM TRANSACTION
        
        while (true) {
            System.out.print("1. Confirm Transaction | 2. Edit Quantity | 3. Cancel Transaction \nChoose an option: ");
            String confirmChoice = scanner.nextLine().trim();
            
            if(confirmChoice.equals("1")){
                System.out.println(salesTransactionController.handleAddSalesTransaction(customerId, orderDetails, totalAmount));
                break;
            }else if(confirmChoice.equals("2")){
                handleEditQuantity(orderDetails);
                totalAmount = Math.round(salesTransactionController.calculateTotalAmount(customerId, orderDetails) * 100.0) / 100.0;
                System.out.println("Updated Bill details:");
                System.out.println(customer.getBillingInfo());
                displayCurrentCart(orderDetails);
                System.out.println("Total Amount: $" + totalAmount);
            }else if(confirmChoice.equals("3")){
                System.out.println("Transaction canceled.");
                return;
            }else{
                System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void handleViewTransactionHistory() {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        List<SalesTransaction> transactions = salesTransactionController.handleGetSalesTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        // Display transactions in newest to oldest order
        //transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        for (SalesTransaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getSalesTransactionID());
            System.out.println("Customer ID: " + transaction.getCustomerID());
            System.out.println("Customer Name: " + salesTransactionController.getCustomerByIdIncludingDeleted(transaction.getCustomerID()).getName());
            System.out.println("Date: " + transaction.getDate());
            System.out.println("Total Amount: $" + transaction.getTotalAmount());
            System.out.println("Order Details:");
            for (OrderDetail detail : transaction.getOrderItems()) {
                Product product = salesTransactionController.getProductByIdIncludingDeleted(detail.getProductID());
                String productName = (product != null) ? product.getName() : "Unknown Product";
                System.out.println("- " + productName + " (ID: " + detail.getProductID() + ") | Quantity: " + detail.getQuantity() + " | Price at Purchase: $" + detail.getPriceAtPurchase());
            }
            System.out.println("-----------------------------------");
        }
    }

    private String validateCustomerInput() {
        System.out.print("Enter Customer ID: (Or type 'cancel' to return to menu) ");
        String customerId = scanner.nextLine().trim();
        if (customerId.equalsIgnoreCase("cancel")) {
            return "cancel";
        }
        String response = salesTransactionController.checkForExistingCustomer(customerId);
        if (response == null) {
            return null; // Không tồn tại hoặc đã bị xóa
        }
        return response; 
    }

    private String validateProductIDInput(){
        System.out.print("Enter Product ID: ('done' to finish; 'cancel' to return to menu) ");
        String productIdInput = scanner.nextLine().trim();
        if (productIdInput.equalsIgnoreCase("done")) {
            return "done"; 
        }
        if (productIdInput.equalsIgnoreCase("cancel")) {
            return "cancel"; 
        }
        Product existingProduct = salesTransactionController.checkForExistingProduct(productIdInput);
            if (existingProduct == null) {
                return null; // Không tồn tại hoặc đã bị xóa
            }
        return existingProduct.getProductID(); 
    }

    private int validateQuantityInput(String productId) {
        while (true) {
            System.out.print("Enter Quantity for product [" + productId + "]: \n ");
            String qtyInput = scanner.nextLine().trim();
            if (qtyInput.equalsIgnoreCase("cancel")) {
                System.out.println("Returning to menu...");
                return -1; 
            }
            try {
                int quantity = Integer.parseInt(qtyInput);
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than zero. Please try again.");
                    continue;
                }
                String stockCheck = salesTransactionController.checkForSufficientStock(productId, quantity);
                if (stockCheck != null) {
                    System.out.println(stockCheck);
                    continue;
                }
                return quantity;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer for quantity.");
            }
        }
    }

    private void displayCurrentCart(List<OrderDetail> orderDetails) {    
        for (OrderDetail detail : orderDetails) {
            Product product = salesTransactionController.checkForExistingProduct(detail.getProductID());
            if (product != null) {
                System.out.println("ID: " + detail.getProductID() + " - " + product.getName()
                    + "\nCategory: " + product.getCategory()
                    + "\nPrice: $" + detail.getPriceAtPurchase()
                    + "\nQuantity: " + detail.getQuantity());
                System.out.println("-----------------------------------");
            }
        }
    }

    private void handleEditQuantity(List<OrderDetail> orderDetails) {
        System.out.print("Enter Product ID to edit quantity: ");
        String productId = scanner.nextLine().trim();
        OrderDetail detailToEdit = null;
        for (OrderDetail detail : orderDetails) {
            if (detail.getProductID().equals(productId)) {
                detailToEdit = detail;
                break;
            }
        }
        if (detailToEdit == null) {
            System.out.println("Product not found in cart. Please try again.");
            return;
        }
        int newQuantity = validateQuantityInput(productId);
        if (newQuantity != -1) {
            detailToEdit.setQuantity(newQuantity);
            System.out.println("Quantity updated successfully.");
        }
    }

    
}
