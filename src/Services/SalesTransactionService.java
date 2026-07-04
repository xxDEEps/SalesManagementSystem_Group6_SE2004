package Services;

import java.util.List;

import Model.OrderDetail;
import Model.Product;
import Model.SalesTransaction;
import Repositories.CustomerRepository;
import Repositories.ProductRepository;
import Repositories.SalesTransactionRepository;

public class SalesTransactionService {
    private final SalesTransactionRepository salesTransactionRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    public SalesTransactionService(SalesTransactionRepository salesTransactionRepository, ProductService productService, CustomerService customerService) {
        this.salesTransactionRepository = salesTransactionRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    public double calculateTotalAmount(List<OrderDetail> orderDetails, String customerId) {
        double total = 0.0;
        for (OrderDetail detail : orderDetails) {
            total += detail.calculateSubTotal();
        }
        return total * (1 - customerService.getCustomerById(customerId).getDiscountRate());
    }

    public void addSalesTransaction(String customerId, List<OrderDetail> orderDetails, double totalAmount) throws Exception {
        SalesTransaction transaction = new SalesTransaction(customerId, orderDetails, totalAmount);
        for (OrderDetail detail : transaction.getOrderItems()) {
            productService.deductProductStock(detail.getProductID(), detail.getQuantity());
        }
        
        salesTransactionRepository.addSalesTransaction(transaction);
        printSalesTransaction(transaction);
    }

    public List<SalesTransaction> getAllSalesTransactions() {
        return salesTransactionRepository.getSalesTransactionsList();
    }

    public String checkForSufficientStock(String productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product != null && product.getStockQuantity() < quantity) {
            return "Insufficient stock for product ID " + productId + ". Available: " + product.getStockQuantity();
        }
        return null;
    }

    public void printSalesTransaction(SalesTransaction transaction) {
        System.out.println("Sales Transaction ID: " + transaction.getSalesTransactionID());
        System.out.println("Customer: " + customerService.getCustomerById(transaction.getCustomerID()).getName());
        System.out.println("Order Details:");
        for (OrderDetail detail : transaction.getOrderItems()) {
            System.out.println("Product Name: " + productService.getProductById(detail.getProductID()).getName() + " | Quantity: " + detail.getQuantity() + " | Subtotal: " + detail.calculateSubTotal());
        }
        System.out.println("Total Amount: " + transaction.getTotalAmount());
    }

}
