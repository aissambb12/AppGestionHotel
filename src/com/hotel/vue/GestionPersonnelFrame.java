package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.UtilisateurService;

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
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === PANEL BOUTONS ===
        JPanel panelBoutons = creerPanelBoutons();
        add(panelBoutons, BorderLayout.SOUTH);

        // === TABLE ===
        String[] colonnes = {"ID", "Nom", "Email", "Rôle", "Statut"};
        modelePersonnel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePersonnel = new JTable(modelePersonnel);
        ThemeUtil.appliquerThemeTable(tablePersonnel);

        JScrollPane scrollPane = new JScrollPane(tablePersonnel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("👥 GESTION DU PERSONNEL");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        JButton btnRetour = new JButton("← Retour");
        btnRetour.setBackground(ThemeUtil.GRIS_CLAIR);
        btnRetour.setForeground(ThemeUtil.TEXTE_SOMBRE);
        btnRetour.setFont(ThemeUtil.POLICE_BOUTON);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.addActionListener(e -> {
            new DashboardAdminFrame(adminConnecte).setVisible(true);
            dispose();
        });

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAjouter = new JButton("➕ Ajouter");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);
        btnAjouter.addActionListener(e -> ajouterEmploye());

        JButton btnActiver = new JButton("✅ Activer");
        ThemeUtil.appliquerThemeBoutonValider(btnActiver);
        btnActiver.addActionListener(e -> changerStatut(true));

        JButton btnDesactiver = new JButton("❌ Désactiver");
        ThemeUtil.appliquerThemeBoutonSuppression(btnDesactiver);
        btnDesactiver.addActionListener(e -> changerStatut(false));

        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnAjouter);
        panel.add(btnActiver);
        panel.add(btnDesactiver);
        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modelePersonnel.setRowCount(0);
        List<Utilisateur> employes = utilisateurService.listerTousLesEmployes();
        for (Utilisateur u : employes) {
            modelePersonnel.addRow(new Object[]{
                    u.getIdUtilisateur(),
                    u.getNom() + " " + u.getPrenom(),
                    u.getEmail(),
                    u.getRole(),
                    u.getStatut()
            });
        }
    }

    private void changerStatut(boolean activer) {
        int ligne = tablePersonnel.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé");
            return;
        }

        int idUtilisateur = (Integer) modelePersonnel.getValueAt(ligne, 0);
        try {
            if (activer) {
                utilisateurService.activerEmploye(idUtilisateur);
                JOptionPane.showMessageDialog(this, "Employé activé");
            } else {
                utilisateurService.desactiverEmploye(idUtilisateur);
                JOptionPane.showMessageDialog(this, "Employé désactivé");
            }
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterEmploye() {
        JOptionPane.showMessageDialog(this, "Fonction à implémenter");
    }
}