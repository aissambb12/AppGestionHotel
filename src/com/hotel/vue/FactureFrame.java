package com.hotel.vue;

import com.hotel.dao.impl.ServiceSupplementaireDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.ModePaiement;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.service.FacturationService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        setTitle("Hotel Manager - Facture N°" + idReservation);
        setSize(900, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JPanel panelFacture = creerPanelFacture();
        add(new JScrollPane(panelFacture), BorderLayout.CENTER);

        JPanel panelPaiement = creerPanelPaiement();
        add(panelPaiement, BorderLayout.SOUTH);
    }

    private JPanel creerPanelFacture() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === EN-TÊTE FACTURE ===
        JPanel panelEnTete = creerEnTeteFacture();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(panelEnTete, gbc);

        // Séparateur
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(sep1, gbc);

        // === INFORMATIONS ===
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblResa = new JLabel("Réservation :");
        lblResa.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        panel.add(lblResa, gbc);

        JLabel valResa = new JLabel("N° " + idReservation);
        valResa.setFont(ThemeUtil.POLICE_NORMAL);
        gbc.gridx = 1;
        panel.add(valResa, gbc);

        gbc.gridy = 3;
        JLabel lblDate = new JLabel("Date :");
        lblDate.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        panel.add(lblDate, gbc);

        JLabel valDate = new JLabel(facture.getDateFacture() != null ?
                facture.getDateFacture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A");
        valDate.setFont(ThemeUtil.POLICE_NORMAL);
        gbc.gridx = 1;
        panel.add(valDate, gbc);

        gbc.gridy = 4;
        JLabel lblStatut = new JLabel("Statut :");
        lblStatut.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        panel.add(lblStatut, gbc);

        JLabel valStatut = new JLabel(facture.getStatutFacture().toString());
        valStatut.setFont(ThemeUtil.POLICE_NORMAL);
        valStatut.setForeground(valStatut.getText().contains("PAYEE") ? ThemeUtil.VERT_VALIDATION : ThemeUtil.ROUGE_ERREUR);
        gbc.gridx = 1;
        panel.add(valStatut, gbc);

        // Séparateur
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JSeparator sep2 = new JSeparator();
        panel.add(sep2, gbc);

        // === DÉTAILS DES FRAIS ===
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JLabel lblDetails = new JLabel("📋 DÉTAILS DES FRAIS");
        lblDetails.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblDetails.setForeground(ThemeUtil.BLEU_NUIT);
        panel.add(lblDetails, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 1;
        JLabel lblHebergement = new JLabel("Hébergement :");
        lblHebergement.setFont(ThemeUtil.POLICE_NORMAL);
        gbc.gridx = 0;
        panel.add(lblHebergement, gbc);

        double prixHebergement = facture.getMontantTotal();
        JLabel lblPrixHebergement = new JLabel(String.format("%.2f MAD", prixHebergement));
        lblPrixHebergement.setFont(ThemeUtil.POLICE_NORMAL);
        lblPrixHebergement.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridx = 1;
        panel.add(lblPrixHebergement, gbc);

        // Services supplémentaires
        gbc.gridy = 8;
        gbc.gridx = 0;
        JLabel lblServices = new JLabel("Services :");
        lblServices.setFont(ThemeUtil.POLICE_NORMAL);
        panel.add(lblServices, gbc);

        double prixServices = 0.0;
        gbc.gridy = 9;

        if (extras != null && !extras.isEmpty()) {
            for (ReservationServices extra : extras) {
                ServiceSupplementaire service = chargerService(extra.getIdService());
                if (service != null) {
                    double prix = service.getPrixService() * extra.getQuantite();
                    prixServices += prix;

                    gbc.gridx = 0;
                    JLabel lblService = new JLabel("  · " + service.getNomService() + " x" + extra.getQuantite());
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
            gbc.gridy++;
        }

        // Séparateur
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JSeparator sep3 = new JSeparator();
        sep3.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(sep3, gbc);

        gbc.gridy++;

        // === TOTAL ===
        double totalFinal = prixHebergement + prixServices;

        JLabel lblTotal = new JLabel("MONTANT TOTAL :");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(lblTotal, gbc);

        JLabel lblMontantTotal = new JLabel(String.format("%.2f MAD", totalFinal));
        lblMontantTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMontantTotal.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridx = 1;
        panel.add(lblMontantTotal, gbc);

        return panel;
    }

    private JPanel creerEnTeteFacture() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblLogo = new JLabel("🏨");
        lblLogo.setFont(new Font("Arial", Font.PLAIN, 40));
        lblLogo.setForeground(ThemeUtil.DORE_LUXE);

        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBackground(ThemeUtil.BLEU_NUIT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitre = new JLabel("FACTURE OFFICIELLE");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(ThemeUtil.BLANC);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelInfo.add(lblTitre, gbc);

        JLabel lblNumFacture = new JLabel("N° " + facture.getIdFacture());
        lblNumFacture.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNumFacture.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridy = 1;
        panelInfo.add(lblNumFacture, gbc);

        panel.add(lblLogo, BorderLayout.WEST);
        panel.add(panelInfo, BorderLayout.CENTER);

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

        JLabel lblMode = new JLabel("💳 Mode de Paiement :");
        lblMode.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(lblMode, gbc);

        JComboBox<ModePaiement> comboMode = new JComboBox<>(ModePaiement.values());
        comboMode.setFont(ThemeUtil.POLICE_NORMAL);
        gbc.gridx = 1;
        panel.add(comboMode, gbc);

        /**
         * IMAGE À AJOUTER : save.png (48x48px)
         * Description: Icône de sauvegarde/validation verte
         */
        JButton btnPayer = new JButton("✓ PAIEMENT EFFECTUÉ");
        ThemeUtil.appliquerThemeBoutonValider(btnPayer);
        btnPayer.setPreferredSize(new Dimension(150, 40));
        btnPayer.addActionListener(e -> effectuerPaiement((ModePaiement) comboMode.getSelectedItem()));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(btnPayer, gbc);

        /**
         * IMAGE À AJOUTER : cancel.png (48x48px)
         * Description: Icône d'une croix rouge
         */
        JButton btnAnnuler = new JButton("✕ ANNULER FACTURE");
        ThemeUtil.appliquerThemeBoutonSuppression(btnAnnuler);
        btnAnnuler.setPreferredSize(new Dimension(150, 40));
        btnAnnuler.addActionListener(e -> {
            facture.setStatutFacture(StatutFacture.ANNULEE);
            dispose();
        });
        gbc.gridx = 1;
        panel.add(btnAnnuler, gbc);

        return panel;
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
                JOptionPane.showMessageDialog(this,
                        "✓ Paiement effectué avec succès\n\n" +
                                "Mode: " + modePaiement + "\n" +
                                "Montant: " + String.format("%.2f MAD", facture.getMontantTotal()) + "\n" +
                                "Statut: PAYÉE",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ServiceSupplementaire chargerService(int idService) {
        try {
            return new ServiceSupplementaireDAOImpl().trouverParId(idService);
        } catch (Exception ex) {
            return null;
        }
    }
}