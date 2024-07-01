package org.core;

import org.utils.GraphConstructor;
import org.utils.TreeConstructor;
import org.core.datamodel.Personnel;
import org.core.datamodel.Product;

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

    public GUI( ) {
        this.productService =new ProductService();
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
                productService.exportData(fileChooser.getSelectedFile().getAbsolutePath() + ".json");
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
            if (fileChooser.getSelectedFile().getAbsolutePath().endsWith(".json")) try {
                productService.importData(fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Data imported successfully.");
                TreeConstructor.recreateTree(mainPanel, productService);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error importing data: " + ex.getMessage());
            }
            else JOptionPane.showMessageDialog(this, "File must be json");
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
        if (productNames.length == 0) return;

        String selectedProductName = (String) JOptionPane.showInputDialog(null, "Выберите продукт для просмотра графика:", "Выбор продукта", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);
        if (selectedProductName != null) {
            Product selectedProduct;
            try {
                selectedProduct = productList.stream().filter(p -> p.getName().equals(selectedProductName)).findFirst().orElse(null);
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(this, "Can't filter" + ex.getMessage());
                return;
            }
            if (selectedProduct != null) {
                GraphConstructor graphConstructor=new GraphConstructor();
                graphConstructor.viewGraph(selectedProduct);
            } else {
                JOptionPane.showMessageDialog(null, "Выбранный продукт не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addElement() {
        String[] names = {ProductService.COMPONENTS_KEY, ProductService.PERSONNEL_KEY, ProductService.EQUIPMENT_KEY, ProductService.ORGANIZATIONS_KEY, ProductService.STAGES_KEY};

        String selectedProductName = (String) JOptionPane.showInputDialog(null, "Выберите то что вы хотите добавить:", "Выбор", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);

        if (selectedProductName != null) {
            displayForm(selectedProductName);
        }
    }

    private void displayForm(String selectedProductName) {
        JPanel panel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // padding

        JTextField nameField = new JTextField(20);
        JTextField strategicDirField = new JTextField(20);

        JList<String> organizationsList = new JList<>(productService.getElementsMap().get(ProductService.ORGANIZATIONS_KEY).toArray(new String[0]));
        organizationsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane organizationsScrollPane = new JScrollPane(organizationsList);

        JList<String> componentsList = new JList<>(productService.getElementsMap().get(ProductService.COMPONENTS_KEY).toArray(new String[0]));
        componentsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane componentsScrollPane = new JScrollPane(componentsList);

        JList<String> equipmentList = new JList<>(productService.getElementsMap().get(ProductService.EQUIPMENT_KEY).toArray(new String[0]));
        equipmentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane equipmentScrollPane = new JScrollPane(equipmentList);

        JList<String> personnelList = new JList<>(productService.getElementsMap().get(ProductService.PERSONNEL_KEY).toArray(new String[0]));
        personnelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane personnelScrollPane = new JScrollPane(personnelList);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        if (selectedProductName.equals(ProductService.STAGES_KEY)) {
            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("Organizations:"), gbc);
            gbc.gridx = 1;
            panel.add(organizationsScrollPane, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("Components:"), gbc);
            gbc.gridx = 1;
            panel.add(componentsScrollPane, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("Equipment:"), gbc);
            gbc.gridx = 1;
            panel.add(equipmentScrollPane, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("Personnel:"), gbc);
            gbc.gridx = 1;
            panel.add(personnelScrollPane, gbc);

            personnelList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    for (int i = panel.getComponentCount() - 1; i >= 0; i--) {
                        if (panel.getComponent(i) instanceof JTextField && ((JLabel) panel.getComponent(i - 1)).getText().endsWith("Человеко-часы:")) {
                            panel.remove(i);
                            panel.remove(i - 1);
                        }
                    }

                    for (String selectedPersonnel : personnelList.getSelectedValuesList()) {
                        gbc.gridx = 0;
                        gbc.gridy++;
                        panel.add(new JLabel(selectedPersonnel + " Человеко-часы:"), gbc);
                        gbc.gridx = 1;
                        JTextField hoursField = new JTextField(20);
                        panel.add(hoursField, gbc);
                    }

                    panel.revalidate();
                    panel.repaint();
                }
            });
        } else if (selectedProductName.equals(ProductService.PRODUCTS_KEY)) {
            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("Strategic Direction:"), gbc);
            gbc.gridx = 1;
            panel.add(strategicDirField, gbc);
        }

        int result;
        boolean isValidInput = false;
        do {
            result = JOptionPane.showConfirmDialog(null, scrollPane, "Enter Data", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                break;
            }

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                switch (selectedProductName) {
                    case ProductService.COMPONENTS_KEY, ProductService.PERSONNEL_KEY, ProductService.ORGANIZATIONS_KEY, ProductService.EQUIPMENT_KEY -> productService.getElementsMap().get(selectedProductName).add(name);
                    case ProductService.STAGES_KEY -> {
                        List<String> selectedEquipment = List.copyOf(equipmentList.getSelectedValuesList());
                        List<String> selectedOrganizations = List.copyOf(organizationsList.getSelectedValuesList());
                        List<String> selectedComponents = List.copyOf(componentsList.getSelectedValuesList());
                        ArrayList<Personnel> selectedPersonnel = new ArrayList<>();

                        for (int i = 0; i < panel.getComponentCount(); i++) {
                            if (panel.getComponent(i) instanceof JLabel && ((JLabel) panel.getComponent(i)).getText().endsWith("Человеко-часы:")) {
                                String profession = ((JLabel) panel.getComponent(i)).getText().replace(" Человеко-часы:", "");
                                JTextField hoursField = (JTextField) panel.getComponent(i + 1);
                                if (hoursField.getText().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Please enter the man-hours for " + profession, "Error", JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                                int manHours = Integer.parseInt(hoursField.getText());
                                selectedPersonnel.add(new Personnel(profession, manHours));
                            }
                        }

                        if (selectedOrganizations.isEmpty() || selectedComponents.isEmpty() || selectedEquipment.isEmpty() || selectedPersonnel.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please select all necessary fields", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        productService.createStage(name,selectedOrganizations,selectedComponents,selectedEquipment,selectedPersonnel);
                    }
                    case ProductService.PRODUCTS_KEY -> {
                        String strategicDirection = strategicDirField.getText();
                        if (strategicDirection.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter the strategic direction", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        productService.createProduct(name, strategicDirection);
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
