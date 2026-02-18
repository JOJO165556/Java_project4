package com.restaurant.utils;

import com.restaurant.view.MainView;
import com.restaurant.utils.DesignSystem;

import javax.swing.*;
import java.awt.Component;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;

// Utilitaires pour l'affichage des alertes et messages
public class AlertUtils {
    
    // Types d'alertes
    public static final int INFORMATION = JOptionPane.INFORMATION_MESSAGE;
    public static final int WARNING = JOptionPane.WARNING_MESSAGE;
    public static final int ERROR = JOptionPane.ERROR_MESSAGE;
    public static final int QUESTION = JOptionPane.QUESTION_MESSAGE;
    public static final int PLAIN = JOptionPane.PLAIN_MESSAGE;
    
    // Affiche un message d'information
    public static void afficherInformation(Component parent, String message) {
        afficherMessage(parent, message, "Information", INFORMATION);
    }
    
    // Affiche un message d'information avec titre personnalisé
    public static void afficherInformation(Component parent, String message, String titre) {
        afficherMessage(parent, message, titre, INFORMATION);
    }
    
    // Affiche un message d'avertissement
    public static void afficherAvertissement(Component parent, String message) {
        afficherMessage(parent, message, "Avertissement", WARNING);
    }
    
    // Affiche un message d'avertissement avec titre personnalisé
    public static void afficherAvertissement(Component parent, String message, String titre) {
        afficherMessage(parent, message, titre, WARNING);
    }
    
    // Affiche un message d'erreur
    public static void afficherErreur(Component parent, String message) {
        afficherMessage(parent, message, "Erreur", ERROR);
    }
    
    // Affiche un message d'erreur avec titre personnalisé
    public static void afficherErreur(Component parent, String message, String titre) {
        afficherMessage(parent, message, titre, ERROR);
    }
    
    // Affiche un message de confirmation
    public static boolean afficherConfirmation(Component parent, String message) {
        return afficherConfirmation(parent, message, "Confirmation");
    }
    
    // Affiche un message de confirmation avec titre personnalisé
    public static boolean afficherConfirmation(Component parent, String message, String titre) {
        int reponse = JOptionPane.showConfirmDialog(
            parent,
            message,
            titre,
            JOptionPane.YES_NO_OPTION,
            QUESTION
        );
        return reponse == JOptionPane.YES_OPTION;
    }
    
    // Affiche un message de confirmation avec options OUI/NON/ANNULER
    public static int afficherConfirmationAnnuler(Component parent, String message, String titre) {
        return JOptionPane.showConfirmDialog(
            parent,
            message,
            titre,
            JOptionPane.YES_NO_CANCEL_OPTION,
            QUESTION
        );
    }
    
    // Affiche une boîte de saisie
    public static String afficherSaisie(Component parent, String message) {
        return afficherSaisie(parent, message, "Saisie", null);
    }
    
    // Affiche une boîte de saisie avec titre et valeur par défaut
    public static String afficherSaisie(Component parent, String message, String titre, String valeurParDefaut) {
        return JOptionPane.showInputDialog(
            parent,
            message,
            titre,
            PLAIN,
            null,
            null,
            valeurParDefaut
        ) != null ? JOptionPane.showInputDialog(parent, message, titre, PLAIN) : null;
    }
    
    // Affiche une boîte de saisie pour un entier
    public static Integer afficherSaisieEntier(Component parent, String message) {
        return afficherSaisieEntier(parent, message, "Saisie", null);
    }
    
    // Affiche une boîte de saisie pour un entier avec titre et valeur par défaut
    public static Integer afficherSaisieEntier(Component parent, String message, String titre, Integer valeurParDefaut) {
        String saisie = JOptionPane.showInputDialog(
            parent,
            message,
            titre,
            PLAIN,
            null,
            null,
            valeurParDefaut != null ? valeurParDefaut.toString() : ""
        ) != null ? JOptionPane.showInputDialog(parent, message, titre, PLAIN) : null;
        
        if (saisie != null && !saisie.trim().isEmpty()) {
            try {
                return Integer.parseInt(saisie.trim());
            } catch (NumberFormatException e) {
                afficherErreur(parent, "Veuillez saisir un nombre entier valide");
                return null;
            }
        }
        
        return null;
    }
    
    // Affiche une boîte de saisie pour un décimal
    public static Double afficherSaisieDecimal(Component parent, String message) {
        return afficherSaisieDecimal(parent, message, "Saisie", null);
    }
    
