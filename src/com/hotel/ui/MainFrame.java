package com.hotel.ui;

import com.hotel.model.enumeration.Role;
import com.hotel.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private Utilisateur utilisateurConnecte;

    public MainFrame(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;

        setTitle("Application de gestion d'hôtel");
        setSize(1250, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(UITheme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BorderLayout());

        JLabel logo = new JLabel(
                "<html><div style='text-align:center;'>🏨<br>GESTION<br>HÔTEL</div></html>",
                SwingConstants.CENTER
        );
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(UITheme.SIDEBAR);
        menuPanel.setLayout(new GridLayout(12, 1, 0, 8));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        ajouterMenusSelonRole(menuPanel);

        sidebar.add(logo, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblBienvenue = new JLabel("Bienvenue, " + utilisateurConnecte.getNom());
        lblBienvenue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenue.setForeground(UITheme.TEXT_DARK);

        JLabel lblRole = new JLabel("Rôle : " + utilisateurConnecte.getRole());
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRole.setForeground(new Color(71, 85, 105));

        topBar.add(lblBienvenue, BorderLayout.WEST);
        topBar.add(lblRole, BorderLayout.EAST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(topBar, BorderLayout.NORTH);
        centerContainer.add(contentPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(centerContainer, BorderLayout.CENTER);

        afficherAccueil();
    }

    private void ajouterMenusSelonRole(JPanel menuPanel) {
        Role role = utilisateurConnecte.getRole();

        if (role == Role.ADMIN) {
            ajouterBouton(menuPanel, "Clients", new ClientPanel());
            ajouterBouton(menuPanel, "Chambres", new ChambrePanel());
            ajouterBouton(menuPanel, "Réservations", new ReservationPanel());
            ajouterBouton(menuPanel, "Check-in", new CheckInPanel());
            ajouterBouton(menuPanel, "Check-out", new CheckOutPanel());
            ajouterBouton(menuPanel, "Maintenance", new MaintenancePanel());
            ajouterBouton(menuPanel, "Plats", new PlatPanel());
            ajouterBouton(menuPanel, "Commandes restaurant", new CommandeRestaurantPanel());
            ajouterBouton(menuPanel, "Factures", new FacturePanel());
            ajouterBouton(menuPanel, "Paiements", new PaiementPanel());
        }

        else if (role == Role.RECEPTIONNISTE) {
            ajouterBouton(menuPanel, "Clients", new ClientPanel());
            ajouterBouton(menuPanel, "Chambres", new ChambrePanel());
            ajouterBouton(menuPanel, "Réservations", new ReservationPanel());
            ajouterBouton(menuPanel, "Check-in", new CheckInPanel());
            ajouterBouton(menuPanel, "Check-out", new CheckOutPanel());
            ajouterBouton(menuPanel, "Factures", new FacturePanel());
            ajouterBouton(menuPanel, "Paiements", new PaiementPanel());
        }

        else if (role == Role.MAINTENANCE) {
            ajouterBouton(menuPanel, "Chambres", new ChambrePanel());
            ajouterBouton(menuPanel, "Maintenance", new MaintenancePanel());
        }

        else if (role == Role.RESTAURANT) {
            ajouterBouton(menuPanel, "Plats", new PlatPanel());
            ajouterBouton(menuPanel, "Commandes restaurant", new CommandeRestaurantPanel());
        }

        JButton btnDeconnexion = UITheme.createSidebarButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        menuPanel.add(btnDeconnexion);
    }

    private void ajouterBouton(JPanel menuPanel, String titre, JPanel panel) {
        JButton button = UITheme.createSidebarButton(titre);

        button.addActionListener(e -> afficherPanel(panel));

        menuPanel.add(button);
    }

    private void afficherAccueil() {
        JPanel accueil = UITheme.createCardPanel();
        accueil.setLayout(new GridBagLayout());

        JLabel titre = new JLabel("Bienvenue dans l'application de gestion d'hôtel");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titre.setForeground(UITheme.PRIMARY_DARK);

        JLabel sousTitre = new JLabel("Sélectionnez un module depuis le menu à gauche.");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sousTitre.setForeground(new Color(100, 116, 139));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titre);
        textPanel.add(sousTitre);

        accueil.add(textPanel);

        afficherPanel(accueil);
    }

    private void afficherPanel(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}