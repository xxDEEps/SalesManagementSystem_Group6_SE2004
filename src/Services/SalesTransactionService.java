package Services;

import Repositories.CustomerRepository;
import Repositories.ProductRepository;
import Repositories.SalesTransactionRepository;

public class SalesTransactionService {
    private final SalesTransactionRepository salesTransactionRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    public SalesTransactionService(SalesTransactionRepository salesTransactionRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.salesTransactionRepository = salesTransactionRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }
}
