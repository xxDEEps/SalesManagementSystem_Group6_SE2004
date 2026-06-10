package Model;

public class Customer {
    private String customerID;
    private String name;
    private String phone;
    private String address;

    public Customer(String customerID, String name, String phone, String address) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Customer() {
    }

    public String displayCustomerInfo() {
        return "Customer ID: " + customerID + ", Name: " + name + ", Phone: " + phone + ", Address: " + address;
    }

    public void updateCustomerInfo(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public void getCustomerInfoById(String customerID) {
        if (this.customerID.equals(customerID)) {
            System.out.println(displayCustomerInfo());
        } else {
            System.out.println("Customer with ID " + customerID + " not found.");
        }
    }


}
