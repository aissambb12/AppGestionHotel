package com.hotel.vue;

import com.hotel.model.Maintenance;
import com.hotel.model.Utilisateur;
import com.hotel.service.MaintenanceService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        JButton btnRafraichir = new JButton("Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir, "icon_refresh");
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnTerminer);
        panel.add(btnRafraichir);
        return panel;
    }

    private JScrollPane creerPanelTable() {
        String[] colonnes = {"ID", "ID Chambre", "Description", "Date Début", "Date Fin", "Statut"};
        modele = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(modele);
        ThemeUtil.appliquerThemeTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
}