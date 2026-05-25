package com.hotel.vue;

import com.hotel.model.*;
import com.hotel.service.FacturationService;

import javax.swing.*;
import java.awt.*;

public class CheckoutFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private FacturationService facturationService;

    private JTextField txtIdReservation;
    private JButton btnCharger;

    public CheckoutFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Check-Out & Facturation");
        setSize(600, 400);
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
        JPanel panelContenu = new JPanel(new GridBagLayout());
        panelContenu.setBackground(Color.WHITE);
        panelContenu.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("Entrez l'ID de la réservation pour le check-out");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelContenu.add(lblTitre, gbc);

        JLabel lblId = new JLabel("ID Réservation :");
        lblId.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridy = 1; gbc.gridwidth = 1;
        panelContenu.add(lblId, gbc);

        txtIdReservation = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtIdReservation);
        gbc.gridx = 1;
        panelContenu.add(txtIdReservation, gbc);

        btnCharger = new JButton("📤 Confirmer Check-Out");
        ThemeUtil.appliquerThemeBoutonValider(btnCharger);
        btnCharger.addActionListener(e -> effectuerCheckout());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelContenu.add(btnCharger, gbc);

        add(panelContenu, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("💳 CHECK-OUT & FACTURATION");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        JButton btnRetour = new JButton("← Retour");
        btnRetour.setBackground(ThemeUtil.GRIS_CLAIR);
        btnRetour.setForeground(ThemeUtil.TEXTE_SOMBRE);
        btnRetour.setFont(ThemeUtil.POLICE_BOUTON);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.addActionListener(e -> dispose());

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private void effectuerCheckout() {
        try {
            int idResa = Integer.parseInt(txtIdReservation.getText());

            // Réaliser le checkout
            boolean succes = facturationService.realiserCheckOut(idResa);

            if (succes) {
                // Afficher la facture
                new FactureFrame(idResa, facturationService).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors du check-out", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}