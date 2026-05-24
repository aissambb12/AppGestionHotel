package com.hotel.vue;

import com.hotel.model.Utilisateur;

import javax.swing.*;
        import java.awt.*;

public class DashboardReceptionnisteFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;

    public DashboardReceptionnisteFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;

        setTitle("Hotel Manager - Réception");
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

        // === PANEL PRINCIPAL AVEC ICÔNES ===
        JPanel panelPrincipal = creerPanelIcones();
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🛎️ RÉCEPTION");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        JLabel lblUtilisateur = new JLabel("👤 " + receptionnisteConnecte.getNom());
        lblUtilisateur.setFont(ThemeUtil.POLICE_NORMALE);
        lblUtilisateur.setForeground(ThemeUtil.BLANC);

        JButton btnDeconnexion = new JButton("🚪 Déconnexion");
        btnDeconnexion.setBackground(ThemeUtil.ROUGE_ERREUR);
        btnDeconnexion.setForeground(ThemeUtil.BLANC);
        btnDeconnexion.setFont(ThemeUtil.POLICE_BOUTON);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setOpaque(true);
        btnDeconnexion.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnDeconnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeconnexion.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(lblUtilisateur, BorderLayout.CENTER);
        panel.add(btnDeconnexion, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelIcones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // === BOUTON 1 : CLIENTS ===
        JPanel btnClients = creerBoutonIcone("👥", "GESTION DES\nCLIENTS", ThemeUtil.BLEU_NUIT);
        btnClients.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new GestionClientsFrame(receptionnisteConnecte).setVisible(true);
                dispose();
            }
        });
        panel.add(btnClients);

        // === BOUTON 2 : RÉSERVATIONS ===
        JPanel btnReservations = creerBoutonIcone("📝", "CRÉER UNE\nRÉSERVATION", new Color(52, 152, 219));
        btnReservations.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new CreerReservationFrame(receptionnisteConnecte).setVisible(true);
                dispose();
            }
        });
        panel.add(btnReservations);

        // === BOUTON 3 : CHECK-IN/OUT ===
        JPanel btnCheckInOut = creerBoutonIcone("🔑", "CHECK-IN &\nCHECK-OUT", new Color(46, 204, 113));
        btnCheckInOut.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new GestionCheckFrame(receptionnisteConnecte).setVisible(true);
                dispose();
            }
        });
        panel.add(btnCheckInOut);

        return panel;
    }

    private JPanel creerBoutonIcone(String icone, String texte, Color couleur) {
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

        // Icône
        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("Arial", Font.PLAIN, 64));
        gbc.gridx = 0;
        gbc.gridy = 0;
        btn.add(lblIcone, gbc);

        // Texte
        JLabel lblTexte = new JLabel("<html><center>" + texte + "</center></html>");
        lblTexte.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTexte.setForeground(ThemeUtil.BLANC);
        gbc.gridy = 1;
        btn.add(lblTexte, gbc);

        // Effet hover
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