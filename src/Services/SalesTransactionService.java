package Services;

import java.util.List;

import Model.OrderDetail;
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

}
