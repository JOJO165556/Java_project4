package com.restaurant.controller;

import com.restaurant.model.Commande;
import com.restaurant.model.LigneCommande;
import com.restaurant.model.Produit;
import com.restaurant.model.enums.EtatCommande;
import com.restaurant.service.CommandeService;
import com.restaurant.service.ProduitService;
import com.restaurant.view.CommandeView;
import java.sql.SQLException;
import java.util.List;

public class CommandeController {

    private CommandeView commandeView;
    private CommandeService commandeService;
    private ProduitService produitService;
    private Commande commandeEnCours;

    public CommandeController() {
        this.commandeService = new CommandeService();
        this.produitService = new ProduitService();
    }

    public void setView(CommandeView view) {
        this.commandeView = view;
    }

    public boolean creerNouvelleCommande() {
        try {
            commandeEnCours = commandeService.creerCommande();
            if (commandeView != null) {
                commandeView.afficherCommande(commandeEnCours);
                commandeView.viderLignes();
                commandeView.afficherMessage("Nouvelle commande créée avec succès");
            }
            return true;
        } catch (Exception e) {
            afficherErreur("Erreur lors de la création de la commande : " + e.getMessage());
            return false;
        }
    }

    public boolean ajouterProduit(int idProduit, int quantite) {
        if (commandeEnCours == null) {
            afficherErreur("Veuillez d'abord créer une nouvelle commande");
            return false;
        }
        try {
            boolean ok = commandeService.ajouterProduit(commandeEnCours.getIdCmde(), idProduit, quantite);
            if (ok) {
                rafraichirAffichageCommande();
                if (commandeView != null) commandeView.afficherMessage("Produit ajouté à la commande");
            }
            return ok;
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'ajout du produit : " + e.getMessage());
            return false;
        }
    }

    public boolean modifierQuantiteLigne(int idLigne, int nouvelleQuantite) {
        if (commandeEnCours == null) return false;
        try {
            boolean ok = commandeService.modifierQuantiteLigne(idLigne, nouvelleQuantite);
            if (ok) {
                rafraichirAffichageCommande();
                if (commandeView != null) commandeView.afficherMessage("Quantité modifiée avec succès");
            }
            return ok;
        } catch (Exception e) {
            afficherErreur("Erreur lors de la modification : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerLigne(int idLigne) {
        if (commandeEnCours == null) return false;
        try {
            boolean ok = commandeService.supprimerLigne(idLigne);
            if (ok) {
                rafraichirAffichageCommande();
                if (commandeView != null) commandeView.afficherMessage("Ligne supprimée avec succès");
            }
            return ok;
        } catch (Exception e) {
            afficherErreur("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }

    public boolean validerCommande() {
        if (commandeEnCours == null) {
            afficherErreur("Aucune commande en cours");
            return false;
        }
        try {
            boolean ok = commandeService.validerCommande(commandeEnCours.getIdCmde());
            if (ok) {
                commandeEnCours = commandeService.getCommande(commandeEnCours.getIdCmde());
                if (commandeView != null) {
                    commandeView.afficherCommande(commandeEnCours);
                    commandeView.afficherMessage("Commande validée avec succès !");
                    commandeView.desactiverModification();
                }
            }
            return ok;
        } catch (Exception e) {
            afficherErreur("Erreur lors de la validation : " + e.getMessage());
            return false;
        }
    }

    public boolean annulerCommande() {
        if (commandeEnCours == null) {
            afficherErreur("Aucune commande en cours");
            return false;
        }
        try {
            boolean ok = commandeService.annulerCommande(commandeEnCours.getIdCmde());
            if (ok) {
                commandeEnCours = commandeService.getCommande(commandeEnCours.getIdCmde());
                if (commandeView != null) {
                    commandeView.afficherCommande(commandeEnCours);
                    commandeView.afficherMessage("Commande annulée avec succès");
                    commandeView.desactiverModification();
                }
            }
            return ok;
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'annulation : " + e.getMessage());
            return false;
        }
    }

    public boolean chargerCommande(int idCommande) {
        try {
            Commande commande = commandeService.getCommande(idCommande);
            if (commande == null) {
                afficherErreur("Commande non trouvée");
                return false;
            }
            commandeEnCours = commande;
            if (commandeView != null) {
                commandeView.afficherCommande(commande);
                commandeView.afficherLignes(commandeService.getLignesCommande(idCommande));
                if (commande.getEtat() != EtatCommande.EN_COURS)
                    commandeView.desactiverModification();
                else
                    commandeView.activerModification();
            }
            return true;
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement : " + e.getMessage());
            return false;
        }
    }

    public List<Produit> getProduits() {
        try {
            return produitService.getAllProduits();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement des produits : " + e.getMessage());
            return null;
        }
    }

    public List<Commande> getCommandes() {
        try {
            return commandeService.getAllCommandes();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement des commandes : " + e.getMessage());
            return null;
        }
    }

    public Commande getCommandeEnCours() {
        return commandeEnCours;
    }

    // Recharge la commande et ses lignes depuis la base pour mettre à jour l'affichage
    private void rafraichirAffichageCommande() {
        if (commandeEnCours == null || commandeView == null) return;
        try {
            commandeEnCours = commandeService.getCommande(commandeEnCours.getIdCmde());
            commandeView.afficherCommande(commandeEnCours);
            commandeView.afficherLignes(commandeService.getLignesCommande(commandeEnCours.getIdCmde()));
        } catch (Exception e) {
            commandeView.afficherErreur("Erreur lors du rafraîchissement : " + e.getMessage());
        }
    }

    private void afficherErreur(String msg) {
        if (commandeView != null) commandeView.afficherErreur(msg);
    }
}
