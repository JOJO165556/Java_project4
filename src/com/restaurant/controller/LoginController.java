package com.restaurant.controller;

import com.restaurant.model.Utilisateur;
import com.restaurant.service.AuthService;
import com.restaurant.view.LoginView;
import com.restaurant.view.MainView;
import com.restaurant.model.enums.Role;
import java.sql.SQLException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private LoginView loginView;
    private AuthService authService;
    private Utilisateur utilisateurConnecte;

    // Initialisation du contrôleur et vérification de l'état initial
    public LoginController() {
        this.authService = new AuthService(new com.restaurant.dao.UtilisateurDAO());
        this.loginView = new LoginView(this);
        chargerPreference();
        verifierEtatBase();
    }

    // Vérifie si la base est vide pour autoriser la création du compte Admin
    private void verifierEtatBase() {
        try {
            if (authService.isBaseVide()) {
                logger.info("Base de données vide : activation du mode création administrateur");
                loginView.activerModePremierLancement(true);
            } else {
                loginView.activerModePremierLancement(false);
            }
        } catch (SQLException e) {
            logger.error("Erreur critique base de données: " + e.getMessage());
            loginView.afficherErreur("ERREUR BASE : Table manquante ou inaccessible");
            JOptionPane.showMessageDialog(loginView,
                    "Erreur critique de base de données :\n" + e.getMessage() +
                            "\n\nVérifiez que le fichier gestion_restaurant.db est présent dans le dossier data.",
                    "Erreur Système", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Récupère le dernier utilisateur ayant coché "Se souvenir de moi"
    private void chargerPreference() {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        String savedUser = prefs.get("remembered_user", "");
        if (!savedUser.isEmpty()) {
            loginView.setNomUtil(savedUser);
            loginView.setSeSouvenir(true);
        }
    }

    // Affiche la fenêtre principale de connexion
    public void afficherLogin() {
        loginView.setVisible(true);
    }

    // Gère la tentative d'authentification
    public boolean seConnecter(String nomUtil, String motDePasse) {
        try {
            if (nomUtil.isEmpty() || motDePasse.isEmpty()) {
                loginView.afficherErreur("Identifiants requis");
                return false;
            }

            utilisateurConnecte = authService.authenticate(nomUtil, motDePasse);

            if (utilisateurConnecte != null) {
                // Gestion de la préférence de mémorisation
                Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
                if (loginView.isSeSouvenir()) {
                    prefs.put("remembered_user", nomUtil);
                } else {
                    prefs.remove("remembered_user");
                }

                loginView.setVisible(false);
                logger.info("Session ouverte pour : " + nomUtil);

                // Lancement de l'interface principale après succès
                new MainView(utilisateurConnecte).setVisible(true);
                return true;
            } else {
                loginView.afficherErreur("Nom d'utilisateur ou mot de passe incorrect");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la connexion : " + e.getMessage());
            loginView.afficherErreur("Erreur technique de connexion");
            return false;
        }
    }

    // Gère la création du compte initial (Admin) ou des comptes employés
    public boolean creerCompte(String nomUtil, String motDePasse, String confirmation) {
        try {
            // Validation des champs
            if (nomUtil.isEmpty() || motDePasse.isEmpty()) {
                loginView.afficherErreur("Tous les champs sont obligatoires");
                return false;
            }
            if (!motDePasse.equals(confirmation)) {
                loginView.afficherErreur("Les mots de passe ne correspondent pas");
                return false;
            }
            if (motDePasse.length() < 4) {
                loginView.afficherErreur("Le mot de passe est trop court (min 4)");
                return false;
            }

            // Détermine le rôle automatiquement : ADMIN si base vide, sinon CAISSIER
            boolean premierUtilisateur = authService.isBaseVide();
            Role roleAttribue = premierUtilisateur ? Role.ADMIN : Role.CAISSIER;

            boolean succes = authService.creerUtilisateur(nomUtil, motDePasse, roleAttribue);

            if (succes) {
                loginView.afficherMessage("Compte " + roleAttribue + " créé avec succès");
                // Met à jour l'interface pour masquer l'option de création
                verifierEtatBase();
                return true;
            }
            return false;

        } catch (SQLException e) {
            // Gestion spécifique du doublon de nom (Contrainte UNIQUE en SQL)
            String msg = e.getMessage().contains("UNIQUE") ? "Ce nom est déjà utilisé" : e.getMessage();
            loginView.afficherErreur(msg);
            return false;
        }
    }

    // Accesseur pour récupérer l'utilisateur actuellement loggé
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}