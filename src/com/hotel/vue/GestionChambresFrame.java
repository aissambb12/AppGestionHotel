package com.hotel.vue;

import com.hotel.model.Chambre;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.service.ChambreService;
import com.hotel.service.MaintenanceService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GestionChambresFrame extends JFrame {

    private ChambreService chambreService;
    private MaintenanceService maintenanceService;
    private DefaultTableModel modeleChambres;
    private JTable tableChambres;
    private Utilisateur adminConnecte;

    public GestionChambresFrame(Utilisateur admin) {
        this.chambreService = new ChambreService();
        this.maintenanceService = new MaintenanceService();
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

        JButton btnRetour = ThemeUtil.creerBoutonRetour(e ->
                NavigationManager.retourVers(this, new DashboardAdminFrame(adminConnecte)));
        add(ThemeUtil.creerHeaderApp("GESTION DES CHAMBRES", "icon_chambres", btnRetour), BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(ThemeUtil.GRIS_FOND);
        centre.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        centre.add(creerPanelBoutons(), BorderLayout.NORTH);
        centre.add(creerPanelTable(), BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);
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

        JButton btnMaintenance = new JButton("Déclarer une panne");
        ThemeUtil.appliquerThemeBoutonAttention(btnMaintenance);
        IconLoader.appliquerIcone(btnMaintenance, "icon_maintenance");
        btnMaintenance.addActionListener(e -> declarerPanne());

        JButton btnDisponible = new JButton("Remettre disponible");
        ThemeUtil.appliquerThemeBoutonValider(btnDisponible);
        IconLoader.appliquerIcone(btnDisponible, "icon_check");
        btnDisponible.addActionListener(e -> remettreDisponible());

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

        // Coloriser la colonne Statut (2 valeurs uniquement : DISPONIBLE / MAINTENANCE)
        tableChambres.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && value != null) {
                    String s = value.toString();
                    if ("DISPONIBLE".equals(s))       c.setForeground(ThemeUtil.VERT_VALIDATION);
                    else if ("MAINTENANCE".equals(s)) c.setForeground(ThemeUtil.ORANGE_ATTENTION);
                    else                              c.setForeground(ThemeUtil.TEXTE_SOMBRE);
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

    /**
     * Déclare une panne sur la chambre sélectionnée :
     * - demande la description et les dates,
     * - crée la ligne dans la table maintenances (visible côté technicien),
     * - passe la chambre en MAINTENANCE.
     */
    private void declarerPanne() {
        int ligne = tableChambres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idChambre = (Integer) modeleChambres.getValueAt(ligne, 0);
        String numero = String.valueOf(modeleChambres.getValueAt(ligne, 1));
        Object statutObj = modeleChambres.getValueAt(ligne, 4);
        if (statutObj != null && "MAINTENANCE".equals(statutObj.toString())) {
            JOptionPane.showMessageDialog(this,
                    "La chambre " + numero + " est déjà en maintenance.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Mini-dialogue panne
        JDialog dialog = new JDialog(this, "Déclarer une panne - Chambre " + numero, true);
        dialog.setSize(550, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(ThemeUtil.creerTitreSection("Détails de la panne"), gbc);

        // Description
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
        JLabel lblDesc = new JLabel("Description :");
        lblDesc.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblDesc, gbc);

        JTextArea txtDescription = new JTextArea(5, 30);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setFont(ThemeUtil.POLICE_NORMAL);
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollDesc = new JScrollPane(txtDescription);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        panel.add(scrollDesc, gbc);
        gbc.gridheight = 1; gbc.fill = GridBagConstraints.HORIZONTAL;

        // Date début
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
        JLabel lblDeb = new JLabel("Date début :");
        lblDeb.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblDeb, gbc);

        JTextField txtDebut = new JTextField(LocalDate.now().toString());
        ThemeUtil.appliquerThemeTextField(txtDebut);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtDebut, gbc);

        // Date fin
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0;
        JLabel lblFin = new JLabel("Date fin estimée :");
        lblFin.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblFin, gbc);

        JTextField txtFin = new JTextField(LocalDate.now().plusDays(7).toString());
        ThemeUtil.appliquerThemeTextField(txtFin);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(txtFin, gbc);

        // Boutons
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        boutons.setOpaque(false);

        JButton btnValider = new JButton("Déclarer");
        ThemeUtil.appliquerThemeBoutonAttention(btnValider);
        IconLoader.appliquerIcone(btnValider, "icon_maintenance");

        JButton btnAnnuler = new JButton("Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        IconLoader.appliquerIcone(btnAnnuler, "icon_cancel");
        btnAnnuler.addActionListener(e -> dialog.dispose());

        boutons.add(btnAnnuler);
        boutons.add(btnValider);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 0, 8);
        panel.add(boutons, gbc);

        btnValider.addActionListener(e -> {
            try {
                if (ValidationUtil.estVide(txtDescription.getText())) {
                    JOptionPane.showMessageDialog(dialog, "La description est obligatoire.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                LocalDate debut = LocalDate.parse(txtDebut.getText().trim());
                LocalDate fin   = LocalDate.parse(txtFin.getText().trim());

                boolean ok = maintenanceService.declarerPanneAvecChambre(
                        idChambre, txtDescription.getText().trim(), debut, fin);

                if (ok) {
                    JOptionPane.showMessageDialog(dialog,
                            "Panne déclarée. La chambre " + numero + " est passée en maintenance.\n"
                                    + "Un ticket a été créé dans le tableau des interventions.",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    chargerDonnees();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Échec de la déclaration.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Format de date invalide (yyyy-MM-dd attendu).", "Validation", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void remettreDisponible() {
        int ligne = tableChambres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idChambre = (Integer) modeleChambres.getValueAt(ligne, 0);
        String numero = String.valueOf(modeleChambres.getValueAt(ligne, 1));

        int rep = JOptionPane.showConfirmDialog(this,
                "Confirmer la remise en service de la chambre " + numero + " ?\n"
                        + "Les maintenances EN_COURS associées seront marquées TERMINEE.",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (rep != JOptionPane.YES_OPTION) return;

        try {
            maintenanceService.libererChambre(idChambre);
            JOptionPane.showMessageDialog(this, "Chambre " + numero + " remise en service.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherDialogueAjoutChambre() {
        JDialog dialog = new JDialog(this, "Ajouter une Chambre", true);
        dialog.setSize(450, 340);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
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

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBoutons.setOpaque(false);

        JButton btnValider = new JButton("Ajouter");
        ThemeUtil.appliquerThemeBoutonValider(btnValider);
        IconLoader.appliquerIcone(btnValider, "icon_check");

        JButton btnAnnuler = new JButton("Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        IconLoader.appliquerIcone(btnAnnuler, "icon_cancel");
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