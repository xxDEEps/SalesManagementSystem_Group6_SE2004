package Services;

import Model.Customer;
import Model.OrderDetail;
import Model.Product;
import Model.SalesTransaction;
import Repositories.CustomerRepository;
import Repositories.ProductRepository;
import Repositories.SalesTransactionRepository;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final SalesTransactionRepository salesRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    public ReportService(SalesTransactionRepository salesRepo, ProductRepository productRepo, CustomerRepository customerRepo) {
        this.salesRepo = salesRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
    }
    
     public double getSalesReportByPeriod(String period) {
        double total = 0;
        List<SalesTransaction> transactions = salesRepo.getSalesTransactionsList();

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");

        for (SalesTransaction t : transactions) {
            String day = dayFormat.format(t.getDate());
            String month = monthFormat.format(t.getDate());

            if (period.equals(day) || period.equals(month)) {
                total += t.getTotalAmount();
            }
        }

        return total;
    }

    public String getBestSellingProductReport() {
        Map<String, Integer> productSoldMap = new HashMap<>();

        for (SalesTransaction t : salesRepo.getSalesTransactionsList()) {
            for (OrderDetail detail : t.getOrderItems()) {
                String productId = detail.getProductID();
                int quantity = detail.getQuantity();

                productSoldMap.put(
                    productId,
                    productSoldMap.getOrDefault(productId, 0) + quantity
                );
            }
        }

        if (productSoldMap.isEmpty()) {
            return "No sales data found.";
        }

        String bestProductId = null;
        int maxSold = 0;

        for (String productId : productSoldMap.keySet()) {
            int sold = productSoldMap.get(productId);
            if (sold > maxSold) {
                maxSold = sold;
                bestProductId = productId;
            }
        }

        Product product = productRepo.findByProductById(bestProductId);
        String productName = product != null ? product.getName() : "Unknown Product";

        return bestProductId + "    " + productName + "    " + maxSold + " units sold";
    }

    public String getTopCustomerReport() {
        Map<String, Double> customerSpendingMap = new HashMap<>();

        for (SalesTransaction t : salesRepo.getSalesTransactionsList()) {
            String customerId = t.getCustomerID();
            double amount = t.getTotalAmount();

            customerSpendingMap.put(
                customerId,
                customerSpendingMap.getOrDefault(customerId, 0.0) + amount
            );
        }

        if (customerSpendingMap.isEmpty()) {
            return "No customer data found.";
        }

        String topCustomerId = null;
        double maxAmount = 0;

        for (String customerId : customerSpendingMap.keySet()) {
            double amount = customerSpendingMap.get(customerId);
            if (amount > maxAmount) {
                maxAmount = amount;
                topCustomerId = customerId;
            }
        }

        Customer customer = customerRepo.findByCustomerById(topCustomerId);
        String customerName = customer != null ? customer.getName() : "Unknown Customer";

        return topCustomerId + "    " + customerName + "    " + String.format("%,.0f", maxAmount) + "$";
    }
}
