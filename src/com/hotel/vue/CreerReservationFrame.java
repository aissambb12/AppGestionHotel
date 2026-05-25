package com.hotel.vue;

import com.hotel.model.*;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.service.*;
import com.hotel.util.DatePickerUtil;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreerReservationFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private ClientService clientService;
    private ChambreService chambreService;
    private ReservationService reservationService;
    private FacturationService facturationService;

    // Services disponibles
    private List<ServiceSupplementaire> servicesDisponibles;

    // UI Components - CLIENT
    private JTextField txtNom, txtPrenom, txtEmail, txtTel, txtCin;

    // UI Components - RÉSERVATION
    private JTextField txtDateArrivee, txtDateDepart;
    private JComboBox<String> comboCategorie;

    // UI Components - CHAMBRES SÉLECTIONNÉES
    private JPanel panelChambresSelectionnees;
    private List<Chambre> chambresSelectionnees = new ArrayList<>();

    // UI Components - EXTRAS
    private JPanel panelExtras;
    private Map<Integer, JLabel> labelsQteExtras = new HashMap<>();
    private Map<Integer, Integer> selectedExtras = new HashMap<>();

    public CreerReservationFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.clientService = new ClientService();
        this.chambreService = new ChambreService();
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();

        // Charger les services disponibles
        chargerServices();

        setTitle("Hotel Manager - Créer Réservation Complète");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === CONTENU PRINCIPAL - 3 COLONNES ===
        JPanel panelContenu = new JPanel(new GridLayout(1, 3, 15, 15));
        panelContenu.setBackground(ThemeUtil.GRIS_FOND);
        panelContenu.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Colonne 1 : Client et Réservation
        JPanel col1 = creerColonneClient();
        panelContenu.add(new JScrollPane(col1));

        // Colonne 2 : Chambres
        JPanel col2 = creerColonneChambres();
        panelContenu.add(new JScrollPane(col2));

        // Colonne 3 : Extras
        JPanel col3 = creerColonneExtras();
        panelContenu.add(new JScrollPane(col3));

        add(panelContenu, BorderLayout.CENTER);

        // === BOUTONS ===
        JPanel panelBoutons = creerPanelBoutons();
        add(panelBoutons, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("📝 CRÉER UNE RÉSERVATION COMPLÈTE");
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

    // ===== COLONNE 1 : CLIENT ET RÉSERVATION =====
    private JPanel creerColonneClient() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === SECTION CLIENT ===
        JLabel lblSecClient = new JLabel("INFORMATIONS CLIENT");
        lblSecClient.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblSecClient.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblSecClient, gbc);

        // Champs client
        txtNom = new JTextField();
        txtPrenom = new JTextField();
        txtEmail = new JTextField();
        txtTel = new JTextField();
        txtCin = new JTextField();

        ThemeUtil.appliquerThemeTextField(txtNom);
        ThemeUtil.appliquerThemeTextField(txtPrenom);
        ThemeUtil.appliquerThemeTextField(txtEmail);
        ThemeUtil.appliquerThemeTextField(txtTel);
        ThemeUtil.appliquerThemeTextField(txtCin);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        panel.add(txtNom, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1;
        panel.add(txtPrenom, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Tél :"), gbc);
        gbc.gridx = 1;
        panel.add(txtTel, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(new JLabel("CIN :"), gbc);
        gbc.gridx = 1;
        panel.add(txtCin, gbc);

        // === SECTION RÉSERVATION ===
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JSeparator sep1 = new JSeparator();
        panel.add(sep1, gbc);

        gbc.gridy = 7;
        JLabel lblSecResa = new JLabel("INFORMATIONS RÉSERVATION");
        lblSecResa.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblSecResa.setForeground(ThemeUtil.BLEU_NUIT);
        panel.add(lblSecResa, gbc);

        // Dates
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Arrivée :"), gbc);

        JPanel panelArrivee = new JPanel(new BorderLayout(5, 0));
        panelArrivee.setBackground(Color.WHITE);
        txtDateArrivee = new JTextField(LocalDate.now().toString());
        ThemeUtil.appliquerThemeTextField(txtDateArrivee);
        JButton btnCalArrivee = new JButton("📅");
        btnCalArrivee.setPreferredSize(new Dimension(35, 35));
        btnCalArrivee.addActionListener(e -> afficherCalendrier(txtDateArrivee));
        panelArrivee.add(txtDateArrivee, BorderLayout.CENTER);
        panelArrivee.add(btnCalArrivee, BorderLayout.EAST);
        gbc.gridx = 1;
        panel.add(panelArrivee, gbc);

        gbc.gridy = 9;
        gbc.gridx = 0;
        panel.add(new JLabel("Départ :"), gbc);

        JPanel panelDepart = new JPanel(new BorderLayout(5, 0));
        panelDepart.setBackground(Color.WHITE);
        txtDateDepart = new JTextField(LocalDate.now().plusDays(1).toString());
        ThemeUtil.appliquerThemeTextField(txtDateDepart);
        JButton btnCalDepart = new JButton("📅");
        btnCalDepart.setPreferredSize(new Dimension(35, 35));
        btnCalDepart.addActionListener(e -> afficherCalendrier(txtDateDepart));
        panelDepart.add(txtDateDepart, BorderLayout.CENTER);
        panelDepart.add(btnCalDepart, BorderLayout.EAST);
        gbc.gridx = 1;
        panel.add(panelDepart, gbc);

        // Catégorie
        gbc.gridy = 10;
        gbc.gridx = 0;
        panel.add(new JLabel("Catégorie :"), gbc);

        comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});
        gbc.gridx = 1;
        panel.add(comboCategorie, gbc);

        // Bouton chercher
        JButton btnChercher = new JButton("🔍 Chercher Chambres");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        btnChercher.addActionListener(e -> rechercherChambres());
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        panel.add(btnChercher, gbc);

        return panel;
    }

    // ===== COLONNE 2 : CHAMBRES =====
    private JPanel creerColonneChambres() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("CHAMBRES DISPONIBLES");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        JLabel lblInfo = new JLabel("Cliquez 'Chercher Chambres'");
        lblInfo.setFont(ThemeUtil.POLICE_PETIT);
        gbc.gridy = 1;
        panel.add(lblInfo, gbc);

        // Panel pour les chambres
        panelChambresSelectionnees = new JPanel(new GridLayout(0, 1, 5, 5));
        panelChambresSelectionnees.setBackground(Color.WHITE);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(panelChambresSelectionnees), gbc);

        return panel;
    }

    // ===== COLONNE 3 : EXTRAS =====
    private JPanel creerColonneExtras() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("SERVICES SUPPLÉMENTAIRES");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitre, gbc);

        // Panel pour les extras
        panelExtras = new JPanel(new GridLayout(0, 1, 5, 5));
        panelExtras.setBackground(Color.WHITE);
        creerPanelExtras();
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(panelExtras), gbc);

        return panel;
    }

    private void creerPanelExtras() {
        panelExtras.removeAll();
        labelsQteExtras.clear();

        if (servicesDisponibles != null && !servicesDisponibles.isEmpty()) {
            for (ServiceSupplementaire service : servicesDisponibles) {
                JPanel panelExtra = new JPanel(new BorderLayout(5, 5));
                panelExtra.setBackground(ThemeUtil.GRIS_FOND);
                panelExtra.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                panelExtra.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));

                // Nom du service
                JLabel lblNom = new JLabel(service.getNomService() + " - " + String.format("%.2f", service.getPrixService()) + " MAD");
                lblNom.setFont(ThemeUtil.POLICE_NORMALE);

                // Contrôles quantité
                JPanel panelQte = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                panelQte.setBackground(ThemeUtil.GRIS_FOND);

                JButton btnMoins = new JButton("−");
                btnMoins.setPreferredSize(new Dimension(30, 30));
                btnMoins.setFont(new Font("Arial", Font.BOLD, 16));

                JLabel lblQte = new JLabel("0");
                lblQte.setFont(ThemeUtil.POLICE_BOUTON);
                lblQte.setPreferredSize(new Dimension(30, 30));
                lblQte.setHorizontalAlignment(JLabel.CENTER);
                labelsQteExtras.put(service.getIdService(), lblQte);

                JButton btnPlus = new JButton("+");
                btnPlus.setPreferredSize(new Dimension(30, 30));
                btnPlus.setFont(new Font("Arial", Font.BOLD, 16));

                btnPlus.addActionListener(e -> {
                    int qte = Integer.parseInt(lblQte.getText()) + 1;
                    lblQte.setText(String.valueOf(qte));
                    selectedExtras.put(service.getIdService(), qte);
                });

                btnMoins.addActionListener(e -> {
                    int qte = Math.max(0, Integer.parseInt(lblQte.getText()) - 1);
                    lblQte.setText(String.valueOf(qte));
                    if (qte == 0) {
                        selectedExtras.remove(service.getIdService());
                    } else {
                        selectedExtras.put(service.getIdService(), qte);
                    }
                });

                panelQte.add(btnMoins);
                panelQte.add(lblQte);
                panelQte.add(btnPlus);

                panelExtra.add(lblNom, BorderLayout.WEST);
                panelExtra.add(panelQte, BorderLayout.EAST);

                panelExtras.add(panelExtra);
            }
        } else {
            JLabel lblAucun = new JLabel("Aucun service disponible");
            lblAucun.setFont(ThemeUtil.POLICE_PETIT);
            panelExtras.add(lblAucun);
        }

        panelExtras.revalidate();
        panelExtras.repaint();
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnValider = new JButton("✓ CONFIRMER RÉSERVATION");
        ThemeUtil.appliquerThemeBoutonValider(btnValider);
        btnValider.setPreferredSize(new Dimension(200, 40));
        btnValider.addActionListener(e -> confirmerReservation());

        JButton btnAnnuler = new JButton("✕ Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        btnAnnuler.setPreferredSize(new Dimension(150, 40));
        btnAnnuler.addActionListener(e -> dispose());

        panel.add(btnValider);
        panel.add(btnAnnuler);

        return panel;
    }

    // ===== MÉTHODES DE RECHERCHE =====
    private void rechercherChambres() {
        try {
            // Validation des dates
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText());

            if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
                JOptionPane.showMessageDialog(this, "❌ La date de départ doit être après l'arrivée", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String categorie = comboCategorie.getSelectedItem().toString();

            // Récupérer les chambres disponibles
            List<Chambre> chambres = chambreService.rechercherChambresDisponibles(arrivee, depart, categorie);

            // Afficher les chambres
            panelChambresSelectionnees.removeAll();
            chambresSelectionnees.clear();

            if (chambres.isEmpty()) {
                JLabel lblPas = new JLabel("❌ Aucune chambre disponible");
                lblPas.setFont(ThemeUtil.POLICE_PETIT);
                panelChambresSelectionnees.add(lblPas);
            } else {
                for (Chambre chambre : chambres) {
                    JCheckBox chkChambre = new JCheckBox(
                            "Chambre " + chambre.getNumero() +
                                    " - " + chambre.getCategorie() +
                                    " (" + String.format("%.2f", chambre.getPrixUnitaire()) + " MAD/nuit)"
                    );
                    chkChambre.setFont(ThemeUtil.POLICE_NORMALE);
                    chkChambre.setBackground(Color.WHITE);

                    Chambre chambreFinal = chambre;
                    chkChambre.addActionListener(e -> {
                        if (chkChambre.isSelected()) {
                            if (!chambresSelectionnees.contains(chambreFinal)) {
                                chambresSelectionnees.add(chambreFinal);
                            }
                        } else {
                            chambresSelectionnees.remove(chambreFinal);
                        }
                    });

                    panelChambresSelectionnees.add(chkChambre);
                }
            }

            panelChambresSelectionnees.revalidate();
            panelChambresSelectionnees.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherCalendrier(JTextField field) {
        try {
            LocalDate dateActuelle = LocalDate.parse(field.getText());
            JDialog dialog = new JDialog(this, "Sélectionner une date", true);
            dialog.setSize(350, 400);
            dialog.setLocationRelativeTo(this);

            JPanel panelCal = DatePickerUtil.creerCalendrier(dateActuelle, selectedDate -> {
                field.setText(selectedDate.toString());
                dialog.dispose();
            });

            dialog.add(panelCal);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Date invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== CONFIRMATION RÉSERVATION =====
    private void confirmerReservation() {
        try {
            // 1. Valider le client
            if (!validerClient()) {
                return;
            }

            // 2. Récupérer ou créer le client
            Client client = obtenirOuCreerClient();
            if (client == null) {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la création du client", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Valider les chambres sélectionnées
            if (chambresSelectionnees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner au moins une chambre", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4. Récupérer les dates
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText());

            // 5. Créer la réservation
            Reservation resa = new Reservation();
            resa.setIdClient(client.getIdClient());
            resa.setIdUtilisateur(receptionnisteConnecte.getIdUtilisateur());
            resa.setDateCreation(LocalDateTime.now());
            resa.setStatut(StatutReservation.CONFIRMEE);

            boolean succes = reservationService.creerNouvelleReservation(
                    resa,
                    new ArrayList<>(chambresSelectionnees),
                    arrivee,
                    depart
            );

            if (!succes) {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la création de la réservation", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 6. Ajouter les extras
            Facture facture = facturationService.obtenirFactureReservation(resa.getIdReservation());

            for (Map.Entry<Integer, Integer> extra : selectedExtras.entrySet()) {
                if (extra.getValue() > 0) {
                    ReservationServices rs = new ReservationServices();
                    rs.setIdReservation(resa.getIdReservation());
                    rs.setIdService(extra.getKey());
                    rs.setQuantite(extra.getValue());
                    rs.setDateConsommation(LocalDate.now());
                    facturationService.ajouterConsommation(rs);
                }
            }

            // 7. Afficher la facture
            new FactureFrame(resa.getIdReservation(), facturationService).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // ===== VALIDATION ET CRÉATION CLIENT =====
    private boolean validerClient() {
        if (ValidationUtil.estVide(txtNom.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Nom obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
            txtNom.requestFocus();
            return false;
        }
        if (ValidationUtil.estVide(txtPrenom.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Prénom obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
            txtPrenom.requestFocus();
            return false;
        }
        if (!ValidationUtil.estEmailValide(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Email invalide", "Validation", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        if (!ValidationUtil.estTelephoneValide(txtTel.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Téléphone invalide (10 chiffres)", "Validation", JOptionPane.WARNING_MESSAGE);
            txtTel.requestFocus();
            return false;
        }
        if (!ValidationUtil.estCinValide(txtCin.getText())) {
            JOptionPane.showMessageDialog(this, "❌ CIN invalide", "Validation", JOptionPane.WARNING_MESSAGE);
            txtCin.requestFocus();
            return false;
        }
        return true;
    }

    private Client obtenirOuCreerClient() {
        try {
            // 1. Chercher si le client existe déjà par CIN
            String cin = txtCin.getText().trim().toUpperCase();
            Client existant = clientService.trouverClientParCin(cin);

            if (existant != null) {
                return existant;  // Client trouvé, on le retourne
            }

            // 2. Le client n'existe pas, on le crée
            Client nouveau = new Client();
            nouveau.setNom(txtNom.getText().trim().toUpperCase());
            nouveau.setPrenom(txtPrenom.getText().trim());
            nouveau.setEmail(txtEmail.getText().trim());
            nouveau.setTelephone(txtTel.getText().trim());
            nouveau.setCin(cin);

            // 3. Enregistrer le nouveau client
            boolean succes = clientService.enregistrerClient(nouveau);

            if (succes) {
                // 4. Récupérer et retourner le client créé avec son ID
                Client clientCree = clientService.trouverClientParCin(cin);
                if (clientCree != null) {
                    return clientCree;
                }
            }

            return null;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur client : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return null;
        }
    }

    // ===== CHARGEMENT SERVICES =====
    private void chargerServices() {
        try {
            com.hotel.dao.ServiceSupplementaireDAOImpl dao = new com.hotel.dao.ServiceSupplementaireDAOImpl();
            servicesDisponibles = dao.listerTous();
            if (servicesDisponibles == null) {
                servicesDisponibles = new ArrayList<>();
            }
        } catch (Exception ex) {
            System.err.println("Erreur lors du chargement des services : " + ex.getMessage());
            servicesDisponibles = new ArrayList<>();
        }
    }
}