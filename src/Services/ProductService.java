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

    // Add Product
    public boolean addNewProduct(Product product) throws Exception {

        Product existing = productRepository.findByProductById(product.getProductID());

        if (existing != null && !existing.isDeleted()) {
            return false;
        }

        productRepository.saveNewProduct(product);
        return true;
    }

    // Update Product
    public boolean updateProduct(String id, Product product) throws Exception {

        Product existing = productRepository.findByProductById(id);

        if (existing == null || existing.isDeleted()) {
            return false;
        }

        // Update Name
        if (product.getName() != null && !product.getName().trim().isEmpty()) {
            existing.setName(product.getName());
        }

        // Update Category
        if (product.getCategory() != null && !product.getCategory().trim().isEmpty()) {
            existing.setCategory(product.getCategory());
        }

        // Update Price
        if (product.getPrice() > 0) {
            existing.setPrice(product.getPrice());
        }

        // Update Stock
        if (product.getStockQuantity() >= 0) {
            existing.setStockQuantity(product.getStockQuantity());
        }

        productRepository.updateProduct(id, existing);

        return true;
    }

    // Delete Product (Soft Delete)
    public boolean deleteProduct(String id) throws Exception {

        Product existing = productRepository.findByProductById(id);

        if (existing == null || existing.isDeleted()) {
            return false;
        }

        existing.setDeleted(true);

        productRepository.updateProduct(id, existing);

        return true;
    }

    // View All Product
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        for (Product p : productRepository.findAllProducts()) {
            if (!p.isDeleted()) {
                products.add(p);
            }
        }

        return products;
    }

    // Search Product
    public List<Product> searchProductsByNameOrCategory(String keyword) {

        List<Product> result = new ArrayList<>();

        keyword = keyword.toLowerCase();

        for (Product p : productRepository.findAllProducts()) {

            if (p.isDeleted()) {
                continue;
            }

            if (p.getName().toLowerCase().contains(keyword)
                    || p.getCategory().toLowerCase().contains(keyword)) {

                result.add(p);
            }
        }

        return result;
    }

    // Get Product By ID
    public Product getProductById(String id) {
        Product product = productRepository.findByProductById(id);
        if (product == null || product.isDeleted()) {
            return null;
        }
        return product;
    }

    // Check Stock Availability
    public boolean isStockAvailable(String productId, int requiredQuantity) {
        Product product = getProductById(productId);
        if (product == null) {
            return false;
        }
        return product.getStockQuantity() >= requiredQuantity;
    }
}