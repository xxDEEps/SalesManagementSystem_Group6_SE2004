package Model;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String customerID;
    private String name;
    private String phone;
    private String address;
    private boolean isDeleted = false;

    // Hàm khởi tạo đầy đủ tham số
    public Customer(String customerID, String name, String phone, String address) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDeleted = false;
    }

    // Hàm khởi tạo trống
    public Customer() {
    }

    public String displayCustomerInfo() {
        return "Customer ID: " + customerID + " || Name: " + name + " || Phone: " + phone + " || Address: " + address;
    }

    public double calculateFinalPrice(double originalPrice) {
        return originalPrice;
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

    // --- ĐÃ SỬA CÁC HÀM GETTER / SETTER CHUẨN XÓA BỎ LỖI UNSUPPORTED ---
    
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }


    // FOR BILLING====================================
    public double getDiscountRate() {
        return 0.0; // Mặc định khách hàng thường không có giảm giá
    }

    public String getBillingInfo() {
        return this.name + " (ID: " + this.customerID + ")"
        + "\nPhone: " + this.phone 
        + "\nAddress: " + this.address
        + "\n---";
    }
}