package com.hotel.ui;

import com.hotel.model.Maintenance;
import com.hotel.service.MaintenanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MaintenancePanel extends JPanel {

    private JTextField txtIdChambre, txtDescription;
    private JTable table;
    private DefaultTableModel model;

    private MaintenanceService maintenanceService = new MaintenanceService();

    public MaintenancePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerMaintenances();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Maintenance des chambres");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(330, 0));

        JLabel formTitle = new JLabel("Nouvelle maintenance");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(4, 1, 6, 4));
        form.setBackground(Color.WHITE);

        txtIdChambre = UITheme.createTextField();
        txtDescription = UITheme.createTextField();

        form.add(new JLabel("ID Chambre"));
        form.add(txtIdChambre);

        form.add(new JLabel("Description"));
        form.add(txtDescription);

        JPanel buttons = new JPanel(new GridLayout(2, 1, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton btnMettre = UITheme.createPrimaryButton("Mettre en maintenance");
        JButton btnTerminer = UITheme.createPrimaryButton("Terminer maintenance");

        buttons.add(btnMettre);
        buttons.add(btnTerminer);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);
        formCard.add(buttons, BorderLayout.SOUTH);

        add(formCard, BorderLayout.WEST);

        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JLabel tableTitle = new JLabel("Liste des maintenances");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Chambre", "Date début", "Date fin", "Description", "Statut"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        btnMettre.addActionListener(e -> mettreEnMaintenance());
        btnTerminer.addActionListener(e -> terminerMaintenance());
    }

    private void mettreEnMaintenance() {
        int idChambre = Integer.parseInt(txtIdChambre.getText());
        String description = txtDescription.getText();

        maintenanceService.mettreEnMaintenance(idChambre, description);
        chargerMaintenances();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Chambre mise en maintenance.");
    }

    private void terminerMaintenance() {
        int ligne = table.getSelectedRow();

        if (ligne < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une maintenance.");
            return;
        }

        int idMaintenance = Integer.parseInt(model.getValueAt(ligne, 0).toString());

        maintenanceService.terminerMaintenance(idMaintenance);
        chargerMaintenances();

        JOptionPane.showMessageDialog(this, "Maintenance terminée.");
    }

    private void chargerMaintenances() {
        model.setRowCount(0);

        List<Maintenance> maintenances = maintenanceService.listerMaintenances();

        for (Maintenance m : maintenances) {
            model.addRow(new Object[]{
                    m.getIdMaintenance(),
                    m.getChambre().getIdChambre(),
                    m.getDateDebut(),
                    m.getDateFin(),
                    m.getDescription(),
                    m.getStatut()
            });
        }
    }

    private void viderChamps() {
        txtIdChambre.setText("");
        txtDescription.setText("");
    }
}