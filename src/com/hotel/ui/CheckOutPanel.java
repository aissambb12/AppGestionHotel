package com.hotel.ui;

import com.hotel.model.Reservation;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.service.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CheckOutPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private ReservationService reservationService = new ReservationService();

    public CheckOutPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerReservationsEnCours();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Check-out des clients");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        JLabel tableTitle = new JLabel("Réservations en cours");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Client", "Chambre", "Date début", "Date fin", "Statut"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        JButton btnCheckOut = UITheme.createPrimaryButton("Effectuer check-out");

        card.add(tableTitle, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        card.add(btnCheckOut, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        btnCheckOut.addActionListener(e -> effectuerCheckOut());
    }

    private void effectuerCheckOut() {
        int ligne = table.getSelectedRow();

        if (ligne < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une réservation.");
            return;
        }

        int idReservation = Integer.parseInt(model.getValueAt(ligne, 0).toString());

        reservationService.effectuerCheckOut(idReservation);
        chargerReservationsEnCours();

        JOptionPane.showMessageDialog(this, "Check-out effectué. Facture générée.");
    }

    private void chargerReservationsEnCours() {
        model.setRowCount(0);

        List<Reservation> reservations = reservationService.listerReservations();

        for (Reservation r : reservations) {
            if (r.getStatut() == StatutReservation.EN_COURS) {
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