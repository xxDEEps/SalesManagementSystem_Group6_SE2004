package Model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class SalesTransaction implements Serializable{
    private String salesTransactionID;
    private String customerID;
    private Date date;
    private double totalAmount;
    private List<OrderDetail> orderItems;
    //for VIPCustomer
    private int pointsUsed = 0;

    private String generateTransactionID() {
        return "TXN" + System.currentTimeMillis();
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public SalesTransaction(String customerID, List<OrderDetail> orderItems,double totalAmount) {
        this.salesTransactionID = generateTransactionID();
        this.customerID = customerID;
        this.date = getCurrentDate();
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    public SalesTransaction() {
    }

    public String getSalesTransactionID() {
        return salesTransactionID;
    }

    public void setSalesTransactionID(String salesTransactionID) {
        this.salesTransactionID = salesTransactionID;
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

    public List<OrderDetail> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderDetail> orderItems) {
        this.orderItems = orderItems;
    }

    public void setPointsUsed(int pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

}
