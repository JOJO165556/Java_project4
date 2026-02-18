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
import java.util.List;

public class CommandeService {

    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneCommandeDAO;
    private ProduitDAO produitDAO;
    private StockService stockService;

    public CommandeService() {
        this.commandeDAO = new CommandeDAO();
        this.ligneCommandeDAO = new LigneCommandeDAO();
        this.produitDAO = new ProduitDAO();
        this.stockService = new StockService();
    }

    // Crée une nouvelle commande en cours
    public Commande creerCommande() throws SQLException {
        Commande commande = new Commande();
        int id = commandeDAO.create(commande);
        if (id > 0) return commande;
        throw new SQLException("Impossible de créer la commande");
    }

    // Ajoute un produit à une commande en cours et met à jour le total
    public boolean ajouterProduit(int idCommande, int idProduit, int quantite) throws SQLException {
        Commande commande = commandeDAO.findById(idCommande);
        if (commande == null || commande.getEtat() != EtatCommande.EN_COURS)
            throw new SQLException("Commande non trouvée ou déjà validée");

        if (quantite <= 0)
            throw new SQLException("La quantité doit être supérieure à 0.");

        Produit produit = produitDAO.getById(idProduit);
        if (produit == null)
            throw new SQLException("Produit non trouvé");

        if (produit.getStockActu() < quantite)
            throw new SQLException("Stock insuffisant pour : " + produit.getNomPro());

        LigneCommande ligne = new LigneCommande(idCommande, idProduit, quantite, produit.getPrixVente());
        int idLigne = ligneCommandeDAO.create(ligne);

        if (idLigne > 0) {
            commande.setTotal(ligneCommandeDAO.calculerTotalCommande(idCommande));
            commandeDAO.update(commande);
            return true;
        }
        return false;
    }

    // Modifie la quantité d'une ligne et recalcule le total
    public boolean modifierQuantiteLigne(int idLigne, int nouvelleQuantite) throws SQLException {
        LigneCommande ligne = ligneCommandeDAO.findById(idLigne);
        if (ligne == null)
            throw new SQLException("Ligne de commande non trouvée");

        Commande commande = commandeDAO.findById(ligne.getIdCmde());
        if (commande == null || commande.getEtat() != EtatCommande.EN_COURS)
            throw new SQLException("Impossible de modifier une commande validée");

        if (nouvelleQuantite <= 0)
            throw new SQLException("La quantité doit être supérieure à 0.");

        Produit produit = produitDAO.getById(ligne.getIdPro());
        int difference = nouvelleQuantite - ligne.getQteLig();
        if (difference > 0 && produit != null && produit.getStockActu() < difference)
            throw new SQLException("Stock insuffisant pour augmenter la quantité");

        ligne.setQteLig(nouvelleQuantite);
        boolean ok = ligneCommandeDAO.update(ligne);
        if (ok) {
            commande.setTotal(ligneCommandeDAO.calculerTotalCommande(ligne.getIdCmde()));
            commandeDAO.update(commande);
        }
        return ok;
    }

    // Supprime une ligne et recalcule le total de la commande
    public boolean supprimerLigne(int idLigne) throws SQLException {
        LigneCommande ligne = ligneCommandeDAO.findById(idLigne);
        if (ligne == null)
            throw new SQLException("Ligne de commande non trouvée");

        Commande commande = commandeDAO.findById(ligne.getIdCmde());
        if (commande == null || commande.getEtat() != EtatCommande.EN_COURS)
            throw new SQLException("Impossible de modifier une commande validée");

        boolean ok = ligneCommandeDAO.delete(idLigne);
        if (ok) {
            commande.setTotal(ligneCommandeDAO.calculerTotalCommande(ligne.getIdCmde()));
            commandeDAO.update(commande);
        }
        return ok;
    }

    // Valide la commande : déduit le stock et enregistre les mouvements
    public boolean validerCommande(int idCommande) throws Exception {
        Commande commande = commandeDAO.findById(idCommande);
        if (commande == null)
            throw new SQLException("Commande non trouvée");
        if (commande.getEtat() != EtatCommande.EN_COURS)
            throw new SQLException("La commande n'est pas en cours");

        List<LigneCommande> lignes = ligneCommandeDAO.findByCommande(idCommande);
        if (lignes.isEmpty())
            throw new SQLException("Impossible de valider une commande sans lignes");

        for (LigneCommande ligne : lignes) {
            Produit produit = produitDAO.getById(ligne.getIdPro());
            if (produit != null) {
                int nouveauStock = produit.getStockActu() - ligne.getQteLig();
                if (nouveauStock < 0)
                    throw new SQLException("Stock insuffisant pour : " + produit.getNomPro());
                produit.setStockActu(nouveauStock);
                produitDAO.modifier(produit);
                stockService.enregistrerSortie(ligne.getIdPro(), ligne.getQteLig(),
                    "Vente commande #" + idCommande);
            }
        }

        commande.setEtat(EtatCommande.VALIDEE);
        return commandeDAO.update(commande);
    }

    // Annule la commande et remet le stock si elle était validée
    public boolean annulerCommande(int idCommande) throws Exception {
        Commande commande = commandeDAO.findById(idCommande);
        if (commande == null)
            throw new SQLException("Commande non trouvée");
        if (commande.getEtat() == EtatCommande.ANNULEE)
            return true;

        if (commande.getEtat() == EtatCommande.VALIDEE) {
            for (LigneCommande ligne : ligneCommandeDAO.findByCommande(idCommande)) {
                Produit produit = produitDAO.getById(ligne.getIdPro());
                if (produit != null) {
                    produit.setStockActu(produit.getStockActu() + ligne.getQteLig());
                    produitDAO.modifier(produit);
                    stockService.enregistrerEntree(ligne.getIdPro(), ligne.getQteLig(),
                        "Annulation commande #" + idCommande);
                }
            }
        }

        commande.setEtat(EtatCommande.ANNULEE);
        return commandeDAO.update(commande);
    }

    public Commande getCommande(int id) throws SQLException {
        return commandeDAO.findById(id);
    }

    public List<Commande> getAllCommandes() throws SQLException {
        return commandeDAO.findAll();
    }

    public List<LigneCommande> getLignesCommande(int idCommande) throws SQLException {
        return ligneCommandeDAO.findByCommande(idCommande);
    }

    public List<Commande> getCommandesByEtat(EtatCommande etat) throws SQLException {
        return commandeDAO.findByEtat(etat);
    }
}
