package Model;

public class CorporateCustomer extends Customer {
    private String companyName;
    private String taxID;

    public CorporateCustomer(String customerID, String name, String phone, String address, String companyName, String taxID) {
        super(customerID, name, phone, address);
        this.companyName = companyName;
        this.taxID = taxID;
    }

    public CorporateCustomer() {
    }

    @Override
    public String displayCustomerInfo() {
        return super.displayCustomerInfo() + " || Company Name: " + companyName + " || Tax ID: " + taxID;
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

}
