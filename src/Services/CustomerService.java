package Services;

import Model.Customer;
import Repositories.CustomerRepository;
import java.util.List;

public class CustomerService {
    // Gọi anh Thủ kho (Repository) ra để chuẩn bị sai vặt
    private final CustomerRepository repository = new CustomerRepository();

    // 1. Gọi xuống Repository để thêm khách
    public boolean addCustomer(Customer customer) {
        return repository.save(customer);
    }

    // 2. Gọi xuống Repository để sửa khách
    public boolean updateCustomer(String id, String name, String phone, String address) {
        return repository.update(id, name, phone, address);
    }

    // 3. Gọi xuống Repository để xóa khách
    public boolean deleteCustomer(String id) {
        return repository.delete(id);
    }

    // 4. Gọi xuống Repository để lấy hết danh sách khách về
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }
}
