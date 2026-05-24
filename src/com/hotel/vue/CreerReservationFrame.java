package com.hotel.vue;

import com.hotel.model.Chambre;
import com.hotel.model.Reservation;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.service.ChambreService;
import com.hotel.service.ReservationService;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreerReservationFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private ChambreService chambreService;
    private ReservationService reservationService;
    private DefaultTableModel modeleDispo;
    private JTable tableDispo;

    private JTextField txtDateArrivee, txtDateDepart, txtIdClient;
    private JComboBox<String> comboCategorie;

    public CreerReservationFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.chambreService = new ChambreService();
        this.reservationService = new ReservationService();

        setTitle("Hotel Manager - Créer Réservation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === FORMULAIRE RECHERCHE ===
        JPanel panelRecherche = creerFormRecherche();
        add(panelRecherche, BorderLayout.CENTER);

        // === PANEL TABLE & BOUTON ===
        JPanel panelTable = creerPanelTable();
        add(panelTable, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("📝 CRÉER UNE RÉSERVATION");
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

    private JPanel creerFormRecherche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel lblTitre = new JLabel("Recherche de Disponibilités");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(lblTitre, gbc);

        // Champs
        txtDateArrivee = new JTextField("2026-06-01", 12);
        txtDateDepart = new JTextField("2026-06-05", 12);
        comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});
        txtIdClient = new JTextField(12);

        ThemeUtil.appliquerThemeTextField(txtDateArrivee);
        ThemeUtil.appliquerThemeTextField(txtDateDepart);
        ThemeUtil.appliquerThemeTextField(txtIdClient);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        ajouterChamp(panel, gbc, "Arrivée :", txtDateArrivee, 0);
        ajouterChamp(panel, gbc, "Départ :", txtDateDepart, 1);
        ajouterChamp(panel, gbc, "Catégorie :", comboCategorie, 2);
        ajouterChamp(panel, gbc, "ID Client :", txtIdClient, 3);

        // Bouton Chercher
        JButton btnChercher = new JButton("🔍 Chercher");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        panel.add(btnChercher, gbc);

        btnChercher.addActionListener(e -> rechercherChambres());

        return panel;
    }

    private void ajouterChamp(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int col) {
        gbc.gridx = col;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridy = 1;
        panel.add(field, gbc);
    }

    private JPanel creerPanelTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(0, 300));

        // Table
        String[] colonnes = {"ID Chambre", "Numéro", "Catégorie", "Prix/Nuit"};
        modeleDispo = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDispo = new JTable(modeleDispo);
        ThemeUtil.appliquerThemeTable(tableDispo);

        JScrollPane scrollPane = new JScrollPane(tableDispo);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bouton Réserver
        JButton btnReserver = new JButton("📝 Créer Réservation");
        ThemeUtil.appliquerThemeBoutonValider(btnReserver);
        btnReserver.addActionListener(e -> creerReservation());

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBouton.setBackground(Color.WHITE);
        panelBouton.add(btnReserver);
        panel.add(panelBouton, BorderLayout.SOUTH);

        return panel;
    }

    private void rechercherChambres() {
        try {
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText());
            String categorie = comboCategorie.getSelectedItem().toString();

            if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
                JOptionPane.showMessageDialog(this, "❌ La date de départ doit être après l'arrivée", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            modeleDispo.setRowCount(0);
            List<Chambre> chambres = chambreService.rechercherChambresDisponibles(arrivee, depart, categorie);

            for (Chambre c : chambres) {
                modeleDispo.addRow(new Object[]{
                        c.getIdChambre(),
                        c.getNumero(),
                        c.getCategorie(),
                        String.format("%.2f MAD", c.getPrixUnitaire())
                });
            }

            if (chambres.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Aucune chambre disponible pour ces dates", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Format de date invalide (YYYY-MM-DD)", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creerReservation() {
        int ligne = tableDispo.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une chambre", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (ValidationUtil.estVide(txtIdClient.getText())) {
                JOptionPane.showMessageDialog(this, "❌ Veuillez saisir l'ID du client", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idClient = Integer.parseInt(txtIdClient.getText());
            int idChambre = (Integer) modeleDispo.getValueAt(ligne, 0);
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText());

            Reservation resa = new Reservation();
            resa.setIdClient(idClient);
            resa.setIdUtilisateur(receptionnisteConnecte.getIdUtilisateur());
            resa.setStatut(StatutReservation.CONFIRMEE);

            Chambre chambre = chambreService.obtenirDetailsChambre(idChambre);
            List<Chambre> chambres = new ArrayList<>();
            chambres.add(chambre);

            boolean succes = reservationService.creerNouvelleReservation(resa, chambres, arrivee, depart);
            if (succes) {
                JOptionPane.showMessageDialog(this, "✓ Réservation créée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                modeleDispo.setRowCount(0);
                viderFormulaire();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viderFormulaire() {
        txtIdClient.setText("");
    }
}