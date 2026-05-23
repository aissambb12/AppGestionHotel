package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.MaintenanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardMaintenanceFrame extends JFrame {

    private Utilisateur technicienConnecte;
    private MaintenanceService maintenanceService;

    // Composants
    private DefaultTableModel modeleInterventions;
    private JTable tableInterventions;

    public DashboardMaintenanceFrame(Utilisateur technicien) {
        this.technicienConnecte = technicien;
        // this.maintenanceService = new MaintenanceService(); // À décommenter une fois prêt

        setTitle("Hotel Manager - Espace Technique (" + technicien.getNom() + ")");
        setSize(900, 600); // Fenêtre un peu plus petite car moins de menus
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // --- EN-TÊTE (Header) ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(ThemeUtil.BLEU_NUIT);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🔧 DÉPARTEMENT TECHNIQUE & MAINTENANCE");
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

        // --- SYSTÈME D'ONGLETS (Un seul onglet principal, et un pour l'historique) ---
        JTabbedPane onglets = new JTabbedPane();
        onglets.setFont(ThemeUtil.POLICE_BOUTON);

        onglets.addTab("🚨 Interventions en cours", creerOngletInterventions());
        onglets.addTab("📋 Historique des réparations", creerOngletHistorique());

        add(onglets, BorderLayout.CENTER);
    }

    // =========================================================
    // ONGLET 1 : INTERVENTIONS EN COURS (Le cœur du métier)
    // =========================================================
    private JPanel creerOngletInterventions() {
        JPanel panel = new JPanel(new BorderLayout());

        // Zone de boutons (Action)
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JButton btnRafraichir = new JButton("🔄 Rafraîchir la liste");
        btnRafraichir.setBackground(ThemeUtil.BLEU_NUIT);
        btnRafraichir.setForeground(Color.WHITE);

        // LE BOUTON MAGIQUE DU TECHNICIEN
        JButton btnTerminer = new JButton("✅ Réparation Terminée (Libérer la chambre)");
        btnTerminer.setBackground(new Color(39, 174, 96)); // Vert validation
        btnTerminer.setForeground(Color.WHITE);
        btnTerminer.setFont(ThemeUtil.POLICE_BOUTON);

        panelActions.add(btnRafraichir);
        panelActions.add(btnTerminer);

        // Tableau des pannes en cours
        String[] colonnes = {"ID Maintenance", "N° Chambre", "Description du problème", "Date Signalement"};
        modeleInterventions = new DefaultTableModel(colonnes, 0);
        tableInterventions = new JTable(modeleInterventions);
        tableInterventions.setRowHeight(30); // Lignes un peu plus larges pour lire la description

        // ACTION : Terminer la maintenance
        btnTerminer.addActionListener(e -> {
            int ligneSelectionnee = tableInterventions.getSelectedRow();
            if (ligneSelectionnee != -1) {
                int idMaintenance = (int) modeleInterventions.getValueAt(ligneSelectionnee, 0);
                String numChambre = modeleInterventions.getValueAt(ligneSelectionnee, 1).toString();

                int confirmation = JOptionPane.showConfirmDialog(
                        this,
                        "Confirmez-vous que les travaux sont terminés pour la chambre " + numChambre + " ?\nElle redeviendra DISPONIBLE pour les clients.",
                        "Confirmation de réparation",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    // Appel au backend : change le statut de la maintenance ET de la chambre
                    // boolean succes = maintenanceService.terminerMaintenance(idMaintenance);

                    // if(succes) {
                    JOptionPane.showMessageDialog(this, "Chambre " + numChambre + " réparée et libérée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    // chargerInterventions(); // Rafraîchit le tableau pour faire disparaître la ligne
                    // } else { ... erreur ... }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner l'intervention que vous avez terminée.", "Attention", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(panelActions, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableInterventions), BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // ONGLET 2 : HISTORIQUE (Lecture seule)
    // =========================================================
    private JPanel creerOngletHistorique() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel lblInfo = new JLabel("Retrouvez ici toutes les interventions terminées (Archives).", SwingConstants.CENTER);
        lblInfo.setFont(ThemeUtil.POLICE_NORMALE);

        // Un simple tableau (vide pour le moment)
        String[] colonnes = {"ID", "N° Chambre", "Problème", "Date Fin", "Coût éventuel"};
        DefaultTableModel modeleHistorique = new DefaultTableModel(colonnes, 0);
        JTable tableHistorique = new JTable(modeleHistorique);
        tableHistorique.setEnabled(false); // Lecture seule

        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableHistorique), BorderLayout.CENTER);

        return panel;
    }
}