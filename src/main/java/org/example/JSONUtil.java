package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import datamodel.Product;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONUtil {

    public static ArrayList<Product> importFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);

        // Read the entire JSON object
        ProductData productData = gson.fromJson(reader, ProductData.class);

        // Close the reader
        reader.close();

        // Return the list of products
        return productData.getProducts();
    }

    static class ProductData {
        private ArrayList<Product> products;

        public ArrayList<Product> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<Product> products) {
            this.products = products;
        }
    }
}
