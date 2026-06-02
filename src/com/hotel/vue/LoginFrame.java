package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.UtilisateurService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private UtilisateurService utilisateurService;
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse;
    private JButton btnConnexion;
    private JLabel lblErreur;

    public LoginFrame() {
        this.utilisateurService = new UtilisateurService();

        setTitle("Hotel Manager - Connexion");
        setSize(1000, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
    }

    private void initialiserComposants() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        panelPrincipal.add(creerPanelGauche(), BorderLayout.WEST);
        panelPrincipal.add(creerPanelDroite(), BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        SwingUtilities.invokeLater(() -> txtEmail.requestFocusInWindow());
    }

    private JPanel creerPanelGauche() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setPreferredSize(new Dimension(380, 580));
        panel.setBorder(new EmptyBorder(80, 40, 40, 40));

        // Logo (image)
        ImageIcon logoIcone = IconLoader.charger("app_logo", 110);
        JLabel lblLogo;
        if (logoIcone != null) {
            lblLogo = new JLabel(logoIcone);
        } else {
            lblLogo = new JLabel("🏨");
            lblLogo.setFont(new Font("Arial", Font.PLAIN, 80));
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblLogo);
        panel.add(Box.createVerticalStrut(25));

        // Titre principal
        JLabel lblTitre = new JLabel("GRAND HÔTEL");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);
        lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitre);
        panel.add(Box.createVerticalStrut(8));

        // Sous-titre
        JLabel lblSousTitre = new JLabel("Système de Gestion");
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSousTitre.setForeground(new Color(180, 180, 180));
        lblSousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSousTitre);
        panel.add(Box.createVerticalStrut(30));

        // Séparateur doré
        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeUtil.DORE_LUXE);
        sep.setBackground(ThemeUtil.DORE_LUXE);
        sep.setMaximumSize(new Dimension(80, 2));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sep);
        panel.add(Box.createVerticalStrut(25));

        // Message bienvenue
        JLabel lblWelcome = new JLabel("Bienvenue");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblWelcome.setForeground(new Color(220, 220, 220));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblWelcome);

        return panel;
    }

    private JPanel creerPanelDroite() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(new EmptyBorder(50, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Titre
        JLabel lblTitre = new JLabel("Connexion");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(lblTitre, gbc);

        // Sous-titre
        JLabel lblDesc = new JLabel("Entrez vos identifiants pour accéder au système");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        panel.add(lblDesc, gbc);

        // Label erreur
        lblErreur = new JLabel(" ");
        lblErreur.setForeground(ThemeUtil.ROUGE_ERREUR);
        lblErreur.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(lblErreur, gbc);

        // Email
        JLabel lblEmail = new JLabel("Email professionnel");
        lblEmail.setFont(ThemeUtil.POLICE_LABEL);
        lblEmail.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtEmail);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(txtEmail, gbc);

        // Mot de passe
        JLabel lblMdp = new JLabel("Mot de passe");
        lblMdp.setFont(ThemeUtil.POLICE_LABEL);
        lblMdp.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(lblMdp, gbc);

        txtMotDePasse = new JPasswordField();
        ThemeUtil.appliquerThemeTextField(txtMotDePasse);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(txtMotDePasse, gbc);

        // Bouton connexion
        btnConnexion = new JButton("Se connecter");
        btnConnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setBackground(ThemeUtil.DORE_LUXE);
        btnConnexion.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setOpaque(true);
        btnConnexion.setContentAreaFilled(true);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        IconLoader.appliquerIcone(btnConnexion, "icon_check");
        btnConnexion.addActionListener(e -> gererConnexion());

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(btnConnexion, gbc);

        ajouterHoverEffet(btnConnexion, ThemeUtil.DORE_LUXE, new Color(190, 155, 35));

        // Entrée = connexion
        KeyAdapter enterKey = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) gererConnexion();
            }
        };
        txtEmail.addKeyListener(enterKey);
        txtMotDePasse.addKeyListener(enterKey);

        return panel;
    }

    private void ajouterHoverEffet(JButton btn, Color normale, Color hover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover); }
            @Override public void mouseExited(java.awt.event.MouseEvent evt)  { btn.setBackground(normale); }
        });
    }

    private void gererConnexion() {
        String email = txtEmail.getText().trim();
        String motDePasse = new String(txtMotDePasse.getPassword()).trim();

        if (email.isEmpty() || motDePasse.isEmpty()) {
            lblErreur.setText("Email et mot de passe requis");
            return;
        }

        try {
            Utilisateur u = utilisateurService.seConnecter(email, motDePasse);
            if (u == null) {
                lblErreur.setText("Email ou mot de passe incorrect");
                txtMotDePasse.setText("");
                return;
            }
            lblErreur.setText(" ");
            switch (u.getRole()) {
                case ADMIN:
                    NavigationManager.naviguerVers(this, new DashboardAdminFrame(u));
                    break;
                case RECEPTIONNISTE:
                    NavigationManager.naviguerVers(this, new DashboardReceptionnisteFrame(u));
                    break;
                case MAINTENANCE:
                    NavigationManager.naviguerVers(this, new DashboardMaintenanceFrame(u));
                    break;
                default:
                    lblErreur.setText("Rôle non reconnu");
            }
        } catch (Exception ex) {
            lblErreur.setText("Erreur : " + ex.getMessage());
        }
    }
}
