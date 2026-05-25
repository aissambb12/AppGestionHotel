package com.hotel.vue;

import com.hotel.model.Chambre;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.service.ChambreService;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionChambresFrame extends JFrame {

    private ChambreService chambreService;
    private DefaultTableModel modeleChambres;
    private JTable tableChambres;
    private Utilisateur adminConnecte;

    public GestionChambresFrame(Utilisateur admin) {
        this.chambreService = new ChambreService();
        this.adminConnecte = admin;

        setTitle("Hotel Manager - Gestion Chambres");
        setSize(1100, 700);
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
        add(panelBoutons, BorderLayout.CENTER);

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
        tableChambres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableChambres);
        add(scrollPane, BorderLayout.SOUTH);
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
         * IMAGE À AJOUTER : add.png (48x48px)
         * Description: Icône d'un plus (+) vert
         */
        JButton btnAjouter = new JButton("➕ Ajouter Chambre");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);
        btnAjouter.addActionListener(e -> afficherDialogueAjoutChambre());

        /**
         * IMAGE À AJOUTER : (orange) Icône maintenance (48x48px)
         * Description: Icône d'une clé à molette orange
         */
        JButton btnMaintenance = new JButton("🔧 En Maintenance");
        btnMaintenance.setBackground(ThemeUtil.ORANGE_ATTENTION);
        btnMaintenance.setForeground(ThemeUtil.BLANC);
        btnMaintenance.setFont(ThemeUtil.POLICE_BOUTON);
        btnMaintenance.setFocusPainted(false);
        btnMaintenance.setOpaque(true);
        btnMaintenance.setContentAreaFilled(true);
        btnMaintenance.setBorderPainted(true);
        btnMaintenance.setBorder(BorderFactory.createLineBorder(ThemeUtil.ORANGE_ATTENTION, 1));
        btnMaintenance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMaintenance.addActionListener(e -> changerStatutMaintenance());

        /**
         * IMAGE À AJOUTER : (vert) Icône validation (48x48px)
         * Description: Icône d'une coche verte
         */
        JButton btnDisponible = new JButton("✓ Disponible");
        ThemeUtil.appliquerThemeBoutonValider(btnDisponible);
        btnDisponible.addActionListener(e -> changerStatutDisponible());

        /**
         * IMAGE À AJOUTER : refresh.png (48x48px)
         * Description: Icône d'une flèche circulaire
         */
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
        try {
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changerStatutMaintenance() {
        int ligne = tableChambres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une chambre", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idChambre = (Integer) modeleChambres.getValueAt(ligne, 0);
        try {
            chambreService.modifierStatutChambre(idChambre, StatutChambre.MAINTENANCE.name());
            JOptionPane.showMessageDialog(this, "✓ Chambre passée en maintenance", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changerStatutDisponible() {
        int ligne = tableChambres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une chambre", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idChambre = (Integer) modeleChambres.getValueAt(ligne, 0);
        try {
            chambreService.modifierStatutChambre(idChambre, StatutChambre.DISPONIBLE.name());
            JOptionPane.showMessageDialog(this, "✓ Chambre marquée disponible", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherDialogueAjoutChambre() {
        JDialog dialog = new JDialog(this, "📝 Ajouter une Chambre", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Titre
        JLabel lblTitre = new JLabel("Nouvelle Chambre");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        // Champs
        gbc.gridwidth = 1;
        JTextField txtNumero = new JTextField();
        JComboBox<String> comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});
        JTextField txtPrix = new JTextField();

        ThemeUtil.appliquerThemeTextField(txtNumero);
        ThemeUtil.appliquerThemeTextField(txtPrix);

        gbc.gridy = 1;
        ajouterChamp(panel, gbc, "Numéro :", txtNumero);

        gbc.gridy = 2;
        ajouterChamp(panel, gbc, "Catégorie :", comboCategorie);

        gbc.gridy = 3;
        ajouterChamp(panel, gbc, "Prix/Nuit (MAD) :", txtPrix);

        // Boutons
        JButton btnValider = new JButton("✓ AJOUTER");
        ThemeUtil.appliquerThemeBoutonValider(btnValider);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(btnValider, gbc);

        JButton btnAnnuler = new JButton("✕ Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        gbc.gridx = 1;
        panel.add(btnAnnuler, gbc);

        btnValider.addActionListener(e -> {
            try {
                if (ValidationUtil.estVide(txtNumero.getText())) {
                    JOptionPane.showMessageDialog(dialog, "❌ Numéro obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double prix = Double.parseDouble(txtPrix.getText());
                if (prix <= 0) {
                    JOptionPane.showMessageDialog(dialog, "❌ Prix doit être positif", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Chambre nouvelleChambre = new Chambre();
                nouvelleChambre.setNumero(txtNumero.getText().trim());
                nouvelleChambre.setCategorie(comboCategorie.getSelectedItem().toString());
                nouvelleChambre.setPrixUnitaire(prix);
                nouvelleChambre.setStatutChambre(StatutChambre.DISPONIBLE);

                boolean succes = chambreService.ajouterChambre(nouvelleChambre);
                if (succes) {
                    JOptionPane.showMessageDialog(dialog, "✓ Chambre ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    chargerDonnees();
                } else {
                    JOptionPane.showMessageDialog(dialog, "❌ Erreur lors de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Prix invalide (nombre décimal)", "Validation", JOptionPane.WARNING_MESSAGE);
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