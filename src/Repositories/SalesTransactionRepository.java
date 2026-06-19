package Repositories;

import java.util.ArrayList;
import java.util.List;

import Model.SalesTransaction;

public class SalesTransactionRepository extends AbstractFileRepository implements IRepository {
    private List<SalesTransaction> salesTransactionsList = new ArrayList<>();
    private String filePath = "sales_transactions.dat";

    @Override
    public void saveToFile() throws Exception {
        super.writeDataToFile(salesTransactionsList, filePath);
    }

    @Override
    public void loadFromFile() throws Exception {
        Object data = super.readDataFromFile(filePath);
        if (data != null) {
            salesTransactionsList = (List<SalesTransaction>) data;
        }
    }
    
    // Helper method for DataSeeder to write transactions
    public void writeTransactionsToFile(List<SalesTransaction> transactions) throws Exception {
        this.salesTransactionsList = transactions;
        saveToFile();
    }
}
