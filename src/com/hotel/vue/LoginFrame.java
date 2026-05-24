package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.Role;
import com.hotel.service.UtilisateurService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginFrame extends JFrame {

    private UtilisateurService utilisateurService;
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse;
    private JButton btnConnexion;
    private JLabel lblErreur;

    public LoginFrame() {
        this.utilisateurService = new UtilisateurService();

        setTitle("Hotel Manager - Connexion");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initialiserComposants();
    }

    private void initialiserComposants() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // === PANEL GAUCHE (Logo/Design) ===
        JPanel panelGauche = creerPanelGauche();

        // === PANEL DROITE (Formulaire) ===
        JPanel panelDroite = creerPanelDroite();

        panelPrincipal.add(panelGauche, BorderLayout.WEST);
        panelPrincipal.add(panelDroite, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private JPanel creerPanelGauche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setPreferredSize(new Dimension(350, 550));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Titre principal
        JLabel lblTitrePrincipal = new JLabel("GRAND HÔTEL");
        lblTitrePrincipal.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitrePrincipal.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitrePrincipal, gbc);

        // Sous-titre
        JLabel lblSousTitre = new JLabel("Système de Gestion");
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSousTitre.setForeground(new Color(180, 180, 180));
        gbc.gridy = 1;
        panel.add(lblSousTitre, gbc);

        // Ligne décorative
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(sep, gbc);

        // Icône/Message de bienvenue
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblWelcome = new JLabel("✓ Bienvenue");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblWelcome.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(lblWelcome, gbc);

        return panel;
    }

    private JPanel creerPanelDroite() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(new EmptyBorder(60, 60, 60, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === TITRE ===
        JLabel lblTitre = new JLabel("Connexion");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridy = 0;
        panel.add(lblTitre, gbc);

        // === SOUS-TITRE ===
        JLabel lblDesc = new JLabel("Entrez vos identifiants pour accéder au système");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 30, 0);
        panel.add(lblDesc, gbc);

        // === LABEL ERREUR (caché par défaut) ===
        lblErreur = new JLabel();
        lblErreur.setForeground(new Color(220, 53, 69));
        lblErreur.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(lblErreur, gbc);

        // === EMAIL ===
        JLabel lblEmail = new JLabel("Email professionnel");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtEmail.setBackground(Color.WHITE);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(txtEmail, gbc);

        // === MOT DE PASSE ===
        JLabel lblMotDePasse = new JLabel("Mot de passe");
        lblMotDePasse.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMotDePasse.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(lblMotDePasse, gbc);

        txtMotDePasse = new JPasswordField();
        txtMotDePasse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMotDePasse.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtMotDePasse.setBackground(Color.WHITE);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 35, 0);
        panel.add(txtMotDePasse, gbc);

        // === BOUTON CONNEXION ===
        btnConnexion = new JButton("Se connecter");
        btnConnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setBackground(ThemeUtil.DORE_LUXE);
        btnConnexion.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConnexion.setOpaque(true);
        btnConnexion.addActionListener(e -> gererConnexion());

        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(btnConnexion, gbc);

        // === HOVER EFFECT ===
        ajouterHoverEffet(btnConnexion, ThemeUtil.DORE_LUXE, new Color(190, 155, 35));

        // === RACCOURCI CLAVIER ===
        txtMotDePasse.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gererConnexion();
                }
            }
        });

        return panel;
    }

    private void ajouterHoverEffet(JButton btn, Color couleurNormale, Color couleurHover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleurHover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleurNormale);
            }
        });
    }

    private void gererConnexion() {
        String email = txtEmail.getText().trim();
        String motDePasse = new String(txtMotDePasse.getPassword()).trim();

        // Validation basique
        if (email.isEmpty() || motDePasse.isEmpty()) {
            lblErreur.setText("⚠ Email et mot de passe requis");
            return;
        }

        try {
            Utilisateur utilisateurConnecte = utilisateurService.seConnecter(email, motDePasse);

            if (utilisateurConnecte != null) {
                lblErreur.setText("");

                switch (utilisateurConnecte.getRole()) {
                    case ADMIN:
                        new DashboardAdminFrame(utilisateurConnecte).setVisible(true);
                        break;
                    case RECEPTIONNISTE:
                        new DashboardReceptionnisteFrame(utilisateurConnecte).setVisible(true);
                        break;
                    case MAINTENANCE:
                        new DashboardMaintenanceFrame(utilisateurConnecte).setVisible(true);
                        break;
                    default:
                        lblErreur.setText("⚠ Rôle non reconnu");
                        return;
                }

                this.dispose();
            } else {
                lblErreur.setText("⚠ Email ou mot de passe incorrect");
                txtMotDePasse.setText("");
            }
        } catch (Exception ex) {
            lblErreur.setText("⚠ Erreur : " + ex.getMessage());
        }
    }
}