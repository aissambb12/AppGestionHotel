package com.hotel.vue;

import com.hotel.model.Chambre;
import com.hotel.model.Client;
import com.hotel.model.Reservation;
import com.hotel.model.ReservationServices;
import com.hotel.model.Utilisateur;
import com.hotel.service.ClientService;
import com.hotel.service.ChambreService;
import com.hotel.service.ReservationService;
import com.hotel.service.FacturationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DashboardReceptionnisteFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;

    // Services (Le Back-End)
    private ClientService clientService;
    private ChambreService chambreService;
    private ReservationService reservationService;
    private FacturationService facturationService;

    // Modèles de tables
    private DefaultTableModel modeleClients;
    private DefaultTableModel modeleDispo;
    private DefaultTableModel modeleReservations;

    // Champs de texte globaux pour y accéder dans les événements
    private JTextField txtNom, txtPrenom, txtTel, txtEmail, txtCin;
    private JTextField txtDateArrivee, txtDateDepart;
    private JComboBox<String> comboCategorie;
    private JTable tableClients, tableDispo, tableReservations;

    public DashboardReceptionnisteFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;

        // 1. Initialisation des services pour se connecter à la BDD
        this.clientService = new ClientService();
        this.chambreService = new ChambreService();
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Accueil / Réception (" + receptionniste.getNom() + ")");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();

        // 2. Chargement initial des données depuis la BDD au démarrage
        chargerDonneesClients();
        chargerDonneesReservations();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // --- EN-TÊTE ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(ThemeUtil.BLEU_NUIT);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("🛎️ FRONT DESK - RÉCEPTION");
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

        onglets.addTab("🧑‍💼 1. Clients", creerOngletClients());
        onglets.addTab("📅 2. Disponibilités & Nouvelle Réservation", creerOngletNouvelleResa());
        onglets.addTab("🔑 3. Séjours (Check-In / Extras)", creerOngletSejours());
        onglets.addTab("💳 4. Check-Out & Facturation", creerOngletCaisse());

        add(onglets, BorderLayout.CENTER);
    }

    // =========================================================
    // ONGLET 1 : CLIENTS (Connecté !)
    // =========================================================
    private JPanel creerOngletClients() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Nouveau Client"));

        txtNom = new JTextField(10);
        txtPrenom = new JTextField(10);
        txtCin = new JTextField(8);
        txtTel = new JTextField(10);
        txtEmail = new JTextField(12);

        panelForm.add(new JLabel("Nom:")); panelForm.add(txtNom);
        panelForm.add(new JLabel("Prénom:")); panelForm.add(txtPrenom);
        panelForm.add(new JLabel("CIN:")); panelForm.add(txtCin);
        panelForm.add(new JLabel("Tél:")); panelForm.add(txtTel);
        panelForm.add(new JLabel("Email:")); panelForm.add(txtEmail);

        JButton btnAjouterClient = new JButton("➕ Enregistrer");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouterClient);
        panelForm.add(btnAjouterClient);

        // Action d'ajout d'un client dans la BDD
        btnAjouterClient.addActionListener(e -> {
            try {
                Client nouveauClient = new Client();
                nouveauClient.setNom(txtNom.getText());
                nouveauClient.setPrenom(txtPrenom.getText());
                nouveauClient.setCin(txtCin.getText());
                nouveauClient.setTelephone(txtTel.getText());
                nouveauClient.setEmail(txtEmail.getText());

                // Appel au Back-End
                boolean succes = clientService.enregistrerClient(nouveauClient);
                if (succes) {
                    JOptionPane.showMessageDialog(this, "Client enregistré avec succès !");
                    txtNom.setText(""); txtPrenom.setText(""); txtCin.setText(""); txtTel.setText(""); txtEmail.setText("");
                    chargerDonneesClients(); // Rafraîchit le tableau immédiatement !
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
            }
        });

        String[] colonnes = {"ID", "Nom", "Prénom", "CIN", "Téléphone", "Email"};
        modeleClients = new DefaultTableModel(colonnes, 0);
        tableClients = new JTable(modeleClients);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableClients), BorderLayout.CENTER);

        return panel;
    }

    private void chargerDonneesClients() {
        modeleClients.setRowCount(0); // Vide le tableau
        List<Client> clients = clientService.obtenirTousLesClients(); // Requête SQL SELECT *
        for (Client c : clients) {
            modeleClients.addRow(new Object[]{c.getIdClient(), c.getNom(), c.getPrenom(), c.getCin(), c.getTelephone(), c.getEmail()});
        }
    }

    // =========================================================
    // ONGLET 2 : DISPONIBILITÉS (Connecté !)
    // =========================================================
    private JPanel creerOngletNouvelleResa() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelRecherche.setBorder(BorderFactory.createTitledBorder("Recherche de Disponibilité"));

        txtDateArrivee = new JTextField("YYYY-MM-DD", 10);
        txtDateDepart = new JTextField("YYYY-MM-DD", 10);
        comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});

        panelRecherche.add(new JLabel("Arrivée:")); panelRecherche.add(txtDateArrivee);
        panelRecherche.add(new JLabel("Départ:")); panelRecherche.add(txtDateDepart);
        panelRecherche.add(new JLabel("Catégorie:")); panelRecherche.add(comboCategorie);

        JButton btnChercher = new JButton("🔍 Chercher");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        panelRecherche.add(btnChercher);

        String[] colonnes = {"ID Chambre", "Numéro", "Catégorie", "Prix/Nuit"};
        modeleDispo = new DefaultTableModel(colonnes, 0);
        tableDispo = new JTable(modeleDispo);

        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnReserver = new JButton("📝 Créer la Réservation");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnReserver);
        panelSud.add(btnReserver);

        // ACTION : Chercher dans la BDD les chambres libres
        btnChercher.addActionListener(e -> {
            try {
                LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
                LocalDate depart = LocalDate.parse(txtDateDepart.getText());
                String categorie = comboCategorie.getSelectedItem().toString();

                modeleDispo.setRowCount(0);
                // Appel au Back-End (La fameuse méthode anti-surbooking !)
                List<Chambre> chambresLibres = chambreService.rechercherChambresDisponibles(arrivee, depart, categorie);

                for (Chambre c : chambresLibres) {
                    modeleDispo.addRow(new Object[]{c.getIdChambre(), c.getNumero(), c.getCategorie(), c.getPrixUnitaire()});
                }
                if(chambresLibres.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Aucune chambre disponible pour ces critères.");
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide (AAAA-MM-JJ).", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ACTION : Finaliser la réservation
        btnReserver.addActionListener(e -> {
            int ligne = tableDispo.getSelectedRow();
            if (ligne != -1) {
                try {
                    String idClientStr = JOptionPane.showInputDialog(this, "Saisissez l'ID du client (Voir Onglet 1) :");
                    if (idClientStr == null || idClientStr.trim().isEmpty()) return;
                    int idClient = Integer.parseInt(idClientStr);

                    LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
                    LocalDate depart = LocalDate.parse(txtDateDepart.getText());

                    // Construction de la réservation
                    Reservation resa = new Reservation();
                    resa.setIdClient(idClient);
                    resa.setIdUtilisateur(receptionnisteConnecte.getIdUtilisateur());

                    // Récupération de la chambre sélectionnée
                    int idChambre = (int) modeleDispo.getValueAt(ligne, 0);
                    Chambre chambreSelectionnee = chambreService.obtenirDetailsChambre(idChambre); // Assurez-vous d'avoir cette méthode ou simulez

                    List<Chambre> listeChambres = new ArrayList<>();
                    listeChambres.add(chambreSelectionnee);

                    // Appel au Back-End
                    boolean succes = reservationService.creerNouvelleReservation(resa, listeChambres, arrivee, depart);

                    if (succes) {
                        JOptionPane.showMessageDialog(this, "Réservation confirmée avec succès !");
                        modeleDispo.setRowCount(0); // On vide les dispos
                        chargerDonneesReservations(); // On met à jour l'onglet 3
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la réservation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre dans la liste.");
            }
        });

        panel.add(panelRecherche, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableDispo), BorderLayout.CENTER);
        panel.add(panelSud, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================
    // ONGLET 3 : SÉJOURS (Check-In & Extras Connectés !)
    // =========================================================
    private JPanel creerOngletSejours() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JButton btnCheckIn = new JButton("📥 Faire le Check-In (Remise des clés)");
        btnCheckIn.setBackground(new Color(46, 204, 113));
        btnCheckIn.setForeground(Color.WHITE);

        JButton btnAjouterExtra = new JButton("☕ Ajouter Extra (Spa, Repas)");
        btnAjouterExtra.setBackground(ThemeUtil.BLEU_NUIT);
        btnAjouterExtra.setForeground(Color.WHITE);

        panelActions.add(btnCheckIn);
        panelActions.add(btnAjouterExtra);

        String[] colonnes = {"ID Resa", "ID Client", "Statut"};
        modeleReservations = new DefaultTableModel(colonnes, 0);
        tableReservations = new JTable(modeleReservations);

        // ACTION : CHECK-IN
        btnCheckIn.addActionListener(e -> {
            int ligne = tableReservations.getSelectedRow();
            if (ligne != -1) {
                int idResa = (int) modeleReservations.getValueAt(ligne, 0);
                boolean succes = reservationService.validerCheckIn(idResa);
                if (succes) {
                    JOptionPane.showMessageDialog(this, "Check-in validé ! Le client a ses clés.");
                    chargerDonneesReservations();
                } else {
                    JOptionPane.showMessageDialog(this, "Impossible de faire le check-in.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une réservation.");
            }
        });

        // ACTION : AJOUTER EXTRA
        btnAjouterExtra.addActionListener(e -> {
            int ligne = tableReservations.getSelectedRow();
            if (ligne != -1) {
                int idResa = (int) modeleReservations.getValueAt(ligne, 0);
                try {
                    String idServiceStr = JOptionPane.showInputDialog(this, "ID du Service (Ex: 1=Petit Dej, 2=Spa) :");
                    String qteStr = JOptionPane.showInputDialog(this, "Quantité :");

                    if (idServiceStr != null && qteStr != null) {
                        ReservationServices extra = new ReservationServices();
                        extra.setIdReservation(idResa);
                        extra.setIdService(Integer.parseInt(idServiceStr));
                        extra.setQuantite(Integer.parseInt(qteStr));
                        extra.setDateConsommation(LocalDate.now());

                        boolean succes = facturationService.ajouterConsommation(extra);
                        if (succes) JOptionPane.showMessageDialog(this, "Extra ajouté à la facture !");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Saisie invalide.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une réservation en cours.");
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
            modeleReservations.addRow(new Object[]{r.getIdReservation(), r.getIdClient(), r.getStatut().name()});
        }
    }

    // =========================================================
    // ONGLET 4 : CHECK-OUT & FACTURATION
    // =========================================================
    private JPanel creerOngletCaisse() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitre = new JLabel("Clôture de Séjour & Caisse");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("ID de la Réservation à clôturer :"), gbc);
        JTextField txtIdResa = new JTextField(10);
        gbc.gridx = 1; panel.add(txtIdResa, gbc);

        JButton btnPayer = new JButton("💸 Réaliser le Check-Out & Libérer Chambre");
        btnPayer.setBackground(new Color(39, 174, 96));
        btnPayer.setForeground(Color.WHITE);
        btnPayer.setFont(ThemeUtil.POLICE_BOUTON);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnPayer, gbc);

        // ACTION : CHECK-OUT
        btnPayer.addActionListener(e -> {
            try {
                int idResa = Integer.parseInt(txtIdResa.getText());
                boolean succes = facturationService.realiserCheckOut(idResa);
                if (succes) {
                    JOptionPane.showMessageDialog(this, "Check-Out validé ! La chambre est de nouveau disponible et la réservation est terminée.");
                    chargerDonneesReservations(); // Rafraîchit l'onglet 3
                    txtIdResa.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Impossible de réaliser le Check-Out.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un ID valide.");
            }
        });

        return panel;
    }
}