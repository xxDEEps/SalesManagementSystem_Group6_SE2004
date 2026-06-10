package Model;

import java.sql.Date;
import java.util.List;

public class Transaction {
    private String transactionID;
    private String customerID;
    private Date date;
    private double totalAmount;
    private List<Product> orderItems;

    public Transaction(String transactionID, String customerID, Date date, double totalAmount, List<Product> orderItems) {
        this.transactionID = transactionID;
        this.customerID = customerID;
        this.date = date;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Product> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Product> orderItems) {
        this.orderItems = orderItems;
    }

    

}
