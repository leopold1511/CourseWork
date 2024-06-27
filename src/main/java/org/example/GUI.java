package org.example;

import controllersandservices.ProductService;
import datamodel.Personnel;
import datamodel.Product;
import datamodel.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {
    private final ProductService productService;
    private JPanel mainPanel;
    JPanel buttonPanel = new JPanel();

    public GUI(ProductService productService) {
        this.productService = productService;
        initialize();
    }

    private void initialize() {
        setTitle("Production Cycle Registrar");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JButton exportButton = new JButton("Export Product");
        JButton addProductButton = new JButton("Add Product");
        JButton importProductButton = new JButton("Import Product");
        JButton viewGraphButton = new JButton("View Graph");
        JButton exportDataButton = new JButton("Export data");
        JButton importDataButton = new JButton("Import data");

        exportButton.addActionListener(e -> export());

        addProductButton.addActionListener(e -> addProduct());

        importProductButton.addActionListener(e -> importProduct());

        viewGraphButton.addActionListener(e -> viewGraph());

        exportDataButton.addActionListener(e-> exportData());

        importDataButton.addActionListener(e-> importData());

        buttonPanel.add(importProductButton);
        buttonPanel.add(importDataButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(viewGraphButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(exportDataButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        TreeConstructor.createTree(mainPanel, productService);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void exportData() {
        JFileChooser fileChooser = new JFileChooser("D:\\Java\\CourseWork");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                productService.exportData(fileChooser.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage());
            }
        }
    }

    private void importData(){
        JFileChooser fileChooser = new JFileChooser("D:\\Java\\CourseWork");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                productService.importData(fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Data imported successfully.");
                TreeConstructor.recreateTree(mainPanel, productService);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error importing data: " + ex.getMessage());
            }
        }
    }

    private void export() {
        ArrayList<Product> productList = productService.getAllProducts();

        String[] productNames = productList.stream().map(Product::getName).toArray(String[]::new);

        String selectedProductName = (String) JOptionPane.showInputDialog(null, "Выберите продукт для экспорта:", "Выбор продукта", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

        if (selectedProductName != null) {
            Product selectedProduct = productList.stream().filter(p -> p.getName().equals(selectedProductName)).findFirst().orElse(null);

            if (selectedProduct != null) {
                JFileChooser fileChooser = new JFileChooser("D:\\Java\\CourseWork");
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        JSONUtil.exportToJson(selectedProduct, fileChooser.getSelectedFile().getAbsolutePath());
                        JOptionPane.showMessageDialog(this, "Product exported successfully.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error importing product: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Выбранный продукт не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }


    }

    private void viewGraph() {

        ArrayList<Product> productList = productService.getAllProducts();

        String[] productNames = productList.stream().map(Product::getName).toArray(String[]::new);

        String selectedProductName = (String) JOptionPane.showInputDialog(null, "Выберите продукт для просмотра графика:", "Выбор продукта", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

        if (selectedProductName != null) {
            Product selectedProduct = productList.stream().filter(p -> p.getName().equals(selectedProductName)).findFirst().orElse(null);

            if (selectedProduct != null) {
                GraphConstructor graphConstructor = new GraphConstructor();
                graphConstructor.viewGraph(selectedProduct);
            } else {
                JOptionPane.showMessageDialog(null, "Выбранный продукт не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addProduct() {
        String[] names = {ProductService.COMPONENTS_KEY, ProductService.PERSONNEL_KEY, ProductService.EQUIPMENT_KEY, ProductService.ORGANIZATIONS_KEY, ProductService.STAGES_KEY, ProductService.PRODUCTS_KEY};

        String selectedProductName = (String) JOptionPane.showInputDialog(null, "Выберите то что вы хотите добавить:", "Выбор", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);

        if (selectedProductName != null) {
            displayForm(selectedProductName);
        }
    }

    private void displayForm(String selectedProductName) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 2, 2));
        JScrollPane scrollPane = new JScrollPane(panel);
        JTextField nameField = new JTextField();
        JTextField strategicDirField = new JTextField();

        JList<String> organizationsList = new JList<>(productService.getStageStringMap().get(ProductService.ORGANIZATIONS_KEY).toArray(new String[0]));
        organizationsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane organizationsScrollPane = new JScrollPane(organizationsList);

        JList<String> componentsList = new JList<>(productService.getStageStringMap().get(ProductService.COMPONENTS_KEY).toArray(new String[0]));
        componentsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane componentsScrollPane = new JScrollPane(componentsList);

        JList<String> equipmentList = new JList<>(productService.getStageStringMap().get(ProductService.EQUIPMENT_KEY).toArray(new String[0]));
        equipmentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane equipmentScrollPane = new JScrollPane(equipmentList);

        JList<String> personnelList = new JList<>(productService.getStageStringMap().get(ProductService.PERSONNEL_KEY).toArray(new String[0]));
        personnelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane personnelScrollPane = new JScrollPane(personnelList);

        switch (selectedProductName) {
            case ProductService.STAGES_KEY -> {

                panel.add(new JLabel("Organizations:"));
                panel.add(nameField);
                panel.add(new JLabel("Organizations:"));
                panel.add(organizationsScrollPane);
                panel.add(new JLabel("Components:"));
                panel.add(componentsScrollPane);
                panel.add(new JLabel("Equipment:"));
                panel.add(equipmentScrollPane);
                panel.add(new JLabel("Personnel:"));
                panel.add(personnelScrollPane);

                personnelList.addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        panel.removeAll();
                        panel.add((new Label(("Name"))));
                        panel.add(nameField);
                        panel.add(new JLabel("Organizations:"));
                        panel.add(organizationsScrollPane);
                        panel.add(new JLabel("Components:"));
                        panel.add(componentsScrollPane);
                        panel.add(new JLabel("Equipment:"));
                        panel.add(equipmentScrollPane);
                        panel.add(new JLabel("Personnel:"));
                        panel.add(personnelScrollPane);

                        for (String selectedPersonnel : personnelList.getSelectedValuesList()) {
                            panel.add(new JLabel(selectedPersonnel + " manHours:"));
                            JTextField hoursField = new JTextField();
                            hoursField.setSize(250, 10);
                            panel.add(hoursField);
                        }

                        panel.revalidate();
                        panel.repaint();
                    }
                });
            }
            default -> {
                panel.removeAll();
                panel.add(new JLabel("Введите наименование:"));
                panel.add(nameField);
            }
            case ProductService.PRODUCTS_KEY -> {
                panel.removeAll();
                panel.add(new JLabel("Введите название:"));
                panel.add(nameField);
                panel.add(new JLabel("Введите стратегическое направление:"));
                panel.add(strategicDirField);
            }
        }

        int result = JOptionPane.showConfirmDialog(null, scrollPane, "Введите данные", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            switch (selectedProductName) {
                case ProductService.COMPONENTS_KEY ->
                        productService.getStageStringMap().get(ProductService.COMPONENTS_KEY).add(name);
                case ProductService.PERSONNEL_KEY ->
                        productService.getStageStringMap().get(ProductService.PERSONNEL_KEY).add(name);
                case ProductService.ORGANIZATIONS_KEY ->
                        productService.getStageStringMap().get(ProductService.ORGANIZATIONS_KEY).add(name);
                case ProductService.EQUIPMENT_KEY ->
                        productService.getStageStringMap().get(ProductService.EQUIPMENT_KEY).add(name);
                case ProductService.STAGES_KEY -> {
                    ArrayList<String> selectedOrganizations = (ArrayList<String>) organizationsList.getSelectedValuesList();
                    ArrayList<String> selectedComponents = (ArrayList<String>) componentsList.getSelectedValuesList();
                    ArrayList<String> selectedEquipment = (ArrayList<String>) equipmentList.getSelectedValuesList();
                    ArrayList<Personnel> selectedPersonnel = new ArrayList<>();

                    Component[] components = panel.getComponents();
                    for (int i = 0; i < components.length; i++) {
                        if (components[i] instanceof JLabel && ((JLabel) components[i]).getText().endsWith(" manHours:")) {
                            String profession = ((JLabel) components[i]).getText().replace(" manHours:", "");
                            JTextField hoursField = (JTextField) components[++i];
                            int manHours = Integer.parseInt(hoursField.getText());
                            selectedPersonnel.add(new Personnel(profession, manHours));
                        }
                    }

                    Stage stage = new Stage();
                    stage.setName(name);
                    stage.setOrganizations(selectedOrganizations);
                    stage.setComponents(selectedComponents);
                    stage.setEquipment(selectedEquipment);
                    stage.setPersonnel(selectedPersonnel);
                    productService.getListOfStages().add(stage);
                }
                case ProductService.PRODUCTS_KEY -> {
                    String strategicDirection = strategicDirField.getText();
                    productService.createProduct(name, strategicDirection);
                }
            }
        }
        TreeConstructor.recreateTree(mainPanel, productService);
    }


    private void importProduct() {
        JFileChooser fileChooser = new JFileChooser("D:\\Java\\CourseWork");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Product importedProduct = JSONUtil.importFromJson(fileChooser.getSelectedFile().getAbsolutePath());
                productService.addProduct(importedProduct);
                JOptionPane.showMessageDialog(this, "Products imported successfully.");
                TreeConstructor.recreateTree(mainPanel, productService);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error importing products: " + ex.getMessage());
            }
        }
    }

}
