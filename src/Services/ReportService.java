package Services;

import Model.Customer;
import Model.OrderDetail;
import Model.Product;
import Model.SalesTransaction;
import Repositories.CustomerRepository;
import Repositories.ProductRepository;
import Repositories.SalesTransactionRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    
    public String getSalesReportByPeriod(String period) {
        List<SalesTransaction> transactions = salesRepo.getSalesTransactionsList();

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");

        double totalRevenue = 0;
        int totalOrders = 0;
        int totalProductsSold = 0;
        String bestProductId = null;
        int bestProductQty = 0;
        String topCustomerId = null;
        double topCustomerSpend = 0;

        Map<String, Integer> productQtyMap = new HashMap<>();
        Map<String, Double> customerSpendMap = new HashMap<>();

        for (SalesTransaction transaction : transactions) {
            String day = dayFormat.format(transaction.getDate());
            String month = monthFormat.format(transaction.getDate());

            if (!period.equals(day) && !period.equals(month)) {
                continue;
            }

            totalRevenue += transaction.getTotalAmount();
            totalOrders++;

            for (OrderDetail detail : transaction.getOrderItems()) {
                int quantity = detail.getQuantity();
                totalProductsSold += quantity;

                String productId = detail.getProductID();
                int currentQty = productQtyMap.getOrDefault(productId, 0);
                productQtyMap.put(productId, currentQty + quantity);

                if (currentQty + quantity > bestProductQty) {
                    bestProductQty = currentQty + quantity;
                    bestProductId = productId;
                }
            }

            String customerId = transaction.getCustomerID();
            double currentSpend = customerSpendMap.getOrDefault(customerId, 0.0);
            currentSpend += transaction.getTotalAmount();
            customerSpendMap.put(customerId, currentSpend);

            if (currentSpend > topCustomerSpend) {
                topCustomerSpend = currentSpend;
                topCustomerId = customerId;
            }
        }

        if (totalOrders == 0) {
            return "No sales data found for this period.";
        }

        double averageOrderValue = totalRevenue / totalOrders;
        Product bestProduct = productRepo.findByProductById(bestProductId);
        String bestProductName = bestProduct != null ? bestProduct.getName() : "Unknown Product";
        Customer topCustomer = customerRepo.findByCustomerById(topCustomerId);
        String topCustomerName = topCustomer != null ? topCustomer.getName() : "Unknown Customer";

        StringBuilder report = new StringBuilder();
        report.append("Reporting Period : ").append(period).append(System.lineSeparator());
        report.append("Total Revenue    : ").append(String.format("%,.0f", totalRevenue)).append("$").append(System.lineSeparator());
        report.append("Total Orders     : ").append(totalOrders).append(System.lineSeparator());
        report.append("Average Order    : ").append(String.format("%,.0f", averageOrderValue)).append("$").append(System.lineSeparator());
        report.append("Products Sold    : ").append(totalProductsSold).append(System.lineSeparator());
        report.append("Best Seller      : ").append(bestProductName).append(" (" ).append(bestProductQty).append(" units)").append(System.lineSeparator());
        report.append("Top Customer     : ").append(topCustomerName).append(" (" ).append(String.format("%,.0f", topCustomerSpend)).append("$)").append(System.lineSeparator());

        return report.toString().trim();
    }

    public String getBestSellingProductReport() {
        List<String> productIds = new ArrayList<>();
        List<Integer> soldQuantities = new ArrayList<>();

        for (SalesTransaction transaction : salesRepo.getSalesTransactionsList()) {
            for (OrderDetail detail : transaction.getOrderItems()) {
                String productId = detail.getProductID();
                int quantity = detail.getQuantity();

                boolean found = false;
                for (int i = 0; i < productIds.size(); i++) {
                    if (productIds.get(i).equals(productId)) {
                        soldQuantities.set(i, soldQuantities.get(i) + quantity);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    productIds.add(productId);
                    soldQuantities.add(quantity);
                }
            }
        }

        if (productIds.isEmpty()) {
            return "No sales data found.";
        }

        StringBuilder report = new StringBuilder();
        int topCount = Math.min(productIds.size(), 3);

        for (int rank = 0; rank < topCount; rank++) {
            int bestIndex = 0;
            int bestSoldQuantity = soldQuantities.get(0);

            for (int i = 1; i < soldQuantities.size(); i++) {
                if (soldQuantities.get(i) > bestSoldQuantity) {
                    bestSoldQuantity = soldQuantities.get(i);
                    bestIndex = i;
                }
            }

            String productId = productIds.get(bestIndex);
            Product product = productRepo.findByProductById(productId);
            String productName = product != null ? product.getName() : "Unknown Product";

            report.append("Top ")
                  .append(rank + 1)
                  .append(": ")
                  .append(productId)
                  .append(" - ")
                  .append(productName)
                  .append(" - ")
                  .append(bestSoldQuantity)
                  .append(" units sold")
                  .append(System.lineSeparator());

            soldQuantities.set(bestIndex, -1);
        }

        return report.toString().trim();
    }

    public String getTopCustomerReport(String period) {
        List<String> customerIds = new ArrayList<>();
        List<Double> totalSpendings = new ArrayList<>();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        for (SalesTransaction transaction : salesRepo.getSalesTransactionsList()) {
            String transactionMonth = monthFormat.format(transaction.getDate());
            String transactionYear = yearFormat.format(transaction.getDate());
            boolean matchesPeriod = period.contains("/")
                    ? period.equals(transactionMonth)
                    : period.equals(transactionYear);

            if (!matchesPeriod) {
                continue;
            }

            String customerId = transaction.getCustomerID();
            double amount = transaction.getTotalAmount();

            boolean found = false;
            for (int i = 0; i < customerIds.size(); i++) {
                if (customerIds.get(i).equals(customerId)) {
                    totalSpendings.set(i, totalSpendings.get(i) + amount);
                    found = true;
                    break;
                }
            }

            if (!found) {
                customerIds.add(customerId);
                totalSpendings.add(amount);
            }
        }

        if (customerIds.isEmpty()) {
            return "No customer data found.";
        }

        StringBuilder report = new StringBuilder();
        int topCount = Math.min(customerIds.size(), 3);

        for (int rank = 0; rank < topCount; rank++) {
            int bestIndex = 0;
            double bestAmount = totalSpendings.get(0);

            for (int i = 1; i < totalSpendings.size(); i++) {
                if (totalSpendings.get(i) > bestAmount) {
                    bestAmount = totalSpendings.get(i);
                    bestIndex = i;
                }
            }

            String customerId = customerIds.get(bestIndex);
            Customer customer = customerRepo.findByCustomerById(customerId);
            String customerName = customer != null ? customer.getName() : "Unknown Customer";

            report.append("Top ")
                  .append(rank + 1)
                  .append(": ")
                  .append(customerId)
                  .append(" - ")
                  .append(customerName)
                  .append(" - ")
                  .append(String.format("%,.0f", bestAmount))
                  .append("$")
                  .append(System.lineSeparator());

            totalSpendings.set(bestIndex, -1.0);
        }

        return report.toString().trim();
    }
}
