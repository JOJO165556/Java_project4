package com.restaurant.controller;

import com.restaurant.model.Categorie;
import com.restaurant.model.Produit;
import com.restaurant.service.CategorieService;
import com.restaurant.service.ProduitService;
import java.util.List;

public class ProduitController {

    private ProduitService produitService;
    private CategorieService categorieService;

    public ProduitController() {
        this.produitService = new ProduitService();
        this.categorieService = new CategorieService();
    }

    public boolean ajouterProduit(String nom, int idCat, double prix, int stock, int seuil) {
        return produitService.ajouterProduit(nom, idCat, prix, stock, seuil);
    }

    public boolean modifierProduit(int idPro, String nom, int idCat, double prix, int stock, int seuil) {
        return produitService.modifierProduit(idPro, nom, idCat, prix, stock, seuil);
    }

    public boolean supprimerProduit(int idPro) {
        return produitService.supprimerProduit(idPro);
    }

    public List<Produit> getAllProduits() {
        return produitService.getAllProduits();
    }

    public List<Produit> rechercherProduits(String nom) {
        return produitService.rechercherProduits(nom);
    }

    public List<Produit> getProduitsParCategorie(int idCat) {
        return produitService.getProduitsParCategorie(idCat);
    }

    public List<Produit> getProduitsSousSeuilAlerte() {
        return produitService.getProduitsSousSeuilAlerte();
    }

    public boolean ajouterCategorie(String libelle) {
        return categorieService.ajouterCategorie(libelle);
    }

    public boolean modifierCategorie(int idCat, String libelle) {
        return categorieService.modifierCategorie(idCat, libelle);
    }

    public boolean supprimerCategorie(int idCat) {
        return categorieService.supprimerCategorie(idCat);
    }

    public List<Categorie> getAllCategories() {
        return categorieService.getAllCategories();
    }

    public Categorie getCategorieById(int idCat) {
        return categorieService.getCategorieById(idCat);
    }
}
