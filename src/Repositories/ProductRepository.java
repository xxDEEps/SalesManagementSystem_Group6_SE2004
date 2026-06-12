package Repositories;

import java.util.HashMap;
import java.util.Map;

import Model.Product;

public class ProductRepository extends AbstractFileRepository implements IRepository {
    private Map<String, Product> productMap = new HashMap<>();

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
