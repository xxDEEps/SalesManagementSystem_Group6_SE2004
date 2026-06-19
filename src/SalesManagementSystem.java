
import View.CustomerView;
import View.MainView;
import View.ProductView;
import View.ReportView;
import View.SalesTransactionView;

import java.util.Scanner;

import Controller.CustomerController;
import Controller.ProductController;
import Controller.ReportController;
import Controller.SalesTransactionController;
import Repositories.CustomerRepository;
import Repositories.ProductRepository;
import Repositories.SalesTransactionRepository;
import Services.CustomerService;
import Services.ProductService;
import Services.ReportService;
import Services.SalesTransactionService;
import Utilities.DataSeeder;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author LENOVO
 */
public class SalesManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ProductRepository productRepo = new ProductRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        SalesTransactionRepository salesTransactionRepo = new SalesTransactionRepository();

        // DataSeeder dataSeeder = new DataSeeder(customerRepo, productRepo, salesTransactionRepo);
        // dataSeeder.seedAllData();

        //cho nay load du lieu tu file
        try {
            productRepo.loadFromFile();
            customerRepo.loadFromFile();
            salesTransactionRepo.loadFromFile();
            System.out.println(">> Nạp dữ liệu cũ thành công!");
        } catch (Exception e) {
            System.out.println(">> Chưa có dữ liệu cũ hoặc lỗi đọc file. Hệ thống sẽ khởi tạo mới.");
            System.out.println(">> Lỗi: " + e.getMessage());
        }

        
        ProductService productService = new ProductService(productRepo);
        CustomerService customerService = new CustomerService(customerRepo);
        
        SalesTransactionService salesTransactionService = new SalesTransactionService(
            salesTransactionRepo, productRepo, customerRepo
        );
        ReportService reportService = new ReportService(salesTransactionRepo, productRepo, customerRepo);
        
        ProductController productController = new ProductController(productService);
        CustomerController customerController = new CustomerController(customerService);
        SalesTransactionController salesTransactionController = new SalesTransactionController(salesTransactionService);
        ReportController reportController = new ReportController(reportService);
        
        ProductView productView = new ProductView(productController, scanner);
        CustomerView customerView = new CustomerView(customerController, scanner);
        SalesTransactionView salesTransactionView = new SalesTransactionView(salesTransactionController, scanner);

        
        ReportView reportView = new ReportView(reportController, scanner);
        MainView mainView = new MainView(productView, customerView, salesTransactionView, reportView, scanner);
        mainView.displayMainMenu();

    }
    
}
