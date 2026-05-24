package com.hotel.vue;

import com.hotel.model.Maintenance;
import com.hotel.model.Utilisateur;
import com.hotel.service.MaintenanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InterventionsFrame extends JFrame {

    private Utilisateur technicienConnecte;
    private MaintenanceService maintenanceService;
    private DefaultTableModel modeleInterventions;
    private JTable tableInterventions;

    public InterventionsFrame(Utilisateur technicien) {
        this.technicienConnecte = technicien;
        this.maintenanceService = new MaintenanceService();

        setTitle("Hotel Manager - Interventions en cours");
        setSize(1000, 700);
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
        String[] colonnes = {"ID", "N° Chambre", "Description", "Date Début"};
        modeleInterventions = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableInterventions = new JTable(modeleInterventions);
        ThemeUtil.appliquerThemeTable(tableInterventions);

        JScrollPane scrollPane = new JScrollPane(tableInterventions);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🚨 INTERVENTIONS EN COURS");
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
            new DashboardMaintenanceFrame(technicienConnecte).setVisible(true);
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

        JButton btnTerminer = new JButton("✅ Réparation Terminée");
        btnTerminer.setBackground(ThemeUtil.VERT_VALIDATION);
        btnTerminer.setForeground(ThemeUtil.BLANC);
        btnTerminer.setFont(ThemeUtil.POLICE_BOUTON);
        btnTerminer.setFocusPainted(false);
        btnTerminer.setOpaque(true);
        btnTerminer.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnTerminer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTerminer.addActionListener(e -> terminerIntervention());

        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnTerminer);
        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modeleInterventions.setRowCount(0);
        List<Maintenance> maintenances = maintenanceService.listerMaintenancesEnCours();
        for (Maintenance m : maintenances) {
            modeleInterventions.addRow(new Object[]{
                    m.getIdMaintenance(),
                    m.getIdChambre(),
                    m.getDescription(),
                    m.getDateDebut()
            });
        }
    }

    private void terminerIntervention() {
        int ligne = tableInterventions.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une intervention", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idMaintenance = (Integer) modeleInterventions.getValueAt(ligne, 0);
            int numChambre = (Integer) modeleInterventions.getValueAt(ligne, 1);

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Confirmez-vous que la réparation de la chambre " + numChambre + " est terminée ?\nElle redeviendra DISPONIBLE.",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                boolean succes = maintenanceService.terminerMaintenance(idMaintenance);
                if (succes) {
                    JOptionPane.showMessageDialog(this, "✓ Chambre " + numChambre + " réparée et libérée", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    chargerDonnees();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}