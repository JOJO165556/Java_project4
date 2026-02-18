package com.restaurant.view;

import com.restaurant.utils.DesignSystem;
import com.restaurant.model.Produit;
import com.restaurant.service.StatistiqueService.ProduitVendu;
import com.restaurant.service.StatistiqueService.StatistiquesGenerales;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class StatistiqueView extends JPanel {

    private JTabbedPane tabbedPane;

    // Onglet Statistiques générales
    private JPanel panelGeneral;
    private JLabel lblNbProduits, lblNbProduitsRupture, lblNbProduitsSousSeuil;
    private JLabel lblNbCommandesJour, lblCaJour, lblNbCommandesEnCours;

    // Onglet Chiffre d'affaires
    private JPanel panelCA;
    private JDateChooser dateChooserCAJour, dateChooserCADebut, dateChooserCAFin;
    private JButton btnCalculerCAJour, btnCalculerCAPeriode;
    private JLabel lblResultatCAJour, lblResultatCAPeriode;

    // Onglet Top produits
    private JPanel panelTopProduits;
    private JDateChooser dateChooserTopDebut, dateChooserTopFin;
    private JSpinner spinLimiteTop;
    private JButton btnCalculerTopQuantite, btnCalculerTopMontant;
    private JTable tableTopProduits;
    private DefaultTableModel tableModelTop;

    // Onglet Stocks
    private JPanel panelStocks;
    private JTable tableProduitsRupture, tableProduitsSousSeuil;
    private DefaultTableModel tableModelRupture, tableModelSousSeuil;
    private JButton btnRafraichirStocks;

    private com.restaurant.controller.StatistiqueController controller;

    public StatistiqueView() {
        this.controller = new com.restaurant.controller.StatistiqueController();
        this.controller.setView(this);
        initComposants();
        initLayout();
        initListeners();
        controller.rafraichirToutesStatistiques();
    }

    private void initComposants() {
        // Onglet résumé - Modernisé avec des cartes
        panelGeneral = new JPanel(new GridLayout(3, 2, 20, 20));
        panelGeneral.setBackground(DesignSystem.BACKGROUND);
        panelGeneral.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        lblNbProduits = createStatLabel("Total Produits");
        lblNbProduitsRupture = createStatLabel("En Rupture");
        lblNbProduitsSousSeuil = createStatLabel("Sous Seuil Alerte");
        lblNbCommandesJour = createStatLabel("Commandes du jour");
        lblCaJour = createStatLabel("CA du jour");
        lblNbCommandesEnCours = createStatLabel("En cours");

        panelGeneral.add(creerCarteStat(lblNbProduits, DesignSystem.PRIMARY));
        panelGeneral.add(creerCarteStat(lblNbProduitsRupture, DesignSystem.DANGER));
        panelGeneral.add(creerCarteStat(lblNbProduitsSousSeuil, new Color(245, 158, 11))); // Orange/Warning
        panelGeneral.add(creerCarteStat(lblNbCommandesJour, DesignSystem.SUCCESS));
        panelGeneral.add(creerCarteStat(lblCaJour, DesignSystem.PRIMARY));
        panelGeneral.add(creerCarteStat(lblNbCommandesEnCours, DesignSystem.SECONDARY));

        // Onglet CA
        panelCA = new JPanel(new BorderLayout());
        JPanel panelCAForm = new JPanel(new GridLayout(2, 3, 10, 10));
        panelCAForm.setBorder(BorderFactory.createTitledBorder("Calcul du chiffre d'affaires"));
        dateChooserCAJour = new JDateChooser();
        dateChooserCADebut = new JDateChooser();
        dateChooserCAFin = new JDateChooser();
        panelCAForm.add(new JLabel("CA par jour:"));
        panelCAForm.add(dateChooserCAJour);
        panelCAForm.add(btnCalculerCAJour = new JButton("Calculer"));
        panelCAForm.add(new JLabel("CA par période:"));
        panelCAForm.add(dateChooserCADebut);
        panelCAForm.add(dateChooserCAFin);

        JPanel panelCAResultats = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCAResultats.setBorder(BorderFactory.createTitledBorder("Résultats"));
        lblResultatCAJour = new JLabel("CA du jour: 0.00 F CFA");
        lblResultatCAPeriode = new JLabel("CA de la période: 0.00 F CFA");
        panelCAResultats.add(lblResultatCAJour);
        panelCAResultats.add(lblResultatCAPeriode);
        // Bouton de calcul par période (non présent dans le formulaire, ajouté ici)
        btnCalculerCAPeriode = new JButton("Calculer période");
        panelCAResultats.add(btnCalculerCAPeriode);
        panelCA.add(panelCAForm, BorderLayout.NORTH);
        panelCA.add(panelCAResultats, BorderLayout.CENTER);

        // Onglet Top produits
        panelTopProduits = new JPanel(new BorderLayout());
        JPanel panelTopForm = new JPanel(new FlowLayout());
        panelTopForm.setBorder(BorderFactory.createTitledBorder("Période d'analyse"));
        dateChooserTopDebut = new JDateChooser();
        dateChooserTopFin = new JDateChooser();
        spinLimiteTop = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        btnCalculerTopQuantite = new JButton("Top par quantité");
        btnCalculerTopMontant = new JButton("Top par montant");
        panelTopForm.add(new JLabel("Du:")); panelTopForm.add(dateChooserTopDebut);
        panelTopForm.add(new JLabel("Au:")); panelTopForm.add(dateChooserTopFin);
        panelTopForm.add(new JLabel("Limite:")); panelTopForm.add(spinLimiteTop);
        panelTopForm.add(btnCalculerTopQuantite); panelTopForm.add(btnCalculerTopMontant);
        String[] colonnesTop = {"Produit", "Quantité totale", "Montant total"};
        tableModelTop = new DefaultTableModel(colonnesTop, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTopProduits = new JTable(tableModelTop);
        panelTopProduits.add(panelTopForm, BorderLayout.NORTH);
        panelTopProduits.add(new JScrollPane(tableTopProduits), BorderLayout.CENTER);

        // Onglet Stocks
        panelStocks = new JPanel(new BorderLayout());
        btnRafraichirStocks = new JButton("Rafraîchir");
        panelStocks.add(btnRafraichirStocks, BorderLayout.NORTH);
        JPanel panelStocksTables = new JPanel(new GridLayout(1, 2, 10, 10));
        String[] colonnesRupture = {"ID", "Produit", "Stock actuel"};
        tableModelRupture = new DefaultTableModel(colonnesRupture, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableProduitsRupture = new JTable(tableModelRupture);
        String[] colonnesSousSeuil = {"ID", "Produit", "Stock actuel", "Seuil d'alerte"};
        tableModelSousSeuil = new DefaultTableModel(colonnesSousSeuil, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableProduitsSousSeuil = new JTable(tableModelSousSeuil);
        panelStocksTables.add(new JScrollPane(tableProduitsRupture));
        panelStocksTables.add(new JScrollPane(tableProduitsSousSeuil));
        panelStocks.add(panelStocksTables, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Statistiques générales", panelGeneral);
        tabbedPane.addTab("Chiffre d'affaires", panelCA);
        tabbedPane.addTab("Top produits", panelTopProduits);
        tabbedPane.addTab("Alertes & Ruptures", panelStocks);
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initListeners() {
        btnCalculerCAJour.addActionListener(e -> controller.afficherChiffreAffairesJour(dateChooserCAJour.getDate()));
        btnCalculerCAPeriode.addActionListener(e -> controller.afficherChiffreAffairesPeriode(dateChooserCADebut.getDate(), dateChooserCAFin.getDate()));
        btnCalculerTopQuantite.addActionListener(e -> controller.afficherTopProduitsQuantitePeriode(dateChooserTopDebut.getDate(), dateChooserTopFin.getDate(), (Integer) spinLimiteTop.getValue()));
        btnCalculerTopMontant.addActionListener(e -> controller.afficherTopProduitsMontantPeriode(dateChooserTopDebut.getDate(), dateChooserTopFin.getDate(), (Integer) spinLimiteTop.getValue()));
        btnRafraichirStocks.addActionListener(e -> { controller.afficherProduitsRupture(); controller.afficherProduitsSousSeuil(); });
    }

    // Methodes controleur

    private JLabel createStatLabel(String title) {
        JLabel label = new JLabel("0");
        label.setFont(new Font("SansSerif", Font.BOLD, 22));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel creerCarteStat(JLabel label, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String title = "";
        if (label == lblNbProduits) title = "Total Produits";
        else if (label == lblNbProduitsRupture) title = "En Rupture";
        else if (label == lblNbProduitsSousSeuil) title = "Alerte Stock";
        else if (label == lblNbCommandesJour) title = "Cmdes Jour";
        else if (label == lblCaJour) title = "CA Jour";
        else if (label == lblNbCommandesEnCours) title = "En cours";

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(DesignSystem.FONT_SUBTITLE);
        lblTitle.setForeground(DesignSystem.TEXT_MUTED);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        label.setForeground(color);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(label, BorderLayout.CENTER);
        return card;
    }

    public void afficherStatistiquesGenerales(StatistiquesGenerales stats) {
        if (stats == null) return;
        lblNbProduits.setText(String.valueOf(stats.getNbProduits()));
        lblNbProduitsRupture.setText(String.valueOf(stats.getNbProduitsRupture()));
        lblNbProduitsSousSeuil.setText(String.valueOf(stats.getNbProduitsSousSeuil()));
        lblNbCommandesJour.setText(String.valueOf(stats.getNbCommandesJour()));
        lblCaJour.setText(String.format("%.2f F CFA", stats.getCaJour()));
        lblNbCommandesEnCours.setText(String.valueOf(stats.getNbCommandesEnCours()));
    }

    public void afficherChiffreAffairesJour(LocalDate date, double ca) {
        lblResultatCAJour.setText(String.format("CA du %s: %.2f F CFA", date, ca));
    }

    public void afficherChiffreAffairesPeriode(LocalDate debut, LocalDate fin, double ca) {
        lblResultatCAPeriode.setText(String.format("CA du %s au %s: %.2f F CFA", debut, fin, ca));
    }

    // Table Top produits
    private void afficherTopProduits(List<ProduitVendu> top) {
        tableModelTop.setRowCount(0);
        if (top != null)
            for (ProduitVendu pv : top)
                tableModelTop.addRow(new Object[]{pv.getProduit().getNomPro(), pv.getQuantiteTotale(), String.format("%.2f F CFA", pv.getMontantTotal())});
    }

    public void afficherTopProduitsQuantite(List<ProduitVendu> top) { afficherTopProduits(top); }
    public void afficherTopProduitsMontant(List<ProduitVendu> top) { afficherTopProduits(top); }
    public void afficherTopProduitsQuantitePeriode(LocalDate d, LocalDate f, List<ProduitVendu> top) { afficherTopProduits(top); }
    public void afficherTopProduitsMontantPeriode(LocalDate d, LocalDate f, List<ProduitVendu> top) { afficherTopProduits(top); }
    public void afficherStatistiquesPersonnalisees(LocalDate d, LocalDate f, double ca, List<ProduitVendu> top) { afficherTopProduits(top); }

    public void afficherProduitsRupture(List<Produit> produits) {
        tableModelRupture.setRowCount(0);
        if (produits != null)
            for (Produit p : produits)
                tableModelRupture.addRow(new Object[]{p.getIdPro(), p.getNomPro(), p.getStockActu()});
    }

    public void afficherProduitsSousSeuil(List<Produit> produits) {
        tableModelSousSeuil.setRowCount(0);
        if (produits != null)
            for (Produit p : produits)
                tableModelSousSeuil.addRow(new Object[]{p.getIdPro(), p.getNomPro(), p.getStockActu(), p.getSeuilAlerte()});
    }

    public void afficherMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void afficherErreur(String err) {
        JOptionPane.showMessageDialog(this, err, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

// Selecteur de date simple
class JDateChooser extends JPanel {

    private JTextField dateField;

    public JDateChooser() {
        setLayout(new BorderLayout());
        dateField = new JTextField(LocalDate.now().toString());
        JButton btn = new JButton("...");
        btn.setPreferredSize(new Dimension(30, 20));
        add(dateField, BorderLayout.CENTER);
        add(btn, BorderLayout.EAST);
        
        btn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Saisir la date (AAAA-MM-JJ):", dateField.getText());
            if (input != null && !input.trim().isEmpty()) {
                try {
                    LocalDate.parse(input.trim());
                    dateField.setText(input.trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Format invalide ! Utilisez AAAA-MM-JJ", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void setDate(LocalDate date) { dateField.setText(date.toString()); }

    public LocalDate getDate() {
        try {
            return LocalDate.parse(dateField.getText());
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}
