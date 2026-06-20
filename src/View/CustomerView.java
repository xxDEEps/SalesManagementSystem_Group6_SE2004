package View;

import java.util.Scanner;
import java.util.List;
import Controller.CustomerController;
import Model.Customer;
import Model.VIPCustomer;
import Model.CorporateCustomer;

public class CustomerView {
    private final CustomerController customerController;
    private final Scanner scanner;

    public CustomerView(CustomerController customerController, Scanner scanner) {
        this.customerController = customerController;
        this.scanner = scanner;
    }

    public void displayMenu() {
        String choice;
        do {
            System.out.println("\n--- CUSTOMER MANAGEMENT ---");
            System.out.println("1. Add New Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. Remove Customer");
            System.out.println("4. View All Customers");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddCustomer();
                    break;
                case "2":
                    handleUpdateCustomer();
                    break;
                case "3":
                    handleRemoveCustomer();
                    break;
                case "4":
                    handleViewAllCustomers();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        } while (!choice.equals("0"));
    }

    // 1. Logic Thêm khách hàng (Phân biệt Thường / VIP / Corporate để tạo đúng đối tượng)
    private void handleAddCustomer() {
        System.out.println("\n--- ADD NEW CUSTOMER ---");
        System.out.println("Select Customer Type:");
        System.out.println("1. Regular Customer");
        System.out.println("2. VIP Customer");
        System.out.println("3. Corporate Customer");
        System.out.print("Choose type (1-3): ");
        String customerType = scanner.nextLine().trim();
        
        System.out.print("Enter Customer ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine().trim();

        Customer customer;
        switch (customerType) {
            case "2":
                // VIP Customer
                System.out.print("Enter VIP Discount Rate (e.g., 0.1 for 10%): ");
                String discountStr = scanner.nextLine().trim();
                double discountRate = Double.parseDouble(discountStr);
                customer = new VIPCustomer(id, name, phone, address, discountRate);
                break;
            case "3":
                // Corporate Customer
                System.out.print("Enter Company Name: ");
                String companyName = scanner.nextLine().trim();
                System.out.print("Enter Tax ID: ");
                String taxID = scanner.nextLine().trim();
                System.out.print("Enter Negotiated Discount Rate (e.g., 0.05 for 5%): ");
                String negotiatedDiscountStr = scanner.nextLine().trim();
                double negotiatedDiscountRate = Double.parseDouble(negotiatedDiscountStr);
                customer = new CorporateCustomer(id, name, phone, address, companyName, taxID, negotiatedDiscountRate);
                break;
            default:
                // Regular Customer
                customer = new Customer(id, name, phone, address);
                break;
        }

        // Gọi sang Controller để xử lý cất vào kho dữ liệu
        if (customerController.handleAdd(customer)) {
            System.out.println(">> Added customer successfully!");
        } else {
            System.out.println(">> Error: Customer ID already exists!");
        }
    }

    // 2. Logic Cập nhật thông tin (Check xem là khách Regular / VIP / Corporate để xử lý đúng đắn)
    private void handleUpdateCustomer() {
        System.out.println("\n--- UPDATE CUSTOMER ---");
        System.out.print("Enter Customer ID to update: ");
        String id = scanner.nextLine().trim();

        // Tìm xem khách hàng có tồn tại trong danh sách hiện hành không
        List<Customer> currentList = customerController.handleView();
        Customer existingCustomer = null;
        for (Customer c : currentList) {
            if (c.getCustomerID().equalsIgnoreCase(id)) {
                existingCustomer = c;
                break;
            }
        }

        if (existingCustomer == null) {
            System.out.println(">> Error: Customer ID not found!");
            return;
        }

        // Hỏi thông tin mới (Nếu để trống thì lấy lại thông tin cũ)
        System.out.print("Enter New Name (leave blank to skip): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = existingCustomer.getName();

        System.out.print("Enter New Phone (leave blank to skip): ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) phone = existingCustomer.getPhone();

        System.out.print("Enter New Address (leave blank to skip): ");
        String address = scanner.nextLine().trim();
        if (address.isEmpty()) address = existingCustomer.getAddress();

        Customer updatedCustomer;
        // Kiểm tra xem đối tượng cũ vốn dĩ là khách nào: Regular / VIP / Corporate
        if (existingCustomer instanceof VIPCustomer) {
            System.out.print("Enter New VIP Discount Rate (leave blank to skip): ");
            String discountStr = scanner.nextLine().trim();
            double discountRate;
            if (discountStr.isEmpty()) {
                discountRate = ((VIPCustomer) existingCustomer).getDiscountRate();
            } else {
                discountRate = Double.parseDouble(discountStr);
            }
            // Tạo mới thực thể VIP để giữ/cập nhật thông tin VIP
            updatedCustomer = new VIPCustomer(id, name, phone, address, discountRate);
        } else if (existingCustomer instanceof CorporateCustomer) {
            System.out.print("Enter New Company Name (leave blank to skip): ");
            String companyName = scanner.nextLine().trim();
            if (companyName.isEmpty()) {
                companyName = ((CorporateCustomer) existingCustomer).getCompanyName();
            }
            System.out.print("Enter New Tax ID (leave blank to skip): ");
            String taxID = scanner.nextLine().trim();
            if (taxID.isEmpty()) {
                taxID = ((CorporateCustomer) existingCustomer).getTaxID();
            }
            System.out.print("Enter New Negotiated Discount Rate (leave blank to skip): ");
            String discountStr = scanner.nextLine().trim();
            double negotiatedDiscountRate;
            if (discountStr.isEmpty()) {
                negotiatedDiscountRate = ((CorporateCustomer) existingCustomer).getNegotiatedDiscountRate();
            } else {
                negotiatedDiscountRate = Double.parseDouble(discountStr);
            }
            // Tạo mới thực thể Corporate để giữ/cập nhật thông tin công ty
            updatedCustomer = new CorporateCustomer(id, name, phone, address, companyName, taxID, negotiatedDiscountRate);
        } else {
            // Regular Customer
            updatedCustomer = new Customer(id, name, phone, address);
        }

        // Đẩy nguyên Object mới xuống cho bộ xử lý cập nhật đè lên cũ
        if (customerController.handleUpdate(id, updatedCustomer)) {
            System.out.println(">> Customer updated successfully!");
        } else {
            System.out.println(">> Update failed!");
        }
    }

    // 3. Logic Xóa khách hàng (Gọi hàm xóa mềm)
    private void handleRemoveCustomer() {
        System.out.println("\n--- REMOVE CUSTOMER ---");
        System.out.print("Enter Customer ID to remove: ");
        String id = scanner.nextLine().trim();

        if (customerController.handleDelete(id)) {
            System.out.println(">> Customer removed successfully (Soft Deleted)!");
        } else {
            System.out.println(">> Error: Customer ID not found or already removed!");
        }
    }

    // 4. Logic Xem tất cả danh sách khách hàng đang hoạt động
    private void handleViewAllCustomers() {
        System.out.println("\n--- CUSTOMER LIST ---");
        List<Customer> customers = customerController.handleView();
        
        if (customers.isEmpty()) {
            System.out.println("No active customers found.");
        } else {
            // Quét qua danh sách và gọi hàm xuất thông tin bạn tự viết ở lớp Model
            for (Customer c : customers) {
                System.out.println(c.displayCustomerInfo());
            }
        }
    }
}
