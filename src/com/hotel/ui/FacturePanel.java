package com.hotel.ui;

import com.hotel.model.Facture;
import com.hotel.service.FacturationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacturePanel extends JPanel {

    private JTextField txtIdReservation;
    private JTable table;
    private DefaultTableModel model;

    private FacturationService facturationService = new FacturationService();

    public FacturePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerFactures();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Gestion des factures");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(330, 0));

        JLabel formTitle = new JLabel("Générer une facture");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(2, 1, 6, 4));
        form.setBackground(Color.WHITE);

        txtIdReservation = UITheme.createTextField();

        form.add(new JLabel("ID Réservation"));
        form.add(txtIdReservation);

        JPanel buttons = new JPanel(new GridLayout(2, 1, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton btnGenerer = UITheme.createPrimaryButton("Générer facture");
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setFont(UITheme.BUTTON_FONT);

        buttons.add(btnGenerer);
        buttons.add(btnActualiser);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);
        formCard.add(buttons, BorderLayout.SOUTH);

        add(formCard, BorderLayout.WEST);

        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JLabel tableTitle = new JLabel("Liste des factures");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Réservation", "Hébergement", "Restaurant", "Total", "Statut"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        btnGenerer.addActionListener(e -> genererFacture());
        btnActualiser.addActionListener(e -> chargerFactures());
    }

    private void genererFacture() {
        int idReservation = Integer.parseInt(txtIdReservation.getText());

        facturationService.genererFacture(idReservation);
        chargerFactures();
        txtIdReservation.setText("");

        JOptionPane.showMessageDialog(this, "Facture générée avec succès.");
    }

    private void chargerFactures() {
        model.setRowCount(0);

        List<Facture> factures = facturationService.listerFactures();

        for (Facture f : factures) {
            model.addRow(new Object[]{
                    f.getIdFacture(),
                    f.getReservation().getIdReservation(),
                    f.getMontantHebergement(),
                    f.getMontantRestaurant(),
                    f.getMontantTotal(),
                    f.getStatut()
            });
        }
    }
}