package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.Role;
import com.hotel.service.UtilisateurService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardAdminFrame extends JFrame {

    private Utilisateur adminConnecte;
    private UtilisateurService utilisateurService;

    // Composants pour la gestion du personnel
    private JTextField txtNom, txtPrenom, txtEmail;
    private JPasswordField txtMotDePasse;
    private JComboBox<Role> comboRole;
    private DefaultTableModel tableModelPersonnel;
    private JTable tablePersonnel;

    public DashboardAdminFrame(Utilisateur admin) {
        this.adminConnecte = admin;
        this.utilisateurService = new UtilisateurService();

        setTitle("Hotel Manager - Espace Administration (" + admin.getNom() + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        chargerDonneesPersonnel(); // Charger la table au démarrage
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // --- EN-TÊTE (Header) ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(ThemeUtil.BLEU_NUIT);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("TABLEAU DE BORD ADMINISTRATEUR");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setBackground(Color.RED);
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        panelHeader.add(lblTitre, BorderLayout.WEST);
        panelHeader.add(btnDeconnexion, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // --- SYSTÈME D'ONGLETS ---
        JTabbedPane onglets = new JTabbedPane();
        onglets.setFont(ThemeUtil.POLICE_BOUTON);

        onglets.addTab("👥 Gestion du Personnel", creerOngletPersonnel());
        onglets.addTab("🗂️ Toutes les Réservations", creerOngletReservations());
        onglets.addTab("🛏️ Parc des Chambres", creerOngletChambres());
        // onglets.addTab("📊 Statistiques", new JPanel()); // Pour plus tard

        add(onglets, BorderLayout.CENTER);
    }

    // =========================================================
    // ONGLET 1 : GESTION DU PERSONNEL (Ajout et Liste)
    // =========================================================
    private JPanel creerOngletPersonnel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);

        // FORMULAIRE D'AJOUT (En haut)
        JPanel panelFormulaire = new JPanel(new GridBagLayout());
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Ajouter un nouvel employé"));
        panelFormulaire.setBackground(ThemeUtil.GRIS_FOND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Ligne 1
        gbc.gridx = 0; gbc.gridy = 0; panelFormulaire.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; txtNom = new JTextField(15); panelFormulaire.add(txtNom, gbc);

        gbc.gridx = 2; panelFormulaire.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 3; txtPrenom = new JTextField(15); panelFormulaire.add(txtPrenom, gbc);

        // Ligne 2
        gbc.gridx = 0; gbc.gridy = 1; panelFormulaire.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(15); panelFormulaire.add(txtEmail, gbc);

        gbc.gridx = 2; panelFormulaire.add(new JLabel("Mot de Passe :"), gbc);
        gbc.gridx = 3; txtMotDePasse = new JPasswordField(15); panelFormulaire.add(txtMotDePasse, gbc);

        // Ligne 3 : Sélection du rôle (Le fameux Full Accès pour créer d'autres rôles !)
        gbc.gridx = 0; gbc.gridy = 2; panelFormulaire.add(new JLabel("Rôle :"), gbc);
        gbc.gridx = 1;
        // On liste uniquement les rôles qu'un admin peut créer (pas de SuperAdmin par exemple)
        comboRole = new JComboBox<>(new Role[]{Role.RECEPTIONNISTE, Role.MAINTENANCE, Role.ADMIN});
        panelFormulaire.add(comboRole, gbc);

        gbc.gridx = 3;
        JButton btnAjouter = new JButton("Créer le compte");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);
        btnAjouter.addActionListener(e -> ajouterEmploye());
        panelFormulaire.add(btnAjouter, gbc);

        // TABLEAU DES EMPLOYÉS (Au centre)
        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Rôle", "Statut"};
        tableModelPersonnel = new DefaultTableModel(colonnes, 0);
        tablePersonnel = new JTable(tableModelPersonnel);
        tablePersonnel.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablePersonnel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des employés"));

        panel.add(panelFormulaire, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // ONGLET 2 : VUE GLOBALE DES RÉSERVATIONS
    // =========================================================
    private JPanel creerOngletReservations() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);

        JLabel lblInfo = new JLabel("Ici, l'administrateur pourra voir l'historique complet de toutes les réservations.", SwingConstants.CENTER);
        lblInfo.setFont(ThemeUtil.POLICE_NORMALE);

        // TODO: Créer une JTable pour afficher la liste des réservations via ReservationService
        panel.add(lblInfo, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // ONGLET 3 : PARC DES CHAMBRES
    // =========================================================
    private JPanel creerOngletChambres() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);

        JLabel lblInfo = new JLabel("Ici, l'administrateur pourra voir toutes les chambres et forcer leur statut.", SwingConstants.CENTER);
        lblInfo.setFont(ThemeUtil.POLICE_NORMALE);

        // TODO: Créer une JTable pour afficher la liste des chambres via ChambreService
        panel.add(lblInfo, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // LOGIQUE MÉTIER (Actions des boutons)
    // =========================================================
    private void ajouterEmploye() {
        try {
            // Récupérer les données
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();
            String motDePasse = new String(txtMotDePasse.getPassword());
            Role role = (Role) comboRole.getSelectedItem();

            // Créer l'objet
            Utilisateur nouvelEmploye = new Utilisateur();
            nouvelEmploye.setNom(nom);
            nouvelEmploye.setPrenom(prenom);
            nouvelEmploye.setEmail(email);
            nouvelEmploye.setMotDEPasse(motDePasse);
            nouvelEmploye.setRole(role);

            // Appel au Service qui gère les validations (email valide, mdp > 4, etc.)
            boolean succes = utilisateurService.inscrireEmploye(nouvelEmploye);

            if (succes) {
                JOptionPane.showMessageDialog(this, "Compte employé créé avec succès !");
                viderFormulairePersonnel();
                chargerDonneesPersonnel(); // Rafraîchir la table
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la création en base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {
            // Attrape les erreurs de notre couche Service (ex: "Email déjà existant")
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Saisie Invalide", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void chargerDonneesPersonnel() {
        // Vider la table actuelle
        tableModelPersonnel.setRowCount(0);

        // Demander la liste au service
        for (Utilisateur u : utilisateurService.listerTousLesEmployes()) {
            Object[] ligne = {
                    u.getIdUtilisateur(),
                    u.getNom(),
                    u.getPrenom(),
                    u.getEmail(),
                    u.getRole().name(),
                    u.getStatut().name()
            };
            tableModelPersonnel.addRow(ligne);
        }
    }

    private void viderFormulairePersonnel() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtMotDePasse.setText("");
        comboRole.setSelectedIndex(0);
    }
}