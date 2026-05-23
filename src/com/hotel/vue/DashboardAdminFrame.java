package com.hotel.vue;

import com.hotel.model.Chambre;
import com.hotel.model.Reservation;
import com.hotel.model.ReservationServices;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.Role;
import com.hotel.service.UtilisateurService;
import com.hotel.service.ChambreService;
import com.hotel.service.ReservationService;
import com.hotel.service.FacturationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DashboardAdminFrame extends JFrame {

    private Utilisateur adminConnecte;

    // Services (Back-End)
    private UtilisateurService utilisateurService;
    private ChambreService chambreService;
    private ReservationService reservationService;
    private FacturationService facturationService;

    // Modèles de tables pour pouvoir les rafraîchir
    private DefaultTableModel modelePersonnel;
    private DefaultTableModel modeleChambres;
    private DefaultTableModel modeleReservations;

    public DashboardAdminFrame(Utilisateur admin) {
        this.adminConnecte = admin;

        // Initialisation des connexions
        this.utilisateurService = new UtilisateurService();
        this.chambreService = new ChambreService();
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Mode Administrateur (" + admin.getNom() + ")");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();

        // Chargement automatique de toutes les données au démarrage
        chargerDonneesPersonnel();
        chargerDonneesChambres();
        chargerDonneesReservations();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // --- EN-TÊTE ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(ThemeUtil.BLEU_NUIT);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("TABLEAU DE BORD - MASTER ADMIN");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setBackground(Color.RED);
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        panelHeader.add(lblTitre, BorderLayout.WEST);
        panelHeader.add(btnDeconnexion, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // --- ONGLETS ---
        JTabbedPane onglets = new JTabbedPane();
        onglets.setFont(ThemeUtil.POLICE_BOUTON);

        onglets.addTab("👥 Personnel", creerOngletPersonnel());
        onglets.addTab("🛏️ Parc des Chambres", creerOngletChambres());
        onglets.addTab("🗂️ Réservations & Extras", creerOngletReservations());
        onglets.addTab("📊 Chiffre d'Affaires", creerOngletStatistiques());

        add(onglets, BorderLayout.CENTER);
    }

    // =========================================================
    // ONGLET 1 : PERSONNEL
    // =========================================================
    private JPanel creerOngletPersonnel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelForm.setBorder(BorderFactory.createTitledBorder("Gérer le personnel"));

        JButton btnAjouter = new JButton("Créer un employé");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);

        JButton btnChangerStatut = new JButton("Activer / Désactiver l'employé sélectionné");
        btnChangerStatut.setBackground(Color.DARK_GRAY);
        btnChangerStatut.setForeground(Color.WHITE);

        panelForm.add(btnAjouter);
        panelForm.add(btnChangerStatut);

        String[] colonnes = {"ID", "Nom", "Email", "Rôle", "Statut"};
        modelePersonnel = new DefaultTableModel(colonnes, 0);
        JTable tablePersonnel = new JTable(modelePersonnel);

        // ACTION : Changer le statut
        btnChangerStatut.addActionListener(e -> {
            int ligne = tablePersonnel.getSelectedRow();
            if (ligne != -1) {
                int idUtilisateur = (int) modelePersonnel.getValueAt(ligne, 0);
                String statutActuel = (String) modelePersonnel.getValueAt(ligne, 4);

                if ("ACTIF".equals(statutActuel)) {
                    utilisateurService.desactiverEmploye(idUtilisateur);
                    JOptionPane.showMessageDialog(this, "Employé désactivé.");
                } else {
                    utilisateurService.activerEmploye(idUtilisateur);
                    JOptionPane.showMessageDialog(this, "Employé réactivé.");
                }
                chargerDonneesPersonnel(); // Rafraîchir la table instantanément
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablePersonnel), BorderLayout.CENTER);
        return panel;
    }

    private void chargerDonneesPersonnel() {
        modelePersonnel.setRowCount(0);
        List<Utilisateur> employes = utilisateurService.listerTousLesEmployes();
        for (Utilisateur u : employes) {
            modelePersonnel.addRow(new Object[]{
                    u.getIdUtilisateur(), u.getNom(), u.getEmail(),
                    u.getRole() != null ? u.getRole().name() : "N/A",
                    u.getStatut() != null ? u.getStatut().name() : "N/A"
            });
        }
    }

    // =========================================================
    // ONGLET 2 : CHAMBRES
    // =========================================================
    private JPanel creerOngletChambres() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouterChambre = new JButton("Ajouter Chambre");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouterChambre);

        JButton btnSignalerPanne = new JButton("⚠️ Signaler en Panne");
        btnSignalerPanne.setBackground(Color.ORANGE);
        btnSignalerPanne.setForeground(Color.BLACK);

        panelActions.add(btnAjouterChambre);
        panelActions.add(btnSignalerPanne);

        String[] colonnes = {"ID", "Numéro", "Catégorie", "Prix", "Statut"};
        modeleChambres = new DefaultTableModel(colonnes, 0);
        JTable tableChambres = new JTable(modeleChambres);

        // ACTION : Mettre en panne
        btnSignalerPanne.addActionListener(e -> {
            int ligne = tableChambres.getSelectedRow();
            if (ligne != -1) {
                String statutActuel = modeleChambres.getValueAt(ligne, 4).toString();
                if ("EN_MAINTENANCE".equals(statutActuel)) {
                    JOptionPane.showMessageDialog(this, "Cette chambre est déjà en maintenance.", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int idChambre = (int) modeleChambres.getValueAt(ligne, 0);

                    try {
                        // Supposant que vous ayez une méthode modifierStatutChambre dans votre service
                        chambreService.modifierStatutChambre(idChambre, "EN_MAINTENANCE");
                        JOptionPane.showMessageDialog(this, "Chambre envoyée en maintenance !");
                        chargerDonneesChambres(); // Rafraîchir la table
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre.");
            }
        });

        panel.add(panelActions, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableChambres), BorderLayout.CENTER);
        return panel;
    }

    private void chargerDonneesChambres() {
        modeleChambres.setRowCount(0);
        List<Chambre> chambres = chambreService.listerToutesLesChambres();
        for (Chambre c : chambres) {
            modeleChambres.addRow(new Object[]{
                    c.getIdChambre(), c.getNumero(), c.getCategorie(), c.getPrixUnitaire(), c.getStatutChambre()
            });
        }
    }

    // =========================================================
    // ONGLET 3 : RÉSERVATIONS & EXTRAS
    // =========================================================
    private JPanel creerOngletReservations() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnDetails = new JButton("🔍 Voir les Extras & Détails");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnDetails);
        panelActions.add(btnDetails);

        String[] colonnes = {"ID Réservation", "ID Client", "Date Arrivée", "Date Départ", "Statut"};
        modeleReservations = new DefaultTableModel(colonnes, 0);
        JTable tableReservations = new JTable(modeleReservations);

        // ACTION : Voir les extras
        btnDetails.addActionListener(e -> {
            int ligne = tableReservations.getSelectedRow();
            if (ligne != -1) {
                int idResa = (int) modeleReservations.getValueAt(ligne, 0);

                // Appel au Back-End
                List<ReservationServices> extras = facturationService.obtenirDetailsConsommations(idResa);
                afficherDetailsPopUp(idResa, extras);

            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation.");
            }
        });

        panel.add(panelActions, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableReservations), BorderLayout.CENTER);
        return panel;
    }

    private void chargerDonneesReservations() {
        modeleReservations.setRowCount(0);
        List<Reservation> reservations = reservationService.listerToutesLesReservations();

        for (Reservation r : reservations) {
            modeleReservations.addRow(new Object[]{
                    r.getIdReservation(),
                    r.getIdClient(),
                    r.getDateCreation(), // On utilise la vraie date disponible !
                    r.getStatut() != null ? r.getStatut().name() : "N/A"
            });
        }
    }

    private void afficherDetailsPopUp(int idResa, List<ReservationServices> extras) {
        if (extras == null || extras.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun service supplémentaire n'a été consommé pour cette réservation.", "Détails Réservation N°" + idResa, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Liste des extras consommés :\n\n");
        for (ReservationServices rs : extras) {
            sb.append("- ID Service: ").append(rs.getIdService())
                    .append(" | Quantité: ").append(rs.getQuantite())
                    .append(" | Date: ").append(rs.getDateConsommation()).append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Extras de la Réservation N°" + idResa, JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================
    // ONGLET 4 : STATISTIQUES (Chiffre d'Affaires)
    // =========================================================
    private JPanel creerOngletStatistiques() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitre = new JLabel("Calcul du Chiffre d'Affaires");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Date de début (YYYY-MM-DD) :"), gbc);
        JTextField txtDateDebut = new JTextField(10);
        gbc.gridx = 1; panel.add(txtDateDebut, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Date de fin (YYYY-MM-DD) :"), gbc);
        JTextField txtDateFin = new JTextField(10);
        gbc.gridx = 1; panel.add(txtDateFin, gbc);

        JButton btnCalculer = new JButton("Calculer CA");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnCalculer);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnCalculer, gbc);

        JLabel lblResultat = new JLabel("Total : 0.00");
        lblResultat.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblResultat.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridy = 4;
        panel.add(lblResultat, gbc);

        // ACTION : Calculer le CA avec la BDD
        btnCalculer.addActionListener(e -> {
            try {
                LocalDate dateDebut = LocalDate.parse(txtDateDebut.getText());
                LocalDate dateFin = LocalDate.parse(txtDateFin.getText());

                double ca = facturationService.obtenirChiffreAffaires(dateDebut, dateFin);
                lblResultat.setText("Total : " + String.format("%.2f", ca) + " MAD");

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez AAAA-MM-JJ.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur de calcul : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
}