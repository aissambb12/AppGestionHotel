package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;

public class DashboardMaintenanceFrame extends JFrame {

    private Utilisateur technicienConnecte;

    public DashboardMaintenanceFrame(Utilisateur technicien) {
        this.technicienConnecte = technicien;

        setTitle("Hotel Manager - Maintenance");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === PANEL PRINCIPAL ===
        JPanel panelPrincipal = creerPanelIcones();
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🔧 DÉPARTEMENT MAINTENANCE");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        JLabel lblUtilisateur = new JLabel("👤 " + technicienConnecte.getNom());
        lblUtilisateur.setFont(ThemeUtil.POLICE_NORMALE);
        lblUtilisateur.setForeground(ThemeUtil.BLANC);

        JButton btnDeconnexion = new JButton("🚪 Déconnexion");
        btnDeconnexion.setBackground(ThemeUtil.ROUGE_ERREUR);
        btnDeconnexion.setForeground(ThemeUtil.BLANC);
        btnDeconnexion.setFont(ThemeUtil.POLICE_BOUTON);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setOpaque(true);
        btnDeconnexion.setContentAreaFilled(true);
        btnDeconnexion.setBorderPainted(true);
        btnDeconnexion.setBorder(BorderFactory.createLineBorder(ThemeUtil.ROUGE_ERREUR, 1));
        btnDeconnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeconnexion.addActionListener(e -> NavigationManager.naviguerVers(this, new LoginFrame()));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(lblUtilisateur, BorderLayout.CENTER);
        panel.add(btnDeconnexion, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelIcones() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 30));
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // BOUTON 1 : INTERVENTIONS EN COURS
        /**
         * IMAGE À AJOUTER : interventions.png (64x64px)
         * Description: Icône d'une alerte rouge ou d'une clé à molette urgente
         */
        JPanel btnInterventions = creerBoutonIcone("🚨 INTERVENTIONS\nEN COURS", ThemeUtil.ROUGE_ERREUR);
        btnInterventions.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NavigationManager.naviguerVers(DashboardMaintenanceFrame.this, new InterventionsFrame(technicienConnecte));
            }
        });
        panel.add(btnInterventions);

        // BOUTON 2 : HISTORIQUE
        /**
         * IMAGE À AJOUTER : historique.png (64x64px)
         * Description: Icône d'une horloge ou d'un historique/archives
         */
        JPanel btnHistorique = creerBoutonIcone("📋 HISTORIQUE DES\nRÉPARATIONS", new Color(52, 152, 219));
        btnHistorique.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NavigationManager.naviguerVers(DashboardMaintenanceFrame.this, new HistoriqueFrame(technicienConnecte));
            }
        });
        panel.add(btnHistorique);

        return panel;
    }

    private JPanel creerBoutonIcone(String texte, Color couleur) {
        JPanel btn = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(couleur);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
            }
        };

        btn.setBackground(couleur);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTexte = new JLabel("<html><center>" + texte + "</center></html>");
        lblTexte.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTexte.setForeground(ThemeUtil.BLANC);
        gbc.gridx = 0;
        gbc.gridy = 0;
        btn.add(lblTexte, gbc);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color couleurOriginal = couleur;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(assombrir(couleurOriginal, 0.2f));
                btn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleurOriginal);
                btn.repaint();
            }
        });

        return btn;
    }

    private Color assombrir(Color couleur, float facteur) {
        return new Color(
                (int) (couleur.getRed() * (1 - facteur)),
                (int) (couleur.getGreen() * (1 - facteur)),
                (int) (couleur.getBlue() * (1 - facteur))
        );
    }
}