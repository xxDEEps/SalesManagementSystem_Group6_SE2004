package Repositories;

import java.util.HashMap;
import java.util.Map;

import Model.Customer;

public class CustomerRepository extends AbstractFileRepository implements IRepository {
    private Map<String, Customer> customerMap = new HashMap<>();
}
