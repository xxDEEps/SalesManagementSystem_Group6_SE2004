package View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Controller.SalesTransactionController;

public class SalesTransactionView {

    private final SalesTransactionController salesTransactionController;
    private final Scanner scanner;

    public SalesTransactionView(SalesTransactionController salesTransactionController, Scanner scanner) {
        this.salesTransactionController = salesTransactionController;
        this.scanner = scanner;
    }

    public void displayMenu() {
        String choice;
        do {
            System.out.println("\n--- SALES TRANSACTION MANAGEMENT ---");
            System.out.println("1. Create New Sales Transaction");
            System.out.println("2. Update Existing Transaction");
            System.out.println("3. Cancel/Remove Transaction");
            System.out.println("4. View Sales Transaction History");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleCreateTransaction();
                    break;
                case "2":
                    handleUpdateTransaction();
                    break;
                case "3":
                    handleCancelTransaction();
                    break;
                case "4":
                    handleViewTransactionHistory();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (!choice.equals("0"));
    }

    /**
     * TÍNH NĂNG 1, 2, 3: Khởi tạo, Thêm sản phẩm và Tính tổng tiền hóa đơn
     */
    private void handleCreateTransaction() {
        System.out.println("\n--- 1. CREATE NEW SALES TRANSACTION ---");
        System.out.print("Enter Transaction ID: ");
        String transId = scanner.nextLine().trim();
        System.out.print("Enter Customer ID: ");
        String cusId = scanner.nextLine().trim();

        List<String> productIds = new ArrayList<>();
        List<String> quantities = new ArrayList<>();

        // TÍNH NĂNG 2: Cho phép thêm liên tục sản phẩm vào hóa đơn
        System.out.println("\n--- 2. ADD PRODUCTS TO TRANSACTION ---");
        while (true) {
            System.out.print("Enter Product ID (or type 'done' to finish & calculate total): ");
            String pId = scanner.nextLine().trim();
            if (pId.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("Enter Quantity for product [" + pId + "]: ");
            String qty = scanner.nextLine().trim();

            productIds.add(pId);
            quantities.add(qty);
            System.out.println("Product added to temporary cart.");
        }

        if (productIds.isEmpty()) {
            System.out.println("Notification: Transaction canceled. Cart is empty.");
            return;
        }

        // TÍNH NĂNG 3: Controller đón nhận dữ liệu, tính tổng tiền (gồm cả chiết khấu VIP nếu có) và in hóa đơn
        // String response = salesTransactionController.handleCreateSalesTransaction(transId, cusId, productIds, quantities);
        // System.out.println("\n=== 3. BILL SUMMARY ===");
        // System.out.println(response);
    }

    /**
     * TÍNH NĂNG 4: Cập nhật thông tin hóa đơn (Thay đổi số lượng món hàng)
     */
    private void handleUpdateTransaction() {
        System.out.println("\n--- 4. UPDATE TRANSACTION ---");
        System.out.print("Enter Transaction ID to update: ");
        String transId = scanner.nextLine().trim();
        System.out.print("Enter Product ID to modify quantity: ");
        String pId = scanner.nextLine().trim();
        System.out.print("Enter New Quantity: ");
        String newQty = scanner.nextLine().trim();

        // String response = salesTransactionController.handleUpdateTransactionItem(transId, pId, newQty);
        // System.out.println("Notification: " + response);
    }

    /**
     * TÍNH NĂNG 4: Hủy hóa đơn (Hoàn trả lại tồn kho cho sản phẩm)
     */
    private void handleCancelTransaction() {
        System.out.println("\n--- 4. CANCEL TRANSACTION ---");
        System.out.print("Enter Transaction ID to cancel: ");
        String transId = scanner.nextLine().trim();

        System.out.print("Are you sure you want to cancel this transaction? Tồn kho sẽ được hoàn trả. (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes")) {
            // String response = salesTransactionController.handleCancelTransaction(transId);
            // System.out.println("Notification: " + response);
        } else {
            System.out.println("Cancel operation aborted.");
        }
    }

    /**
     * TÍNH NĂNG 5: Xem lịch sử giao dịch
     */
    private void handleViewTransactionHistory() {
        System.out.println("\n--- 5. VIEW TRANSACTION HISTORY ---");
        // String response = salesTransactionController.handleGetSalesTransactionHistory();
        // System.out.println(response);
    }
}
