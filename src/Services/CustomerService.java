package Services;

import Model.Customer;
import Repositories.CustomerRepository;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerService() {
        this.repository = new CustomerRepository();
    }

    // LOGIC 1: Thêm khách hàng (Kiểm tra trùng ID trước khi gọi Repo lưu)
    public boolean addNewCustomer(Customer customer) {
        Customer existing = repository.findByCustomerById(customer.getCustomerID());
        // Nếu đã tồn tại khách hàng và chưa bị xóa mềm thì báo trùng
        if (existing != null) {
            return false;
        }
        return repository.saveNewCustomer(customer);
    }

    // LOGIC 2: Sửa khách hàng (Kiểm tra tồn tại và trạng thái xóa mềm)
    public boolean updateCustomer(String id, Customer customer) {
        Customer existing = repository.findByCustomerById(id);
        if (existing == null || existing.isDeleted()) {
            return false; // Không tìm thấy hoặc đã bị xóa mềm thì không cho sửa
        }
        return repository.updateCustomer(id, customer);
    }

    // LOGIC 3: XÓA MỀM (SOFT DELETE) - Chuyển cờ isDeleted thành true thay vì xóa hẳn khỏi Map
    public boolean deleteCustomer(String id) {
        Customer existing = repository.findByCustomerById(id);
        if (existing == null || existing.isDeleted()) {
            return false; // Đã xóa rồi hoặc không tồn tại
        }
        
        // Thực hiện logic Xóa mềm tại tầng Service
        existing.setDeleted(true); 
        return repository.updateCustomer(id, existing); // Lưu lại trạng thái đã xóa mềm xuống Repo
    }

    // LOGIC 4: Lọc danh sách (Chỉ trả về những người CHƯA bị xóa mềm)
    public List<Customer> getAllCustomers() {
        List<Customer> all = repository.findAllCustomers();
        List<Customer> activeCustomers = new ArrayList<>();
        
        for (Customer c : all) {
            if (!c.isDeleted()) {
                activeCustomers.add(c);
            }
        }
        return activeCustomers;
    }

    //get by ID 
    public Customer getCustomerById(String id) {
        Customer customer = repository.findByCustomerById(id);
        if (customer != null && !customer.isDeleted()) {
            return customer;
        }
        return null;
    }

    public Customer getCustomerByIdIncludingDeleted(String id) {
        return repository.findByCustomerById(id);
    }

    public void addPointsToVIPCustomer(String customerId, int pointsToAdd) {
        Customer customer = repository.findByCustomerById(customerId);
        if (customer instanceof Model.VIPCustomer) {
            Model.VIPCustomer vipCustomer = (Model.VIPCustomer) customer;
            vipCustomer.addPoints(pointsToAdd);
            repository.updateCustomer(customerId, vipCustomer);
        }
    }

    public boolean deductPointsFromVIPCustomer(String customerId, int pointsToDeduct) {
        Customer customer = repository.findByCustomerById(customerId);
        if (customer instanceof Model.VIPCustomer) {
            Model.VIPCustomer vipCustomer = (Model.VIPCustomer) customer;
            if (vipCustomer.getPoints() >= pointsToDeduct) {
                vipCustomer.deductPoints(pointsToDeduct);
                repository.updateCustomer(customerId, vipCustomer);
                return true;
            }
        }
        return false;
    }
}