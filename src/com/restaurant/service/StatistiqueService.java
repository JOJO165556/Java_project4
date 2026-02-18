package com.restaurant.service;

import com.restaurant.dao.CommandeDAO;
import com.restaurant.dao.LigneCommandeDAO;
import com.restaurant.dao.ProduitDAO;
import com.restaurant.model.Commande;
import com.restaurant.model.LigneCommande;
import com.restaurant.model.Produit;
import com.restaurant.model.enums.EtatCommande;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiqueService {

    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneCommandeDAO;
    private ProduitDAO produitDAO;

    public StatistiqueService() {
        this.commandeDAO = new CommandeDAO();
        this.ligneCommandeDAO = new LigneCommandeDAO();
        this.produitDAO = new ProduitDAO();
    }

    // Somme le total des commandes validées pour un jour donné
    public double getChiffreAffairesParJour(LocalDate date) throws SQLException {
        return getChiffreAffairesPeriode(date, date);
    }

    // Somme le total des commandes validées sur une période
    public double getChiffreAffairesPeriode(LocalDate debut, LocalDate fin) throws SQLException {
        double ca = 0.0;
        for (Commande c : commandeDAO.findByPeriode(debut, fin)) {
            if (c.getEtat() == EtatCommande.VALIDEE) ca += c.getTotal();
        }
        return ca;
    }

    // Produits les plus vendus (quantité)
    public List<ProduitVendu> getTopProduitsQuantite(LocalDate debut, LocalDate fin, int limite) throws SQLException {
        Map<Integer, ProduitVendu> map = new HashMap<>();

        for (Commande c : commandeDAO.findByPeriode(debut, fin)) {
            if (c.getEtat() != EtatCommande.VALIDEE) continue;
            for (LigneCommande ligne : ligneCommandeDAO.findByCommande(c.getIdCmde())) {
                ProduitVendu pv = map.computeIfAbsent(ligne.getIdPro(), id -> {
                    Produit p = produitDAO.getById(id);
                    return p != null ? new ProduitVendu(p) : null;
                });
                if (pv != null) {
                    pv.ajouterQuantite(ligne.getQteLig());
                    pv.ajouterMontant(ligne.getMontant());
                }
            }
        }

        List<ProduitVendu> result = new ArrayList<>(map.values());
        result.sort((a, b) -> Integer.compare(b.getQuantiteTotale(), a.getQuantiteTotale()));
        return limite > 0 && result.size() > limite ? result.subList(0, limite) : result;
    }

    // Produits les plus vendus (montant)
    public List<ProduitVendu> getTopProduitsMontant(LocalDate debut, LocalDate fin, int limite) throws SQLException {
        List<ProduitVendu> result = getTopProduitsQuantite(debut, fin, 0);
        result.sort((a, b) -> Double.compare(b.getMontantTotal(), a.getMontantTotal()));
        return limite > 0 && result.size() > limite ? result.subList(0, limite) : result;
    }

    // Retourne les produits dont le stock est à 0
    public List<Produit> getProduitsEnRupture() throws SQLException {
        List<Produit> rupture = new ArrayList<>();
        for (Produit p : produitDAO.getAll()) {
            if (p.getStockActu() <= 0) rupture.add(p);
        }
        return rupture;
    }

    // Retourne les produits dont le stock est inférieur ou égal au seuil d'alerte
    public List<Produit> getProduitsSousSeuilAlerte() throws SQLException {
        List<Produit> sousSeuil = new ArrayList<>();
        for (Produit p : produitDAO.getAll()) {
            if (p.getStockActu() <= p.getSeuilAlerte()) sousSeuil.add(p);
        }
        return sousSeuil;
    }

    // Calcule et retourne un ensemble de statistiques générales
    public StatistiquesGenerales getStatistiquesGenerales() throws SQLException {
        StatistiquesGenerales stats = new StatistiquesGenerales();
        LocalDate today = LocalDate.now();

        stats.setNbProduits(produitDAO.getAll().size());
        stats.setNbProduitsRupture(getProduitsEnRupture().size());
        stats.setNbProduitsSousSeuil(getProduitsSousSeuilAlerte().size());
        stats.setNbCommandesJour(commandeDAO.findByPeriode(today, today).size());
        stats.setCaJour(getChiffreAffairesParJour(today));
        stats.setNbCommandesEnCours(commandeDAO.findByEtat(EtatCommande.EN_COURS).size());

        return stats;
    }

    // Représente un produit avec ses totaux de vente (quantité et montant)
    public static class ProduitVendu {
        private Produit produit;
        private int quantiteTotale;
        private double montantTotal;

        public ProduitVendu(Produit produit) {
            this.produit = produit;
        }

        public void ajouterQuantite(int q) { this.quantiteTotale += q; }
        public void ajouterMontant(double m) { this.montantTotal += m; }
        public Produit getProduit() { return produit; }
        public int getQuantiteTotale() { return quantiteTotale; }
        public double getMontantTotal() { return montantTotal; }

        @Override
        public String toString() {
            return produit.getNomPro() + " - " + quantiteTotale + " unités - " + montantTotal + "€";
        }
    }

    // Contient les indicateurs clés affichés dans l'onglet Statistiques générales
    public static class StatistiquesGenerales {
        private int nbProduits, nbProduitsRupture, nbProduitsSousSeuil, nbCommandesJour, nbCommandesEnCours;
        private double caJour;

        public int getNbProduits() { return nbProduits; }
        public void setNbProduits(int v) { this.nbProduits = v; }
        public int getNbProduitsRupture() { return nbProduitsRupture; }
        public void setNbProduitsRupture(int v) { this.nbProduitsRupture = v; }
        public int getNbProduitsSousSeuil() { return nbProduitsSousSeuil; }
        public void setNbProduitsSousSeuil(int v) { this.nbProduitsSousSeuil = v; }
        public int getNbCommandesJour() { return nbCommandesJour; }
        public void setNbCommandesJour(int v) { this.nbCommandesJour = v; }
        public double getCaJour() { return caJour; }
        public void setCaJour(double v) { this.caJour = v; }
        public int getNbCommandesEnCours() { return nbCommandesEnCours; }
        public void setNbCommandesEnCours(int v) { this.nbCommandesEnCours = v; }
    }
}
