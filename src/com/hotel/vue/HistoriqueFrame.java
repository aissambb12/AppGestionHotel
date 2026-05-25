package com.hotel.vue;

import com.hotel.model.Maintenance;
import com.hotel.model.Utilisateur;
import com.hotel.service.MaintenanceService;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoriqueFrame extends JFrame {

    private Utilisateur technicienConnecte;
    private MaintenanceService maintenanceService;
    private DefaultTableModel modeleHistorique;
    private JTable tableHistorique;

    public HistoriqueFrame(Utilisateur technicien) {
        this.technicienConnecte = technicien;
        this.maintenanceService = new MaintenanceService();

        setTitle("Hotel Manager - Historique Réparations");
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

        String[] colonnes = {"ID", "N° Chambre", "Description", "Date Début", "Date Fin", "Statut"};
        modeleHistorique = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHistorique = new JTable(modeleHistorique);
        ThemeUtil.appliquerThemeTable(tableHistorique);
        tableHistorique.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableHistorique);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("📋 HISTORIQUE DES RÉPARATIONS");
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
        btnRetour.addActionListener(e ->
                NavigationManager.retourVers(this, new DashboardMaintenanceFrame(technicienConnecte)));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /**
         * IMAGE À AJOUTER : refresh.png (48x48px)
         * Description: Icône d'une flèche circulaire
         */
        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modeleHistorique.setRowCount(0);
        try {
            List<Maintenance> maintenances = maintenanceService.listerMaintenancesEnCours();
            for (Maintenance m : maintenances) {
                modeleHistorique.addRow(new Object[]{
                        m.getIdMaintenance(),
                        m.getIdChambre(),
                        m.getDescription(),
                        m.getDateDebut(),
                        m.getDateFin(),
                        m.getStatutMaintenance()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}