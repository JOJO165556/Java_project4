package com.restaurant.view;

import com.restaurant.model.Utilisateur;
import com.restaurant.service.StatistiqueService;
import com.restaurant.service.StatistiqueService.StatistiquesGenerales;
import com.restaurant.utils.DesignSystem;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

// Vue principale (Dashboard et Sidebar)
public class MainView extends JFrame {

    private void defineIcon() {
        try {
            // Icône personnalisée si disponible
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
            if (icon.getImage() != null) setIconImage(icon.getImage());
        } catch (Exception e) {
            // Pas d'icône trouvée
        }
    }

    private Utilisateur utilisateurConnecte;
    private StatistiqueService statistiqueService;
    private JPanel panelContent;
    
    // Widgets Dashboard et Statut
    private JLabel lblCA, lblCommandes, lblAlerte, lblTotalProduits;
    private JLabel lblStatus;

    public MainView(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.statistiqueService = new StatistiqueService();
        
        setupFrame();
        defineIcon();
        initLayout();
        showView("Dashboard");
    }

    private void setupFrame() {
        setTitle("Gestion Restaurant - Accueil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        // Navigation latérale (Sidebar)
        add(createSidebar(), BorderLayout.WEST);

        // Contenu principal (CardLayout)
        panelContent = new JPanel(new CardLayout());
        
        panelContent.add(createDashboardPanel(), "Dashboard");
        panelContent.add(new CommandeView(), "Commandes");
        panelContent.add(new ProduitView(), "Produits");
        panelContent.add(new StockView(), "Stocks");
        panelContent.add(new StatistiqueView(), "Statistiques");

        add(panelContent, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(DesignSystem.PRIMARY);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblLogo = new JLabel("GESTION RESTAU");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(30));

        addSidebarButton(sidebar, "🏠 Accueil", "Dashboard");
        addSidebarButton(sidebar, "💰 Commandes", "Commandes");
        addSidebarButton(sidebar, "📦 Produits", "Produits");
        addSidebarButton(sidebar, "📦 Gestion Stock", "Stocks");
        addSidebarButton(sidebar, "📉 Statistiques", "Statistiques");
        
        sidebar.add(Box.createVerticalGlue());

        sidebar.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Se déconnecter");
        DesignSystem.styleButton(btnLogout, DesignSystem.DANGER);
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.addActionListener(e -> seDeconnecter());
        sidebar.add(btnLogout);

        return sidebar;
    }

    private void addSidebarButton(JPanel sidebar, String text, String viewName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        DesignSystem.styleButton(btn, DesignSystem.PRIMARY);
        btn.addActionListener(e -> showView(viewName));
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }

    // Panel Dashboard
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(DesignSystem.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header du dashboard
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Tableau de Bord");
        DesignSystem.styleLabel(title, true);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        // Grille de widgets
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.add(createWidget("Chiffre d'Affaires", lblCA = new JLabel("0.00 F CFA"), DesignSystem.PRIMARY));
        grid.add(createWidget("Commandes effectuées", lblCommandes = new JLabel("0"), DesignSystem.SECONDARY));
        grid.add(createWidget("Alertes Stock", lblAlerte = new JLabel("0"), DesignSystem.DANGER));
        grid.add(createWidget("Produits au Total", lblTotalProduits = new JLabel("0"), DesignSystem.SUCCESS));
        panel.add(grid, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createWidget(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(DesignSystem.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel lblT = new JLabel(title);
        lblT.setFont(DesignSystem.FONT_SUBTITLE);
        lblT.setForeground(DesignSystem.TEXT_MUTED);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        valueLabel.setForeground(color);
        card.add(lblT, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void rafraichirDashboard() {
        setStatus("Actualisation...", new Color(245, 158, 11)); // Orange
        Timer timer = new Timer(500, e -> {
            try {
                StatistiquesGenerales stats = statistiqueService.getStatistiquesGenerales();
                lblCA.setText(String.format("%.2f F CFA", stats.getCaJour()));
                lblCommandes.setText(String.valueOf(stats.getNbCommandesJour()));
                lblAlerte.setText(String.valueOf(stats.getNbProduitsSousSeuil()));
                lblTotalProduits.setText(String.valueOf(stats.getNbProduits()));
                setStatus("Système prêt", DesignSystem.SUCCESS);
            } catch (SQLException ex) {
                System.err.println("Erreur SQL Dashboard : " + ex.getMessage());
                setStatus("Erreur Base de données", DesignSystem.DANGER);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void setStatus(String message, Color color) {
        if (lblStatus != null) {
            lblStatus.setText("● " + message);
            lblStatus.setForeground(color);
        }
    }

    private void showView(String nom) {
        CardLayout cl = (CardLayout) panelContent.getLayout();
        cl.show(panelContent, nom);
        setTitle("Gestion Restaurant - " + nom);
        
        // Rafraîchir automatiquement si on revient sur le Dashboard
        if ("Dashboard".equals(nom)) {
            rafraichirDashboard();
        }
    }

    private void seDeconnecter() {
        int rep = JOptionPane.showConfirmDialog(this, "Confirmer la déconnexion ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
        if (rep == JOptionPane.YES_OPTION) {
            dispose();
            new com.restaurant.controller.LoginController().afficherLogin();
        }
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        bar.setPreferredSize(new Dimension(getWidth(), 30));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        left.setOpaque(false);
        lblStatus = new JLabel("● Système prêt");
        lblStatus.setForeground(DesignSystem.SUCCESS);
        lblStatus.setFont(DesignSystem.FONT_BODY);
        left.add(lblStatus);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        right.setOpaque(false);
        JLabel lblUser = new JLabel("👤 " + utilisateurConnecte.getNomUtil());
        lblUser.setFont(DesignSystem.FONT_BODY);
        lblUser.setForeground(DesignSystem.TEXT_MUTED);
        
        JLabel lblDate = new JLabel("📅 " + LocalDate.now());
        lblDate.setFont(DesignSystem.FONT_BODY);
        lblDate.setForeground(DesignSystem.TEXT_MUTED);

        right.add(lblUser);
        right.add(new JLabel("|"));
        right.add(lblDate);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        return bar;
    }
}
