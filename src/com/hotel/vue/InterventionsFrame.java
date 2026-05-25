package com.hotel.vue;

import com.hotel.model.Maintenance;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.StatutMaintenance;
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

        setTitle("Hotel Manager - Interventions en Cours");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelBoutons = creerPanelBoutons();
        add(panelBoutons, BorderLayout.CENTER);

        String[] colonnes = {"ID", "N° Chambre", "Description", "Date Début", "Date Fin"};
        modeleInterventions = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableInterventions = new JTable(modeleInterventions);
        ThemeUtil.appliquerThemeTable(tableInterventions);
        tableInterventions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableInterventions);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🚨 INTERVENTIONS EN COURS");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        /**
         * IMAGE À AJOUTER : back.png (48x48px)
         * Description: Icône d'une flèche gauche
         */
        JButton btnRetour = new JButton("← Retour");
        btnRetour.setBackground(ThemeUtil.GRIS_CLAIR);
        btnRetour.setForeground(ThemeUtil.TEXTE_SOMBRE);
        btnRetour.setFont(ThemeUtil.POLICE_BOUTON);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setContentAreaFilled(true);
        btnRetour.setBorderPainted(true);
        btnRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.addActionListener(e -> dispose());

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /**
         * IMAGE À AJOUTER : (vert) Icône validation (48x48px)
         * Description: Icône d'une coche verte
         */
        JButton btnTerminer = new JButton("✅ RÉPARATION TERMINÉE");
        btnTerminer.setBackground(ThemeUtil.VERT_VALIDATION);
        btnTerminer.setForeground(ThemeUtil.BLANC);
        btnTerminer.setFont(ThemeUtil.POLICE_BOUTON);
        btnTerminer.setFocusPainted(false);
        btnTerminer.setOpaque(true);
        btnTerminer.setContentAreaFilled(true);
        btnTerminer.setBorderPainted(true);
        btnTerminer.setBorder(BorderFactory.createLineBorder(ThemeUtil.VERT_VALIDATION, 1));
        btnTerminer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTerminer.addActionListener(e -> terminerIntervention());

        /**
         * IMAGE À AJOUTER : refresh.png (48x48px)
         * Description: Icône d'une flèche circulaire
         */
        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnTerminer);
        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modeleInterventions.setRowCount(0);
        try {
            List<Maintenance> maintenances = maintenanceService.listerMaintenancesEnCours();
            for (Maintenance m : maintenances) {
                modeleInterventions.addRow(new Object[]{
                        m.getIdMaintenance(),
                        m.getIdChambre(),
                        m.getDescription(),
                        m.getDateDebut(),
                        m.getDateFin()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
                    "Confirmez-vous que la réparation de la chambre " + numChambre + " est terminée ?\n\nElle redeviendra DISPONIBLE.",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                boolean succes = maintenanceService.terminerMaintenance(idMaintenance);
                if (succes) {
                    JOptionPane.showMessageDialog(this,
                            "✓ Chambre " + numChambre + " réparée et libérée avec succès",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    chargerDonnees();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Erreur lors de la terminaison", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}