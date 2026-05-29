package com.hotel.vue;

import com.hotel.model.Maintenance;
import com.hotel.model.Utilisateur;
import com.hotel.service.MaintenanceService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class InterventionsFrame extends JFrame {

    private Utilisateur technicienConnecte;
    private MaintenanceService maintenanceService;
    private DefaultTableModel modele;
    private JTable table;

    public InterventionsFrame(Utilisateur technicien) {
        this.technicienConnecte = technicien;
        this.maintenanceService = new MaintenanceService();

        setTitle("Hotel Manager - Interventions en Cours");
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JButton btnRetour = ThemeUtil.creerBoutonRetour(e ->
                NavigationManager.retourVers(this, new DashboardMaintenanceFrame(technicienConnecte)));
        add(ThemeUtil.creerHeaderApp("INTERVENTIONS EN COURS", "icon_maintenance", btnRetour), BorderLayout.NORTH);

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

        JButton btnTerminer = new JButton("Marquer comme terminée");
        ThemeUtil.appliquerThemeBoutonValider(btnTerminer);
        IconLoader.appliquerIcone(btnTerminer, "icon_check");
        btnTerminer.addActionListener(e -> terminerIntervention());

        JButton btnDetails = new JButton("Voir détails");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnDetails);
        IconLoader.appliquerIcone(btnDetails, "icon_search");
        btnDetails.addActionListener(e -> voirDetails());

        JButton btnRafraichir = new JButton("Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir, "icon_refresh");
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnTerminer);
        panel.add(btnDetails);
        panel.add(btnRafraichir);
        return panel;
    }

    private JScrollPane creerPanelTable() {
        String[] colonnes = {"ID", "Chambre", "Description", "Date Début", "Date Fin", "Statut"};
        modele = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(modele) {
            // Tooltip sur la colonne description : affiche le texte intégral au survol
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row >= 0 && col == 2) {
                    Object v = getValueAt(row, col);
                    if (v != null) return "<html><div style='width:300px;padding:5px'>" + v + "</div></html>";
                }
                return super.getToolTipText(e);
            }
        };
        ThemeUtil.appliquerThemeTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(50); // un peu plus de hauteur pour lire les descriptions wrappées

        // Largeurs colonnes
        TableColumn colId       = table.getColumnModel().getColumn(0);
        TableColumn colChambre  = table.getColumnModel().getColumn(1);
        TableColumn colDesc     = table.getColumnModel().getColumn(2);
        TableColumn colDebut    = table.getColumnModel().getColumn(3);
        TableColumn colFin      = table.getColumnModel().getColumn(4);
        TableColumn colStatut   = table.getColumnModel().getColumn(5);
        colId.setPreferredWidth(50);
        colChambre.setPreferredWidth(80);
        colDesc.setPreferredWidth(450);     // ★ large pour la description
        colDebut.setPreferredWidth(110);
        colFin.setPreferredWidth(110);
        colStatut.setPreferredWidth(110);

        // Renderer multi-lignes (wrap) pour la colonne description
        colDesc.setCellRenderer(new MultilineCellRenderer());

        // Coloriser le statut
        colStatut.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected && value != null) {
                    String s = value.toString();
                    if ("TERMINEE".equals(s)) c.setForeground(ThemeUtil.VERT_VALIDATION);
                    else                       c.setForeground(ThemeUtil.ORANGE_ATTENTION);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        return scroll;
    }

    private void chargerDonnees() {
        modele.setRowCount(0);
        try {
            List<Maintenance> liste = maintenanceService.listerMaintenancesEnCours();
            for (Maintenance m : liste) {
                modele.addRow(new Object[]{
                        m.getIdMaintenance(),
                        m.getIdChambre(),
                        m.getDescription(),
                        m.getDateDebut(),
                        m.getDateFin(),
                        m.getStatutMaintenance()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void terminerIntervention() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une intervention", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idMaintenance = (Integer) modele.getValueAt(ligne, 0);
        int idChambre     = (Integer) modele.getValueAt(ligne, 1);

        int rep = JOptionPane.showConfirmDialog(this,
                "Confirmer que la maintenance de la chambre " + idChambre + " est TERMINÉE ?\n"
                        + "La chambre redeviendra DISPONIBLE.",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (rep != JOptionPane.YES_OPTION) return;

        try {
            if (maintenanceService.terminerMaintenance(idMaintenance)) {
                JOptionPane.showMessageDialog(this, "Maintenance clôturée. Chambre " + idChambre + " libérée.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerDonnees();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la clôture", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Affiche la description complète de l'intervention sélectionnée dans une popup.
     */
    private void voirDetails() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une intervention", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object desc = modele.getValueAt(ligne, 2);
        int idChambre = (Integer) modele.getValueAt(ligne, 1);
        Object dDeb = modele.getValueAt(ligne, 3);
        Object dFin = modele.getValueAt(ligne, 4);

        JTextArea ta = new JTextArea(desc == null ? "(aucune description)" : desc.toString());
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        ta.setFont(ThemeUtil.POLICE_NORMAL);
        ta.setBackground(Color.WHITE);
        ta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(ta);
        scroll.setPreferredSize(new Dimension(450, 200));

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBackground(Color.WHITE);
        content.add(new JLabel("Chambre " + idChambre + " — du " + dDeb + " au " + dFin), BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, content, "Détail de l'intervention", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Renderer multi-lignes : affiche le texte sur plusieurs lignes avec wrap.
     */
    private static class MultilineCellRenderer extends JTextArea implements TableCellRenderer {
        public MultilineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(ThemeUtil.POLICE_NORMAL);
            setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), Integer.MAX_VALUE);
            if (isSelected) {
                setBackground(ThemeUtil.DORE_LUXE);
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(ThemeUtil.TEXTE_SOMBRE);
            }
            return this;
        }
    }
}
