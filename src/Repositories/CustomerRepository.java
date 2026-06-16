package Repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import Model.Customer;

public class CustomerRepository extends AbstractFileRepository implements IRepository {
    
    // 1. Dùng HashMap để lưu dữ liệu: Key là ID (String), Value là đối tượng Customer
    private Map<String, Customer> customerMap = new HashMap<>();
    private final String filePath = "customers.dat";

    // Constructor: Tự động nạp dữ liệu cũ từ file lên khi khởi tạo
    public CustomerRepository() {
        try { loadFromFile(); } catch (Exception e) { }
    }

    // 2. Hàm GHI dữ liệu ra file (Ghi cả cái Map xuống ổ đĩa)
    @Override
    public void saveToFile() throws Exception {
        writeDataToFile(this.customerMap, filePath);
    }

    // 3. Hàm ĐỌC dữ liệu từ file lên (Ép kiểu dữ liệu đọc được về lại dạng Map)
    @Override
    public void loadFromFile() throws Exception {
        Object data = readDataFromFile(filePath);
        if (data instanceof Map) {
            this.customerMap = (Map<String, Customer>) data;
        }
    }

    // Chức năng 1: THÊM mới (Nếu Map đã chứa ID này rồi thì báo trùng = false)
    public boolean save(Customer customer) {
        if (customerMap.containsKey(customer.getCustomerID())) return false;
        customerMap.put(customer.getCustomerID(), customer); // Thêm vào Map
        try { saveToFile(); } catch (Exception e) { }       // Lưu file
        return true;
    }

    // Chức năng 2: SỬA (Tìm khách hàng bằng ID, nếu thấy thì gọi hàm sửa của bạn)
    public boolean update(String id, String name, String phone, String address) {
        Customer c = customerMap.get(id); // Lấy nhanh khách hàng bằng ID (Key)
        if (c == null) return false;
        c.updateCustomerInfo(name, phone, address); // Gọi hàm update có sẵn của bạn
        try { saveToFile(); } catch (Exception e) { } // Lưu file
        return true;
    }

    // Chức năng 3: XÓA (Xóa phần tử trong Map dựa vào khóa ID)
    public boolean delete(String id) {
        if (!customerMap.containsKey(id)) return false;
        customerMap.remove(id);                       // Xóa khỏi Map
        try { saveToFile(); } catch (Exception e) { } // Lưu file
        return true;
    }

    // Chức năng 4: XEM (Chuyển toàn bộ các giá trị Value của Map thành một List để trả về)
    public List<Customer> findAll() {
        return new ArrayList<>(
            customerMap.values());
    }
}
