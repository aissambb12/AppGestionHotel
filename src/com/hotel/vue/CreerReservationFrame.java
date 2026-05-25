package com.hotel.vue;

import com.hotel.model.*;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.service.*;
import com.hotel.util.DatePickerUtil;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
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
    private ServiceSupplementaire[] servicesDisponibles;

    // UI Components
    private JTextField txtNom, txtPrenom, txtEmail, txtTel, txtCin;
    private JTextField txtDateArrivee, txtDateDepart;
    private JComboBox<String> comboCategorie;
    private JLabel lblChambresDisponibles;
    private JPanel panelChambres;
    private JPanel panelExtras;
    private Map<Integer, Integer> selectedExtras = new HashMap<>();
    private List<Chambre> chambresSelectionnees = new ArrayList<>();

    public CreerReservationFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.clientService = new ClientService();
        this.chambreService = new ChambreService();
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();
        this.servicesDisponibles = chargerServices();

        setTitle("Hotel Manager - Créer Réservation Complète");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === CONTENU PRINCIPAL ===
        JPanel panelContenu = new JPanel(new GridLayout(1, 2, 10, 10));
        panelContenu.setBackground(ThemeUtil.GRIS_FOND);
        panelContenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel gauche - Saisie client et réservation
        JPanel panelGauche = creerPanelGauche();
        panelContenu.add(new JScrollPane(panelGauche));

        // Panel droite - Sélection chambres et extras
        JPanel panelDroite = creerPanelDroite();
        panelContenu.add(new JScrollPane(panelDroite));

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

    private JPanel creerPanelGauche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === SECTION CLIENT ===
        JLabel lblSecClient = new JLabel("INFORMATIONS CLIENT");
        lblSecClient.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblSecClient, gbc);

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
        ajouterChamp(panel, gbc, "Nom :", txtNom);

        gbc.gridy = 2;
        ajouterChamp(panel, gbc, "Prénom :", txtPrenom);

        gbc.gridy = 3;
        ajouterChamp(panel, gbc, "Email :", txtEmail);

        gbc.gridy = 4;
        ajouterChamp(panel, gbc, "Tél :", txtTel);

        gbc.gridy = 5;
        ajouterChamp(panel, gbc, "CIN :", txtCin);

        // === SECTION RÉSERVATION ===
        JLabel lblSecResa = new JLabel("INFORMATIONS RÉSERVATION");
        lblSecResa.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(lblSecResa, gbc);

        txtDateArrivee = new JTextField("2026-06-01");
        txtDateDepart = new JTextField("2026-06-05");
        comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});

        ThemeUtil.appliquerThemeTextField(txtDateArrivee);
        ThemeUtil.appliquerThemeTextField(txtDateDepart);

        gbc.gridwidth = 1;
        gbc.gridy = 7;
        ajouterChamp(panel, gbc, "Arrivée :", txtDateArrivee);

        JButton btnCalArrivee = new JButton("📅");
        btnCalArrivee.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCalArrivee.setPreferredSize(new Dimension(40, 40));
        btnCalArrivee.addActionListener(e -> afficherCalendrier(txtDateArrivee, LocalDate.parse(txtDateArrivee.getText())));
        gbc.gridx = 1; gbc.gridy = 7;
        panel.add(btnCalArrivee, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        ajouterChamp(panel, gbc, "Départ :", txtDateDepart);

        JButton btnCalDepart = new JButton("📅");
        btnCalDepart.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCalDepart.setPreferredSize(new Dimension(40, 40));
        btnCalDepart.addActionListener(e -> afficherCalendrier(txtDateDepart, LocalDate.parse(txtDateDepart.getText())));
        gbc.gridx = 1; gbc.gridy = 8;
        panel.add(btnCalDepart, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        ajouterChamp(panel, gbc, "Catégorie :", comboCategorie);

        // Bouton chercher chambres
        JButton btnChercher = new JButton("🔍 Chercher Chambres");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        btnChercher.addActionListener(e -> rechercherChambres());
        gbc.gridy = 10; gbc.gridwidth = 2;
        panel.add(btnChercher, gbc);

        return panel;
    }

    private JPanel creerPanelDroite() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === CHAMBRES ===
        JLabel lblChambres = new JLabel("CHAMBRES DISPONIBLES");
        lblChambres.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblChambres, gbc);

        lblChambresDisponibles = new JLabel("Cliquez sur 'Chercher Chambres'");
        lblChambresDisponibles.setFont(ThemeUtil.POLICE_PETIT);
        gbc.gridy = 1;
        panel.add(lblChambresDisponibles, gbc);

        panelChambres = new JPanel(new GridLayout(0, 1, 5, 5));
        panelChambres.setBackground(Color.WHITE);
        gbc.gridy = 2; gbc.weighty = 0.5;
        panel.add(panelChambres, gbc);

        // === EXTRAS ===
        JLabel lblExtras = new JLabel("SERVICES SUPPLÉMENTAIRES");
        lblExtras.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridy = 3; gbc.weighty = 0.0;
        panel.add(lblExtras, gbc);

        panelExtras = new JPanel(new GridLayout(0, 1, 5, 5));
        panelExtras.setBackground(Color.WHITE);
        creerPanelExtras();
        gbc.gridy = 4; gbc.weighty = 0.5;
        panel.add(panelExtras, gbc);

        return panel;
    }

    private void creerPanelExtras() {
        panelExtras.removeAll();

        for (ServiceSupplementaire service : servicesDisponibles) {
            if (service != null) {
                JPanel panelExtra = new JPanel(new BorderLayout());
                panelExtra.setBackground(ThemeUtil.GRIS_FOND);
                panelExtra.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

                JLabel lblNom = new JLabel(service.getNomService() + " (" + service.getPrixService() + " MAD)");
                lblNom.setFont(ThemeUtil.POLICE_NORMALE);

                JPanel panelQte = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                panelQte.setBackground(ThemeUtil.GRIS_FOND);

                JButton btnMoins = new JButton("-");
                JLabel lblQte = new JLabel("0");
                JButton btnPlus = new JButton("+");

                btnMoins.setPreferredSize(new Dimension(30, 30));
                btnPlus.setPreferredSize(new Dimension(30, 30));
                lblQte.setFont(ThemeUtil.POLICE_BOUTON);

                btnPlus.addActionListener(e -> {
                    int qte = Integer.parseInt(lblQte.getText()) + 1;
                    lblQte.setText(String.valueOf(qte));
                    selectedExtras.put(service.getIdService(), qte);
                });

                btnMoins.addActionListener(e -> {
                    int qte = Math.max(0, Integer.parseInt(lblQte.getText()) - 1);
                    lblQte.setText(String.valueOf(qte));
                    selectedExtras.put(service.getIdService(), qte);
                });

                panelQte.add(btnMoins);
                panelQte.add(lblQte);
                panelQte.add(btnPlus);

                panelExtra.add(lblNom, BorderLayout.WEST);
                panelExtra.add(panelQte, BorderLayout.EAST);

                panelExtras.add(panelExtra);
            }
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
        btnValider.addActionListener(e -> confirmerReservation());

        JButton btnAnnuler = new JButton("✕ Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        btnAnnuler.addActionListener(e -> dispose());

        panel.add(btnValider);
        panel.add(btnAnnuler);

        return panel;
    }

    private void ajouterChamp(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
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

            List<Chambre> chambres = chambreService.rechercherChambresDisponibles(arrivee, depart, categorie);

            panelChambres.removeAll();

            if (chambres.isEmpty()) {
                JLabel lblPas = new JLabel("❌ Aucune chambre disponible pour cette catégorie");
                lblPas.setFont(ThemeUtil.POLICE_PETIT);
                panelChambres.add(lblPas);
            } else {
                for (Chambre chambre : chambres) {
                    JCheckBox chkChambre = new JCheckBox("Chambre " + chambre.getNumero() + " - " + chambre.getCategorie() + " (" + String.format("%.2f", chambre.getPrixUnitaire()) + " MAD/nuit)");
                    chkChambre.setFont(ThemeUtil.POLICE_NORMALE);
                    chkChambre.addActionListener(e -> {
                        if (chkChambre.isSelected()) {
                            chambresSelectionnees.add(chambre);
                        } else {
                            chambresSelectionnees.remove(chambre);
                        }
                    });
                    panelChambres.add(chkChambre);
                }
            }

            lblChambresDisponibles.setText("✓ " + chambres.size() + " chambre(s) disponible(s)");
            panelChambres.revalidate();
            panelChambres.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmerReservation() {
        try {
            // 1. Validation client
            if (!validerClient()) return;

            // 2. Créer ou récupérer client
            Client client = obtenirOuCreerClient();
            if (client == null) return;

            // 3. Validation chambres
            if (chambresSelectionnees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner au moins une chambre", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4. Créer la réservation
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText());

            Reservation resa = new Reservation();
            resa.setIdClient(client.getIdClient());
            resa.setIdUtilisateur(receptionnisteConnecte.getIdUtilisateur());
            resa.setStatut(StatutReservation.CONFIRMEE);

            boolean succes = reservationService.creerNouvelleReservation(resa, new ArrayList<>(chambresSelectionnees), arrivee, depart);

            if (!succes) {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la création de la réservation", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 5. Ajouter les extras
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

            // 6. Afficher la facture
            new FactureFrame(resa.getIdReservation(), facturationService).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validerClient() {
        if (ValidationUtil.estVide(txtNom.getText()) || ValidationUtil.estVide(txtPrenom.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Nom et Prénom obligatoires", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtil.estEmailValide(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Email invalide", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtil.estTelephoneValide(txtTel.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Téléphone invalide", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtil.estCinValide(txtCin.getText())) {
            JOptionPane.showMessageDialog(this, "❌ CIN invalide", "Validation", JOptionPane.WARNING_MESSAGE);
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
                return clientService.trouverClientParCin(cin);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la création du client", "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur client : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void afficherCalendrier(JTextField field, LocalDate date) {
        JDialog dialog = new JDialog(this, "Sélectionner une date", true);
        dialog.setSize(350, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panelCal = DatePickerUtil.creerCalendrier(date, selectedDate -> {
            field.setText(selectedDate.toString());
            dialog.dispose();
        });

        dialog.add(panelCal);
        dialog.setVisible(true);
    }

    private ServiceSupplementaire[] chargerServices() {
        ServiceSupplementaireDAOImpl dao = new ServiceSupplementaireDAOImpl();
        List<ServiceSupplementaire> services = dao.listerTous();
        return services.toArray(new ServiceSupplementaire[0]);
    }

    private static class ServiceSupplementaireDAOImpl extends com.hotel.dao.ServiceSupplementaireDAOImpl {
    }
}