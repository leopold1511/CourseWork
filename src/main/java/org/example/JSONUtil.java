package org.example;

import com.google.gson.Gson;
import datamodel.Product;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class JSONUtil {

    public static Product importFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);

        ProductData productData = gson.fromJson(reader, ProductData.class);

        reader.close();

        return productData.getProduct();
    }

    static class ProductData {
        private Product product;

        public Product getProduct() {
            return product;
        }

        public void setProducts(Product product) {
            this.product = product;
        }
    }
}
