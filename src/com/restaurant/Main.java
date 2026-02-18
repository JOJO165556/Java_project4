package com.restaurant;

import com.restaurant.controller.LoginController;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        setupLookAndFeel();
        SwingUtilities.invokeLater(() -> new LoginController().afficherLogin());
    }

    private static void setupLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Style Nimbus non disponible.");
        }
    }
}