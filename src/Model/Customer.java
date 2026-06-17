package Model;

public class Customer {
    private String customerID;
    private String name;
    private String phone;
    private String address;
    private boolean isDeleted = false; // 1. Đã thêm thuộc tính này

    public Customer(String customerID, String name, String phone, String address) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDeleted = false; // 2. Đã thêm gán mặc định trong constructor
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

    // 3. Đã thêm cặp hàm Getter/Setter này ở cuối file
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
}
