package com.restaurant.view;

import com.restaurant.controller.StockController;
import com.restaurant.model.Produit;
import com.restaurant.model.MouvementStock;
import com.restaurant.dao.ProduitDAO;
import com.restaurant.dao.MouvementStockDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockView extends JPanel {

    private JComboBox<Produit> comboProduits;
    private JTextField txtQuantite, txtMotif;
    private JRadioButton rbEntree, rbSortie;
    private JButton btnValider;
    private JTable tableHistorique;
    private DefaultTableModel tableModel;
    private final StockController controller;

    public StockView() {
        initAndLayout();
        this.controller = new StockController(this);

        chargerProduits();
        actualiserHistorique();
    }

    private void initAndLayout() {
        setLayout(new BorderLayout(15, 15));

        // Formulaire de saisie d'un mouvement
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Nouveau Mouvement"));

        panelForm.add(new JLabel("Produit :"));
        comboProduits = new JComboBox<>();
        panelForm.add(comboProduits);

        panelForm.add(new JLabel("Type :"));
        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbEntree = new JRadioButton("ENTRÉE", true);
        rbSortie = new JRadioButton("SORTIE");
        ButtonGroup group = new ButtonGroup();
        group.add(rbEntree);
        group.add(rbSortie);
        panelRadio.add(rbEntree);
        panelRadio.add(rbSortie);
        panelForm.add(panelRadio);

        panelForm.add(new JLabel("Quantité :"));
        txtQuantite = new JTextField();
        panelForm.add(txtQuantite);

        panelForm.add(new JLabel("Motif :"));
        txtMotif = new JTextField();
        panelForm.add(txtMotif);

        btnValider = new JButton("Enregistrer le mouvement");
        btnValider.setBackground(new Color(70, 130, 180));
        btnValider.setForeground(Color.WHITE);
        panelForm.add(new JLabel());
        panelForm.add(btnValider);

        // Tableau de l'historique des mouvements
        String[] columns = {"ID", "Produit", "Type", "Quantité", "Date", "Motif"};
        tableModel = new DefaultTableModel(columns, 0);
        tableHistorique = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableHistorique);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historique des mouvements"));

        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnValider.addActionListener(e -> controller.enregistrerMouvement());
    }

    public void actualiserHistorique() {
        try {
            tableModel.setRowCount(0);
            for (MouvementStock m : new MouvementStockDAO().listerHistorique()) {
                tableModel.addRow(new Object[]{
                    m.getId(), m.getProduit().getNomPro(), m.getType(),
                    m.getQuantite(), m.getDate(), m.getMotif()
                });
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement historique : " + e.getMessage());
        }
    }

    public void resetChamps() {
        txtQuantite.setText("");
        txtMotif.setText("");
        rbEntree.setSelected(true);
        if (comboProduits.getItemCount() > 0) comboProduits.setSelectedIndex(0);
        txtQuantite.requestFocus();
    }

    private void chargerProduits() {
        try {
            comboProduits.removeAllItems();
            for (Produit p : new ProduitDAO().getAll()) comboProduits.addItem(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement produits : " + e.getMessage());
        }
    }

    // Getters pour le contrôleur
    public Produit getProduitSelectionne() { return (Produit) comboProduits.getSelectedItem(); }
    public String getQuantiteSaisie() { return txtQuantite.getText(); }
    public boolean isEntreeSelectionnee() { return rbEntree.isSelected(); }
    public String getMotifSaisi() { return txtMotif.getText(); }
}
