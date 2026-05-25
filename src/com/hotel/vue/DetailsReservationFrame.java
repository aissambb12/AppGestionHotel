package com.hotel.vue;

import com.hotel.model.ReservationServices;
import com.hotel.service.FacturationService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DetailsReservationFrame extends JFrame {

    private int idReservation;
    private FacturationService facturationService;

    public DetailsReservationFrame(int idReservation, FacturationService facturationService) {
        this.idReservation = idReservation;
        this.facturationService = facturationService;

        setTitle("Hotel Manager - Détails Réservation N°" + idReservation);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === CONTENU ===
        JPanel panelContenu = creerContenu();
        add(new JScrollPane(panelContenu), BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("📋 DÉTAILS RÉSERVATION N°" + idReservation);
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        panel.add(lblTitre, BorderLayout.WEST);

        return panel;
    }

    private JPanel creerContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === TITRE SERVICES ===
        JLabel lblServices = new JLabel("SERVICES SUPPLÉMENTAIRES CONSOMMÉS");
        lblServices.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblServices, gbc);

        // Récupérer les extras
        List<ReservationServices> extras = facturationService.obtenirDetailsConsommations(idReservation);

        gbc.gridy = 1;
        gbc.gridwidth = 1;

        if (extras == null || extras.isEmpty()) {
            JLabel lblAucun = new JLabel("Aucun service supplémentaire");
            lblAucun.setFont(ThemeUtil.POLICE_PETIT);
            panel.add(lblAucun, gbc);
        } else {
            for (ReservationServices extra : extras) {
                JLabel lblExtra = new JLabel("Service ID: " + extra.getIdService());
                lblExtra.setFont(ThemeUtil.POLICE_NORMALE);
                gbc.gridx = 0; gbc.gridy++;
                panel.add(lblExtra, gbc);

                JLabel lblQte = new JLabel("Quantité: " + extra.getQuantite());
                lblQte.setFont(ThemeUtil.POLICE_PETIT);
                gbc.gridx = 1;
                panel.add(lblQte, gbc);

                JLabel lblDate = new JLabel("Date: " + extra.getDateConsommation());
                lblDate.setFont(ThemeUtil.POLICE_PETIT);
                gbc.gridx = 0; gbc.gridy++;
                gbc.gridwidth = 2;
                panel.add(lblDate, gbc);
                gbc.gridwidth = 1;
            }
        }

        return panel;
    }
}