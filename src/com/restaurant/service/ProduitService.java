package com.restaurant.service;

import com.restaurant.dao.ProduitDAO;
import com.restaurant.model.Produit;
import java.util.List;

public class ProduitService {

    private ProduitDAO produitDAO;

    public ProduitService() {
        this.produitDAO = new ProduitDAO();
    }

    // Valide les données et ajoute le produit en base
    public boolean ajouterProduit(String nom, int idCat, double prix, int stock, int seuil) {
        if (!valider(nom, prix, stock, seuil)) return false;
        return produitDAO.ajouter(new Produit(nom.trim(), idCat, prix, stock, seuil));
    }

    // Valide les données et met à jour le produit en base
    public boolean modifierProduit(int idPro, String nom, int idCat, double prix, int stock, int seuil) {
        if (!valider(nom, prix, stock, seuil)) return false;
        return produitDAO.modifier(new Produit(idPro, nom.trim(), idCat, prix, stock, seuil));
    }

    public boolean supprimerProduit(int idPro) {
        return produitDAO.supprimer(idPro);
    }

    public List<Produit> getAllProduits() {
        return produitDAO.getAll();
    }

    public Produit getProduitById(int idPro) {
        return produitDAO.getById(idPro);
    }

    // Retourne tous les produits si la recherche est vide, sinon filtre par nom
    public List<Produit> rechercherProduits(String nom) {
        if (nom == null || nom.trim().isEmpty()) return getAllProduits();
        return produitDAO.rechercherParNom(nom.trim());
    }

    public List<Produit> getProduitsParCategorie(int idCat) {
        return produitDAO.getByCategorie(idCat);
    }

    public List<Produit> getProduitsSousSeuilAlerte() {
        return produitDAO.getProduitsSousSeuilAlerte();
    }

    public boolean estSousSeuilAlerte(int idPro) {
        Produit p = produitDAO.getById(idPro);
        return p != null && p.isSousSeuilAlerte();
    }

    // Règles de validation communes à l'ajout et à la modification
    private boolean valider(String nom, double prix, int stock, int seuil) {
        if (nom == null || nom.trim().isEmpty()) {
            System.err.println("Le nom du produit ne peut pas être vide");
            return false;
        }
        if (nom.length() > 50) {
            System.err.println("Le nom ne doit pas dépasser 50 caractères");
            return false;
        }
        if (prix <= 0) {
            System.err.println("Le prix doit être supérieur à 0");
            return false;
        }
        if (stock < 0 || seuil < 0) {
            System.err.println("Le stock et le seuil ne peuvent pas être négatifs");
            return false;
        }
        return true;
    }
}