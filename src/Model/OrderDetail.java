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

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}
