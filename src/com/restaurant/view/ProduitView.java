package com.restaurant.view;

import com.restaurant.controller.ProduitController;
import com.restaurant.model.Categorie;
import com.restaurant.model.Produit;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProduitView extends JPanel {

    private ProduitController controller;

    // Composants - onglet Catégories
    private JTextField txtLibelleCategorie;
    private JButton btnAjouterCategorie, btnModifierCategorie, btnSupprimerCategorie;
    private JTable tableCategoriesSimple;
    private DefaultTableModel modeleCategoriesSimple;

    // Composants - onglet Produits
    private JTextField txtNomProduit, txtPrix, txtStock, txtSeuil, txtRecherche;
    private JComboBox<Categorie> cmbCategorie;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRechercher, btnAfficherTous, btnProduitsAlerte;
    private JTable tableProduits;
    private DefaultTableModel modeleProduits;

    private Produit produitSelectionne = null;
    private Categorie categorieSelectionnee = null;

    public ProduitView() {
        controller = new ProduitController();
        initComponents();
        chargerCategories();
        chargerProduits();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Catégories", creerPanelCategories());
        tabbedPane.addTab("Produits", creerPanelProduits());
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Onglet Categories

    private JPanel creerPanelCategories() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Gestion des Catégories"));
        panelForm.add(new JLabel("Libellé :"));
        txtLibelleCategorie = new JTextField(20);
        panelForm.add(txtLibelleCategorie);

        btnAjouterCategorie = new JButton("Ajouter");
        btnAjouterCategorie.addActionListener(e -> ajouterCategorie());
        panelForm.add(btnAjouterCategorie);

        btnModifierCategorie = new JButton("Modifier");
        btnModifierCategorie.addActionListener(e -> modifierCategorie());
        panelForm.add(btnModifierCategorie);

        btnSupprimerCategorie = new JButton("Supprimer");
        btnSupprimerCategorie.addActionListener(e -> supprimerCategorie());
        panelForm.add(btnSupprimerCategorie);

        panel.add(panelForm, BorderLayout.NORTH);

        String[] colonnesCat = {"ID", "Libellé"};
        modeleCategoriesSimple = new DefaultTableModel(colonnesCat, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCategoriesSimple = new JTable(modeleCategoriesSimple);
        tableCategoriesSimple.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCategoriesSimple.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectionnerCategorie();
        });
        panel.add(new JScrollPane(tableCategoriesSimple), BorderLayout.CENTER);

        return panel;
    }

    // Onglet Produits

    private JPanel creerPanelProduits() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulaire de saisie
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Informations du Produit"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; txtNomProduit = new JTextField(20); panelForm.add(txtNomProduit, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Catégorie :"), gbc);
        gbc.gridx = 1; cmbCategorie = new JComboBox<>(); panelForm.add(cmbCategorie, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Prix (FCFA) :"), gbc);
        gbc.gridx = 1; txtPrix = new JTextField(20); panelForm.add(txtPrix, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Stock actuel :"), gbc);
        gbc.gridx = 1; txtStock = new JTextField(20); panelForm.add(txtStock, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelForm.add(new JLabel("Seuil alerte :"), gbc);
        gbc.gridx = 1; txtSeuil = new JTextField(20); panelForm.add(txtSeuil, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAjouter = new JButton("Ajouter"); btnAjouter.addActionListener(e -> ajouterProduit()); panelBoutons.add(btnAjouter);
        btnModifier = new JButton("Modifier"); btnModifier.addActionListener(e -> modifierProduit()); panelBoutons.add(btnModifier);
        btnSupprimer = new JButton("Supprimer"); btnSupprimer.addActionListener(e -> supprimerProduit()); panelBoutons.add(btnSupprimer);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panelForm.add(panelBoutons, gbc);
        panel.add(panelForm, BorderLayout.NORTH);

        // Barre de recherche
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelRecherche.add(new JLabel("Rechercher :"));
        txtRecherche = new JTextField(25); panelRecherche.add(txtRecherche);
        btnRechercher = new JButton("Rechercher"); btnRechercher.addActionListener(e -> rechercherProduits()); panelRecherche.add(btnRechercher);
        btnAfficherTous = new JButton("Afficher Tous"); btnAfficherTous.addActionListener(e -> chargerProduits()); panelRecherche.add(btnAfficherTous);
        btnProduitsAlerte = new JButton("Produits en Alerte");
        btnProduitsAlerte.setForeground(Color.RED);
        btnProduitsAlerte.addActionListener(e -> afficherProduitsAlerte());
        panelRecherche.add(btnProduitsAlerte);
        panel.add(panelRecherche, BorderLayout.CENTER);

        // Tableau des produits
        String[] colonnes = {"ID", "Nom", "Catégorie", "Prix (FCFA)", "Stock", "Seuil", "État"};
        modeleProduits = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableProduits = new JTable(modeleProduits);
        tableProduits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProduits.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectionnerProduit();
        });
        panel.add(new JScrollPane(tableProduits), BorderLayout.SOUTH);

        return panel;
    }

    // Logique Categories

    private void chargerCategories() {
        cmbCategorie.removeAllItems();
        modeleCategoriesSimple.setRowCount(0);
        for (Categorie cat : controller.getAllCategories()) {
            cmbCategorie.addItem(cat);
            modeleCategoriesSimple.addRow(new Object[]{cat.getIdCat(), cat.getLibelleCat()});
        }
    }

    private void ajouterCategorie() {
        if (controller.ajouterCategorie(txtLibelleCategorie.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Catégorie ajoutée avec succès !");
            txtLibelleCategorie.setText("");
            chargerCategories();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la catégorie", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierCategorie() {
        if (categorieSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une catégorie à modifier", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (controller.modifierCategorie(categorieSelectionnee.getIdCat(), txtLibelleCategorie.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Catégorie modifiée avec succès !");
            txtLibelleCategorie.setText("");
            categorieSelectionnee = null;
            chargerCategories();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerCategorie() {
        if (categorieSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une catégorie à supprimer", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette catégorie ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.supprimerCategorie(categorieSelectionnee.getIdCat())) {
                JOptionPane.showMessageDialog(this, "Catégorie supprimée avec succès !");
                txtLibelleCategorie.setText("");
                categorieSelectionnee = null;
                chargerCategories();
            } else {
                JOptionPane.showMessageDialog(this, "Impossible de supprimer cette catégorie.\nElle est peut-être utilisée par des produits.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void selectionnerCategorie() {
        int row = tableCategoriesSimple.getSelectedRow();
        if (row >= 0) {
            categorieSelectionnee = controller.getCategorieById((int) modeleCategoriesSimple.getValueAt(row, 0));
            if (categorieSelectionnee != null) {
                txtLibelleCategorie.setText(categorieSelectionnee.getLibelleCat());
                txtLibelleCategorie.requestFocus();
            }
        }
    }

    // Logique Produits

    private void chargerProduits() {
        modeleProduits.setRowCount(0);
        for (Produit p : controller.getAllProduits()) ajouterLigneProduit(p);
    }

    private void ajouterProduit() {
        try {
            Categorie cat = (Categorie) cmbCategorie.getSelectedItem();
            if (cat == null) { JOptionPane.showMessageDialog(this, "Veuillez sélectionner une catégorie", "Attention", JOptionPane.WARNING_MESSAGE); return; }
            if (controller.ajouterProduit(txtNomProduit.getText().trim(), cat.getIdCat(),
                    Double.parseDouble(txtPrix.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    Integer.parseInt(txtSeuil.getText().trim()))) {
                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès !");
                viderChamps(); chargerProduits();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du produit", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierProduit() {
        if (produitSelectionne == null) { JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à modifier", "Attention", JOptionPane.WARNING_MESSAGE); return; }
        try {
            Categorie cat = (Categorie) cmbCategorie.getSelectedItem();
            if (cat == null) { JOptionPane.showMessageDialog(this, "Veuillez sélectionner une catégorie", "Attention", JOptionPane.WARNING_MESSAGE); return; }
            if (controller.modifierProduit(produitSelectionne.getIdPro(), txtNomProduit.getText().trim(), cat.getIdCat(),
                    Double.parseDouble(txtPrix.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    Integer.parseInt(txtSeuil.getText().trim()))) {
                JOptionPane.showMessageDialog(this, "Produit modifié avec succès !");
                viderChamps(); produitSelectionne = null; chargerProduits();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerProduit() {
        if (produitSelectionne == null) { JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à supprimer", "Attention", JOptionPane.WARNING_MESSAGE); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce produit ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.supprimerProduit(produitSelectionne.getIdPro())) {
                JOptionPane.showMessageDialog(this, "Produit supprimé avec succès !");
                viderChamps(); produitSelectionne = null; chargerProduits();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rechercherProduits() {
        modeleProduits.setRowCount(0);
        for (Produit p : controller.rechercherProduits(txtRecherche.getText().trim())) ajouterLigneProduit(p);
    }

    private void afficherProduitsAlerte() {
        List<Produit> produits = controller.getProduitsSousSeuilAlerte();
        if (produits.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun produit en alerte !", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        modeleProduits.setRowCount(0);
        for (Produit p : produits) ajouterLigneProduit(p);
        JOptionPane.showMessageDialog(this, produits.size() + " produit(s) sous le seuil d'alerte !", "Alerte Stock", JOptionPane.WARNING_MESSAGE);
    }

    private void selectionnerProduit() {
        int row = tableProduits.getSelectedRow();
        if (row >= 0) {
            int idPro = (int) modeleProduits.getValueAt(row, 0);
            produitSelectionne = controller.getAllProduits().stream().filter(p -> p.getIdPro() == idPro).findFirst().orElse(null);
            if (produitSelectionne != null) {
                txtNomProduit.setText(produitSelectionne.getNomPro());
                for (int i = 0; i < cmbCategorie.getItemCount(); i++) {
                    if (cmbCategorie.getItemAt(i).getIdCat() == produitSelectionne.getIdCat()) { cmbCategorie.setSelectedIndex(i); break; }
                }
                txtPrix.setText(String.valueOf(produitSelectionne.getPrixVente()));
                txtStock.setText(String.valueOf(produitSelectionne.getStockActu()));
                txtSeuil.setText(String.valueOf(produitSelectionne.getSeuilAlerte()));
            }
        }
    }

    // Ajoute une ligne dans le tableau des produits avec l'état de stock
    private void ajouterLigneProduit(Produit p) {
        modeleProduits.addRow(new Object[]{
            p.getIdPro(), p.getNomPro(), p.getNomCategorie(),
            String.format("%.2f", p.getPrixVente()), p.getStockActu(), p.getSeuilAlerte(),
            p.isSousSeuilAlerte() ? "ALERTE" : "OK"
        });
    }

    private void viderChamps() {
        txtNomProduit.setText(""); txtPrix.setText(""); txtStock.setText(""); txtSeuil.setText("");
        if (cmbCategorie.getItemCount() > 0) cmbCategorie.setSelectedIndex(0);
    }
}