package com.restaurant.view;

import com.restaurant.controller.LoginController;
import com.restaurant.utils.DesignSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter; // Pour simplifier l'écoute du clavier
import java.awt.event.KeyEvent; // Pour identifier les touches 

public class LoginView extends JFrame {

    private LoginController controller;
    private JTextField txtNomUtil, txtNomUtilNew;
    private JPasswordField txtMotDePasse, txtMotDePasseNew, txtConfirmationMdp;
    private JButton btnConnexion, btnCreerCompte, btnValiderCreation, btnAnnulerCreation;
    private JLabel lblMessage;
    private JCheckBox chkSeSouvenir;
    private JPanel panelConnexion, panelCreation, panelCards;

    public LoginView(LoginController controller) {
        this.controller = controller;
        setTitle("Gestion Restaurant - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(true);

        initComposants();
        initLayout();
        initListeners();
    }

    private void initComposants() {
        // --- Panneau Connexion ---
        panelConnexion = new JPanel(new GridBagLayout());
        panelConnexion.setBackground(Color.WHITE);
        panelConnexion.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        txtNomUtil = new JTextField(15);
        txtMotDePasse = new JPasswordField(15);
        chkSeSouvenir = new JCheckBox("Se souvenir de moi");
        btnConnexion = new JButton("Se connecter");
        btnCreerCompte = new JButton("Créer un compte");
        btnCreerCompte.setVisible(false);

        DesignSystem.styleTextField(txtNomUtil);
        DesignSystem.styleTextField(txtMotDePasse);
        DesignSystem.styleButton(btnConnexion);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
        titleLabel.setFont(DesignSystem.FONT_TITLE);
        panelConnexion.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panelConnexion.add(new JLabel("Identifiant :"), gbc);
        gbc.gridy++;
        panelConnexion.add(txtNomUtil, gbc);
        gbc.gridy++;
        panelConnexion.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridy++;
        panelConnexion.add(txtMotDePasse, gbc);
        gbc.gridy++;
        panelConnexion.add(chkSeSouvenir, gbc);
        gbc.gridy++;
        panelConnexion.add(btnConnexion, gbc);
        gbc.gridy++;
        panelConnexion.add(btnCreerCompte, gbc);

        // --- Panneau Création ---
        panelCreation = new JPanel(new GridBagLayout());
        panelCreation.setBackground(Color.WHITE);
        panelCreation.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        txtNomUtilNew = new JTextField(15);
        txtMotDePasseNew = new JPasswordField(15);
        txtConfirmationMdp = new JPasswordField(15);
        btnValiderCreation = new JButton("Créer l'Admin");
        btnAnnulerCreation = new JButton("Annuler");

        DesignSystem.styleTextField(txtNomUtilNew);
        DesignSystem.styleTextField(txtMotDePasseNew);
        DesignSystem.styleTextField(txtConfirmationMdp);
        DesignSystem.styleButton(btnValiderCreation, DesignSystem.SUCCESS);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(8, 0, 8, 0);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.gridx = 0;
        gbc2.gridy = 0;

        JLabel titleCreate = new JLabel("Créer un Administrateur", SwingConstants.CENTER);
        titleCreate.setFont(DesignSystem.FONT_TITLE);
        panelCreation.add(titleCreate, gbc2);

        gbc2.gridy++;
        panelCreation.add(new JLabel("Identifiant :"), gbc2);
        gbc2.gridy++;
        panelCreation.add(txtNomUtilNew, gbc2);
        gbc2.gridy++;
        panelCreation.add(new JLabel("Mot de passe :"), gbc2);
        gbc2.gridy++;
        panelCreation.add(txtMotDePasseNew, gbc2);
        gbc2.gridy++;
        panelCreation.add(new JLabel("Confirmer le mot de passe :"), gbc2);
        gbc2.gridy++;
        panelCreation.add(txtConfirmationMdp, gbc2);
        gbc2.gridy++;
        panelCreation.add(btnValiderCreation, gbc2);
        gbc2.gridy++;
        panelCreation.add(btnAnnulerCreation, gbc2);

        lblMessage = new JLabel(" ");
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        panelCards = new JPanel(new CardLayout());
        panelCards.add(panelConnexion, "CONNEXION");
        panelCards.add(panelCreation, "CREATION");
        add(panelCards, BorderLayout.CENTER);
        add(lblMessage, BorderLayout.SOUTH);
    }

    private void initListeners() {
        // Actions des boutons
        btnConnexion.addActionListener(e -> seConnecter());
        btnCreerCompte.addActionListener(e -> afficherPanelCreation());
        btnAnnulerCreation.addActionListener(e -> afficherPanelConnexion());
        btnValiderCreation.addActionListener(e -> validerCreation());

        // Gestion de la touche Entrée sur le panneau de Connexion
        txtMotDePasse.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    seConnecter();
                }
            }
        });

        // Gestion de la touche Entrée sur le panneau de Création
        txtConfirmationMdp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    validerCreation();
                }
            }
        });
    }

    private void seConnecter() {
        String nom = txtNomUtil.getText().trim();
        String mdp = new String(txtMotDePasse.getPassword());
        controller.seConnecter(nom, mdp);
    }

    private void validerCreation() {
        String nom = txtNomUtilNew.getText().trim();
        String mdp = new String(txtMotDePasseNew.getPassword());
        String conf = new String(txtConfirmationMdp.getPassword());
        if (controller.creerCompte(nom, mdp, conf)) {
            afficherPanelConnexion();
        }
    }

    public void activerModePremierLancement(boolean actif) {
        btnCreerCompte.setVisible(actif);
        if (actif) {
            afficherErreur("Base vide : Créez un compte Administrateur");
        }
    }

    public void afficherPanelConnexion() {
        ((CardLayout) panelCards.getLayout()).show(panelCards, "CONNEXION");
        txtNomUtil.requestFocus();
    }

    public void afficherPanelCreation() {
        ((CardLayout) panelCards.getLayout()).show(panelCards, "CREATION");
        txtNomUtilNew.requestFocus();
    }

    public void afficherErreur(String msg) {
        lblMessage.setText(msg);
        lblMessage.setForeground(DesignSystem.DANGER);
    }

    public void afficherMessage(String msg) {
        lblMessage.setText(msg);
        lblMessage.setForeground(DesignSystem.SUCCESS);
    }

    // Getters et Setters pour le Controller
    public void setNomUtil(String nom) {
        txtNomUtil.setText(nom);
    }

    public void setSeSouvenir(boolean b) {
        chkSeSouvenir.setSelected(b);
    }

    public boolean isSeSouvenir() {
        return chkSeSouvenir.isSelected();
    }
}