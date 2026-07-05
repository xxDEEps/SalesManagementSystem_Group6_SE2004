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
        return super.displayCustomerInfo() + " || Company Name: " + companyName 
                                           + " || Tax ID: " + taxID 
                                           + " || Negotiated Discount Rate: " + negotiatedDiscountRate;
    }

    @Override
    public double calculateFinalPrice(double originalPrice) {
        if (originalPrice > 500) {
            return originalPrice * (1 - negotiatedDiscountRate);
        }
        return originalPrice;
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


    //FOR SALES TRANSACTION
    @Override
    public double getDiscountRate() {
        return negotiatedDiscountRate;
    }
    @Override
    public String getBillingInfo() {
        return super.getBillingInfo() 
        + "\nCompany Name: " + companyName 
        + "\nTax ID: " + taxID
        + "\nNegotiated Discount Rate: " + negotiatedDiscountRate
        + "\n---";
    }

    public void printVATInvoice(double totalAmount){
        System.out.println("=== VAT Invoice ===");
        System.out.println("Company Name: " + companyName);
        System.out.println("Tax ID: " + taxID);
        System.out.println("Original Amount: $" + String.format("%.2f", totalAmount*0.9));
        System.out.println("VAT (10%): $" + String.format("%.2f", totalAmount*0.1));
        System.out.println("Total Amount (including VAT): $" + String.format("%.2f", totalAmount));
        System.out.println("===================");
    }
}
