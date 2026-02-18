package com.restaurant.controller;

import com.restaurant.model.Produit;
import com.restaurant.service.StatistiqueService;
import com.restaurant.service.StatistiqueService.ProduitVendu;
import com.restaurant.service.StatistiqueService.StatistiquesGenerales;
import com.restaurant.view.StatistiqueView;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StatistiqueController {

    private StatistiqueView statistiqueView;
    private StatistiqueService statistiqueService;

    public StatistiqueController() {
        this.statistiqueService = new StatistiqueService();
    }

    public void setView(StatistiqueView view) {
        this.statistiqueView = view;
    }

    // Charge et affiche toutes les statistiques du tableau de bord
    public void rafraichirToutesStatistiques() {
        try {
            LocalDate debutMois = LocalDate.now().withDayOfMonth(1);
            LocalDate finMois = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

            if (statistiqueView == null) return;
            statistiqueView.afficherStatistiquesGenerales(statistiqueService.getStatistiquesGenerales());
            statistiqueView.afficherProduitsRupture(statistiqueService.getProduitsEnRupture());
            statistiqueView.afficherProduitsSousSeuil(statistiqueService.getProduitsSousSeuilAlerte());
            statistiqueView.afficherTopProduitsQuantite(statistiqueService.getTopProduitsQuantite(debutMois, finMois, 10));
            statistiqueView.afficherTopProduitsMontant(statistiqueService.getTopProduitsMontant(debutMois, finMois, 10));
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des statistiques : " + e.getMessage());
        }
    }

    public void afficherChiffreAffairesJour(LocalDate date) {
        try {
            if (statistiqueView != null)
                statistiqueView.afficherChiffreAffairesJour(date, statistiqueService.getChiffreAffairesParJour(date));
        } catch (SQLException e) {
            afficherErreur("Erreur lors du calcul du CA journalier : " + e.getMessage());
        }
    }

    public void afficherChiffreAffairesPeriode(LocalDate debut, LocalDate fin) {
        try {
            if (statistiqueView != null)
                statistiqueView.afficherChiffreAffairesPeriode(debut, fin, statistiqueService.getChiffreAffairesPeriode(debut, fin));
        } catch (SQLException e) {
            afficherErreur("Erreur lors du calcul du CA sur la période : " + e.getMessage());
        }
    }

    public void afficherTopProduitsQuantitePeriode(LocalDate debut, LocalDate fin, int limite) {
        try {
            if (statistiqueView != null)
                statistiqueView.afficherTopProduitsQuantitePeriode(debut, fin, statistiqueService.getTopProduitsQuantite(debut, fin, limite));
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des top produits : " + e.getMessage());
        }
    }

    public void afficherTopProduitsMontantPeriode(LocalDate debut, LocalDate fin, int limite) {
        try {
            if (statistiqueView != null)
                statistiqueView.afficherTopProduitsMontantPeriode(debut, fin, statistiqueService.getTopProduitsMontant(debut, fin, limite));
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des top produits : " + e.getMessage());
        }
    }

    public void afficherProduitsRupture() {
        try {
            if (statistiqueView != null)
                statistiqueView.afficherProduitsRupture(statistiqueService.getProduitsEnRupture());
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des produits en rupture : " + e.getMessage());
        }
    }

    public void afficherProduitsSousSeuil() {
        try {
            if (statistiqueView != null)
                statistiqueView.afficherProduitsSousSeuil(statistiqueService.getProduitsSousSeuilAlerte());
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des produits sous seuil : " + e.getMessage());
        }
    }

    // Affiche le CA et les top 5 produits sur une période personnalisée
    public void afficherStatistiquesPersonnalisees(LocalDate debut, LocalDate fin) {
        try {
            double ca = statistiqueService.getChiffreAffairesPeriode(debut, fin);
            List<ProduitVendu> top = statistiqueService.getTopProduitsQuantite(debut, fin, 5);
            if (statistiqueView != null)
                statistiqueView.afficherStatistiquesPersonnalisees(debut, fin, ca, top);
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des statistiques personnalisées : " + e.getMessage());
        }
    }

    private void afficherErreur(String msg) {
        if (statistiqueView != null) statistiqueView.afficherErreur(msg);
    }
}
