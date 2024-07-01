package org.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.core.datamodel.Product;
import org.core.datamodel.Stage;

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

    static class ProductData {
        private Product product;

        public Product getProduct() {
            return product;
        }

    }

    public static void exportData(String filePath, Map<String, HashSet<String>> elementsMap, ArrayList<Stage> listOfStages) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(filePath);

        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.elementsMap = elementsMap;
        dataWrapper.listOfStages = listOfStages;

        gson.toJson(dataWrapper, writer);
        writer.close();
    }

    public static class DataWrapper {
        private Map<String, HashSet<String>> elementsMap;
        private ArrayList<Stage> listOfStages;

        public Map<String, HashSet<String>> getElementsMap() {
            return elementsMap;
        }

        public ArrayList<Stage> getListOfStages() {
            return listOfStages;
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
