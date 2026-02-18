package com.restaurant.service;

import com.restaurant.model.MouvementStock;
import com.restaurant.model.Produit;
import com.restaurant.model.enums.TypeMouvement;
import com.restaurant.dao.ProduitDAO;
import com.restaurant.dao.MouvementStockDAO;
import java.time.LocalDate;

public class StockService {

    private ProduitDAO produitDAO = new ProduitDAO();
    private MouvementStockDAO mouvementDAO = new MouvementStockDAO();

    // Applique un mouvement de stock : met à jour la quantité et enregistre l'historique
    public void traiterMouvement(MouvementStock mvt) throws Exception {
        Produit p = mvt.getProduit();
        int ancienStock = p.getStockActu();
        int nouveauStock;

        if (mvt.getQuantite() <= 0) {
            throw new Exception("La quantité doit être supérieure à 0.");
        }

        if (mvt.getType() == TypeMouvement.ENTREE) {
            nouveauStock = ancienStock + mvt.getQuantite();
        } else {
            if (ancienStock < mvt.getQuantite())
                throw new Exception("Stock insuffisant pour cette sortie !");
            nouveauStock = ancienStock - mvt.getQuantite();
        }

        if (produitDAO.mettreAJourStock(p.getIdPro(), nouveauStock)) {
            mouvementDAO.ajouter(mvt);
            p.setStockActu(nouveauStock);
        } else {
            throw new Exception("Erreur lors de la mise à jour du stock en base.");
        }
    }

    public void enregistrerEntree(int idProduit, int quantite, String motif) throws Exception {
        Produit produit = produitDAO.getById(idProduit);
        if (produit == null) throw new Exception("Produit non trouvé");

        if (motif != null && motif.length() > 50) {
            throw new Exception("Le motif ne doit pas dépasser 50 caractères.");
        }

        MouvementStock mvt = new MouvementStock();
        mvt.setProduit(produit);
        mvt.setType(TypeMouvement.ENTREE);
        mvt.setQuantite(quantite);
        mvt.setMotif(motif);
        mvt.setDate(LocalDate.now());
        traiterMouvement(mvt);
    }

    public void enregistrerSortie(int idProduit, int quantite, String motif) throws Exception {
        Produit produit = produitDAO.getById(idProduit);
        if (produit == null) throw new Exception("Produit non trouvé");

        if (motif != null && motif.length() > 50) {
            throw new Exception("Le motif ne doit pas dépasser 50 caractères.");
        }

        MouvementStock mvt = new MouvementStock();
        mvt.setProduit(produit);
        mvt.setType(TypeMouvement.SORTIE);
        mvt.setQuantite(quantite);
        mvt.setMotif(motif);
        mvt.setDate(LocalDate.now());
        traiterMouvement(mvt);
    }
}
