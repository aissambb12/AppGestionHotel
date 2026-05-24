package com.hotel.vue;

import com.hotel.model.Chambre;
import com.hotel.service.ChambreService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionChambresFrame extends JFrame {

    private ChambreService chambreService;
    private DefaultTableModel modeleChambres;
    private JTable tableChambres;

    public GestionChambresFrame() {
        this.chambreService = new ChambreService();

        setTitle("Hotel Manager - Gestion Chambres");
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
        String[] colonnes = {"ID", "Numéro", "Catégorie", "Prix/Nuit", "Statut"};
        modeleChambres = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChambres = new JTable(modeleChambres);
        ThemeUtil.appliquerThemeTable(tableChambres);

        JScrollPane scrollPane = new JScrollPane(tableChambres);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🛏️ GESTION DES CHAMBRES");
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
        btnRetour.addActionListener(e -> dispose());

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

        JButton btnMaintenance = new JButton("🔧 En Maintenance");
        btnMaintenance.setBackground(ThemeUtil.ORANGE_ATTENTION);
        btnMaintenance.setForeground(ThemeUtil.BLANC);
        btnMaintenance.setFont(ThemeUtil.POLICE_BOUTON);
        btnMaintenance.setFocusPainted(false);
        btnMaintenance.setOpaque(true);
        btnMaintenance.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnMaintenance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMaintenance.addActionListener(e -> changerStatutMaintenance());

        JButton btnDisponible = new JButton("✓ Disponible");
        ThemeUtil.appliquerThemeBoutonValider(btnDisponible);
        btnDisponible.addActionListener(e -> changerStatutDisponible());

        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnAjouter);
        panel.add(btnMaintenance);
        panel.add(btnDisponible);
        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modeleChambres.setRowCount(0);
        List<Chambre> chambres = chambreService.listerToutesLesChambres();
        for (Chambre c : chambres) {
            modeleChambres.addRow(new Object[]{
                    c.getIdChambre(),
                    c.getNumero(),
                    c.getCategorie(),
                    String.format("%.2f MAD", c.getPrixUnitaire()),
                    c.getStatutChambre()
            });
        }
    }

    private void changerStatutMaintenance() {
        int ligne = tableChambres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre");
            return;
        }

        int idChambre = (Integer) modeleChambres.getValueAt(ligne, 0);
        try {
            chambreService.modifierStatutChambre(idChambre, "MAINTENANCE");
            JOptionPane.showMessageDialog(this, "Chambre en maintenance");
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changerStatutDisponible() {
        int ligne = tableChambres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre");
            return;
        }

        int idChambre = (Integer) modeleChambres.getValueAt(ligne, 0);
        try {
            chambreService.modifierStatutChambre(idChambre, "DISPONIBLE");
            JOptionPane.showMessageDialog(this, "Chambre disponible");
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}