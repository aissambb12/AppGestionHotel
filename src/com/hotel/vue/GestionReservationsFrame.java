package com.hotel.vue;

import com.hotel.model.Reservation;
import com.hotel.model.ReservationServices;
import com.hotel.service.ReservationService;
import com.hotel.service.FacturationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionReservationsFrame extends JFrame {

    private ReservationService reservationService;
    private FacturationService facturationService;
    private DefaultTableModel modeleReservations;
    private JTable tableReservations;

    public GestionReservationsFrame() {
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Gestion Réservations");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === PANEL BOUTONS ===
        JPanel panelBoutons = creerPanelBoutons();
        add(panelBoutons, BorderLayout.SOUTH);

        // === TABLE ===
        String[] colonnes = {"ID Resa", "ID Client", "Date Création", "Statut"};
        modeleReservations = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableReservations = new JTable(modeleReservations);
        ThemeUtil.appliquerThemeTable(tableReservations);

        JScrollPane scrollPane = new JScrollPane(tableReservations);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("📋 GESTION DES RÉSERVATIONS");
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

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnDetails = new JButton("🔍 Voir Détails");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnDetails);
        btnDetails.addActionListener(e -> afficherDetails());

        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnDetails);
        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modeleReservations.setRowCount(0);
        List<Reservation> reservations = reservationService.listerToutesLesReservations();
        for (Reservation r : reservations) {
            modeleReservations.addRow(new Object[]{
                    r.getIdReservation(),
                    r.getIdClient(),
                    r.getDateCreation(),
                    r.getStatut()
            });
        }
    }

    private void afficherDetails() {
        int ligne = tableReservations.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation");
            return;
        }

        int idResa = (Integer) modeleReservations.getValueAt(ligne, 0);
        List<ReservationServices> extras = facturationService.obtenirDetailsConsommations(idResa);

        StringBuilder sb = new StringBuilder();
        sb.append("Détails Réservation N°").append(idResa).append("\n\n");

        if (extras != null && !extras.isEmpty()) {
            sb.append("Services consommés :\n");
            for (ReservationServices rs : extras) {
                sb.append("  • Service ").append(rs.getIdService())
                        .append(" - Qté: ").append(rs.getQuantite())
                        .append(" - Date: ").append(rs.getDateConsommation()).append("\n");
            }
        } else {
            sb.append("Aucun service supplémentaire");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }
}