package com.hotel.vue;

import com.hotel.model.*;
import com.hotel.model.enumeration.ModePaiement;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.service.FacturationService;
import com.hotel.util.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class FactureFrame extends JFrame {

    private int idReservation;
    private FacturationService facturationService;
    private Facture facture;
    private List<ReservationServices> extras;

    public FactureFrame(int idReservation, FacturationService facturationService) {
        this.idReservation = idReservation;
        this.facturationService = facturationService;
        this.facture = facturationService.obtenirFactureReservation(idReservation);
        this.extras = facturationService.obtenirDetailsConsommations(idReservation);

        setTitle("Hotel Manager - Facture Réservation N°" + idReservation);
        setSize(900, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === PANEL FACTURE ===
        JPanel panelFacture = creerPanelFacture();
        add(new JScrollPane(panelFacture), BorderLayout.CENTER);

        // === PANEL PAIEMENT ===
        JPanel panelPaiement = creerPanelPaiement();
        add(panelPaiement, BorderLayout.SOUTH);
    }

    private JPanel creerPanelFacture() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === EN-TÊTE FACTURE ===
        JLabel lblEntete = new JLabel("FACTURE OFFICIELLE");
        lblEntete.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblEntete.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblEntete, gbc);

        // Numéro facture
        JLabel lblNumFacture = new JLabel("N° " + facture.getIdFacture());
        lblNumFacture.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNumFacture.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridy = 1;
        panel.add(lblNumFacture, gbc);

        // Séparateur
        gbc.gridy = 2;
        JSeparator sep = new JSeparator();
        panel.add(sep, gbc);

        // === INFORMATIONS ===
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        ajouterLigne(panel, gbc, "Réservation N°", String.valueOf(idReservation));

        gbc.gridy = 4;
        ajouterLigne(panel, gbc, "Date facture", facture.getDateFacture().toString());

        gbc.gridy = 5;
        ajouterLigne(panel, gbc, "Statut", facture.getStatutFacture().toString());

        // Séparateur
        gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);

        // === DÉTAILS ===
        gbc.gridy = 7;
        JLabel lblDetails = new JLabel("DÉTAILS DES FRAIS");
        lblDetails.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridwidth = 2;
        panel.add(lblDetails, gbc);

        gbc.gridy = 8;
        gbc.gridwidth = 1;
        JLabel lblHebergement = new JLabel("Hébergement :");
        lblHebergement.setFont(ThemeUtil.POLICE_NORMALE);
        panel.add(lblHebergement, gbc);

        double prixHebergement = facture.getMontantTotal();
        JLabel lblPrixHebergement = new JLabel(String.format("%.2f MAD", prixHebergement));
        lblPrixHebergement.setFont(ThemeUtil.POLICE_NORMALE);
        gbc.gridx = 1;
        panel.add(lblPrixHebergement, gbc);

        // Services supplémentaires
        gbc.gridx = 0; gbc.gridy = 9;
        JLabel lblServices = new JLabel("Services :");
        lblServices.setFont(ThemeUtil.POLICE_NORMALE);
        panel.add(lblServices, gbc);

        double prixServices = 0.0;
        gbc.gridy = 10;
        if (extras != null && !extras.isEmpty()) {
            for (ReservationServices extra : extras) {
                ServiceSupplementaire service = chargerService(extra.getIdService());
                if (service != null) {
                    double prix = service.getPrixService() * extra.getQuantite();
                    prixServices += prix;

                    gbc.gridx = 0;
                    JLabel lblService = new JLabel("  - " + service.getNomService() + " x" + extra.getQuantite());
                    lblService.setFont(ThemeUtil.POLICE_PETIT);
                    panel.add(lblService, gbc);

                    gbc.gridx = 1;
                    JLabel lblPrixService = new JLabel(String.format("%.2f MAD", prix));
                    lblPrixService.setFont(ThemeUtil.POLICE_PETIT);
                    panel.add(lblPrixService, gbc);

                    gbc.gridy++;
                }
            }
        } else {
            gbc.gridx = 1;
            JLabel lblAucun = new JLabel("Aucun");
            lblAucun.setFont(ThemeUtil.POLICE_PETIT);
            panel.add(lblAucun, gbc);
        }

        // Séparateur
        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);

        // === TOTAL ===
        gbc.gridy++;
        double totalFinal = prixHebergement + prixServices;

        JLabel lblTotal = new JLabel("MONTANT TOTAL");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        panel.add(lblTotal, gbc);

        JLabel lblMontantTotal = new JLabel(String.format("%.2f MAD", totalFinal));
        lblMontantTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMontantTotal.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridx = 1;
        panel.add(lblMontantTotal, gbc);

        return panel;
    }

    private JPanel creerPanelPaiement() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblMode = new JLabel("Mode de Paiement");
        lblMode.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblMode, gbc);

        JComboBox<ModePaiement> comboMode = new JComboBox<>(ModePaiement.values());
        comboMode.setFont(ThemeUtil.POLICE_NORMALE);
        gbc.gridx = 1;
        panel.add(comboMode, gbc);

        JButton btnPayer = new JButton("✓ CONFIRMER LE PAIEMENT");
        ThemeUtil.appliquerThemeBoutonValider(btnPayer);
        btnPayer.addActionListener(e -> effectuerPaiement((ModePaiement) comboMode.getSelectedItem()));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(btnPayer, gbc);

        JButton btnAnnuler = new JButton("✕ ANNULER");
        ThemeUtil.appliquerThemeBoutonSuppression(btnAnnuler);
        btnAnnuler.addActionListener(e -> {
            facture.setStatutFacture(StatutFacture.ANNULEE);
            dispose();
        });
        gbc.gridy = 2;
        panel.add(btnAnnuler, gbc);

        return panel;
    }

    private void ajouterLigne(JPanel panel, GridBagConstraints gbc, String label, String valeur) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        panel.add(lblLabel, gbc);

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(ThemeUtil.POLICE_NORMALE);
        gbc.gridx = 1;
        panel.add(lblValeur, gbc);
    }

    private void effectuerPaiement(ModePaiement modePaiement) {
        try {
            Paiement paiement = new Paiement();
            paiement.setIdFacture(facture.getIdFacture());
            paiement.setMontantPaye(facture.getMontantTotal());
            paiement.setDatePaiement(LocalDateTime.now());
            paiement.setModePaiement(modePaiement);

            boolean succes = facturationService.enregistrerPaiement(paiement);
            if (succes) {
                facture.setStatutFacture(StatutFacture.PAYEE);
                JOptionPane.showMessageDialog(this, "✓ Paiement effectué avec succès\nStatut: PAYÉE", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ServiceSupplementaire chargerService(int idService) {
        try {
            return new com.hotel.dao.ServiceSupplementaireDAOImpl().trouverParId(idService);
        } catch (Exception ex) {
            return null;
        }
    }
}