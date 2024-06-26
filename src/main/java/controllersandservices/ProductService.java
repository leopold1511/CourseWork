package controllersandservices;// ProductService.java

import datamodel.Data;
import datamodel.Product;
import datamodel.Stage;

import java.util.*;

public class ProductService {
    private final ArrayList<Product> products = new ArrayList<>();
    private final Map<String, ArrayList<Data>> stageDataMap = new HashMap<>();
    private final Map<String, HashSet<String>> stageStringMap = new HashMap<>();

    public static final String PERSONNEL_KEY = "Персонал";
    public static final String STAGES_KEY = "Стадии";
    public static final String COMPONENTS_KEY = "Компоненты";
    public static final String EQUIPMENT_KEY = "Оборудование";
    public static final String ORGANIZATIONS_KEY = "Организации";

    {
        stageDataMap.put(PERSONNEL_KEY, new ArrayList<>());
        stageDataMap.put(STAGES_KEY, new ArrayList<>());
        stageStringMap.put(COMPONENTS_KEY, new HashSet<>());
        stageStringMap.put(EQUIPMENT_KEY, new HashSet<>());
        stageStringMap.put(ORGANIZATIONS_KEY, new HashSet<>());
    }

    public ArrayList<Product> getAllProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
        fillMaps(product);
    }

    public void fillMaps(Product product) {
        for (Stage stage : product.getStages()) {
            stageDataMap.get(STAGES_KEY).add(stage);
            if (stage.getPersonnel() != null) stageDataMap.get(PERSONNEL_KEY).addAll(stage.getPersonnel());
            if (stage.getEquipment() != null) stageStringMap.get(EQUIPMENT_KEY).addAll(stage.getEquipment());
            if (stage.getEquipment() != null) stageStringMap.get(COMPONENTS_KEY).addAll(stage.getComponents());
            if (stage.getOrganizations() != null)
                stageStringMap.get(ORGANIZATIONS_KEY).addAll(stage.getOrganizations());
        }
    }

    public void importProducts(ArrayList<Product> importedProducts) {
        for (Product importedProduct : importedProducts) {
            boolean exists = false;
            for (Product product : products) {
                if (product.getName().equals(importedProduct.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                products.add(importedProduct);
            }
        }
    }

    public Map<String, ArrayList<Data>> getStageDataMap() {
        return stageDataMap;
    }

    public Map<String, HashSet<String>> getStageStringMap() {
        return stageStringMap;
    }
}
