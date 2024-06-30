package org.core;// ProductService.java

import org.core.datamodel.Personnel;
import org.core.datamodel.Product;
import org.core.datamodel.Stage;
import org.utils.JSONUtil;

import java.io.IOException;
import java.util.*;

public class ProductService {
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Stage> listOfStages = new ArrayList<>();
    private final Map<String, HashSet<String>> elementsMap = new HashMap<>();

    public static final String PERSONNEL_KEY = "Персонал";
    public static final String STAGES_KEY = "Стадии";
    public static final String COMPONENTS_KEY = "Компоненты";
    public static final String EQUIPMENT_KEY = "Оборудование";
    public static final String ORGANIZATIONS_KEY = "Организации";
    public static final String PRODUCTS_KEY = "Продукт";

    {
        elementsMap.put(PERSONNEL_KEY, new HashSet<>());
        elementsMap.put(COMPONENTS_KEY, new HashSet<>());
        elementsMap.put(EQUIPMENT_KEY, new HashSet<>());
        elementsMap.put(ORGANIZATIONS_KEY, new HashSet<>());
    }

    public ArrayList<Product> getAllProducts() {
        return products;
    }

    private void addProduct(Product product) {
        products.add(product);
        replaceDuplicates(product);
        fillMaps(product);
    }

    private void fillMaps(Product product) {
        for (Stage stage : product.getStages()) {
            boolean stageExists = listOfStages.stream().anyMatch(existingStage -> existingStage.getName().equals(stage.getName()));
            if (!stageExists) {
                listOfStages.add(stage);
            }
            if (stage.getPersonnel() != null)
                stage.getPersonnel().forEach(personnel -> elementsMap.get(PERSONNEL_KEY).add(personnel.getProfession()));
            if (stage.getEquipment() != null) elementsMap.get(EQUIPMENT_KEY).addAll(stage.getEquipment());
            if (stage.getEquipment() != null) elementsMap.get(COMPONENTS_KEY).addAll(stage.getComponents());
            if (stage.getOrganizations() != null)
                elementsMap.get(ORGANIZATIONS_KEY).addAll(stage.getOrganizations());
        }
    }

    public void createProduct(String name, String strategicDirection) {
        Product product = new Product();
        product.setName(name);
        product.setStrategicDirection(strategicDirection);
        product.setStages(new ArrayList<>());
        addProduct(product);

    }

    public ArrayList<Stage> getListOfStages() {
        return listOfStages;
    }

    public Map<String, HashSet<String>> getElementsMap() {
        return elementsMap;
    }

    public void exportData(String path) throws IOException {
        JSONUtil.exportData(path, elementsMap, listOfStages);
    }

    public void importData(String path) throws Exception {
        addDataFromWrapper(JSONUtil.importData(path));
    }

    public void importProduct(String path) throws Exception {
        addProduct(JSONUtil.importProduct(path));
    }

    private void addDataFromWrapper(JSONUtil.DataWrapper dataWrapper) {
        dataWrapper.getElementsMap().forEach((key, value) -> elementsMap.getOrDefault(key, new HashSet<>()).addAll(value));

        for (Stage stage : dataWrapper.getListOfStages()) {
            boolean stageExists = false;
            for (Stage existingStage : listOfStages) {
                if (existingStage.getName().equals(stage.getName())) {
                    stageExists = true;
                    replaceIfEqual(stage, existingStage);
                    break;
                }
            }
            if (!stageExists) {
                listOfStages.add(stage);
            }

            if (stage.getPersonnel() != null) {
                for (Personnel personnel : stage.getPersonnel()) {
                    elementsMap.get(PERSONNEL_KEY).add(personnel.getProfession());
                }
            }
            if (stage.getOrganizations() != null) {
                elementsMap.get(ORGANIZATIONS_KEY).addAll(stage.getOrganizations());
            }
            if (stage.getComponents() != null) {
                elementsMap.get(COMPONENTS_KEY).addAll(stage.getComponents());
            }
            if (stage.getEquipment() != null) {
                elementsMap.get(EQUIPMENT_KEY).addAll(stage.getEquipment());
            }
        }
    }

    private void replaceDuplicates(Product importedProduct) {
        for (Stage importedStage : importedProduct.getStages()) {
            for (Stage existingStage : listOfStages) {
                if (importedStage.getName().equals(existingStage.getName())) {
                    replaceIfEqual(importedStage, existingStage);
                }
            }
        }
    }

    private void replaceIfEqual(Stage importedStage, Stage existingStage) {
        if (importedStage.getOrganizations() != null && importedStage.getOrganizations().equals(existingStage.getOrganizations())) {
            importedStage.setOrganizations(existingStage.getOrganizations());
        }

        if (importedStage.getComponents() != null && importedStage.getComponents().equals(existingStage.getComponents())) {
            importedStage.setComponents(existingStage.getComponents());
        }

        if (importedStage.getEquipment() != null && importedStage.getEquipment().equals(existingStage.getEquipment())) {
            importedStage.setEquipment(existingStage.getEquipment());
        }

        if (importedStage.getPersonnel() != null && importedStage.getPersonnel().equals(existingStage.getPersonnel())) {
            importedStage.setPersonnel(existingStage.getPersonnel());
        }

        if (importedStage.getNextStages() != null && importedStage.getNextStages().equals(existingStage.getNextStages())) {
            importedStage.setNextStages(existingStage.getNextStages());
        }
    }

    public void createStage(String name, List<String> selectedOrganizations, List<String> selectedComponents,
                            List<String> selectedEquipment, List<Personnel> selectedPersonnel) {
        Stage stage = new Stage();
        stage.setName(name);
        stage.setOrganizations(new ArrayList<>(selectedOrganizations));
        stage.setComponents(new ArrayList<>(selectedComponents));
        stage.setEquipment(new ArrayList<>(selectedEquipment));
        stage.setPersonnel(new ArrayList<>(selectedPersonnel));

        addStage(stage);
    }

    private void addStage(Stage stage) {
        boolean stageExists = false;
        for (Stage existingStage : listOfStages) {
            if (existingStage.getName().equals(stage.getName())) {
                stageExists = true;
                replaceIfEqual(stage, existingStage);
                break;
            }
        }
        if (!stageExists) {
            listOfStages.add(stage);
        }

        if (stage.getPersonnel() != null) {
            for (Personnel personnel : stage.getPersonnel()) {
                elementsMap.get(PERSONNEL_KEY).add(personnel.getProfession());
            }
        }
        if (stage.getOrganizations() != null) {
            elementsMap.get(ORGANIZATIONS_KEY).addAll(stage.getOrganizations());
        }
        if (stage.getComponents() != null) {
            elementsMap.get(COMPONENTS_KEY).addAll(stage.getComponents());
        }
        if (stage.getEquipment() != null) {
            elementsMap.get(EQUIPMENT_KEY).addAll(stage.getEquipment());
        }
    }
}
