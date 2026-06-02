package com.hotel.vue;

import com.hotel.model.Reservation;
import com.hotel.model.Utilisateur;
import com.hotel.service.ReservationService;
import com.hotel.service.FacturationService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionReservationsFrame extends JFrame {

    private ReservationService reservationService;
    private FacturationService facturationService;
    private DefaultTableModel modeleReservations;
    private JTable tableReservations;
    private Utilisateur adminConnecte;

    public GestionReservationsFrame(Utilisateur admin) {
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();
        this.adminConnecte = admin;

        setTitle("Hotel Manager - Gestion Réservations");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());
        add(creerHeader(), BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(ThemeUtil.GRIS_FOND);
        centre.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        centre.add(creerPanelBoutons(), BorderLayout.NORTH);

        String[] colonnes = {"ID", "ID Client", "ID Utilisateur", "Date Création", "Statut"};
        modeleReservations = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tableReservations = new JTable(modeleReservations);
        ThemeUtil.appliquerThemeTable(tableReservations);
        tableReservations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableReservations);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        centre.add(scrollPane, BorderLayout.CENTER);

        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel(" GESTION DES RÉSERVATIONS");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        // Icône d'une flèche gauche

        JButton btnRetour = new JButton(" Retour");
        btnRetour.setBackground(ThemeUtil.GRIS_CLAIR);
        btnRetour.setForeground(ThemeUtil.TEXTE_SOMBRE);
        btnRetour.setFont(ThemeUtil.POLICE_BOUTON);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setContentAreaFilled(true);
        btnRetour.setBorderPainted(true);
        btnRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        IconLoader.appliquerIcone(btnRetour , "icon_back");
        btnRetour.addActionListener(e ->
                NavigationManager.retourVers(this, new DashboardAdminFrame(adminConnecte)));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /**
         * IMAGE À AJOUTER : (bleu) Icône détails/info (48x48px)
         * Description: Icône d'une loupe ou d'une information
         */
        JButton btnDetails = new JButton("Voir Détails");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnDetails);
        IconLoader.appliquerIcone(btnDetails , "icon_search");
        btnDetails.addActionListener(e -> afficherDetails());

        /**
         * IMAGE À AJOUTER : refresh.png (48x48px)
         * Description: Icône d'une flèche circulaire
         */
        JButton btnRafraichir = new JButton(" Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir , "icon_refresh");
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnDetails);
        panel.add(btnRafraichir);

        return panel;
    }

    private void chargerDonnees() {
        modeleReservations.setRowCount(0);
        try {
            List<Reservation> reservations = reservationService.listerToutesLesReservations();
            for (Reservation r : reservations) {
                modeleReservations.addRow(new Object[]{
                        r.getIdReservation(),
                        r.getIdClient(),
                        r.getIdUtilisateur(),
                        r.getDateCreation(),
                        r.getStatut()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherDetails() {
        int ligne = tableReservations.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner une réservation", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idResa = (Integer) modeleReservations.getValueAt(ligne, 0);
        new DetailsReservationFrame(idResa, facturationService).setVisible(true);
    }
}