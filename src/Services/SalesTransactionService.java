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
            //logic sử dụng điểm thưởng cho khách hàng VIP
            VIPCustomer vipCustomer = (VIPCustomer) customer;
            int currentPoints = vipCustomer.getPoints();
            int pointsToRedeem = -1;
            double finalAmount = totalAmount;
            if(currentPoints < 10){
                System.out.println("Not enough points to redeem. Current points: " + currentPoints);
            } else {
                System.out.println("You have " + currentPoints + " points. Please enter the number of points you want to redeem" 
                +"\n(10 points = $1 discount, enter multiple of 10 || 0 = do not redeem): ");
                while (pointsToRedeem == -1) {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        pointsToRedeem = Integer.parseInt(scanner.nextLine());
                        if (pointsToRedeem % 10 != 0 && pointsToRedeem != 0) {
                            System.out.println("Please enter a multiple of 10 or 0.");
                            pointsToRedeem = -1;
                        } else if (pointsToRedeem > currentPoints) {
                            System.out.println("You don't have enough points. Please enter a valid number.");
                            pointsToRedeem = -1;
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
        System.out.println("------------------------------------------------------");
        System.out.println("Transaction ID: " + transaction.getSalesTransactionID());
            System.out.println("Customer ID: " + transaction.getCustomerID());
            System.out.println("Customer Name: " + customerService.getCustomerByIdIncludingDeleted(transaction.getCustomerID()).getName());
            System.out.println("Date: " + transaction.getDate());
            
            Customer customer = customerService.getCustomerById(transaction.getCustomerID());
            if (customer instanceof VIPCustomer) {
                System.out.println("Points Used: " + transaction.getPointsUsed());
            }

            System.out.println("Total Amount: $" + String.format("%.2f", transaction.getTotalAmount()));

            if (customer instanceof VIPCustomer) {
                System.out.println("Points Earned: " + (int) (transaction.getTotalAmount() / 10));
            }
            
            System.out.println("Order Details:");
            for (OrderDetail detail : transaction.getOrderItems()) {
                Product product = productService.getProductByIdIncludingDeleted(detail.getProductID());
                String productName = (product != null) ? product.getName() : "Unknown Product";
                System.out.println("- " + productName + " (ID: " + detail.getProductID() + ") | Quantity: " + detail.getQuantity() + " | Price at Purchase: $" + detail.getPriceAtPurchase() + "\n >Subtotal: $" + String.format("%.2f", detail.calculateSubTotal()));
            }
            
    }

}
