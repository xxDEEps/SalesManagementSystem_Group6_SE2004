package Repositories;

import java.util.ArrayList;
import java.util.List;

import Model.SalesTransaction;

public class SalesTransactionRepository extends AbstractFileRepository implements IRepository {
    private List<SalesTransaction> salesTransactionsList = new ArrayList<>();

    @Override
    public void saveToFile() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveToFile'");
    }

    @Override
    public void loadFromFile() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadFromFile'");
    }
}
