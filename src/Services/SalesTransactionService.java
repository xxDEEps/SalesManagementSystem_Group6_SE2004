package Services;

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


}
