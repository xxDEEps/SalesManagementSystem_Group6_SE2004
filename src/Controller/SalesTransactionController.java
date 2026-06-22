package Controller;

import java.util.List;

import Model.Customer;
import Model.OrderDetail;
import Model.Product;
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

    public String checkForExistingProduct(String productId) {
        if (productService.getProductById(productId) == null) {
            return "Product with ID " + productId + " does not exist or has been deleted.";
        }
        return null;
    }

    public String checkForExistingCustomer(String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return "Customer with ID " + customerId + " does not exist or has been deleted.";
        }
        return customer.getName();
    }

    public String checkForSufficientStock(String productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product != null && product.getStockQuantity() < quantity) {
            return "Insufficient stock for product ID " + productId + ". Available: " + product.getStockQuantity();
        }
        return null;
    }

    public double calculateTotalAmount(String customerId, List<OrderDetail> orderDetails) {
        return salesTransactionService.calculateTotalAmount(orderDetails, customerId);
    }

    
}
