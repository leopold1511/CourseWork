package org.core;

import javax.swing.*;



public class Main extends JFrame {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        GUI gui = new GUI(productService);
        gui.setVisible(true);
    }
}
