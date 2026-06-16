package Controller;

import Model.Customer;
import Services.CustomerService;
import java.util.List;

public class CustomerController {
    private final CustomerService customerService;

    // Khởi tạo Constructor nhận CustomerService theo đúng chuẩn của nhóm bạn
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Nhận khách hàng từ giao diện và đẩy xuống tầng Service
    public boolean handleAdd(Customer customer) {
        return customerService.addCustomer(customer);
    }

    // Nhận thông tin sửa từ giao diện và đẩy xuống tầng Service
    public boolean handleUpdate(String id, String name, String phone, String address) {
        return customerService.updateCustomer(id, name, phone, address);
    }

    // Nhận ID cần xóa từ giao diện và đẩy xuống tầng Service
    public boolean handleDelete(String id) {
        return customerService.deleteCustomer(id);
    }

    // Gọi tầng Service lấy danh sách về để ném lên cho giao diện hiển thị
    public List<Customer> handleView() {
        return customerService.getAllCustomers();
    }
}
