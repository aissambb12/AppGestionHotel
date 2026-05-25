package com.hotel.vue;

import com.hotel.model.ReservationServices;
import com.hotel.service.FacturationService;
import com.hotel.dao.ReservationChambreDAOImpl;
import com.hotel.model.ReservationChambre;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class DetailsReservationFrame extends JFrame {

    private int idReservation;
    private FacturationService facturationService;
    private ReservationChambreDAOImpl reservationChambreDAO;

    public DetailsReservationFrame(int idReservation, FacturationService facturationService) {
        this.idReservation = idReservation;
        this.facturationService = facturationService;
        this.reservationChambreDAO = new ReservationChambreDAOImpl();

        setTitle("Hotel Manager - Détails Réservation N°" + idReservation);
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenu = creerContenu();
        add(new JScrollPane(panelContenu), BorderLayout.CENTER);

        JPanel panelBoutons = creerPanelBoutons();
        add(panelBoutons, BorderLayout.SOUTH);
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
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === INFORMATIONS RÉSERVATION ===
        JLabel lblSecResa = new JLabel("📅 INFORMATIONS RÉSERVATION");
        lblSecResa.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblSecResa.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblSecResa, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        ajouterLigne(panel, gbc, "ID Réservation :", String.valueOf(idReservation));

        gbc.gridy = 2;
        ajouterLigne(panel, gbc, "Statut :", "✓ CONFIRMÉE");

        gbc.gridy = 3;
        ajouterLigne(panel, gbc, "Date Création :", LocalDate.now().toString());

        // === CHAMBRES ===
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JSeparator sep1 = new JSeparator();
        panel.add(sep1, gbc);

        gbc.gridy = 5;
        JLabel lblChambre = new JLabel("🛏️ CHAMBRES RÉSERVÉES");
        lblChambre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblChambre.setForeground(ThemeUtil.BLEU_NUIT);
        panel.add(lblChambre, gbc);

        try {
            List<ReservationChambre> chambres = reservationChambreDAO.listerParReservation(idReservation);

            gbc.gridwidth = 1;
            if (chambres != null && !chambres.isEmpty()) {
                gbc.gridy = 6;
                for (ReservationChambre rc : chambres) {
                    JLabel lblNumChambre = new JLabel("• Chambre ID " + rc.getIdChambre());
                    lblNumChambre.setFont(ThemeUtil.POLICE_NORMAL);
                    gbc.gridx = 0;
                    panel.add(lblNumChambre, gbc);

                    JLabel lblPrix = new JLabel(String.format("%.2f MAD", rc.getPrixApplique()));
                    lblPrix.setFont(ThemeUtil.POLICE_NORMAL);
                    gbc.gridx = 1;
                    panel.add(lblPrix, gbc);

                    gbc.gridy++;

                    JLabel lblDates = new JLabel("  Du " + rc.getDateArrivee() + " au " + rc.getDateDepart());
                    lblDates.setFont(ThemeUtil.POLICE_PETIT);
                    gbc.gridx = 0;
                    gbc.gridwidth = 2;
                    panel.add(lblDates, gbc);

                    gbc.gridy++;
                    gbc.gridwidth = 1;
                }
            } else {
                gbc.gridy = 6;
                JLabel lblAucune = new JLabel("Aucune chambre trouvée");
                lblAucune.setFont(ThemeUtil.POLICE_PETIT);
                panel.add(lblAucune, gbc);
                gbc.gridy++;
            }
        } catch (Exception ex) {
            gbc.gridy = 6;
            JLabel lblErreur = new JLabel("❌ Erreur : " + ex.getMessage());
            lblErreur.setFont(ThemeUtil.POLICE_PETIT);
            panel.add(lblErreur, gbc);
            gbc.gridy++;
        }

        // === SERVICES SUPPLÉMENTAIRES ===
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JSeparator sep2 = new JSeparator();
        panel.add(sep2, gbc);

        gbc.gridy++;
        JLabel lblServices = new JLabel("☕ SERVICES SUPPLÉMENTAIRES");
        lblServices.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblServices.setForeground(ThemeUtil.BLEU_NUIT);
        panel.add(lblServices, gbc);

        try {
            List<ReservationServices> extras = facturationService.obtenirDetailsConsommations(idReservation);

            gbc.gridwidth = 1;
            gbc.gridy++;

            if (extras != null && !extras.isEmpty()) {
                for (ReservationServices extra : extras) {
                    JLabel lblExtra = new JLabel("• Service ID " + extra.getIdService());
                    lblExtra.setFont(ThemeUtil.POLICE_NORMAL);
                    gbc.gridx = 0;
                    panel.add(lblExtra, gbc);

                    JLabel lblQte = new JLabel("Quantité : " + extra.getQuantite());
                    lblQte.setFont(ThemeUtil.POLICE_NORMAL);
                    gbc.gridx = 1;
                    panel.add(lblQte, gbc);

                    gbc.gridy++;
                }
            } else {
                JLabel lblAucun = new JLabel("Aucun service supplémentaire");
                lblAucun.setFont(ThemeUtil.POLICE_PETIT);
                gbc.gridx = 0;
                panel.add(lblAucun, gbc);
                gbc.gridy++;
            }
        } catch (Exception ex) {
            JLabel lblErreur = new JLabel("❌ Erreur : " + ex.getMessage());
            lblErreur.setFont(ThemeUtil.POLICE_PETIT);
            gbc.gridx = 0;
            panel.add(lblErreur, gbc);
            gbc.gridy++;
        }

        // === MONTANT TOTAL ===
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JSeparator sep3 = new JSeparator();
        sep3.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(sep3, gbc);

        gbc.gridy++;
        JLabel lblTotal = new JLabel("💰 MONTANT TOTAL FACTURE :");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(lblTotal, gbc);

        try {
            double montantTotal = facturationService.obtenirFactureReservation(idReservation).getMontantTotal();
            JLabel lblMontant = new JLabel(String.format("%.2f MAD", montantTotal));
            lblMontant.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblMontant.setForeground(ThemeUtil.DORE_LUXE);
            gbc.gridx = 1;
            panel.add(lblMontant, gbc);
        } catch (Exception ex) {
            JLabel lblMontant = new JLabel("N/A");
            gbc.gridx = 1;
            panel.add(lblMontant, gbc);
        }

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /**
         * IMAGE À AJOUTER : back.png (48x48px)
         * Description: Icône d'une flèche gauche
         */
        JButton btnFermer = new JButton("← Fermer");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnFermer);
        btnFermer.setPreferredSize(new Dimension(150, 40));
        btnFermer.addActionListener(e -> dispose());

        panel.add(btnFermer);

        return panel;
    }

    private void ajouterLigne(JPanel panel, GridBagConstraints gbc, String label, String valeur) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        panel.add(lblLabel, gbc);

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(ThemeUtil.POLICE_NORMAL);
        gbc.gridx = 1;
        panel.add(lblValeur, gbc);
    }
}