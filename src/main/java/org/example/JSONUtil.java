package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datamodel.Product;
import datamodel.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


public class JSONUtil {

    public static Product importFromJson(String filePath) throws Exception {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);
        ProductData productData = gson.fromJson(reader, ProductData.class);
        reader.close();
        return productData.getProduct();
    }

    public static void exportToJson(Product product, String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(filePath);

        ProductData productData = new ProductData();
        productData.setProduct(product);

        gson.toJson(productData, writer);

        writer.close();
    }

    static class ProductData {
        private Product product;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    public static void exportData(String filePath, Map<String, HashSet<String>> stageStringMap, ArrayList<Stage> listOfStages) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(filePath);

        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.stageStringMap = stageStringMap;
        dataWrapper.listOfStages = listOfStages;

        gson.toJson(dataWrapper, writer);
        writer.close();
    }

    public static class DataWrapper {
        private Map<String, HashSet<String>> stageStringMap;
        private ArrayList<Stage> listOfStages;

        public Map<String, HashSet<String>> getStageStringMap() {
            return stageStringMap;
        }

        public void setStageStringMap(Map<String, HashSet<String>> stageStringMap) {
            this.stageStringMap = stageStringMap;
        }

        public ArrayList<Stage> getListOfStages() {
            return listOfStages;
        }

        public void setListOfStages(ArrayList<Stage> listOfStages) {
            this.listOfStages = listOfStages;
        }
    }

    public static DataWrapper importData(String filePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);

        DataWrapper dataWrapper = gson.fromJson(reader, DataWrapper.class);

        reader.close();

        return dataWrapper;
    }
}
