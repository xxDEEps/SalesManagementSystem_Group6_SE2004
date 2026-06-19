package Services;

import java.util.ArrayList;
import java.util.List;

import Model.Product;
import Repositories.ProductRepository;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean addNewProduct(Model.Product product) throws Exception {
        Product existing = productRepository.findByProductById(product.getProductID());
        if (existing != null && !existing.isDeleted()) {
            return false;
        }
        productRepository.saveNewProduct(product);
        return true;
    }

    public boolean updateProduct(String id, Product product) throws Exception {

    Product existing = productRepository.findByProductById(id);

    // Không tồn tại hoặc đã bị xóa
    if (existing == null || existing.isDeleted()) {
        return false;
    }

    // Chỉ cập nhật Name nếu người dùng có nhập
    if (!product.getName().isBlank()) {
        existing.setName(product.getName());
    }

    // Chỉ cập nhật Category nếu người dùng có nhập
    if (!product.getCategory().isBlank()) {
        existing.setCategory(product.getCategory());
    }

    // Chỉ cập nhật Price nếu > 0
    if (product.getPrice() > 0) {
        existing.setPrice(product.getPrice());
    }

    // Chỉ cập nhật Stock nếu >= 0
    if (product.getStockQuantity() >= 0) {
        existing.setStockQuantity(product.getStockQuantity());
    }

    productRepository.updateProduct(existing);

    return true;
}

    public boolean deleteProduct(String id) throws Exception {
        Product existing = productRepository.findByProductById(id);
        if (existing == null || existing.isDeleted()) {
            return false;
        }
        existing.setDeleted(true);
        productRepository.updateProduct(id, existing);
        return true;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllProducts();
    }

    public List<Product> searchProductsByNameOrCategory(String keyword) {
        List<Product> allProducts = productRepository.findAllProducts();
        List<Product> matchedProducts = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Product p : allProducts) {
            if (!p.isDeleted() && (p.getName().toLowerCase().contains(lowerKeyword) || 
                                    p.getCategory().toLowerCase().contains(lowerKeyword))) {
                matchedProducts.add(p);
            }
        }
        return matchedProducts;
    }
}
