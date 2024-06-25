package org.example;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import controllersandservices.ProductService;
import datamodel.Product;
import datamodel.Stage;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


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
        ArrayList<Product> products= productService.getAllProducts();
        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        // Add vertices and edges
        for (Product product : products) {
            for (Stage stage : product.getStages()) {
                graph.addVertex(stage.getStageId() + ":" + stage.getStageName());

                for (String nextStageId : stage.getNextStages()) {
                    for (Stage nextStage : product.getStages()) {
                        if (nextStage.getStageId().equals(nextStageId)) {
                            graph.addVertex(nextStage.getStageId() + ":" + nextStage.getStageName());
                            graph.addEdge(stage.getStageId() + ":" + stage.getStageName(), nextStage.getStageId() + ":" + nextStage.getStageName());
                            break;
                        }
                    }
                }
            }
        }

        // Visualize the graph
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph) {
            @Override
            public String convertValueToString(Object cell) {
                if (cell instanceof mxCell) {
                    Object value = ((mxCell) cell).getValue();
                    return value != null ? value.toString().split(":")[1] : "";
                }
                return super.convertValueToString(cell);
            }
        };

        // Set vertex style
        Map<String, Object> style = graphAdapter.getStylesheet().getDefaultVertexStyle();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_FILLCOLOR, "#C3D9FF");
        style.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        style.put(mxConstants.STYLE_FONTSIZE, 16);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_SPACING_TOP, 30);
        style.put(mxConstants.STYLE_SPACING_LEFT, 30);
        style.put(mxConstants.STYLE_SPACING_RIGHT, 30);
        style.put(mxConstants.STYLE_SPACING_BOTTOM, 30);

        // Apply a hierarchical layout
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        // Create and display the frame
        JFrame frame = new JFrame();
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        graphComponent.getViewport().setOpaque(true);
        graphComponent.getViewport().setBackground(Color.WHITE);
        graphComponent.zoomTo(1.25, true);
        frame.getContentPane().add(graphComponent);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        Main mainFrame = new Main(productService);
        mainFrame.setVisible(true);
    }
}
