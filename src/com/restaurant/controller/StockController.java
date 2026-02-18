package com.restaurant.controller;

import com.restaurant.view.StockView;
import com.restaurant.model.Produit;
import com.restaurant.model.MouvementStock;
import com.restaurant.model.enums.TypeMouvement;
import com.restaurant.service.StockService;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class StockController {

    private final StockView view;
    private final StockService service = new StockService();

    public StockController(StockView view) {
        this.view = view;
    }

    // Lit les données de la vue, crée le mouvement et délègue au service
    public void enregistrerMouvement() {
        try {
            int qte;
            try {
                qte = Integer.parseInt(view.getQuantiteSaisie());
            } catch (NumberFormatException e) {
                throw new Exception("La quantité doit être un nombre entier valide !");
            }

            MouvementStock mvt = new MouvementStock();
            mvt.setProduit(view.getProduitSelectionne());
            mvt.setQuantite(qte);
            mvt.setType(view.isEntreeSelectionnee() ? TypeMouvement.ENTREE : TypeMouvement.SORTIE);
            mvt.setMotif(view.getMotifSaisi());
            mvt.setDate(LocalDate.now());

            service.traiterMouvement(mvt);
            
            // Alerte de stock immédiate
            Produit p = mvt.getProduit();
            if (p.getStockActu() == 0) {
                JOptionPane.showMessageDialog(view, "ALERTE : Le produit " + p.getNomPro() + " est en RUPTURE DE STOCK !", "Rupture de Stock", JOptionPane.ERROR_MESSAGE);
            } else if (p.isSousSeuilAlerte()) {
                JOptionPane.showMessageDialog(view, "Attention : Le stock de " + p.getNomPro() + " est faible (" + p.getStockActu() + ")", "Seuil d'alerte atteint", JOptionPane.WARNING_MESSAGE);
            }

            JOptionPane.showMessageDialog(view, "Mouvement enregistré avec succès !");
            view.resetChamps();
            view.actualiserHistorique();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erreur : " + e.getMessage(), "Échec", JOptionPane.ERROR_MESSAGE);
        }
    }
}