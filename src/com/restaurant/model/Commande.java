package com.restaurant.model;

import com.restaurant.model.enums.EtatCommande;
import java.time.LocalDate;

/**
 * Classe représentant une commande client
 */
public class Commande {
    private int idCmde;
    private LocalDate date;
    private EtatCommande etat;
    private double total;
    
    // Constructeurs
    public Commande() {
        this.etat = EtatCommande.EN_COURS;
        this.total = 0.0;
        this.date = LocalDate.now();
    }
    
    public Commande(int idCmde, LocalDate date, EtatCommande etat, double total) {
        this.idCmde = idCmde;
        this.date = date;
        this.etat = etat;
        this.total = total;
    }
    
    public Commande(LocalDate date, EtatCommande etat, double total) {
        this.date = date;
        this.etat = etat;
        this.total = total;
    }
    
    // Getters et Setters
    public int getIdCmde() {
        return idCmde;
    }
    
    public void setIdCmde(int idCmde) {
        this.idCmde = idCmde;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public EtatCommande getEtat() {
        return etat;
    }
    
    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        return "Commande #" + idCmde + " - " + date + " - " + etat + " - " + total + "€";
    }
}
