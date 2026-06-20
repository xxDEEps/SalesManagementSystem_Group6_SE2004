package Model;

public class CorporateCustomer extends Customer {
    private String companyName;
    private String taxID;
    private double negotiatedDiscountRate; 
    public CorporateCustomer(String customerID, String name, String phone, String address, String companyName, String taxID, double negotiatedDiscountRate) {
        super(customerID, name, phone, address);
        this.companyName = companyName;
        this.taxID = taxID;
        this.negotiatedDiscountRate = negotiatedDiscountRate;
    }

    public CorporateCustomer() {
    }

    @Override
    public String displayCustomerInfo() {
        return super.displayCustomerInfo() + " || Company Name: " + companyName + " || Tax ID: " + taxID + " || Negotiated Discount Rate: " + negotiatedDiscountRate;
    }

    public void updateCompanyInfo(String companyName, String taxID) {
        this.companyName = companyName;
        this.taxID = taxID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public double getNegotiatedDiscountRate() {
        return negotiatedDiscountRate;
    }

    public void setNegotiatedDiscountRate(double negotiatedDiscountRate) {
        this.negotiatedDiscountRate = negotiatedDiscountRate;
    }

    // Override: Tính giá cuối cùng với chiết khấu thương lượng cho khách hàng doanh nghiệp
    @Override
    public double calculateFinalPrice(double originalPrice) {
        // Nếu hoá đơn trên 500 đô thì áp dụng chiết khấu thương lượng, ngược lại không có chiết khấu
        if (originalPrice > 500) {
            return originalPrice * (1 - negotiatedDiscountRate);
        }
        return originalPrice;
    }

}
