package com.restaurant.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

// Design system centralisé
public class DesignSystem {
    
    // Couleurs
    public static final Color PRIMARY = new Color(37, 99, 235);     // Blue 600
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Blue 700
    public static final Color SECONDARY = new Color(71, 85, 105);   // Slate 600
    public static final Color BACKGROUND = new Color(248, 250, 252); // Slate 50
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT = new Color(15, 23, 42);         // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139); // Slate 500
    public static final Color SUCCESS = new Color(22, 163, 74);     // Green 600
    public static final Color DANGER = new Color(220, 38, 38);       // Red 600
    
    // Polices
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 13);
    
    // Méthode pour appliquer le design à un bouton
    public static void styleButton(JButton button) {
        styleButton(button, PRIMARY);
    }
    
    public static void styleButton(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bordure arrondie
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1, true),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
    }
    
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1), // Slate 300
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
    
    public static void styleLabel(JLabel label, boolean isTitle) {
        if (isTitle) {
            label.setFont(FONT_TITLE);
            label.setForeground(TEXT);
        } else {
            label.setFont(FONT_BODY);
            label.setForeground(SECONDARY);
        }
    }
}
