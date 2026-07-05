package Services;

import java.util.List;

import Model.CorporateCustomer;
import Model.OrderDetail;
import Model.Product;
import Model.SalesTransaction;
import Model.VIPCustomer;
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
        //in hoa don do
        if (customerService.getCustomerById(customerId) instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) customerService.getCustomerById(customerId);
            corporateCustomer.printVATInvoice(totalAmount);
        }
        if (customerService.getCustomerById(customerId) instanceof VIPCustomer) {
            int pointsEarned = (int) (totalAmount / 10);
            customerService.addPointsToVIPCustomer(customerId, pointsEarned);
            System.out.println("Points earned from this transaction: " + pointsEarned);
            VIPCustomer vipCustomer = (VIPCustomer) customerService.getCustomerById(customerId);
            System.out.println("Total points: " + vipCustomer.getPoints());
        }
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
        System.out.println("------------------------------------------------------");
        System.out.println("Transaction ID: " + transaction.getSalesTransactionID());
            System.out.println("Customer ID: " + transaction.getCustomerID());
            System.out.println("Customer Name: " + customerService.getCustomerByIdIncludingDeleted(transaction.getCustomerID()).getName());
            System.out.println("Date: " + transaction.getDate());
            System.out.println("Total Amount: $" + String.format("%.2f", transaction.getTotalAmount()));
            System.out.println("Order Details:");
            for (OrderDetail detail : transaction.getOrderItems()) {
                Product product = productService.getProductByIdIncludingDeleted(detail.getProductID());
                String productName = (product != null) ? product.getName() : "Unknown Product";
                System.out.println("- " + productName + " (ID: " + detail.getProductID() + ") | Quantity: " + detail.getQuantity() + " | Price at Purchase: $" + detail.getPriceAtPurchase() + "\n >Subtotal: $" + String.format("%.2f", detail.calculateSubTotal()));
            }
            
    }

}
