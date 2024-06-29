package org.example;

import controllersandservices.ProductService;
import datamodel.Personnel;
import datamodel.Product;
import datamodel.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private final ProductService productService;
    private JPanel mainPanel;
    JPanel buttonPanel = new JPanel();
    public static final String SVE_ELECTRONICS = "СВЧ электроника";
    public static final String MICROELECTRONICS = "Микроэлектроника";
    public static final String ELECTROTECHNICS = "Электротехника";
    public static final String OPTICS_AND_PHOTONICS = "Оптика и фотоника";
    public static final String RADIO_PHOTONICS = "Радиофотоника";
    public static final String PASSIVE_EKB = "Пассивная ЭКБ";
    public static final String RADIATION_RESISTANT_EKB = "Радиационностойкая ЭКБ";

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

        JButton importAllButton = new JButton("Import all");
        JButton addElementButton = new JButton("Add Element");
        JButton importProductButton = new JButton("Import Product");
        JButton viewGraphButton = new JButton("View Graph");
        JButton exportDataButton = new JButton("Export data");
        JButton importDataButton = new JButton("Import data");

        importAllButton.addActionListener(e -> importAll());

        addElementButton.addActionListener(e -> addElement());

        importProductButton.addActionListener(e -> importProduct());

        viewGraphButton.addActionListener(e -> viewGraph());

        exportDataButton.addActionListener(e -> exportData());

        importDataButton.addActionListener(e -> importData());

        buttonPanel.add(importAllButton);
        buttonPanel.add(importProductButton);
        buttonPanel.add(importDataButton);
        buttonPanel.add(addElementButton);
        buttonPanel.add(viewGraphButton);
        buttonPanel.add(exportDataButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        TreeConstructor.createTree(mainPanel, productService);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void importAll() {
        File file = new File(System.getProperty("java.class.path"));
        File dir = file.getAbsoluteFile().getParentFile();
        String path = dir.toString();

        File cardsDir = new File(path + "/Cards");
        if (cardsDir.isDirectory()) {
            File[] files = cardsDir.listFiles();
            assert files != null;
            for (File cardFile : files) {
                if (cardFile.isFile() && cardFile.getName().endsWith(".json")) {
                    String filePath = cardFile.getAbsolutePath();
                    try {
                        productService.importProduct(filePath);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Can't import product:" + ex.getMessage());
                    }
                }
            }
        }
        File dataDir = new File(path + "/Data");
        if (dataDir.isDirectory()) {
            File[] files = dataDir.listFiles();
            assert files != null;
            for (File dataFile : files) {
                if (dataFile.isFile() && dataFile.getName().endsWith(".json")) {
                    String filePath = dataFile.getAbsolutePath();
                    try {
                        productService.importData(filePath);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Can't import data:" + ex.getMessage());
                    }
                }
            }
        }
        TreeConstructor.recreateTree(mainPanel, productService);
    }

    private void exportData() {
        File file = new File(System.getProperty("java.class.path"));
        File dir = file.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        JFileChooser fileChooser = new JFileChooser(path);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                productService.exportData(fileChooser.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage());
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(this, "Nothing to export");
            }
        }
    }

    private void importData() {
        File file = new File(System.getProperty("java.class.path"));
        File dir = file.getAbsoluteFile().getParentFile();
        String path = dir.toString();

        JFileChooser fileChooser = new JFileChooser(path);
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

    private void viewGraph() {

        ArrayList<Product> productList = productService.getAllProducts();

        String[] strategicDirections = {SVE_ELECTRONICS, MICROELECTRONICS, ELECTROTECHNICS, OPTICS_AND_PHOTONICS, RADIO_PHOTONICS, PASSIVE_EKB, RADIATION_RESISTANT_EKB};
        String selectedStrategicDirection = (String) JOptionPane.showInputDialog(null, "Выберите направление для фильтрации:", "Фильтрация по направлению", JOptionPane.QUESTION_MESSAGE, null, strategicDirections, strategicDirections[0]);

        List<Product> filteredProducts = productList.stream().filter(product -> product.getStrategicDirection().equals(selectedStrategicDirection)).toList();
        if (filteredProducts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ничего не найдено по выбранному направлению", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }

        String[] productNames = filteredProducts.stream().map(Product::getName).toArray(String[]::new);

        String selectedProductName = (String) JOptionPane.showInputDialog(null, "Выберите продукт для просмотра графика:", "Выбор продукта", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);
        if (selectedProductName != null) {
            Product selectedProduct;
            try {
                selectedProduct = productList.stream().filter(p -> p.getName().equals(selectedProductName)).findFirst().orElse(null);
            }catch (ArrayIndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(this, "Can't filter" + ex.getMessage());
                return;
            }
            if (selectedProduct != null) {
                GraphConstructor graphConstructor = new GraphConstructor();
                graphConstructor.viewGraph(selectedProduct);
            } else {
                JOptionPane.showMessageDialog(null, "Выбранный продукт не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addElement() {
        String[] names = {ProductService.COMPONENTS_KEY, ProductService.PERSONNEL_KEY, ProductService.EQUIPMENT_KEY, ProductService.ORGANIZATIONS_KEY, ProductService.STAGES_KEY
        };

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
                panel.add(new JLabel("Name:"));
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
                            panel.add(new JLabel(selectedPersonnel + " Человеко-часы:"));
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

        int result;
        boolean isValidInput = false;
        do {
            result = JOptionPane.showConfirmDialog(null, scrollPane, "Введите данные", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.CANCEL_OPTION) {
                break;
            }
            if (result == JOptionPane.CLOSED_OPTION) {
                break;
            }

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Введите наименование", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                switch (selectedProductName) {
                    case ProductService.COMPONENTS_KEY:
                    case ProductService.PERSONNEL_KEY:
                    case ProductService.ORGANIZATIONS_KEY:
                    case ProductService.EQUIPMENT_KEY:
                        productService.getStageStringMap().get(selectedProductName).add(name);
                        break;
                    case ProductService.STAGES_KEY: {

                        List<String> selectedEquipment = List.copyOf(equipmentList.getSelectedValuesList());
                        List<String> selectedOrganizations = List.copyOf(organizationsList.getSelectedValuesList());
                        List<String> selectedComponents = List.copyOf(componentsList.getSelectedValuesList());
                        ArrayList<Personnel> selectedPersonnel;

                        selectedPersonnel = new ArrayList<>();

                        Component[] components = panel.getComponents();
                        for (int i = 0; i < components.length; i++) {
                            if (components[i] instanceof JLabel && ((JLabel) components[i]).getText().endsWith("Человеко-часы:")) {
                                String profession = ((JLabel) components[i]).getText().replace("Человеко-часы:", "");
                                JTextField hoursField = (JTextField) components[++i];
                                if (hoursField.getText().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Введите количество человеко-часов для " + profession, "Ошибка", JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                                int manHours = Integer.parseInt(hoursField.getText());
                                selectedPersonnel.add(new Personnel(profession, manHours));
                            }
                        }
                        if (selectedOrganizations.isEmpty() || selectedComponents.isEmpty() || selectedEquipment.isEmpty() || selectedPersonnel.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Выберите все необходимые поля", "Ошибка", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        Stage stage = new Stage();
                        stage.setName(name);
                        stage.setOrganizations(new ArrayList<>(selectedOrganizations));
                        stage.setComponents(new ArrayList<>(selectedComponents));
                        stage.setEquipment(new ArrayList<>(selectedEquipment));
                        stage.setPersonnel(new ArrayList<>(selectedPersonnel));
                        productService.getListOfStages().add(stage);
                        break;
                    }
                    case ProductService.PRODUCTS_KEY: {
                        String strategicDirection = strategicDirField.getText();
                        if (strategicDirection.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Введите стратегическое направление", "Ошибка", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        productService.createProduct(name, strategicDirection);
                        break;
                    }
                }
                isValidInput = true;
            }
        } while (!isValidInput);

        TreeConstructor.recreateTree(mainPanel, productService);
    }


    private void importProduct() {
        File file = new File(System.getProperty("java.class.path"));
        File dir = file.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        JFileChooser fileChooser = new JFileChooser(path);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                productService.importProduct(fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Products imported successfully.");
                TreeConstructor.recreateTree(mainPanel, productService);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error importing products: " + ex.getMessage());
            }
        }
    }

}
