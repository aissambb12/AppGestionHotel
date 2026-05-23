package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.Role;
import com.hotel.service.UtilisateurService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private UtilisateurService utilisateurService;

    // Composants de l'interface
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse;
    private JButton btnConnexion;

    public LoginFrame() {
        this.utilisateurService = new UtilisateurService();

        // Configuration de la fenêtre principale
        setTitle("Hotel Manager - Connexion");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer sur l'écran
        setResizable(false);

        initialiserComposants();
    }

    private void initialiserComposants() {
        // Layout principal séparé en deux : Image à gauche, Formulaire à droite
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // --- PANEL GAUCHE (Image / Logo) ---
        JPanel panelImage = new JPanel(new BorderLayout());
        panelImage.setBackground(ThemeUtil.BLEU_NUIT);
        panelImage.setPreferredSize(new Dimension(300, 450));

        // Essayez de charger l'image (si elle n'existe pas, affiche juste un texte)
        try {
            // Remplacez le chemin par celui de votre dossier d'images
            ImageIcon iconeHotel = new ImageIcon(getClass().getResource("/images/hotel_logo.png"));
            JLabel labelImage = new JLabel(iconeHotel);
            panelImage.add(labelImage, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel labelTitreHotel = new JLabel("GRAND HÔTEL", SwingConstants.CENTER);
            labelTitreHotel.setForeground(ThemeUtil.DORE_LUXE);
            labelTitreHotel.setFont(ThemeUtil.POLICE_TITRE);
            panelImage.add(labelTitreHotel, BorderLayout.CENTER);
        }

        // --- PANEL DROITE (Formulaire de connexion) ---
        JPanel panelFormulaire = new JPanel(new GridBagLayout());
        panelFormulaire.setBackground(ThemeUtil.GRIS_FOND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre "Bienvenue"
        JLabel lblTitre = new JLabel("Bienvenue");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelFormulaire.add(lblTitre, gbc);

        // Champ Email
        gbc.gridwidth = 1; gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email professionnel :");
        lblEmail.setFont(ThemeUtil.POLICE_NORMALE);
        panelFormulaire.add(lblEmail, gbc);

        gbc.gridy = 2;
        txtEmail = new JTextField(20);
        txtEmail.setFont(ThemeUtil.POLICE_NORMALE);
        panelFormulaire.add(txtEmail, gbc);

        // Champ Mot de passe
        gbc.gridy = 3;
        JLabel lblMotDePasse = new JLabel("Mot de passe :");
        lblMotDePasse.setFont(ThemeUtil.POLICE_NORMALE);
        panelFormulaire.add(lblMotDePasse, gbc);

        gbc.gridy = 4;
        txtMotDePasse = new JPasswordField(20);
        txtMotDePasse.setFont(ThemeUtil.POLICE_NORMALE);
        panelFormulaire.add(txtMotDePasse, gbc);

        // Bouton Connexion
        gbc.gridy = 5; gbc.insets = new Insets(30, 10, 10, 10);
        btnConnexion = new JButton("Se connecter");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnConnexion); // Utilisation de notre thème !
        panelFormulaire.add(btnConnexion, gbc);

        // --- ACTIONS DU BOUTON ---
        btnConnexion.addActionListener(e -> gererConnexion());

        // Assemblage final
        panelPrincipal.add(panelImage, BorderLayout.WEST);
        panelPrincipal.add(panelFormulaire, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private void gererConnexion() {
        String email = txtEmail.getText();
        String motDePasse = new String(txtMotDePasse.getPassword());

        try {
            // Appel à notre couche métier (Service d'authentification)
            Utilisateur utilisateurConnecte = utilisateurService.seConnecter(email, motDePasse);

            if (utilisateurConnecte != null) {
                // REDIRECTION STRICTE SELON LE RÔLE (Contrôle d'accès RBAC)
                switch (utilisateurConnecte.getRole()) {
                    case ADMIN:
                        JOptionPane.showMessageDialog(this, "Accès Master - Bienvenue Directeur " + utilisateurConnecte.getNom());
                        new DashboardAdminFrame(utilisateurConnecte).setVisible(true); // Vue Full Accès
                        break;

                    case RECEPTIONNISTE:
                        JOptionPane.showMessageDialog(this, "Accès Accueil - Bienvenue Réceptionniste " + utilisateurConnecte.getNom());
                        new DashboardReceptionnisteFrame(utilisateurConnecte).setVisible(true); // Vue Réservations/Clients
                        break;

                    case MAINTENANCE:
                        JOptionPane.showMessageDialog(this, "Accès Technique - Bienvenue Technicien " + utilisateurConnecte.getNom());
                        // new DashboardMaintenanceFrame(utilisateurConnecte).setVisible(true); // Vue Pannes/Travaux
                        break;

                    default:
                        JOptionPane.showMessageDialog(this, "Erreur : Rôle utilisateur non reconnu.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                }

                this.dispose(); // Ferme proprement la fenêtre de connexion

            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Attention", JOptionPane.WARNING_MESSAGE);
        }
    }


}