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
        if (existing == null || existing.isDeleted()) {
            return false;
        }
        productRepository.updateProduct(id, product);
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
