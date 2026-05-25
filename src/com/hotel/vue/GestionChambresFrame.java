package com.hotel.vue;

import com.hotel.model.Chambre;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.service.ChambreService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());
        add(creerHeader(), BorderLayout.NORTH);

        // Panel central : barre de boutons en haut + table qui prend tout le reste
        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(ThemeUtil.GRIS_FOND);
        centre.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        centre.add(creerPanelBoutons(), BorderLayout.NORTH);
        centre.add(creerPanelTable(), BorderLayout.CENTER);

        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("GESTION DES CHAMBRES");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);
        ImageIcon ic = IconLoader.charger("icon_chambres", 24);
        if (ic != null) { lblTitre.setIcon(ic); lblTitre.setIconTextGap(10); }

        JButton btnRetour = new JButton("Retour");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRetour);
        IconLoader.appliquerIcone(btnRetour, "icon_back");
        btnRetour.addActionListener(e ->
                NavigationManager.retourVers(this, new DashboardAdminFrame(adminConnecte)));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);
        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton btnAjouter = new JButton("Ajouter Chambre");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);
        IconLoader.appliquerIcone(btnAjouter, "icon_add");
        btnAjouter.addActionListener(e -> afficherDialogueAjoutChambre());

        JButton btnMaintenance = new JButton("Marquer en maintenance");
        btnMaintenance.setBackground(ThemeUtil.ORANGE_ATTENTION);
        btnMaintenance.setForeground(ThemeUtil.BLANC);
        btnMaintenance.setFont(ThemeUtil.POLICE_BOUTON);
        btnMaintenance.setFocusPainted(false);
        btnMaintenance.setOpaque(true);
        btnMaintenance.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeUtil.ORANGE_ATTENTION, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        btnMaintenance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        IconLoader.appliquerIcone(btnMaintenance, "icon_maintenance");
        btnMaintenance.addActionListener(e -> changerStatutMaintenance());

        JButton btnDisponible = new JButton("Marquer disponible");
        ThemeUtil.appliquerThemeBoutonValider(btnDisponible);
        IconLoader.appliquerIcone(btnDisponible, "icon_check");
        btnDisponible.addActionListener(e -> changerStatutDisponible());

        JButton btnRafraichir = new JButton("Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir, "icon_refresh");
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnAjouter);
        panel.add(btnMaintenance);
        panel.add(btnDisponible);
        panel.add(btnRafraichir);

        return panel;
    }

    private JScrollPane creerPanelTable() {
        String[] colonnes = {"ID", "Numéro", "Catégorie", "Prix/Nuit", "Statut"};
        modeleChambres = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tableChambres = new JTable(modeleChambres);
        ThemeUtil.appliquerThemeTable(tableChambres);
        tableChambres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Colorer la colonne Statut
        tableChambres.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && value != null) {
                    String s = value.toString();
                    if ("DISPONIBLE".equals(s))         c.setForeground(ThemeUtil.VERT_VALIDATION);
                    else if ("OCCUPEE".equals(s))       c.setForeground(ThemeUtil.ROUGE_ERREUR);
                    else if ("MAINTENANCE".equals(s))   c.setForeground(ThemeUtil.ORANGE_ATTENTION);
                    else                                c.setForeground(ThemeUtil.TEXTE_SOMBRE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tableChambres);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        return scroll;
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
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changerStatutMaintenance() { changerStatut(StatutChambre.MAINTENANCE, "passée en maintenance"); }
    private void changerStatutDisponible()  { changerStatut(StatutChambre.DISPONIBLE, "marquée disponible"); }

    private void changerStatut(StatutChambre nouveauStatut, String message) {
        int ligne = tableChambres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idChambre = (Integer) modeleChambres.getValueAt(ligne, 0);
        try {
            chambreService.modifierStatutChambre(idChambre, nouveauStatut.name());
            JOptionPane.showMessageDialog(this, "Chambre " + message, "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherDialogueAjoutChambre() {
        JDialog dialog = new JDialog(this, "Ajouter une Chambre", true);
        dialog.setSize(450, 320);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(ThemeUtil.creerTitreSection("Nouvelle Chambre"), gbc);
        gbc.gridwidth = 1;

        JTextField txtNumero = new JTextField();
        JComboBox<String> comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});
        JTextField txtPrix = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtNumero);
        ThemeUtil.appliquerThemeTextField(txtPrix);

        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
        panel.add(label("Numéro :"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtNumero, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0;
        panel.add(label("Catégorie :"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(comboCategorie, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
        panel.add(label("Prix/Nuit (MAD) :"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtPrix, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBoutons.setOpaque(false);

        JButton btnValider = new JButton("Ajouter");
        ThemeUtil.appliquerThemeBoutonValider(btnValider);
        IconLoader.appliquerIcone(btnValider, "icon_check");

        JButton btnAnnuler = new JButton("Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        btnAnnuler.addActionListener(e -> dialog.dispose());

        panelBoutons.add(btnAnnuler);
        panelBoutons.add(btnValider);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 8, 0, 8);
        panel.add(panelBoutons, gbc);

        btnValider.addActionListener(e -> {
            try {
                if (ValidationUtil.estVide(txtNumero.getText())) {
                    JOptionPane.showMessageDialog(dialog, "Numéro obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                double prix = Double.parseDouble(txtPrix.getText().trim().replace(',', '.'));
                if (prix <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Prix doit être positif", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Chambre nouvelle = new Chambre();
                nouvelle.setNumero(txtNumero.getText().trim());
                nouvelle.setCategorie(comboCategorie.getSelectedItem().toString());
                nouvelle.setPrixUnitaire(prix);
                nouvelle.setStatutChambre(StatutChambre.DISPONIBLE);

                if (chambreService.ajouterChambre(nouvelle)) {
                    JOptionPane.showMessageDialog(dialog, "Chambre ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    chargerDonnees();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erreur lors de l'ajout (numéro en doublon ?)", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Prix invalide", "Validation", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private JLabel label(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(ThemeUtil.POLICE_LABEL);
        lbl.setForeground(ThemeUtil.BLEU_NUIT);
        return lbl;
    }
}
