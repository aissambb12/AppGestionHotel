package com.hotel.ui;

import com.hotel.model.Reservation;
import com.hotel.service.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservationPanel extends JPanel {

    private JTextField txtIdClient, txtIdChambre, txtDateDebut, txtDateFin;
    private JTable table;
    private DefaultTableModel model;

    private ReservationService reservationService = new ReservationService();

    public ReservationPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerReservations();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Gestion des réservations");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(330, 0));

        JLabel formTitle = new JLabel("Nouvelle réservation");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(8, 1, 8, 6));
        form.setBackground(Color.WHITE);

        txtIdClient = UITheme.createTextField();
        txtIdChambre = UITheme.createTextField();
        txtDateDebut = UITheme.createTextField();
        txtDateFin = UITheme.createTextField();

        form.add(new JLabel("ID Client"));
        form.add(txtIdClient);

        form.add(new JLabel("ID Chambre"));
        form.add(txtIdChambre);

        form.add(new JLabel("Date début yyyy-MM-dd"));
        form.add(txtDateDebut);

        form.add(new JLabel("Date fin yyyy-MM-dd"));
        form.add(txtDateFin);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton btnReserver = UITheme.createPrimaryButton("Réserver");
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setFont(UITheme.BUTTON_FONT);

        buttons.add(btnReserver);
        buttons.add(btnActualiser);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);
        formCard.add(buttons, BorderLayout.SOUTH);

        add(formCard, BorderLayout.WEST);

        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JLabel tableTitle = new JLabel("Liste des réservations");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Client", "Chambre", "Date début", "Date fin", "Statut"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        btnReserver.addActionListener(e -> creerReservation());
        btnActualiser.addActionListener(e -> chargerReservations());
    }

    private void creerReservation() {
        int idClient = Integer.parseInt(txtIdClient.getText());
        int idChambre = Integer.parseInt(txtIdChambre.getText());

        reservationService.creerReservation(
                idClient,
                idChambre,
                txtDateDebut.getText(),
                txtDateFin.getText()
        );

        chargerReservations();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Réservation créée avec succès.");
    }

    private void chargerReservations() {
        model.setRowCount(0);

        List<Reservation> reservations = reservationService.listerReservations();

        for (Reservation r : reservations) {
            model.addRow(new Object[]{
                    r.getIdReservation(),
                    r.getClient().getIdClient(),
                    r.getChambre().getIdChambre(),
                    r.getDateDebut(),
                    r.getDateFin(),
                    r.getStatut()
            });
        }
    }

    private void viderChamps() {
        txtIdClient.setText("");
        txtIdChambre.setText("");
        txtDateDebut.setText("");
        txtDateFin.setText("");
    }
}