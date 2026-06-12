package Services;

import Repositories.CustomerRepository;
import Repositories.ProductRepository;
import Repositories.SalesTransactionRepository;

public class ReportService {
    private final SalesTransactionRepository salesRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    public ReportService(SalesTransactionRepository salesRepo, ProductRepository productRepo, CustomerRepository customerRepo) {
        this.salesRepo = salesRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
    }
}
