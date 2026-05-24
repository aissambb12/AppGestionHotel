package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.MaintenanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoriqueFrame extends JFrame {

    private Utilisateur technicienConnecte;
    private MaintenanceService maintenanceService;
    private DefaultTableModel modeleHistorique;
    private JTable tableHistorique;

    public HistoriqueFrame(Utilisateur technicien) {
        this.technicienConnecte = technicien;
        this.maintenanceService = new MaintenanceService();

        setTitle("Hotel Manager - Historique Maintenance");
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
        String[] colonnes = {"ID", "N° Chambre", "Description", "Date Début", "Date Fin", "Statut"};
        modeleHistorique = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHistorique = new JTable(modeleHistorique);
        ThemeUtil.appliquerThemeTable(tableHistorique);

        JScrollPane scrollPane = new JScrollPane(tableHistorique);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("📋 HISTORIQUE DES RÉPARATIONS");
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

        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modeleHistorique.setRowCount(0);
        try {
            // À adapter selon la méthode disponible dans MaintenanceDAO
            // Pour l'instant, on affiche un message
            JOptionPane.showMessageDialog(this, "Historique à récupérer depuis la base de données", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}