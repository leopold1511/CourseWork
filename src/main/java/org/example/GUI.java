package org.example;

import controllersandservices.ProductService;
import datamodel.Product;
import datamodel.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {
    private final ProductService productService;

    public GUI(ProductService productService) {
        this.productService = productService;
        initialize();
    }

    private void initialize() {
        setTitle("Production Cycle Registrar");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JButton viewProductsButton = new JButton("View Products");
        JButton addProductButton = new JButton("Add Product");
        JButton importProductButton = new JButton("Import Product");
        JButton viewGraphButton = new JButton("View Graph");

        viewProductsButton.addActionListener(e -> viewProducts());

        addProductButton.addActionListener(e -> addProduct());

        importProductButton.addActionListener(e -> importProduct());

        viewGraphButton.addActionListener(e -> {

            ArrayList<Product> productList = productService.getAllProducts();

            String[] productNames = productList.stream()
                    .map(Product::getProductName)
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
                        .filter(p -> p.getProductName().equals(selectedProductName))
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
        buttonPanel.add(viewProductsButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(importProductButton);
        buttonPanel.add(viewGraphButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);


        add(mainPanel, BorderLayout.CENTER);
    }

    private void viewProducts() {
        ArrayList<Product> products = productService.getAllProducts();
        for (Stage stage : products.getFirst().getStages()) {
            System.out.println(stage.getStageName());
        }
    }

    private void addProduct() {
        String[] names= {
                "Компонент",
                "Персонал",
                "Оборудование",
                "Организация",
                "Стадия",
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
