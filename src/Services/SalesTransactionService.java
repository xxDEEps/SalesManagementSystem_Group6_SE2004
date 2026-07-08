package Services;

import java.util.List;
import java.util.Scanner;

import Model.CorporateCustomer;
import Model.Customer;
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
        
        Customer customer = customerService.getCustomerById(customerId);
        if (customer instanceof VIPCustomer) {
            VIPCustomer vipCustomer = (VIPCustomer) customer;
            int currentPoints = vipCustomer.getPoints();
            int pointsToRedeem = 0;
            double finalAmount = totalAmount;
            Scanner scanner = new Scanner(System.in);

            if (currentPoints < 10) {
                System.out.println("Not enough points to redeem. Current points: " + currentPoints);
            } else {
                System.out.println("You have " + currentPoints + " points. Please enter the number of points you want to redeem"
                        + "\n(10 points = $1 discount, enter multiple of 10 || 0 = do not redeem): ");

                while (true) {
                    try {
                        pointsToRedeem = Integer.parseInt(scanner.nextLine().trim());
                        if (pointsToRedeem % 10 != 0 && pointsToRedeem != 0) {
                            System.out.println("Please enter a multiple of 10 or 0.");
                        } else if (pointsToRedeem > currentPoints) {
                            System.out.println("You don't have enough points. Please enter a valid number.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                }

                if (pointsToRedeem > 0) {
                    double discount = pointsToRedeem / 10.0;
                    finalAmount -= discount;
                    vipCustomer.deductPoints(pointsToRedeem);
                    transaction.setPointsUsed(pointsToRedeem);
                    System.out.println("Redeemed " + pointsToRedeem + " points for a $" + discount + " discount.");
                }
            }

            int pointsEarned = (int) (finalAmount / 10);
            customerService.addPointsToVIPCustomer(customerId, pointsEarned);
            transaction.setTotalAmount(Math.round(finalAmount * 100.0) / 100.0);
            System.out.println("Points earned from this transaction: " + pointsEarned);
            System.out.println("Total points: " + vipCustomer.getPoints());
        }

        salesTransactionRepository.addSalesTransaction(transaction);
        printSalesTransaction(transaction);
        //in hoa don do
        if (customer instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
            corporateCustomer.printVATInvoice(totalAmount);
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
        Customer customer = customerService.getCustomerById(transaction.getCustomerID());
        String customerName = (customer != null) ? customer.getName() : "Unknown Customer";

        System.out.println("======================================================");
        System.out.println("                  SALES INVOICE");
        System.out.println("======================================================");
        System.out.println("Transaction ID : " + transaction.getSalesTransactionID());
        System.out.println("Customer ID    : " + transaction.getCustomerID());
        System.out.println("Customer Name  : " + customerName);
        System.out.println("Date           : " + transaction.getDate());
        System.out.println("------------------------------------------------------");
        System.out.println("ITEMS");
        System.out.println("------------------------------------------------------");

        double subtotal = 0.0;
        for (OrderDetail detail : transaction.getOrderItems()) {
            Product product = productService.getProductByIdIncludingDeleted(detail.getProductID());
            String productName = (product != null) ? product.getName() : "Unknown Product";
            double lineSubtotal = detail.calculateSubTotal();
            subtotal += lineSubtotal;

            System.out.printf("- %-28s Qty: %2d  @ $%7.2f%n", productName, detail.getQuantity(), detail.getPriceAtPurchase());
            System.out.printf("  Subtotal: $%10.2f%n", lineSubtotal);
        }

        System.out.println("------------------------------------------------------");
        System.out.printf("Subtotal        : $%10.2f%n", subtotal);

        if (customer instanceof VIPCustomer) {
            VIPCustomer vipCustomer = (VIPCustomer) customer;
            int pointsUsed = transaction.getPointsUsed();
            if (pointsUsed > 0) {
                System.out.printf("Points Used    : %10d%n", pointsUsed);
            }
            System.out.printf("VIP Discount   : $%10.2f%n", subtotal * vipCustomer.getDiscountRate());
        }

        if (customer instanceof CorporateCustomer && subtotal > 500) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
            System.out.printf("Corporate Discount   : $%10.2f%n", subtotal * corporateCustomer.getDiscountRate());
        }

        System.out.println("------------------------------------------------------");
        System.out.printf("TOTAL          : $%10.2f%n", transaction.getTotalAmount());

        if (customer instanceof VIPCustomer) {
            int pointsEarned = (int) (transaction.getTotalAmount() / 10);
            System.out.println("Points Earned  : " + pointsEarned);
            System.out.println("Current Points : " + ((VIPCustomer) customer).getPoints());
        }

        System.out.println("======================================================");
    }

}
