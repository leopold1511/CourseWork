package org.example;

import controllersandservices.ProductService;
import datamodel.Product;
import datamodel.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


public class Main extends JFrame {
    private final ProductService productService;

    public Main(ProductService productService) {
        this.productService = productService;
        initialize();
    }

    private void initialize() {
        setTitle("Production Cycle Registrar");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create buttons
        JButton viewProductsButton = new JButton("View Products");
        JButton addProductButton = new JButton("Add Product");
        JButton importProductsButton = new JButton("Import Products");
        JButton viewGraphButton = new JButton("View Graph");

        viewProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewProducts();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        importProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importProducts();
            }
        });

        viewGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewGraph();
            }
        });

        // Add buttons to panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewProductsButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(importProductsButton);
        buttonPanel.add(viewGraphButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);


        add(mainPanel, BorderLayout.CENTER);
    }

    private void viewProducts() {
        ArrayList<Product> products= productService.getAllProducts();
        for (Stage stage: products.getFirst().getStages()) {
            System.out.println(stage.getStageName());
        }
    }

    private void addProduct() {
        // Implement add product logic
    }

    private void importProducts() {
        JFileChooser fileChooser = new JFileChooser("D:\\Java\\CourseWork");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                ArrayList<Product> importedProducts = JSONUtil.importFromJson(fileChooser.getSelectedFile().getAbsolutePath());
                productService.importProducts(importedProducts);


                JOptionPane.showMessageDialog(this, "Products imported successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error importing products: " + ex.getMessage());
            }
        }
    }

    private void viewGraph() {
        GraphModel graphModel = new GraphModel();

        // Populate the graph with data (example data for demonstration)
        graphModel.addVertex("Company A");
        graphModel.addVertex("Company B");
        graphModel.addVertex("Company C");
        graphModel.addVertex("Company D");
        graphModel.addVertex("End Device");

        graphModel.addEdge("Company A", "Company B");
        graphModel.addEdge("Company B", "Company C");
        graphModel.addEdge("Company C", "Company D");
        graphModel.addEdge("Company D", "End Device");

        GraphPanel graphPanel = new GraphPanel(graphModel.getGraph());
        JFrame graphFrame = new JFrame("Technology Map Graph");
        graphFrame.setSize(800, 600);
        graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        graphFrame.add(graphPanel);
        graphFrame.setVisible(true);
    }

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        Main mainFrame = new Main(productService);
        mainFrame.setVisible(true);
    }
}
