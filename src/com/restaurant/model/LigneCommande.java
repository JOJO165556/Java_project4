package com.restaurant.model;

public class LigneCommande {
    private int idLig;
    private int idCmde;
    private int idPro;
    private int qteLig;
    private double prixUnit;
    private double montant; // Non stocké en BD, calculé dynamiquement
    private Produit produit;

    public LigneCommande() {
    }

    // Constructeur complet
    public LigneCommande(int idLig, int idCmde, int idPro, int qteLig, double prixUnit) {
        this.idLig = idLig;
        this.idCmde = idCmde;
        this.idPro = idPro;
        this.qteLig = qteLig;
        this.prixUnit = prixUnit;
        calculerMontant();
    }

    // Constructeur sans ID (pour création)
    public LigneCommande(int idCmde, int idPro, int qteLig, double prixUnit) {
        this.idCmde = idCmde;
        this.idPro = idPro;
        this.qteLig = qteLig;
        this.prixUnit = prixUnit;
        calculerMontant();
    }

    // Constructeur basé sur un objet Produit
    public LigneCommande(Produit produit, int quantite) {
        this.produit = produit;
        if (produit != null) {
            this.idPro = produit.getIdPro();
            this.prixUnit = produit.getPrixVente();
        }
        this.qteLig = quantite;
        calculerMontant();
    }

    // Recalcule le montant total de la ligne
    private void calculerMontant() {
        this.montant = this.qteLig * this.prixUnit;
    }

    public int getIdLig() { return idLig; }
    public void setIdLig(int idLig) { this.idLig = idLig; }

    public int getIdCmde() { return idCmde; }
    public void setIdCmde(int idCmde) { this.idCmde = idCmde; }

    public int getIdPro() { return idPro; }
    public void setIdPro(int idPro) { this.idPro = idPro; }

    public int getQteLig() { return qteLig; }
    // Met à jour la quantité et recalcule
    public void setQteLig(int qteLig) {
        this.qteLig = qteLig;
        calculerMontant();
    }

    public double getPrixUnit() { return prixUnit; }
    // Met à jour le prix et recalcule
    public void setPrixUnit(double prixUnit) {
        this.prixUnit = prixUnit;
        calculerMontant();
    }

    public double getMontant() { return montant; }

    public Produit getProduit() { return produit; }
    // Lie un produit et synchronise les données
    public void setProduit(Produit produit) {
        this.produit = produit;
        if (produit != null) {
            this.idPro = produit.getIdPro();
            this.prixUnit = produit.getPrixVente();
            calculerMontant();
        }
    }

    @Override
    public String toString() {
        String nom = (produit != null) ? produit.getNomPro() : "Produit #" + idPro;
        return String.format("%s - %d x %.2f = %.2f", nom, qteLig, prixUnit, montant);
    }
}