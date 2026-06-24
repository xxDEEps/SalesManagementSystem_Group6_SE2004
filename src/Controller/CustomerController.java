package Controller;
import Model.Customer;
import Services.CustomerService;
import java.util.List;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public boolean handleAdd(Customer customer) {
        return customerService.addNewCustomer(customer);
    }

    public boolean handleUpdate(String id, Customer updatedCustomer) {
        if (id == null || id.trim().isEmpty()) return false;
        return customerService.updateCustomer(id, updatedCustomer);
    }

    public boolean handleDelete(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return customerService.deleteCustomer(id);
    }

    public List<Customer> handleView() {
        return customerService.getAllCustomers();
    }
}
