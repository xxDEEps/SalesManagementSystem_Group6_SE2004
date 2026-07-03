package Controller;

import java.util.Collections;
import java.util.List;

import Model.Product;
import Services.ProductService;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public String handleAddNewProduct(Product product) {
        try {
            boolean success = productService.addNewProduct(product);
            if (success) {
                return "Product added successfully.";
            } else {
                return "Failed to add product. Product with the same ID already exists.";
            }
        } catch (Exception e) {
            return "Error occurred while saving product to file: " + e.getMessage();
        }
    }

    public String handleUpdateProduct(String id, Product product) {
        try {
            boolean success = productService.updateProduct(id, product);
            if (success) {
                return "Product updated successfully.";
            } else {
                return "Failed to update product. Product may not exist or has been deleted.";
            }
        } catch (Exception e) {
            return "Error occurred while updating product in file: " + e.getMessage();
        }
    }

    public String handleDeleteProduct(String id) {
        try {
            boolean success = productService.deleteProduct(id);
            if (success) {
                Product product = productService.getProductByIdIncludingDeleted(id);
                return "Product " + product.getName() + " (" + product.getProductID() + ") deleted successfully.";
            } else {
                return "Failed to delete product. Product may not exist or has been deleted.";
            }
        } catch (Exception e) {
            return "Error occurred while deleting product from file: " + e.getMessage();
        }
    }

    public List<Product> handleGetAllProducts() {
        try {
            return productService.getAllProducts();
        } catch (Exception e) {
            System.err.println("Error occurred while retrieving products from file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Product> handleSearchProductsByNameOrCategory(String keyword) {
        try {
            return productService.searchProductsByNameOrCategory(keyword);
        } catch (Exception e) {
            System.err.println("Error occurred while searching products: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public Product handleGetProductById(String id) {
        try {
            return productService.getProductById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while retrieving product by ID: " + e.getMessage());
            return null;
        }
    }
    public boolean isProductIdExistsIncludingDeleted(String id) {
        try {
            return productService.getProductByIdIncludingDeleted(id) != null;
        } catch (Exception e) {
            System.err.println("Error occurred while checking product ID existence: " + e.getMessage());
            return false;
        }
    }
}