    // Affiche une boîte de saisie pour un décimal avec titre et valeur par défaut
    public static Double afficherSaisieDecimal(Component parent, String message, String titre, Double valeurParDefaut) {
        String saisie = JOptionPane.showInputDialog(
            parent,
            message,
            titre,
            PLAIN,
            null,
            null,
            valeurParDefaut != null ? valeurParDefaut.toString() : ""
        ) != null ? JOptionPane.showInputDialog(parent, message, titre, PLAIN) : null;
        
        if (saisie != null && !saisie.trim().isEmpty()) {
            try {
                return Double.parseDouble(saisie.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                afficherErreur(parent, "Veuillez saisir un nombre décimal valide");
                return null;
            }
        }
        
        return null;
    }
    
    // Affiche un message temporaire qui disparaît après quelques secondes
    public static void afficherMessageTemporaire(Component parent, String message, int dureeSecondes) {
        afficherMessageTemporaire(parent, message, "Information", INFORMATION, dureeSecondes);
    }
    
    // Affiche un message temporaire avec titre et type personnalisés
    public static void afficherMessageTemporaire(Component parent, String message, String titre, int type, int dureeSecondes) {
        // Créer une boîte de dialogue personnalisée
        JDialog dialog = new JDialog();
        dialog.setTitle(titre);
        dialog.setModal(false);
        dialog.setAlwaysOnTop(true);
        
        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        dialog.add(label);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        
        // Timer pour fermer automatiquement
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();
            }
        }, dureeSecondes * 1000);
        
        dialog.setVisible(true);
    }
    
    // Affiche un message dans la barre de statut (si disponible)
    public static void afficherMessageStatut(Component parent, String message) {
        // Chercher une barre de statut dans la hiérarchie des composants
        Component parentComponent = parent;
        while (parentComponent != null && !(parentComponent instanceof JFrame)) {
            parentComponent = parentComponent.getParent();
        }
        
        if (parentComponent instanceof MainView) {
            ((MainView) parentComponent).setStatus(message, DesignSystem.SUCCESS);
        } else {
            System.out.println("STATUT: " + message);
        }
    }
    
    // Affiche un message d'erreur avec les détails de l'exception
    public static void afficherErreurException(Component parent, String message, Exception exception) {
        String messageComplet = message + "\n\nDétails de l'erreur:\n" + exception.getMessage();
        
        // Option pour voir la pile complète
        int reponse = JOptionPane.showConfirmDialog(
            parent,
            messageComplet,
            "Erreur détaillée",
            JOptionPane.YES_NO_OPTION,
            ERROR
        );
        
        if (reponse == JOptionPane.YES_OPTION) {
            // Afficher la pile d'exception dans une fenêtre de texte
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setText(getStackTrace(exception));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
            
            JOptionPane.showMessageDialog(
                parent,
                scrollPane,
                "Pile d'exception",
                PLAIN
            );
        }
    }
    
    // Affiche une barre de progression
    public static JProgressBar afficherProgression(Component parent, String message, int min, int max) {
        JProgressBar progressBar = new JProgressBar(min, max);
        progressBar.setStringPainted(true);
        progressBar.setString(message);
        
        JOptionPane.showMessageDialog(
            parent,
            progressBar,
            "Progression",
            PLAIN
        );
        
        return progressBar;
    }
    
    // Méthode générique pour afficher un message
    private static void afficherMessage(Component parent, String message, String titre, int type) {
        JOptionPane.showMessageDialog(parent, message, titre, type);
    }
    
    // Convertit une pile d'exception en chaîne de caractères
    private static String getStackTrace(Exception exception) {
        StringBuilder sb = new StringBuilder();
        sb.append(exception.toString()).append("\n");
        
        for (StackTraceElement element : exception.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        
        return sb.toString();
    }
    
    // Affiche un message de succès avec icône personnalisée
    public static void afficherSucces(Component parent, String message) {
        afficherMessage(parent, message, "Succès", INFORMATION);
    }
    
    // Affiche un message d'avertissement pour les validations
    public static void afficherValidation(Component parent, String champ, String erreur) {
        afficherAvertissement(parent, "Erreur de validation pour le champ '" + champ + "': " + erreur);
    }
    
    // Affiche un message de confirmation pour la suppression
    public static boolean confirmerSuppression(Component parent, String element) {
        return afficherConfirmation(parent, 
            "Voulez-vous vraiment supprimer " + element + " ?", 
            "Confirmation de suppression");
    }
    
    // Affiche un message de confirmation pour la modification
    public static boolean confirmerModification(Component parent, String element) {
        return afficherConfirmation(parent, 
            "Voulez-vous vraiment modifier " + element + " ?", 
            "Confirmation de modification");
    }
}
