package controllersandservices;// ProductService.java

import datamodel.Data;
import datamodel.Personnel;
import datamodel.Product;
import datamodel.Stage;
import org.example.JSONUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProductService {
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Stage> listOfStages = new ArrayList<>();
    private final Map<String, HashSet<String>> stageStringMap = new HashMap<>();

    public static final String PERSONNEL_KEY = "Персонал";
    public static final String STAGES_KEY = "Стадии";
    public static final String COMPONENTS_KEY = "Компоненты";
    public static final String EQUIPMENT_KEY = "Оборудование";
    public static final String ORGANIZATIONS_KEY = "Организации";
    public static final String PRODUCTS_KEY = "Продукт";

    {
        stageStringMap.put(PERSONNEL_KEY, new HashSet<>());
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
            boolean stageExists = listOfStages.stream().anyMatch(existingStage -> existingStage.getName().equals(stage.getName()));
            if (!stageExists) {
                listOfStages.add(stage);
            }
            if (stage.getPersonnel() != null)
                stage.getPersonnel().forEach(personnel -> stageStringMap.get(PERSONNEL_KEY).add(personnel.getProfession()));
            if (stage.getEquipment() != null) stageStringMap.get(EQUIPMENT_KEY).addAll(stage.getEquipment());
            if (stage.getEquipment() != null) stageStringMap.get(COMPONENTS_KEY).addAll(stage.getComponents());
            if (stage.getOrganizations() != null)
                stageStringMap.get(ORGANIZATIONS_KEY).addAll(stage.getOrganizations());
        }
    }

    public void createProduct(String name, String strategicDirection){
        Product product=new Product();
        product.setName(name);
        product.setStrategicDirection(strategicDirection);
        product.setStages(new ArrayList<>());
        addProduct(product);

    }

    public ArrayList<Stage> getListOfStages() {
        return listOfStages;
    }

    public Map<String, HashSet<String>> getStageStringMap() {
        return stageStringMap;
    }

    public void exportData(String path) throws IOException {
        JSONUtil.exportData(path,stageStringMap,listOfStages);
    }
    public void importData(String path) throws IOException {
        addDataFromWrapper(JSONUtil.importData(path));
    }
    private void addDataFromWrapper(JSONUtil.DataWrapper dataWrapper) {

        dataWrapper.getStageStringMap().forEach((key, value) -> {
            stageStringMap.getOrDefault(key, new HashSet<>()).addAll(value);
        });

        listOfStages.addAll(dataWrapper.getListOfStages());

        for (Stage stage : dataWrapper.getListOfStages()) {
            if (stage.getPersonnel() != null) {
                for (Personnel personnel : stage.getPersonnel()) {
                    stageStringMap.get(PERSONNEL_KEY).add(personnel.getProfession());
                }
            }
            if (stage.getOrganizations() != null) {
                stageStringMap.get(ORGANIZATIONS_KEY).addAll(stage.getOrganizations());
            }
            if (stage.getComponents() != null) {
                stageStringMap.get(COMPONENTS_KEY).addAll(stage.getComponents());
            }
            if (stage.getEquipment() != null) {
                stageStringMap.get(EQUIPMENT_KEY).addAll(stage.getEquipment());
            }
        }
    }
}
