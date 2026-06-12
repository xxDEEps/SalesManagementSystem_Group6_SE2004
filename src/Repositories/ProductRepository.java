package Repositories;

import java.util.HashMap;
import java.util.Map;

import Model.Product;

public class ProductRepository extends AbstractFileRepository implements IRepository {
    private Map<String, Product> productMap = new HashMap<>();
}
