package View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import Controller.ReportController;

public class ReportView {

    private final ReportController reportController;
    private final Scanner scanner;

    public ReportView(ReportController reportController, Scanner scanner) {
        this.reportController = reportController;
        this.scanner = scanner;
    }

    public void displayMenu() {
        String choice;
        do {
            System.out.println("\n=================================");
            System.out.println("       REPORTS & ANALYTICS       ");
            System.out.println("=================================");
            System.out.println("1. Daily/Monthly Sales Report");
            System.out.println("2. Best-Selling Products");
            System.out.println("3. Top Spending Customers");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showSalesReport();
                    break;
                case "2":
                    showBestSellingProducts();
                    break;
                case "3":
                    showTopCustomers();
                    break;
                case "0":
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println(" Invalid option! Please try again.");
            }
        } while (!choice.equals("0"));
    }

    private void showSalesReport() {
        String period = "";
        boolean isValid = false;

        System.out.println("\n----------------------------------");
        System.out.println("     GENERATE SALES REPORT        ");
        System.out.println("----------------------------------");
        
        // Vòng lặp ép người dùng nhập đúng định dạng
        while (!isValid) {
            System.out.print("Enter date (dd/MM/yyyy) or month (MM/yyyy) [Or '0' to cancel]: ");
            period = scanner.nextLine().trim();

            if (period.equals("0")) {
                System.out.println("Operation cancelled.");
                return;
            }

            if (validatePeriod(period)) {
                isValid = true;
            } else {
                System.out.println(" Error: Invalid format or non-existent date! Please follow dd/MM/yyyy or MM/yyyy.");
            }
        }

        System.out.println("\n⚡ Fetching data from database...");
        double total = reportController.handleSalesReport(period);

        // Hiển thị report chi tiết và "dài" hơn một chút cho đẹp mắt
        System.out.println("\n==================================================");
        System.out.println("                SALES REVENUE REPORT              ");
        System.out.println("==================================================");
        System.out.printf(" Reporting Period : %s\n", period);
        System.out.printf(" Currency         : $ \n");
        System.out.println("--------------------------------------------------");
        System.out.printf(" Total Gross Revenue : %s $\n", String.format("%,.0f", total));
        System.out.println("--------------------------------------------------");
        System.out.println(" Status              : Successfully Generated");
        System.out.println("==================================================");
        
        pressEnterToReturn();
    }


    private boolean validatePeriod(String period) {
        // Kiểm tra xem có khớp với regex dd/MM/yyyy hoặc MM/yyyy không trước khi parse
        if (!period.matches("\\d{2}/\\d{4}") && !period.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return false;
        }

        String format = period.length() == 7 ? "MM/yyyy" : "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // setLenient(false) giúp chặn các ngày vô lý như 32/01 hay 29/02 (năm không nhuận)

        try {
            sdf.parse(period);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void showBestSellingProducts() {
        System.out.println("\n---------- BEST-SELLING PRODUCTS ----------");
        System.out.println(reportController.handleBestSellingProducts());
        System.out.println("-------------------------------------------");
        pressEnterToReturn();
    }

    private void showTopCustomers() {
        System.out.println("\n---------- TOP CUSTOMERS ----------");
        System.out.println(reportController.handleTopCustomers());
        System.out.println("-----------------------------------");
        pressEnterToReturn();
    }

    private void pressEnterToReturn() {
        System.out.print("\nPress ENTER to return...");
        scanner.nextLine();
    }
}