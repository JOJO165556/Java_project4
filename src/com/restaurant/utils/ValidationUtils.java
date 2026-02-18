package com.restaurant.utils;

/**
 * Classe utilitaire pour la validation des données
 */
public class ValidationUtils {
    
    // Valider un nom (catégorie, produit, utilisateur)
    public static boolean validerNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return false;
        }
        
        String nomTrim = nom.trim();
        
        // Longueur minimale et maximale
        if (nomTrim.length() < 2 || nomTrim.length() > 50) {
            return false;
        }
        
        // Caractères autorisés: lettres, chiffres, espaces, tirets, apostrophes
        return nomTrim.matches("^[a-zA-Z0-9\\s\\-']+$");
    }
    
    // Valider un libellé de catégorie
    public static boolean validerLibelleCategorie(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            return false;
        }
        
        String libelleTrim = libelle.trim();
        
        // Longueur spécifique pour les catégories (max 30 en BDD)
        if (libelleTrim.length() < 2 || libelleTrim.length() > 30) {
            return false;
        }
        
        return validerNom(libelleTrim);
    }
    
    // Valider un nom de produit
    public static boolean validerNomProduit(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return false;
        }
        
        String nomTrim = nom.trim();
        
        // Longueur spécifique pour les produits (max 50 en BDD)
        if (nomTrim.length() < 2 || nomTrim.length() > 50) {
            return false;
        }
        
        return validerNom(nomTrim);
    }
    
    // Valider un prix (doit être positif)
    public static boolean validerPrix(double prix) {
        return prix > 0 && prix <= 999999.99; // Limite raisonnable
    }
    
    // Valider un prix sous forme de chaîne
    public static boolean validerPrix(String prixStr) {
        try {
            double prix = Double.parseDouble(prixStr.replace(',', '.'));
            return validerPrix(prix);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Valider une quantité (doit être positive)
    public static boolean validerQuantite(int quantite) {
        return quantite > 0 && quantite <= 9999; // Limite raisonnable
    }
    
    // Valider une quantité sous forme de chaîne
    public static boolean validerQuantite(String quantiteStr) {
        try {
            int quantite = Integer.parseInt(quantiteStr);
            return validerQuantite(quantite);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Valider un stock (doit être non négatif)
    public static boolean validerStock(int stock) {
        return stock >= 0 && stock <= 99999; // Limite raisonnable
    }
    
    // Valider un stock sous forme de chaîne
    public static boolean validerStock(String stockStr) {
        try {
            int stock = Integer.parseInt(stockStr);
            return validerStock(stock);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Valider un seuil d'alerte
    public static boolean validerSeuilAlerte(int seuil) {
        return seuil >= 0 && seuil <= 9999; // Limite raisonnable
    }
    
    // Valider un seuil d'alerte sous forme de chaîne
    public static boolean validerSeuilAlerte(String seuilStr) {
        try {
            int seuil = Integer.parseInt(seuilStr);
            return validerSeuilAlerte(seuil);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Valider un nom d'utilisateur
    public static boolean validerNomUtilisateur(String nomUtil) {
        if (nomUtil == null || nomUtil.trim().isEmpty()) {
            return false;
        }
        
        String nomTrim = nomUtil.trim();
        
        // Longueur pour les noms d'utilisateur (max 50 en BDD)
        if (nomTrim.length() < 3 || nomTrim.length() > 50) {
            return false;
        }
        
        // Caractères autorisés: lettres, chiffres, underscores, tirets
        return nomTrim.matches("^[a-zA-Z0-9\\-_]+$");
    }
    
    // Valider un mot de passe
    public static boolean validerMotDePasse(String motDePasse) {
        if (motDePasse == null || motDePasse.trim().isEmpty()) {
            return false;
        }
        
        String mdpTrim = motDePasse.trim();
        
        // Longueur minimale et maximale
        if (mdpTrim.length() < 4 || mdpTrim.length() > 256) {
            return false;
        }
        
        // Au moins un caractère non-espace
        return !mdpTrim.contains(" ");
    }
    
    // Valider un motif de mouvement de stock
    public static boolean validerMotif(String motif) {
        if (motif == null || motif.trim().isEmpty()) {
            return true; // Le motif peut être optionnel
        }
        
        String motifTrim = motif.trim();
        
        // Longueur maximale (max 50 en BDD)
        if (motifTrim.length() > 50) {
            return false;
        }
        
        // Caractères autorisés: lettres, chiffres, espaces, tirets, apostrophes
        return motifTrim.matches("^[a-zA-Z0-9\\s\\-']+$");
    }
    
    // Valider une description ou commentaire
    public static boolean validerDescription(String description, int longueurMax) {
        if (description == null) {
            return true; // La description peut être nulle
        }
        
        String descTrim = description.trim();
        
        if (descTrim.length() > longueurMax) {
            return false;
        }
        
        return true;
    }
    
    // Valider qu'une chaîne n'est pas vide
    public static boolean validerChampNonVide(String champ) {
        return champ != null && !champ.trim().isEmpty();
    }
    
    // Valider un entier dans une plage
    public static boolean validerEntierDansPlage(int valeur, int min, int max) {
        return valeur >= min && valeur <= max;
    }
    
    // Valider un entier dans une plage (sous forme de chaîne)
    public static boolean validerEntierDansPlage(String valeurStr, int min, int max) {
        try {
            int valeur = Integer.parseInt(valeurStr);
            return validerEntierDansPlage(valeur, min, max);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Nettoyer et normaliser une chaîne
    public static String normaliserChaine(String chaine) {
        if (chaine == null) {
            return "";
        }
        
        return chaine.trim().replaceAll("\\s+", " ");
    }
    
    // Obtenir un message d'erreur approprié
    public static String getMessageErreur(String champ, String type) {
        switch (type.toLowerCase()) {
            case "nom":
                return "Le " + champ + " doit contenir entre 2 et 50 caractères alphanumériques";
            case "prix":
                return "Le " + champ + " doit être un nombre positif";
            case "quantite":
                return "La " + champ + " doit être un nombre entier positif";
            case "stock":
                return "Le " + champ + " doit être un nombre entier positif ou nul";
            case "utilisateur":
                return "Le " + champ + " doit contenir entre 3 et 50 caractères (lettres, chiffres, _, -)";
            case "motdepasse":
                return "Le " + champ + " doit contenir au moins 4 caractères";
            case "nonvide":
                return "Le " + champ + " ne peut pas être vide";
            default:
                return "Le " + champ + " n'est pas valide";
        }
    }
}
