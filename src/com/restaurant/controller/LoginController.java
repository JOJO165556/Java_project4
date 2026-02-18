package com.restaurant.controller;

import com.restaurant.model.Utilisateur;
import com.restaurant.service.AuthService;
import com.restaurant.view.LoginView;
import com.restaurant.view.MainView;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class LoginController {

    private LoginView loginView;
    private AuthService authService;
    private Utilisateur utilisateurConnecte;

    public LoginController() {
        this.authService = new AuthService();
        this.loginView = new LoginView(this);
        chargerPreference();
    }

    private void chargerPreference() {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        String savedUser = prefs.get("remembered_user", "");
        if (!savedUser.isEmpty()) {
            loginView.setNomUtil(savedUser);
            loginView.setSeSouvenir(true);
        }
    }

    // Affiche la fenêtre de connexion
    public void afficherLogin() {
        loginView.setVisible(true);
    }

    // Tente d'authentifier l'utilisateur
    public boolean seConnecter(String nomUtil, String motDePasse) {
        try {
            if (nomUtil.isEmpty()) {
                loginView.afficherErreur("Veuillez saisir un nom d'utilisateur");
                return false;
            }
            if (motDePasse.isEmpty()) {
                loginView.afficherErreur("Veuillez saisir un mot de passe");
                return false;
            }

            utilisateurConnecte = authService.authenticate(nomUtil, motDePasse);

            if (utilisateurConnecte != null) {
                // Gestion de la préférence "Se souvenir de moi"
                Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
                if (loginView.isSeSouvenir()) {
                    prefs.put("remembered_user", nomUtil);
                } else {
                    prefs.remove("remembered_user");
                }

                loginView.setVisible(false);
                new MainView(utilisateurConnecte).setVisible(true);
                return true;
            } else {
                loginView.afficherErreur("Identifiants incorrects");
                return false;
            }

        } catch (SQLException e) {
            loginView.afficherErreur("Erreur base de données : " + e.getMessage());
            return false;
        }
    }

    // Crée un nouveau compte utilisateur
    public boolean creerCompte(String nomUtil, String motDePasse, String confirmation) {
        try {
            if (nomUtil.isEmpty()) {
                loginView.afficherErreur("Veuillez saisir un nom d'utilisateur");
                return false;
            }
            if (motDePasse.isEmpty()) {
                loginView.afficherErreur("Veuillez saisir un mot de passe");
                return false;
            }
            if (!motDePasse.equals(confirmation)) {
                loginView.afficherErreur("Les mots de passe ne correspondent pas");
                return false;
            }
            if (motDePasse.length() < 4) {
                loginView.afficherErreur("Le mot de passe doit contenir au moins 4 caractères");
                return false;
            }

            boolean success = authService.creerUtilisateur(nomUtil, motDePasse);
            if (success) {
                loginView.afficherMessage("Compte créé ! Vous pouvez vous connecter.");
                return true;
            } else {
                loginView.afficherErreur("Erreur lors de la création du compte");
                return false;
            }

        } catch (SQLException e) {
            loginView.afficherErreur(e.getMessage().contains("existe") ?
                "Ce nom d'utilisateur existe déjà" : "Erreur : " + e.getMessage());
            return false;
        }
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}
