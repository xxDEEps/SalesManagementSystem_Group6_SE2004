package Repositories;

import Model.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    // Kho lưu trữ danh sách khách hàng trong bộ nhớ máy tính
    private final List<Customer> customerList = new ArrayList<>();

    // 1. Logic Thêm khách hàng mới (Kiểm tra trùng ID)
    public boolean save(Customer customer) {
        for (Customer c : customerList) {
            // Dùng hàm displayCustomerInfo của bạn để bóc tách ID kiểm tra trùng lặp
            if (c.displayCustomerInfo().contains("Customer ID: " + customer.displayCustomerInfo().split(",")[0].split(": ")[1])) {
                return false; 
            }
        }
        customerList.add(customer);
        return true;
    }

    // 2. Logic Cập nhật thông tin (Gọi đúng hàm updateCustomerInfo bạn đã viết)
    public boolean update(String customerID, String name, String phone, String address) {
        for (Customer c : customerList) {
            if (c.displayCustomerInfo().contains("Customer ID: " + customerID)) {
                c.updateCustomerInfo(name, phone, address); // Kích hoạt hàm của bạn
                return true;
            }
        }
        return false;
    }

    // 3. Logic Xóa khách hàng
    public boolean delete(String customerID) {
        return customerList.removeIf(c -> c.displayCustomerInfo().contains("Customer ID: " + customerID));
    }

    // 4. Logic Lấy ra tất cả khách hàng để xem
    public List<Customer> findAll() {
        return customerList;
    }
}
