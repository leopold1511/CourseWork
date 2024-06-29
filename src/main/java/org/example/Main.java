package org.example;

import controllersandservices.ProductService;
import javax.swing.*;



public class Main extends JFrame {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        GUI gui = new GUI(productService);
        gui.setVisible(true);
    }
}
