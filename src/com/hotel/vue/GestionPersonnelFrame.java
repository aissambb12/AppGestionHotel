package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.Role;
import com.hotel.model.enumeration.StatutUtilisateur;
import com.hotel.service.UtilisateurService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionPersonnelFrame extends JFrame {

    private Utilisateur adminConnecte;
    private UtilisateurService utilisateurService;
    private DefaultTableModel modelePersonnel;
    private JTable tablePersonnel;

    public GestionPersonnelFrame(Utilisateur admin) {
        this.adminConnecte = admin;
        this.utilisateurService = new UtilisateurService();

        setTitle("Hotel Manager - Gestion Personnel");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // Header inchangé : NORTH
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // ★ Nouveau panel central : boutons en haut + table qui remplit le reste
        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(ThemeUtil.GRIS_FOND);
        centre.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelBoutons = creerPanelBoutons();
        centre.add(panelBoutons, BorderLayout.NORTH);

        // Création de la table
        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Rôle", "Statut"};
        modelePersonnel = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablePersonnel = new JTable(modelePersonnel);
        ThemeUtil.appliquerThemeTable(tablePersonnel);
        tablePersonnel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablePersonnel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        centre.add(scrollPane, BorderLayout.CENTER);

        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel(" GESTION DU PERSONNEL");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setBackground(ThemeUtil.GRIS_CLAIR);
        btnRetour.setForeground(ThemeUtil.TEXTE_SOMBRE);
        btnRetour.setFont(ThemeUtil.POLICE_BOUTON);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setContentAreaFilled(true);
        btnRetour.setBorderPainted(true);
        btnRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        IconLoader.appliquerIcone(btnRetour , "icon_back");
        btnRetour.addActionListener(e ->
                NavigationManager.retourVers(this, new DashboardAdminFrame(adminConnecte)));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /**
         * IMAGE À AJOUTER : add.png (48x48px)
         * Description: Icône d'un plus (+) vert pour l'ajout
         */
        JButton btnAjouter = new JButton("Ajouter Employé");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);
        IconLoader.appliquerIcone(btnAjouter, "icon_add");
        btnAjouter.addActionListener(e -> afficherDialogueAjoutEmploye());

        /**
         * IMAGE À AJOUTER : (vert) Icône de validation (32x32px)
         * Description: Icône de validation/activation (coche verte)
         */
        JButton btnActiver = new JButton("Activer");
        ThemeUtil.appliquerThemeBoutonValider(btnActiver);
        IconLoader.appliquerIcone(btnActiver,"icon_user_add");
        btnActiver.addActionListener(e -> changerStatut(true));

        /**
         * IMAGE À AJOUTER : delete.png (48x48px)
         * Description: Icône d'une croix rouge pour désactivation
         */
        JButton btnDesactiver = new JButton("Désactiver");
        ThemeUtil.appliquerThemeBoutonSuppression(btnDesactiver);
        IconLoader.appliquerIcone(btnDesactiver , "icon_user_disable");
        btnDesactiver.addActionListener(e -> changerStatut(false));

        /**
         * IMAGE À AJOUTER : refresh.png (48x48px)
         * Description: Icône d'une flèche circulaire pour rafraîchissement
         */
        JButton btnRafraichir = new JButton("Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir , "icon_refresh");
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnAjouter);
        panel.add(btnActiver);
        panel.add(btnDesactiver);
        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modelePersonnel.setRowCount(0);
        try {
            List<Utilisateur> employes = utilisateurService.listerTousLesEmployes();
            for (Utilisateur u : employes) {
                modelePersonnel.addRow(new Object[]{
                        u.getIdUtilisateur(),
                        u.getNom(),
                        u.getPrenom(),
                        u.getEmail(),
                        u.getRole(),
                        u.getStatut()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changerStatut(boolean activer) {
        int ligne = tablePersonnel.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner un employé", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUtilisateur = (Integer) modelePersonnel.getValueAt(ligne, 0);
        try {
            if (activer) {
                utilisateurService.activerEmploye(idUtilisateur);
                JOptionPane.showMessageDialog(this, "✓ Employé activé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                utilisateurService.desactiverEmploye(idUtilisateur);
                JOptionPane.showMessageDialog(this, "✓ Employé désactivé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherDialogueAjoutEmploye() {
        JDialog dialog = new JDialog(this, "📝 Créer un Employé", true);
        dialog.setSize(550, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Titre
        JLabel lblTitre = new JLabel("Nouvel Employé");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        // Champs
        gbc.gridwidth = 1;
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtMotDePasse = new JPasswordField();
        JComboBox<Role> comboRole = new JComboBox<>(Role.values());

        ThemeUtil.appliquerThemeTextField(txtNom);
        ThemeUtil.appliquerThemeTextField(txtPrenom);
        ThemeUtil.appliquerThemeTextField(txtEmail);
        txtMotDePasse.setFont(ThemeUtil.POLICE_NORMAL);
        txtMotDePasse.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        gbc.gridy = 1;
        ajouterChamp(panel, gbc, "Nom :", txtNom);

        gbc.gridy = 2;
        ajouterChamp(panel, gbc, "Prénom :", txtPrenom);

        gbc.gridy = 3;
        ajouterChamp(panel, gbc, "Email :", txtEmail);

        gbc.gridy = 4;
        ajouterChamp(panel, gbc, "Mot de passe :", txtMotDePasse);

        gbc.gridy = 5;
        ajouterChamp(panel, gbc, "Rôle :", comboRole);

        // Boutons
        JButton btnValider = new JButton("ENREGISTRER");
        ThemeUtil.appliquerThemeBoutonValider(btnValider);
        IconLoader.appliquerIcone(btnValider , "icon_check");
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(btnValider, gbc);

        JButton btnAnnuler = new JButton("Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        IconLoader.appliquerIcone(btnAnnuler , "icon_cancel");
        gbc.gridx = 1;
        panel.add(btnAnnuler, gbc);

        btnValider.addActionListener(e -> {
            try {
                // Validation
                if (ValidationUtil.estVide(txtNom.getText())) {
                    JOptionPane.showMessageDialog(dialog, "❌ Nom obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (ValidationUtil.estVide(txtPrenom.getText())) {
                    JOptionPane.showMessageDialog(dialog, "❌ Prénom obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!ValidationUtil.estEmailValide(txtEmail.getText())) {
                    JOptionPane.showMessageDialog(dialog, "❌ Email invalide", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!ValidationUtil.estMotDePasseValide(new String(txtMotDePasse.getPassword()))) {
                    JOptionPane.showMessageDialog(dialog, "❌ Mot de passe minimum 4 caractères", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Formatage : NOM en MAJUSCULE, Prénom avec 1ère lettre majuscule
                String nom = txtNom.getText().trim().toUpperCase();
                String prenom = txtPrenom.getText().trim();
                prenom = prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase();

                Utilisateur nouvelUtilisateur = new Utilisateur();
                nouvelUtilisateur.setNom(nom);
                nouvelUtilisateur.setPrenom(prenom);
                nouvelUtilisateur.setEmail(txtEmail.getText().trim());
                nouvelUtilisateur.setMotDEPasse(new String(txtMotDePasse.getPassword()));
                nouvelUtilisateur.setRole((Role) comboRole.getSelectedItem());
                nouvelUtilisateur.setStatut(StatutUtilisateur.ACTIF);

                boolean succes = utilisateurService.inscrireEmploye(nouvelUtilisateur);
                if (succes) {
                    JOptionPane.showMessageDialog(dialog, "✓ Employé créé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    chargerDonnees();
                } else {
                    JOptionPane.showMessageDialog(dialog, "❌ Erreur lors de la création", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAnnuler.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void ajouterChamp(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }
}