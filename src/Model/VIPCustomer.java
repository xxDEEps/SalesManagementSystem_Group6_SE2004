package Model;

public class VIPCustomer extends Customer {
    private double discountRate;
    private int points;

    public VIPCustomer(String customerID, String name, String phone, String address, double discountRate) {
        super(customerID, name, phone, address);
        this.discountRate = discountRate;
        this.points = 0;
    }

    public VIPCustomer() {
    }

    @Override
    public String displayCustomerInfo() {
        return super.displayCustomerInfo() + " || Discount Rate: " + discountRate + " || Points: " + points;
    }

    @Override
    public double calculateFinalPrice(double originalPrice) {
        return originalPrice * (1 - discountRate);
    }
    
    public void updateDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    //FOR SALES TRANSACTION 
    @Override
    public String getBillingInfo() {
        return super.getBillingInfo() 
        + "\nDiscount Rate: " + discountRate
        + "\nCurrentPoints: " + points
        + "\n---";
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
