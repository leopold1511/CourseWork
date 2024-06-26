package org.example;

import controllersandservices.ProductService;
import datamodel.Data;
import datamodel.Product;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {
    private final ProductService productService;
    private  JPanel mainPanel;

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

        JButton refreshTreeButton = new JButton("Refresh Tree");
        JButton addProductButton = new JButton("Add Product");
        JButton importProductButton = new JButton("Import Product");
        JButton viewGraphButton = new JButton("View Graph");

        refreshTreeButton.addActionListener(e -> refreshTree());

        addProductButton.addActionListener(e -> addProduct());

        importProductButton.addActionListener(e -> importProduct());

        viewGraphButton.addActionListener(e -> {

            ArrayList<Product> productList = productService.getAllProducts();

            String[] productNames = productList.stream()
                    .map(Product::getName)
                    .toArray(String[]::new);

            String selectedProductName = (String) JOptionPane.showInputDialog(
                    null,
                    "Выберите продукт для просмотра графика:",
                    "Выбор продукта",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    productNames,
                    productNames[0]);

            if (selectedProductName != null) {
                Product selectedProduct = productList.stream()
                        .filter(p -> p.getName().equals(selectedProductName))
                        .findFirst()
                        .orElse(null);

                if (selectedProduct != null) {
                    GraphConstructor graphConstructor = new GraphConstructor();
                    graphConstructor.viewGraph(selectedProduct);
                } else {
                    JOptionPane.showMessageDialog(null, "Выбранный продукт не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshTreeButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(importProductButton);
        buttonPanel.add(viewGraphButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);
    }
    private void createTree(JPanel mainPanel){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        Map<String, List<?>> newDataMap = new HashMap<>();
        for (Map.Entry<String, ArrayList<Data>> entry : productService.getStageDataMap().entrySet()) {
            newDataMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        Map<String, List<?>> newStringMap = new HashMap<>();
        for (Map.Entry<String, HashSet<String>> entry : productService.getStageStringMap().entrySet()) {
            newStringMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        addMapToTree(root, newDataMap);
        addMapToTree(root, newStringMap);

        JTree tree = new JTree(root);
        JScrollPane treeScrollPane = new JScrollPane(tree);

        mainPanel.add(treeScrollPane, BorderLayout.CENTER);
    }
    private void addMapToTree(DefaultMutableTreeNode root, Map<String, List<?>> map) {
        for (Map.Entry<String, List<?>> entry : map.entrySet()) {
            DefaultMutableTreeNode keyNode = new DefaultMutableTreeNode(entry.getKey());
            root.add(keyNode);
            for (Object value : entry.getValue()) {
                DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(value.toString());
                keyNode.add(valueNode);
            }
        }
    }

    private void refreshTree() {
        createTree(mainPanel);
        mainPanel.revalidate();
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addProduct() {
        String[] names = {
                ProductService.COMPONENTS_KEY,
                ProductService.PERSONNEL_KEY,
                ProductService.EQUIPMENT_KEY,
                ProductService.ORGANIZATIONS_KEY,
                ProductService.STAGES_KEY,
        };

        String selectedProductName = (String) JOptionPane.showInputDialog(
                null,
                "Выберите то что вы хотите добавить:",
                "Выбор",
                JOptionPane.QUESTION_MESSAGE,
                null,
                names,
                names[0]);
    }

    private void importProduct() {
        JFileChooser fileChooser = new JFileChooser("D:\\Java\\CourseWork");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Product importedProduct = JSONUtil.importFromJson(fileChooser.getSelectedFile().getAbsolutePath());
                productService.addProduct(importedProduct);
                JOptionPane.showMessageDialog(this, "Products imported successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error importing products: " + ex.getMessage());
            }
        }
    }
}
