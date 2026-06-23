package Controller;

import java.util.List;

import Model.Customer;
import Model.OrderDetail;
import Model.Product;
import Model.SalesTransaction;
import Services.CustomerService;
import Services.ProductService;
import Services.SalesTransactionService;

public class SalesTransactionController {
    private final SalesTransactionService salesTransactionService;
    private final ProductService productService;
    private final CustomerService customerService;
    public SalesTransactionController(SalesTransactionService salesTransactionService, ProductService productService, CustomerService customerService) {
        this.salesTransactionService = salesTransactionService;
        this.productService = productService;
        this.customerService = customerService;
    }

    public Product checkForExistingProduct(String productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return null;
        }
        return product;
    }

    public double getProductPrice(String productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return product.getPrice();
        }
        return -1;
    }

    public String checkForExistingCustomer(String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return null;
        }
        return customer.getCustomerID();
    }

    public Customer getCustomerById(String customerId) {
        return customerService.getCustomerById(customerId);
    }

    public String checkForSufficientStock(String productId, int quantity) {
        String result = salesTransactionService.checkForSufficientStock(productId, quantity);
        if (result != null) {
            return result;
        }
        return null;
    }

    public double calculateTotalAmount(String customerId, List<OrderDetail> orderDetails) {
        return salesTransactionService.calculateTotalAmount(orderDetails, customerId);
    }

    public String handleAddSalesTransaction(String customerId, List<OrderDetail> orderDetails, double totalAmount) {
        try {
            salesTransactionService.addSalesTransaction(customerId, orderDetails, totalAmount);
            return "Sales transaction added successfully.";
        } catch (Exception e) {
            return "Error adding sales transaction: " + e.getMessage();
        }
    }

    public List<SalesTransaction> handleGetSalesTransactionHistory() {
        return salesTransactionService.getAllSalesTransactions();
    }
    
}
