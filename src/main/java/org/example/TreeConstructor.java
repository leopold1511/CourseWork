package org.example;

import controllersandservices.ProductService;
import datamodel.Stage;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TreeConstructor {
    public static void createTree(JPanel mainPanel, ProductService productService){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        Map<String, List<String>> newStringMap = new HashMap<>();

        for (Map.Entry<String, HashSet<String>> entry : productService.getStageStringMap().entrySet()) {
            newStringMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        addStagesToTree(root, productService.getListOfStages());
        addMapToTree(root, newStringMap);

        JTree tree = new JTree(root);
        JScrollPane treeScrollPane = new JScrollPane(tree);

        mainPanel.add(treeScrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private static void addMapToTree(DefaultMutableTreeNode root, Map<String, List<String>> map) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            DefaultMutableTreeNode keyNode = new DefaultMutableTreeNode(entry.getKey());
            root.add(keyNode);
            for (Object value : entry.getValue()) {
                DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(value.toString());
                keyNode.add(valueNode);
            }
        }
    }

    private static void addStagesToTree(DefaultMutableTreeNode root, ArrayList<Stage> stages){
        DefaultMutableTreeNode keyNode = new DefaultMutableTreeNode(ProductService.STAGES_KEY);
        root.add(keyNode);

        for (Stage stage: stages){
            DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(stage.toString());
            DefaultMutableTreeNode personalNode = new DefaultMutableTreeNode(ProductService.PERSONNEL_KEY);
            DefaultMutableTreeNode equipmentNode = new DefaultMutableTreeNode(ProductService.EQUIPMENT_KEY);
            DefaultMutableTreeNode organizationsNode = new DefaultMutableTreeNode(ProductService.ORGANIZATIONS_KEY);
            DefaultMutableTreeNode componentsNode= new DefaultMutableTreeNode(ProductService.COMPONENTS_KEY);
            valueNode.add(personalNode);
            valueNode.add(equipmentNode);
            valueNode.add(organizationsNode);
            valueNode.add(componentsNode);
            keyNode.add(valueNode);
            if(stage.getPersonnel()!=null)stage.getPersonnel().forEach(p ->{
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(p.getProfession());
                node.add(new DefaultMutableTreeNode(p.getManHours()));
                personalNode.add(node);
            } );
            if(stage.getEquipment()!=null)stage.getEquipment().forEach(e -> equipmentNode.add(new DefaultMutableTreeNode(e)));
            if(stage.getOrganizations()!=null)stage.getOrganizations().forEach(o-> organizationsNode.add(new DefaultMutableTreeNode(o)));
            if(stage.getComponents()!=null)stage.getComponents().forEach(c->componentsNode.add(new DefaultMutableTreeNode(c)));
        }
    }
    public static void recreateTree(JPanel mainPanel, ProductService productService){
        SwingUtilities.invokeLater(() -> {
            Component treeScrollPane = null;
            for (Component component : mainPanel.getComponents()) {
                if (component instanceof JScrollPane) {
                    treeScrollPane = component;
                    break;
                }
            }
            if (treeScrollPane != null) {
                mainPanel.remove(treeScrollPane);
            }
            createTree(mainPanel, productService);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }
}
