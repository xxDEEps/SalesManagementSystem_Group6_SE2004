package Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Product;

public class ProductRepository extends AbstractFileRepository implements IRepository {
    private Map<String, Product> productMap = new HashMap<>();
    private final String filepath = "products.dat";
    @Override
    public void saveToFile() throws Exception {
        super.writeDataToFile(this.productMap, filepath);
    }

    @Override
    public void loadFromFile() throws Exception {
        Object data = readDataFromFile(filepath);
        if(data instanceof Map){
            this.productMap = (Map<String, Product>) data;
        }
    }

    public Product findByProductById(String id) {
        return productMap.get(id);
    }

    public void saveNewProduct(Product product) throws Exception {
        productMap.put(product.getProductID(), product);
        saveToFile();
    }

    public void updateProduct(String id, Product product) throws Exception {
        productMap.put(id, product);
        saveToFile();
    }

    public List<Product> findAllProducts() {
        return new ArrayList<>(this.productMap.values());
    }
}
