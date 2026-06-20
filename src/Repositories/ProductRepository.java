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
        super.writeDataToFile(productMap, filepath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile() throws Exception {

        Object data = readDataFromFile(filepath);

        if (data instanceof Map) {
            productMap = (Map<String, Product>) data;
        }
    }

    // Tìm theo ID
    public Product findByProductById(String id) {
        return productMap.get(id);
    }

    // Thêm sản phẩm
    public void saveNewProduct(Product product) throws Exception {
        productMap.put(product.getProductID(), product);
        saveToFile();
    }

    // Cập nhật theo ID
    public void updateProduct(String id, Product product) throws Exception {
        productMap.put(id, product);
        saveToFile();
    }

    // Lấy tất cả sản phẩm
    public List<Product> findAllProducts() {
        return new ArrayList<>(productMap.values());
    }

    // Xóa khỏi Map (nếu sau này cần Hard Delete)
    public boolean removeProduct(String id) throws Exception {

        if (!productMap.containsKey(id)) {
            return false;
        }

        productMap.remove(id);
        saveToFile();

        return true;
    }

    // Kiểm tra tồn tại
    public boolean exists(String id) {
        return productMap.containsKey(id);
    }

    // Đếm số sản phẩm
    public int countProducts() {
        return productMap.size();
    }

    // Lấy toàn bộ Map (nếu cần)
    public Map<String, Product> getProductMap() {
        return productMap;
    }
}