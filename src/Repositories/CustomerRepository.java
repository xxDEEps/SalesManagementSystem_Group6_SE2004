package Repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import Model.Customer;

public class CustomerRepository extends AbstractFileRepository implements IRepository {
    
    private Map<String, Customer> customerMap = new HashMap<>();
    private final String filePath = "customers.dat";

    public CustomerRepository() {
        try { loadFromFile(); } catch (Exception e) { }
    }

    @Override
    public void saveToFile() throws Exception {
        writeDataToFile(this.customerMap, filePath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile() throws Exception {
        Object data = readDataFromFile(filePath);
        if (data instanceof Map) {
            this.customerMap = (Map<String, Customer>) data;
        }
    }

    // Hàm bổ trợ: Chỉ tìm kiếm thực thể gốc trong Map
    public Customer findById(String id) {
        return customerMap.get(id);
    }

    // Repo: Lưu thô vào Map và file
    public boolean save(Customer customer) {
        customerMap.put(customer.getCustomerID(), customer);
        try { saveToFile(); } catch (Exception e) { }
        return true;
    }

    // Repo: Cập nhật đè dữ liệu thô vào Map và file
    public boolean updateCustomer(String id, Customer customer) {
        customerMap.put(id, customer);
        try { saveToFile(); } catch (Exception e) { }
        return true;
    }

    // Repo: Trả về toàn bộ dữ liệu thô để Service tự lọc
    public List<Customer> findAll() {
        return new ArrayList<>(customerMap.values());
    }
}