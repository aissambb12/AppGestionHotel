package com.hotel.ui;

import com.hotel.model.Reservation;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.service.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CheckInPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private ReservationService reservationService = new ReservationService();

    public CheckInPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerReservationsReservees();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Check-in des clients");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        JLabel tableTitle = new JLabel("Réservations en attente de check-in");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Client", "Chambre", "Date début", "Date fin", "Statut"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        JButton btnCheckIn = UITheme.createPrimaryButton("Effectuer check-in");

        card.add(tableTitle, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        card.add(btnCheckIn, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        btnCheckIn.addActionListener(e -> effectuerCheckIn());
    }

    private void effectuerCheckIn() {
        int ligne = table.getSelectedRow();

        if (ligne < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une réservation.");
            return;
        }

        int idReservation = Integer.parseInt(model.getValueAt(ligne, 0).toString());

        reservationService.effectuerCheckIn(idReservation);
        chargerReservationsReservees();

        JOptionPane.showMessageDialog(this, "Check-in effectué avec succès.");
    }

    private void chargerReservationsReservees() {
        model.setRowCount(0);

        List<Reservation> reservations = reservationService.listerReservations();

        for (Reservation r : reservations) {
            if (r.getStatut() == StatutReservation.RESERVEE) {
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
    }
}