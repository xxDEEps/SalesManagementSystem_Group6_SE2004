package Controller;

import Model.Customer;
import Services.CustomerService;
import java.util.List;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 1. Validate và thêm khách hàng
    public boolean handleAdd(Customer customer) {
        // VALIDATE: Kiểm tra dữ liệu trống hoặc sai định dạng
        if (customer.getCustomerID() == null || customer.getCustomerID().trim().isEmpty()) {
            System.out.println(">> [Validation Error] Customer ID cannot be empty!");
            return false;
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            System.out.println(">> [Validation Error] Customer Name cannot be empty!");
            return false;
        }
        if (customer.getPhone() == null || !customer.getPhone().matches("\\d{10,11}")) {
            System.out.println(">> [Validation Error] Phone number must contain 10-11 digits!");
            return false;
        }
        
        return customerService.addCustomer(customer);
    }

    // 2. Validate và sửa khách hàng
    public boolean handleUpdate(String id, Customer updatedCustomer) {
        // VALIDATE: ID tìm kiếm và dữ liệu mới
        if (id == null || id.trim().isEmpty()) return false;
        if (updatedCustomer.getName() == null || updatedCustomer.getName().trim().isEmpty()) {
            System.out.println(">> [Validation Error] Name cannot be empty!");
            return false;
        }
        if (updatedCustomer.getPhone() == null || !updatedCustomer.getPhone().matches("\\d{10,11}")) {
            System.out.println(">> [Validation Error] Phone number must contain 10-11 digits!");
            return false;
        }

        return customerService.updateCustomer(id, updatedCustomer);
    }

    // 3. Validate ID trước khi xóa
    public boolean handleDelete(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return customerService.deleteCustomer(id);
    }

    // 4. Xem danh sách khách hàng
    public List<Customer> handleView() {
        return customerService.getAllCustomers();
    }
}