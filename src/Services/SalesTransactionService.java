package Services;

import java.util.List;

import Model.OrderDetail;
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
            total += detail.getQuantity() * detail.getPriceAtPurchase();
        }
        return total * (1 - customerService.getCustomerById(customerId).getDiscountRate());
    }

    public void addSalesTransaction(String customerId, List<OrderDetail> orderDetails, double totalAmount) throws Exception {
        SalesTransaction transaction = new SalesTransaction(customerId, orderDetails, totalAmount);
        for (OrderDetail detail : transaction.getOrderItems()) {
            productService.deductProductStock(detail.getProductID(), detail.getQuantity());
        }
        
        salesTransactionRepository.addSalesTransaction(transaction);
    }

    public List<SalesTransaction> getAllSalesTransactions() {
        return salesTransactionRepository.getSalesTransactionsList();
    }

}
