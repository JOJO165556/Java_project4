package com.restaurant.service;

import com.restaurant.dao.CategorieDAO;
import com.restaurant.model.Categorie;
import java.util.List;

public class CategorieService {

    private CategorieDAO categorieDAO;

    public CategorieService() {
        this.categorieDAO = new CategorieDAO();
    }

    // Valide et ajoute une catégorie si elle n'existe pas déjà
    public boolean ajouterCategorie(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            System.err.println("Le libellé ne peut pas être vide");
            return false;
        }
        if (libelle.length() > 30) {
            System.err.println("Le libellé ne doit pas dépasser 30 caractères");
            return false;
        }
        if (categorieDAO.existe(libelle.trim())) {
            System.err.println("Cette catégorie existe déjà");
            return false;
        }
        return categorieDAO.ajouter(new Categorie(libelle.trim()));
    }

    public boolean modifierCategorie(int idCat, String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            System.err.println("Le libellé ne peut pas être vide");
            return false;
        }
        if (libelle.length() > 30) {
            System.err.println("Le libellé ne doit pas dépasser 30 caractères");
            return false;
        }
        return categorieDAO.modifier(new Categorie(idCat, libelle.trim()));
    }

    public boolean supprimerCategorie(int idCat) {
        return categorieDAO.supprimer(idCat);
    }

    public List<Categorie> getAllCategories() {
        return categorieDAO.getAll();
    }

    public Categorie getCategorieById(int idCat) {
        return categorieDAO.getById(idCat);
    }
}
