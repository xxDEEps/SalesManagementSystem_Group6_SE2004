package Model;

import java.io.Serializable;

public class OrderDetail implements Serializable{
    private String productID;
    private int quantity;
    private double priceAtPurchase;

    public OrderDetail(String productID, int quantity, double priceAtPurchase) {
        this.productID = productID;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public double calculateSubTotal() {
        return priceAtPurchase * quantity;
    }
}
