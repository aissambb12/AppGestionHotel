package com.hotel.vue;

import com.hotel.model.Reservation;
import com.hotel.model.ReservationServices;
import com.hotel.model.Utilisateur;
import com.hotel.service.ReservationService;
import com.hotel.service.FacturationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GestionCheckFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private ReservationService reservationService;
    private FacturationService facturationService;
    private DefaultTableModel modeleReservations;
    private JTable tableReservations;

    public GestionCheckFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Check-in / Check-out");
        setSize(1000, 700);
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
        add(panelBoutons, BorderLayout.CENTER);

        // === TABLE ===
        JPanel panelTable = creerPanelTable();
        add(panelTable, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🔑 CHECK-IN / CHECK-OUT");
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
        btnRetour.addActionListener(e -> {
            new DashboardReceptionnisteFrame(receptionnisteConnecte).setVisible(true);
            dispose();
        });

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("Opérations de Séjour");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(lblTitre, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Bouton Check-in
        JButton btnCheckIn = new JButton("📥 Faire Check-in");
        btnCheckIn.setBackground(ThemeUtil.VERT_VALIDATION);
        btnCheckIn.setForeground(ThemeUtil.BLANC);
        btnCheckIn.setFont(ThemeUtil.POLICE_BOUTON);
        btnCheckIn.setFocusPainted(false);
        btnCheckIn.setOpaque(true);
        btnCheckIn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnCheckIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckIn.addActionListener(e -> effectuerCheckIn());
        gbc.gridx = 0;
        panel.add(btnCheckIn, gbc);

        // Bouton Ajouter Extra
        JButton btnExtra = new JButton("☕ Ajouter Extra");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnExtra);
        btnExtra.addActionListener(e -> ajouterExtra());
        gbc.gridx = 1;
        panel.add(btnExtra, gbc);

        // Bouton Check-out
        JButton btnCheckOut = new JButton("📤 Faire Check-out");
        btnCheckOut.setBackground(ThemeUtil.ORANGE_ATTENTION);
        btnCheckOut.setForeground(ThemeUtil.BLANC);
        btnCheckOut.setFont(ThemeUtil.POLICE_BOUTON);
        btnCheckOut.setFocusPainted(false);
        btnCheckOut.setOpaque(true);
        btnCheckOut.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnCheckOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckOut.addActionListener(e -> effectuerCheckOut());
        gbc.gridx = 2;
        panel.add(btnCheckOut, gbc);

        return panel;
    }

    private JPanel creerPanelTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(0, 350));

        // Bouton Rafraîchir
        JPanel panelBtnRafr = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtnRafr.setBackground(Color.WHITE);
        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());
        panelBtnRafr.add(btnRafraichir);
        panel.add(panelBtnRafr, BorderLayout.NORTH);

        // Table
        String[] colonnes = {"ID Resa", "ID Client", "Statut", "Date Création"};
        modeleReservations = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableReservations = new JTable(modeleReservations);
        ThemeUtil.appliquerThemeTable(tableReservations);

        JScrollPane scrollPane = new JScrollPane(tableReservations);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void chargerDonnees() {
        modeleReservations.setRowCount(0);
        List<Reservation> reservations = reservationService.listerToutesLesReservations();
        for (Reservation r : reservations) {
            modeleReservations.addRow(new Object[]{
                    r.getIdReservation(),
                    r.getIdClient(),
                    r.getStatut(),
                    r.getDateCreation()
            });
        }
    }

    private void effectuerCheckIn() {
        int ligne = tableReservations.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une réservation", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idResa = (Integer) modeleReservations.getValueAt(ligne, 0);
            boolean succes = reservationService.validerCheckIn(idResa);
            if (succes) {
                JOptionPane.showMessageDialog(this, "✓ Check-in validé - Clés remises", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerDonnees();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterExtra() {
        int ligne = tableReservations.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une réservation", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idResa = (Integer) modeleReservations.getValueAt(ligne, 0);
            String idServiceStr = JOptionPane.showInputDialog(this, "ID du Service (1=Resto, 2=Parking, 3=Spa) :");
            if (idServiceStr == null) return;

            String qtyStr = JOptionPane.showInputDialog(this, "Quantité :");
            if (qtyStr == null) return;

            ReservationServices extra = new ReservationServices();
            extra.setIdReservation(idResa);
            extra.setIdService(Integer.parseInt(idServiceStr));
            extra.setQuantite(Integer.parseInt(qtyStr));
            extra.setDateConsommation(LocalDate.now());

            boolean succes = facturationService.ajouterConsommation(extra);
            if (succes) {
                JOptionPane.showMessageDialog(this, "✓ Extra ajouté à la facture", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effectuerCheckOut() {
        int ligne = tableReservations.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une réservation", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idResa = (Integer) modeleReservations.getValueAt(ligne, 0);
            boolean succes = facturationService.realiserCheckOut(idResa);
            if (succes) {
                JOptionPane.showMessageDialog(this, "✓ Check-out réalisé - Chambre libérée", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerDonnees();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}