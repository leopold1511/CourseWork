package controllersandservices;// ProductService.java
import datamodel.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final ArrayList<Product> products = new ArrayList<>();

    public ArrayList<Product> getAllProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void importProducts(ArrayList<Product> importedProducts) {
        for (Product importedProduct : importedProducts) {
            boolean exists = false;
            for (Product product : products) {
                if (product.getProductName().equals(importedProduct.getProductName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                products.add(importedProduct);
            }
        }
    }
}
