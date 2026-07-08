package Utilities;

import Model.Customer;
import Model.VIPCustomer;
import Model.CorporateCustomer;
import Model.Product;
import Model.SalesTransaction;
import Model.OrderDetail;
import Repositories.CustomerRepository;
import Repositories.ProductRepository;
import Repositories.SalesTransactionRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DataSeeder {
    
    private CustomerRepository customerRepo;
    private ProductRepository productRepo;
    private SalesTransactionRepository salesTransactionRepo;

    public DataSeeder(CustomerRepository customerRepo, ProductRepository productRepo, 
                      SalesTransactionRepository salesTransactionRepo) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.salesTransactionRepo = salesTransactionRepo;
    }

    // Method to seed all data
    public void seedAllData() {
        System.out.println("\n=== Starting Data Seeding ===");
        
        seedProducts();
        seedCustomers();
        seedSalesTransactions();
        
        System.out.println("=== Data Seeding Completed Successfully ===\n");
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double applyCustomerDiscount(String customerId, double amount) {
        Customer customer = customerRepo.findByCustomerById(customerId);
        if (customer != null) {
            return roundToTwoDecimals(customer.calculateFinalPrice(amount));
        }
        return roundToTwoDecimals(amount);
    }

    private void applyVIPTransactionPoints(String customerId, int pointsUsed, double finalAmount) {
        Customer customer = customerRepo.findByCustomerById(customerId);
        if (customer instanceof VIPCustomer) {
            VIPCustomer vipCustomer = (VIPCustomer) customer;
            if (pointsUsed > 0) {
                vipCustomer.deductPoints(pointsUsed);
            }
            int pointsEarned = (int) (finalAmount / 10);
            vipCustomer.addPoints(pointsEarned);
            customerRepo.updateCustomer(customerId, vipCustomer);
        }
    }

    private VIPCustomer createVIPCustomer(String customerID, String name, String phone, String address, double discountRate, int initialPoints) {
        VIPCustomer vipCustomer = new VIPCustomer(customerID, name, phone, address, discountRate);
        vipCustomer.addPoints(initialPoints);
        return vipCustomer;
    }

    // Seed Products
    private void seedProducts() {
        System.out.println(">> Seeding Products...");
        
        Product[] products = {
            new Product("P001", "Laptop Dell XPS 13", "Electronics", 999.99, 15),
            new Product("P002", "iPhone 15 Pro", "Electronics", 1299.99, 25),
            new Product("P003", "Sony Headphones WH-1000XM5", "Audio", 349.99, 40),
            new Product("P004", "Apple iPad Air", "Electronics", 599.99, 20),
            new Product("P005", "Samsung 4K Monitor", "Electronics", 499.99, 12),
            new Product("P006", "Mechanical Keyboard RGB", "Accessories", 149.99, 50),
            new Product("P007", "Logitech Wireless Mouse", "Accessories", 49.99, 80),
            new Product("P008", "USB-C Cable 2m", "Accessories", 19.99, 200),
            new Product("P009", "Phone Case iPhone 15", "Accessories", 29.99, 150),
            new Product("P010", "Screen Protector Glass", "Accessories", 9.99, 300)
        };
        
        try {
            for (Product product : products) {
                productRepo.saveNewProduct(product);
            }
        } catch (Exception e) {
            System.out.println("   Error seeding products: " + e.getMessage());
        }
        
        System.out.println("   > " + products.length + " products added");
    }

    // Seed Customers
    private void seedCustomers() {
        System.out.println(">> Seeding Customers...");
        
        // Regular Customers
        Customer[] customers = {
            new Customer("C001", "John Smith", "0901234567", "123 Main Street, New York"),
            new Customer("C002", "Sarah Johnson", "0902345678", "456 Oak Avenue, Los Angeles"),
            new Customer("C003", "Michael Brown", "0903456789", "789 Pine Road, Chicago"),
            new Customer("C004", "Emily Davis", "0904567890", "321 Elm Street, Houston"),
            new Customer("C005", "Robert Wilson", "0905678901", "654 Maple Drive, Phoenix")
        };
        
        for (Customer customer : customers) {
            customerRepo.saveNewCustomer(customer);
        }
        
        // VIP Customers
        VIPCustomer[] vipCustomers = {
            createVIPCustomer("V001", "Lisa Anderson", "0906789012", "987 Cedar Lane, Philadelphia", 0.15, 120),
            createVIPCustomer("V002", "James Taylor", "0907890123", "147 Birch Court, San Antonio", 0.20, 90),
            createVIPCustomer("V003", "Mary Martinez", "0908901234", "258 Oak Plaza, San Diego", 0.18, 50),
            createVIPCustomer("V004", "David Lee", "0909012345", "369 Spruce Street, Dallas", 0.12, 60)
        };
        
        for (VIPCustomer vipCustomer : vipCustomers) {
            customerRepo.saveNewCustomer(vipCustomer);
        }
        
        // Corporate Customers
        CorporateCustomer[] corporateCustomers = {
            new CorporateCustomer("CORP001", "Tech Solutions Inc.", "0910123456", "1000 Tech Park, San Francisco", "Tech Solutions Inc.", "TAX123456", 0.08),
            new CorporateCustomer("CORP002", "Global Enterprises Ltd.", "0911234567", "2000 Enterprise Boulevard, Boston", "Global Enterprises Ltd.", "TAX234567", 0.10),
            new CorporateCustomer("CORP003", "Digital Innovations Co.", "0912345678", "3000 Innovation Drive, Seattle", "Digital Innovations Co.", "TAX345678", 0.12),
            new CorporateCustomer("CORP004", "Future Systems Ltd.", "0913456789", "4000 Future Way, Austin", "Future Systems Ltd.", "TAX456789", 0.15)
        };
        
        for (CorporateCustomer corporateCustomer : corporateCustomers) {
            customerRepo.saveNewCustomer(corporateCustomer);
        }
        
        try {
            customerRepo.saveToFile();
        } catch (Exception e) {
            System.out.println("   Error saving customers: " + e.getMessage());
        }
        
        System.out.println("   > " + customers.length + " regular customers added");
        System.out.println("   > " + vipCustomers.length + " VIP customers added");
        System.out.println("   > " + corporateCustomers.length + " corporate customers added");
    }

    // Seed Sales Transactions
    private void seedSalesTransactions() {
        System.out.println(">> Seeding Sales Transactions...");
        
        List<SalesTransaction> transactions = new ArrayList<>();
        
        // Transaction 1: John Smith bought Laptop and Mouse
        List<OrderDetail> orderItems1 = new ArrayList<>();
        orderItems1.add(new OrderDetail("P001", 1, 999.99));   // 1x Laptop
        orderItems1.add(new OrderDetail("P007", 2, 49.99));    // 2x Mouse
        double total1 = 999.99 + (2 * 49.99);
        SalesTransaction txn1 = new SalesTransaction("C001", orderItems1, total1);
        txn1.setSalesTransactionID("T001");
        txn1.setDate(Date.valueOf("2024-01-15"));
        transactions.add(txn1);
        
        // Transaction 2: VIP Customer Lisa Anderson bought iPhone and Headphones
        List<OrderDetail> orderItems2 = new ArrayList<>();
        orderItems2.add(new OrderDetail("P002", 2, 1299.99));  // 2x iPhone 15 Pro
        orderItems2.add(new OrderDetail("P003", 1, 349.99));   // 1x Headphones
        double subtotal2 = (2 * 1299.99) + 349.99;
        double finalTotal2 = roundToTwoDecimals(applyCustomerDiscount("V001", subtotal2) - 2.0);
        SalesTransaction txn2 = new SalesTransaction("V001", orderItems2, finalTotal2);
        txn2.setSalesTransactionID("T002");
        txn2.setDate(Date.valueOf("2024-01-18"));
        txn2.setPointsUsed(20);
        applyVIPTransactionPoints("V001", 20, finalTotal2);
        transactions.add(txn2);
        
        // Transaction 3: Sarah Johnson bought Monitor and Keyboard
        List<OrderDetail> orderItems3 = new ArrayList<>();
        orderItems3.add(new OrderDetail("P005", 1, 499.99));   // 1x Monitor
        orderItems3.add(new OrderDetail("P006", 1, 149.99));   // 1x Keyboard
        double total3 = 499.99 + 149.99;
        SalesTransaction txn3 = new SalesTransaction("C002", orderItems3, total3);
        txn3.setSalesTransactionID("T003");
        txn3.setDate(Date.valueOf("2024-01-20"));
        transactions.add(txn3);
        
        // Transaction 4: VIP Customer James Taylor bought iPad and Accessories
        List<OrderDetail> orderItems4 = new ArrayList<>();
        orderItems4.add(new OrderDetail("P004", 1, 599.99));   // 1x iPad Air
        orderItems4.add(new OrderDetail("P009", 3, 29.99));    // 3x Phone Case
        orderItems4.add(new OrderDetail("P010", 2, 9.99));     // 2x Screen Protector
        double subtotal4 = 599.99 + (3 * 29.99) + (2 * 9.99);
        double finalTotal4 = roundToTwoDecimals(applyCustomerDiscount("V002", subtotal4) - 3.0);
        SalesTransaction txn4 = new SalesTransaction("V002", orderItems4, finalTotal4);
        txn4.setSalesTransactionID("T004");
        txn4.setDate(Date.valueOf("2024-01-25"));
        txn4.setPointsUsed(30);
        applyVIPTransactionPoints("V002", 30, finalTotal4);
        transactions.add(txn4);
        
        // Transaction 5: Michael Brown bought USB Cables and Screen Protectors
        List<OrderDetail> orderItems5 = new ArrayList<>();
        orderItems5.add(new OrderDetail("P008", 5, 19.99));    // 5x USB Cable
        orderItems5.add(new OrderDetail("P010", 10, 9.99));    // 10x Screen Protector
        double total5 = (5 * 19.99) + (10 * 9.99);
        SalesTransaction txn5 = new SalesTransaction("C003", orderItems5, total5);
        txn5.setSalesTransactionID("T005");
        txn5.setDate(Date.valueOf("2024-02-01"));
        transactions.add(txn5);
        
        // Transaction 6: VIP Customer Mary Martinez bulk purchase
        List<OrderDetail> orderItems6 = new ArrayList<>();
        orderItems6.add(new OrderDetail("P002", 3, 1299.99));  // 3x iPhone 15 Pro
        orderItems6.add(new OrderDetail("P006", 2, 149.99));   // 2x Keyboard
        double subtotal6 = (3 * 1299.99) + (2 * 149.99);
        double finalTotal6 = roundToTwoDecimals(applyCustomerDiscount("V003", subtotal6) - 4.0);
        SalesTransaction txn6 = new SalesTransaction("V003", orderItems6, finalTotal6);
        txn6.setSalesTransactionID("T006");
        txn6.setDate(Date.valueOf("2024-02-05"));
        txn6.setPointsUsed(40);
        applyVIPTransactionPoints("V003", 40, finalTotal6);
        transactions.add(txn6);
        
        // Transaction 7: Emily Davis small purchase
        List<OrderDetail> orderItems7 = new ArrayList<>();
        orderItems7.add(new OrderDetail("P007", 1, 49.99));    // 1x Mouse
        orderItems7.add(new OrderDetail("P008", 2, 19.99));    // 2x USB Cable
        double total7 = 49.99 + (2 * 19.99);
        SalesTransaction txn7 = new SalesTransaction("C004", orderItems7, total7);
        txn7.setSalesTransactionID("T007");
        txn7.setDate(Date.valueOf("2024-02-10"));
        transactions.add(txn7);
        
        // Transaction 8: Robert Wilson bought multiple products
        List<OrderDetail> orderItems8 = new ArrayList<>();
        orderItems8.add(new OrderDetail("P001", 1, 999.99));   // 1x Laptop
        orderItems8.add(new OrderDetail("P003", 1, 349.99));   // 1x Headphones
        orderItems8.add(new OrderDetail("P009", 1, 29.99));    // 1x Phone Case
        double total8 = 999.99 + 349.99 + 29.99;
        SalesTransaction txn8 = new SalesTransaction("C005", orderItems8, total8);
        txn8.setSalesTransactionID("T008");
        txn8.setDate(Date.valueOf("2024-02-15"));
        transactions.add(txn8);
        
        // Transaction 9: VIP Customer David Lee premium purchase
        List<OrderDetail> orderItems9 = new ArrayList<>();
        orderItems9.add(new OrderDetail("P004", 2, 599.99));   // 2x iPad Air
        orderItems9.add(new OrderDetail("P005", 1, 499.99));   // 1x Monitor
        double total9 = (2 * 599.99) + 499.99;
        SalesTransaction txn9 = new SalesTransaction("V004", orderItems9, total9);
        txn9.setSalesTransactionID("T009");
        txn9.setDate(Date.valueOf("2024-02-20"));
        transactions.add(txn9);
        
        // Transaction 10: Corporate Customer Tech Solutions Inc. bulk purchase
        List<OrderDetail> orderItems10 = new ArrayList<>();
        orderItems10.add(new OrderDetail("P001", 5, 999.99));   // 5x Laptop
        orderItems10.add(new OrderDetail("P005", 3, 499.99));   // 3x Monitor
        orderItems10.add(new OrderDetail("P006", 10, 149.99));  // 10x Keyboard
        double total10 = (5 * 999.99) + (3 * 499.99) + (10 * 149.99);
        double finalTotal10 = roundToTwoDecimals(applyCustomerDiscount("CORP001", total10));
        SalesTransaction txn10 = new SalesTransaction("CORP001", orderItems10, finalTotal10);
        txn10.setSalesTransactionID("T010");
        txn10.setDate(Date.valueOf("2024-03-01"));
        transactions.add(txn10);
        
        // Transaction 11: Corporate Customer Global Enterprises Ltd. purchase
        List<OrderDetail> orderItems11 = new ArrayList<>();
        orderItems11.add(new OrderDetail("P002", 8, 1299.99));  // 8x iPhone 15 Pro
        orderItems11.add(new OrderDetail("P003", 5, 349.99));   // 5x Headphones
        orderItems11.add(new OrderDetail("P009", 20, 29.99));   // 20x Phone Case
        double total11 = (8 * 1299.99) + (5 * 349.99) + (20 * 29.99);
        double finalTotal11 = roundToTwoDecimals(applyCustomerDiscount("CORP002", total11));
        SalesTransaction txn11 = new SalesTransaction("CORP002", orderItems11, finalTotal11);
        txn11.setSalesTransactionID("T011");
        txn11.setDate(Date.valueOf("2024-03-05"));
        transactions.add(txn11);
        
        // Transaction 12: Corporate Customer Digital Innovations Co. office supplies
        List<OrderDetail> orderItems12 = new ArrayList<>();
        orderItems12.add(new OrderDetail("P006", 15, 149.99));  // 15x Keyboard
        orderItems12.add(new OrderDetail("P007", 25, 49.99));   // 25x Mouse
        orderItems12.add(new OrderDetail("P008", 50, 19.99));   // 50x USB Cable
        orderItems12.add(new OrderDetail("P010", 100, 9.99));   // 100x Screen Protector
        double total12 = (15 * 149.99) + (25 * 49.99) + (50 * 19.99) + (100 * 9.99);
        double finalTotal12 = roundToTwoDecimals(applyCustomerDiscount("CORP003", total12));
        SalesTransaction txn12 = new SalesTransaction("CORP003", orderItems12, finalTotal12);
        txn12.setSalesTransactionID("T012");
        txn12.setDate(Date.valueOf("2024-03-10"));
        transactions.add(txn12);
        
        // Transaction 13: Corporate Customer Future Systems Ltd. premium setup
        List<OrderDetail> orderItems13 = new ArrayList<>();
        orderItems13.add(new OrderDetail("P001", 3, 999.99));   // 3x Laptop
        orderItems13.add(new OrderDetail("P004", 4, 599.99));   // 4x iPad Air
        orderItems13.add(new OrderDetail("P003", 3, 349.99));   // 3x Headphones
        double total13 = (3 * 999.99) + (4 * 599.99) + (3 * 349.99);
        double finalTotal13 = roundToTwoDecimals(applyCustomerDiscount("CORP004", total13));
        SalesTransaction txn13 = new SalesTransaction("CORP004", orderItems13, finalTotal13);
        txn13.setSalesTransactionID("T013");
        txn13.setDate(Date.valueOf("2024-03-15"));
        transactions.add(txn13);

        // Transaction 14: John Smith buys more laptops and accessories in April
        List<OrderDetail> orderItems14 = new ArrayList<>();
        orderItems14.add(new OrderDetail("P001", 2, 999.99));
        orderItems14.add(new OrderDetail("P008", 10, 19.99));
        orderItems14.add(new OrderDetail("P010", 5, 9.99));
        double total14 = (2 * 999.99) + (10 * 19.99) + (5 * 9.99);
        SalesTransaction txn14 = new SalesTransaction("C001", orderItems14, total14);
        txn14.setSalesTransactionID("T014");
        txn14.setDate(Date.valueOf("2024-04-02"));
        transactions.add(txn14);

        // Transaction 15: VIP customer buys phone and audio gear in April
        List<OrderDetail> orderItems15 = new ArrayList<>();
        orderItems15.add(new OrderDetail("P002", 1, 1299.99));
        orderItems15.add(new OrderDetail("P003", 2, 349.99));
        orderItems15.add(new OrderDetail("P007", 4, 49.99));
        double total15 = 1299.99 + (2 * 349.99) + (4 * 49.99);
        double finalTotal15 = roundToTwoDecimals(applyCustomerDiscount("V001", total15));
        SalesTransaction txn15 = new SalesTransaction("V001", orderItems15, finalTotal15);
        txn15.setSalesTransactionID("T015");
        txn15.setDate(Date.valueOf("2024-04-10"));
        transactions.add(txn15);

        // Transaction 16: Sarah Johnson buys accessories in April
        List<OrderDetail> orderItems16 = new ArrayList<>();
        orderItems16.add(new OrderDetail("P006", 3, 149.99));
        orderItems16.add(new OrderDetail("P007", 6, 49.99));
        orderItems16.add(new OrderDetail("P008", 15, 19.99));
        double total16 = (3 * 149.99) + (6 * 49.99) + (15 * 19.99);
        SalesTransaction txn16 = new SalesTransaction("C002", orderItems16, total16);
        txn16.setSalesTransactionID("T016");
        txn16.setDate(Date.valueOf("2024-04-18"));
        transactions.add(txn16);

        // Transaction 17: Corporate customer buys office equipment in May
        List<OrderDetail> orderItems17 = new ArrayList<>();
        orderItems17.add(new OrderDetail("P005", 2, 499.99));
        orderItems17.add(new OrderDetail("P001", 1, 999.99));
        orderItems17.add(new OrderDetail("P006", 8, 149.99));
        double total17 = (2 * 499.99) + 999.99 + (8 * 149.99);
        double finalTotal17 = roundToTwoDecimals(applyCustomerDiscount("CORP001", total17));
        SalesTransaction txn17 = new SalesTransaction("CORP001", orderItems17, finalTotal17);
        txn17.setSalesTransactionID("T017");
        txn17.setDate(Date.valueOf("2024-05-03"));
        transactions.add(txn17);

        // Transaction 18: VIP customer buys iPad and cases in May
        List<OrderDetail> orderItems18 = new ArrayList<>();
        orderItems18.add(new OrderDetail("P004", 2, 599.99));
        orderItems18.add(new OrderDetail("P009", 5, 29.99));
        double total18 = (2 * 599.99) + (5 * 29.99);
        double finalTotal18 = roundToTwoDecimals(applyCustomerDiscount("V002", total18));
        SalesTransaction txn18 = new SalesTransaction("V002", orderItems18, finalTotal18);
        txn18.setSalesTransactionID("T018");
        txn18.setDate(Date.valueOf("2024-05-12"));
        transactions.add(txn18);

        // Transaction 19: Michael Brown buys lots of accessories in May
        List<OrderDetail> orderItems19 = new ArrayList<>();
        orderItems19.add(new OrderDetail("P007", 10, 49.99));
        orderItems19.add(new OrderDetail("P008", 20, 19.99));
        orderItems19.add(new OrderDetail("P010", 30, 9.99));
        double total19 = (10 * 49.99) + (20 * 19.99) + (30 * 9.99);
        SalesTransaction txn19 = new SalesTransaction("C003", orderItems19, total19);
        txn19.setSalesTransactionID("T019");
        txn19.setDate(Date.valueOf("2024-05-20"));
        transactions.add(txn19);

        // Transaction 20: Corporate customer buys premium phones in June
        List<OrderDetail> orderItems20 = new ArrayList<>();
        orderItems20.add(new OrderDetail("P002", 4, 1299.99));
        orderItems20.add(new OrderDetail("P003", 3, 349.99));
        orderItems20.add(new OrderDetail("P005", 2, 499.99));
        double total20 = (4 * 1299.99) + (3 * 349.99) + (2 * 499.99);
        double finalTotal20 = roundToTwoDecimals(applyCustomerDiscount("CORP002", total20));
        SalesTransaction txn20 = new SalesTransaction("CORP002", orderItems20, finalTotal20);
        txn20.setSalesTransactionID("T020");
        txn20.setDate(Date.valueOf("2024-06-01"));
        transactions.add(txn20);

        // Transaction 21: Emily Davis buys laptop and iPad in June
        List<OrderDetail> orderItems21 = new ArrayList<>();
        orderItems21.add(new OrderDetail("P001", 1, 999.99));
        orderItems21.add(new OrderDetail("P004", 1, 599.99));
        orderItems21.add(new OrderDetail("P009", 2, 29.99));
        double total21 = 999.99 + 599.99 + (2 * 29.99);
        SalesTransaction txn21 = new SalesTransaction("C004", orderItems21, total21);
        txn21.setSalesTransactionID("T021");
        txn21.setDate(Date.valueOf("2024-06-15"));
        transactions.add(txn21);

        // Transaction 22: VIP customer makes another strong purchase in June
        List<OrderDetail> orderItems22 = new ArrayList<>();
        orderItems22.add(new OrderDetail("P002", 1, 1299.99));
        orderItems22.add(new OrderDetail("P001", 1, 999.99));
        orderItems22.add(new OrderDetail("P006", 5, 149.99));
        double total22 = 1299.99 + 999.99 + (5 * 149.99);
        double finalTotal22 = roundToTwoDecimals(applyCustomerDiscount("V003", total22));
        SalesTransaction txn22 = new SalesTransaction("V003", orderItems22, finalTotal22);
        txn22.setSalesTransactionID("T022");
        txn22.setDate(Date.valueOf("2024-06-30"));
        transactions.add(txn22);

        // Transaction 23: Another year sample for yearly demo
        List<OrderDetail> orderItems23 = new ArrayList<>();
        orderItems23.add(new OrderDetail("P003", 1, 349.99));
        orderItems23.add(new OrderDetail("P008", 10, 19.99));
        orderItems23.add(new OrderDetail("P010", 12, 9.99));
        double total23 = 349.99 + (10 * 19.99) + (12 * 9.99);
        SalesTransaction txn23 = new SalesTransaction("C005", orderItems23, total23);
        txn23.setSalesTransactionID("T023");
        txn23.setDate(Date.valueOf("2025-01-12"));
        transactions.add(txn23);

        // Transaction 24: Busy month demo - many transactions in one month
        List<OrderDetail> orderItems24 = new ArrayList<>();
        orderItems24.add(new OrderDetail("P001", 3, 999.99));
        orderItems24.add(new OrderDetail("P002", 2, 1299.99));
        orderItems24.add(new OrderDetail("P006", 10, 149.99));
        double total24 = (3 * 999.99) + (2 * 1299.99) + (10 * 149.99);
        double finalTotal24 = roundToTwoDecimals(applyCustomerDiscount("CORP001", total24));
        SalesTransaction txn24 = new SalesTransaction("CORP001", orderItems24, finalTotal24);
        txn24.setSalesTransactionID("T024");
        txn24.setDate(Date.valueOf("2024-07-01"));
        transactions.add(txn24);

        List<OrderDetail> orderItems25 = new ArrayList<>();
        orderItems25.add(new OrderDetail("P004", 4, 599.99));
        orderItems25.add(new OrderDetail("P005", 3, 499.99));
        orderItems25.add(new OrderDetail("P007", 15, 49.99));
        double total25 = (4 * 599.99) + (3 * 499.99) + (15 * 49.99);
        double finalTotal25 = roundToTwoDecimals(applyCustomerDiscount("V002", total25));
        SalesTransaction txn25 = new SalesTransaction("V002", orderItems25, finalTotal25);
        txn25.setSalesTransactionID("T025");
        txn25.setDate(Date.valueOf("2024-07-03"));
        transactions.add(txn25);

        List<OrderDetail> orderItems26 = new ArrayList<>();
        orderItems26.add(new OrderDetail("P003", 5, 349.99));
        orderItems26.add(new OrderDetail("P008", 25, 19.99));
        orderItems26.add(new OrderDetail("P009", 20, 29.99));
        double total26 = (5 * 349.99) + (25 * 19.99) + (20 * 29.99);
        SalesTransaction txn26 = new SalesTransaction("C003", orderItems26, total26);
        txn26.setSalesTransactionID("T026");
        txn26.setDate(Date.valueOf("2024-07-06"));
        transactions.add(txn26);

        List<OrderDetail> orderItems27 = new ArrayList<>();
        orderItems27.add(new OrderDetail("P001", 2, 999.99));
        orderItems27.add(new OrderDetail("P003", 3, 349.99));
        orderItems27.add(new OrderDetail("P010", 40, 9.99));
        double total27 = (2 * 999.99) + (3 * 349.99) + (40 * 9.99);
        SalesTransaction txn27 = new SalesTransaction("C001", orderItems27, total27);
        txn27.setSalesTransactionID("T027");
        txn27.setDate(Date.valueOf("2024-07-09"));
        transactions.add(txn27);

        List<OrderDetail> orderItems28 = new ArrayList<>();
        orderItems28.add(new OrderDetail("P002", 3, 1299.99));
        orderItems28.add(new OrderDetail("P004", 2, 599.99));
        orderItems28.add(new OrderDetail("P006", 12, 149.99));
        double total28 = (3 * 1299.99) + (2 * 599.99) + (12 * 149.99);
        double finalTotal28 = roundToTwoDecimals(applyCustomerDiscount("V001", total28));
        SalesTransaction txn28 = new SalesTransaction("V001", orderItems28, finalTotal28);
        txn28.setSalesTransactionID("T028");
        txn28.setDate(Date.valueOf("2024-07-12"));
        transactions.add(txn28);

        List<OrderDetail> orderItems29 = new ArrayList<>();
        orderItems29.add(new OrderDetail("P005", 4, 499.99));
        orderItems29.add(new OrderDetail("P008", 30, 19.99));
        orderItems29.add(new OrderDetail("P009", 25, 29.99));
        double total29 = (4 * 499.99) + (30 * 19.99) + (25 * 29.99);
        double finalTotal29 = roundToTwoDecimals(applyCustomerDiscount("CORP003", total29));
        SalesTransaction txn29 = new SalesTransaction("CORP003", orderItems29, finalTotal29);
        txn29.setSalesTransactionID("T029");
        txn29.setDate(Date.valueOf("2024-07-15"));
        transactions.add(txn29);

        List<OrderDetail> orderItems30 = new ArrayList<>();
        orderItems30.add(new OrderDetail("P001", 1, 999.99));
        orderItems30.add(new OrderDetail("P002", 1, 1299.99));
        orderItems30.add(new OrderDetail("P003", 2, 349.99));
        orderItems30.add(new OrderDetail("P007", 8, 49.99));
        double total30 = 999.99 + 1299.99 + (2 * 349.99) + (8 * 49.99);
        SalesTransaction txn30 = new SalesTransaction("C004", orderItems30, total30);
        txn30.setSalesTransactionID("T030");
        txn30.setDate(Date.valueOf("2024-07-20"));
        transactions.add(txn30);
        
        try {
            salesTransactionRepo.writeTransactionsToFile(transactions);
        } catch (Exception e) {
            System.out.println("   Error saving transactions: " + e.getMessage());
        }
        
        System.out.println("   > " + transactions.size() + " sales transactions added");
    }

    // Main method to run seeder standalone
    public static void main(String[] args) {
        System.out.println("Starting Data Seeder...\n");
        
        CustomerRepository customerRepo = new CustomerRepository();
        ProductRepository productRepo = new ProductRepository();
        SalesTransactionRepository salesTransactionRepo = new SalesTransactionRepository();
        
        DataSeeder seeder = new DataSeeder(customerRepo, productRepo, salesTransactionRepo);
        seeder.seedAllData();
        
        System.out.println(">> All data files have been created successfully!");
        System.out.println("  - customers.dat");
        System.out.println("  - products.dat");
        System.out.println("  - sales_transactions.dat");
    }
}
