package Model;

public class OrderDetail {
    private String productID;
    private int quantity;
    private double subTotal;

    public OrderDetail(String productID, int quantity, double subTotal) {
        this.productID = productID;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public double calculateSubTotal(double price) {
        return price * quantity;
    }
}
