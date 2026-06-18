package Model;

public class VIPCustomer extends Customer {
    private double discountRate;

    public VIPCustomer(String customerID, String name, String phone, String address, double discountRate) {
        super(customerID, name, phone, address);
        this.discountRate = discountRate;
    }

    public VIPCustomer() {
    }

    @Override
    public String displayCustomerInfo() {
        return super.displayCustomerInfo() + ", Discount Rate: " + discountRate;
    }

    public void updateDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
    
}
