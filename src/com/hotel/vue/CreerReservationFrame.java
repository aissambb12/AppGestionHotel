package com.hotel.vue;

import com.hotel.model.*;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.service.*;
import com.hotel.util.DatePickerUtil;
import com.hotel.util.NavigationManager;
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

    private List<ServiceSupplementaire> servicesDisponibles;

    // UI Components - CLIENT
    private JTextField txtNom, txtPrenom, txtEmail, txtTel, txtCin;

    // UI Components - RÉSERVATION
    private JTextField txtDateArrivee, txtDateDepart;
    private JComboBox<String> comboCategorie;

    // UI Components - CHAMBRES
    private JPanel panelChambresSelectionnees;
    private List<Chambre> chambresSelectionnees = new ArrayList<>();

    // UI Components - EXTRAS
    private JPanel panelExtras;
    private Map<Integer, Integer> selectedExtras = new HashMap<>();

    public CreerReservationFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.clientService = new ClientService();
        this.chambreService = new ChambreService();
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();

        chargerServices();

        setTitle("Hotel Manager - Créer Réservation");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenu = new JPanel(new GridLayout(1, 3, 15, 15));
        panelContenu.setBackground(ThemeUtil.GRIS_FOND);
        panelContenu.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel col1 = creerColonneClient();
        panelContenu.add(new JScrollPane(col1));

        JPanel col2 = creerColonneChambres();
        panelContenu.add(new JScrollPane(col2));

        JPanel col3 = creerColonneExtras();
        panelContenu.add(new JScrollPane(col3));

        add(panelContenu, BorderLayout.CENTER);

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

        /**
         * IMAGE À AJOUTER : back.png (48x48px)
         * Description: Icône d'une flèche gauche pour retour
         */
        JButton btnRetour = new JButton("← Retour");
        btnRetour.setBackground(ThemeUtil.GRIS_CLAIR);
        btnRetour.setForeground(ThemeUtil.TEXTE_SOMBRE);
        btnRetour.setFont(ThemeUtil.POLICE_BOUTON);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setContentAreaFilled(true);
        btnRetour.setBorderPainted(true);
        btnRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.addActionListener(e -> dispose());

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerColonneClient() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblSecClient = new JLabel("👥 CLIENT");
        lblSecClient.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblSecClient.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
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

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JSeparator sep1 = new JSeparator();
        panel.add(sep1, gbc);

        gbc.gridy = 7;
        JLabel lblSecResa = new JLabel("📅 RÉSERVATION");
        lblSecResa.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblSecResa.setForeground(ThemeUtil.BLEU_NUIT);
        panel.add(lblSecResa, gbc);

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

        gbc.gridy = 10;
        gbc.gridx = 0;
        panel.add(new JLabel("Catégorie :"), gbc);

        comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});
        gbc.gridx = 1;
        panel.add(comboCategorie, gbc);

        JButton btnChercher = new JButton("🔍 CHERCHER CHAMBRES");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        btnChercher.addActionListener(e -> rechercherChambres());
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        panel.add(btnChercher, gbc);

        return panel;
    }

    private JPanel creerColonneChambres() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("🛏️ CHAMBRES DISPONIBLES");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        JLabel lblInfo = new JLabel("Cliquez 'CHERCHER CHAMBRES'");
        lblInfo.setFont(ThemeUtil.POLICE_PETIT);
        gbc.gridy = 1;
        panel.add(lblInfo, gbc);

        panelChambresSelectionnees = new JPanel(new GridLayout(0, 1, 5, 5));
        panelChambresSelectionnees.setBackground(Color.WHITE);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(panelChambresSelectionnees), gbc);

        return panel;
    }

    private JPanel creerColonneExtras() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("☕ SERVICES SUPPLÉMENTAIRES");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitre, gbc);

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

        if (servicesDisponibles != null && !servicesDisponibles.isEmpty()) {
            for (ServiceSupplementaire service : servicesDisponibles) {
                JPanel panelExtra = new JPanel(new BorderLayout(5, 5));
                panelExtra.setBackground(ThemeUtil.GRIS_FOND);
                panelExtra.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));

                JLabel lblNom = new JLabel(service.getNomService() + " - " + String.format("%.2f MAD", service.getPrixService()));
                lblNom.setFont(ThemeUtil.POLICE_NORMAL);

                JPanel panelQte = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                panelQte.setBackground(ThemeUtil.GRIS_FOND);

                JButton btnMoins = new JButton("−");
                btnMoins.setPreferredSize(new Dimension(30, 30));
                btnMoins.setFont(new Font("Arial", Font.BOLD, 16));

                JLabel lblQte = new JLabel("0");
                lblQte.setFont(ThemeUtil.POLICE_BOUTON);
                lblQte.setPreferredSize(new Dimension(30, 30));
                lblQte.setHorizontalAlignment(JLabel.CENTER);

                JButton btnPlus = new JButton("+");
                btnPlus.setPreferredSize(new Dimension(30, 30));
                btnPlus.setFont(new Font("Arial", Font.BOLD, 16));

                final int idService = service.getIdService();
                btnPlus.addActionListener(e -> {
                    int qte = Integer.parseInt(lblQte.getText()) + 1;
                    lblQte.setText(String.valueOf(qte));
                    selectedExtras.put(idService, qte);
                });

                btnMoins.addActionListener(e -> {
                    int qte = Math.max(0, Integer.parseInt(lblQte.getText()) - 1);
                    lblQte.setText(String.valueOf(qte));
                    if (qte == 0) {
                        selectedExtras.remove(idService);
                    } else {
                        selectedExtras.put(idService, qte);
                    }
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

        /**
         * IMAGE À AJOUTER : save.png (48x48px)
         * Description: Icône d'une disquette ou d'une sauvegarde verte
         */
        JButton btnValider = new JButton("✓ CONFIRMER RÉSERVATION");
        ThemeUtil.appliquerThemeBoutonValider(btnValider);
        btnValider.setPreferredSize(new Dimension(220, 40));
        btnValider.addActionListener(e -> confirmerReservation());

        /**
         * IMAGE À AJOUTER : cancel.png (48x48px)
         * Description: Icône d'une croix rouge
         */
        JButton btnAnnuler = new JButton("✕ Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        btnAnnuler.setPreferredSize(new Dimension(150, 40));
        btnAnnuler.addActionListener(e -> dispose());

        panel.add(btnValider);
        panel.add(btnAnnuler);

        return panel;
    }

    private void rechercherChambres() {
        try {
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText());

            if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
                JOptionPane.showMessageDialog(this, "❌ La date de départ doit être après l'arrivée", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String categorie = comboCategorie.getSelectedItem().toString();
            List<Chambre> chambres = chambreService.rechercherChambresDisponibles(arrivee, depart, categorie);

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
                    chkChambre.setFont(ThemeUtil.POLICE_NORMAL);
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

    private void confirmerReservation() {
        try {
            // 1️⃣ VALIDATION CLIENT - ATOMIQUE
            if (!validerClient()) {
                return;
            }

            // 2️⃣ OBTENIR OU CRÉER CLIENT - ATOMIQUE
            Client client = obtenirOuCreerClient();
            if (client == null) {
                return;
            }

            // 3️⃣ VALIDATION CHAMBRES - ATOMIQUE
            if (chambresSelectionnees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner au moins une chambre", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4️⃣ VALIDATION DATES - ATOMIQUE
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText());

            if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
                JOptionPane.showMessageDialog(this, "❌ Les dates sont invalides", "Validation", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 5️⃣ CRÉER LA RÉSERVATION - ATOMIQUE
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

            // 6️⃣ AJOUTER LES EXTRAS - ATOMIQUE
            for (Map.Entry<Integer, Integer> extra : selectedExtras.entrySet()) {
                if (extra.getValue() > 0) {
                    ReservationServices rs = new ReservationServices();
                    rs.setIdReservation(resa.getIdReservation());
                    rs.setIdService(extra.getKey());
                    rs.setQuantite(extra.getValue());
                    rs.setDateConsommation(LocalDate.now());

                    boolean extraAjoute = facturationService.ajouterConsommation(rs);
                    if (!extraAjoute) {
                        JOptionPane.showMessageDialog(this, "⚠️ Erreur lors de l'ajout d'un extra", "Avertissement", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            // 7️⃣ AFFICHER LA FACTURE
            Facture facture = facturationService.obtenirFactureReservation(resa.getIdReservation());
            if (facture != null) {
                new FactureFrame(resa.getIdReservation(), facturationService).setVisible(true);
            }

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur critique : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean validerClient() {
        if (ValidationUtil.estVide(txtNom.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Nom obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ValidationUtil.estVide(txtPrenom.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Prénom obligatoire", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtil.estEmailValide(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Email invalide", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtil.estTelephoneValide(txtTel.getText())) {
            JOptionPane.showMessageDialog(this, "❌ Téléphone invalide (10 chiffres)", "Validation", JOptionPane.WARNING_MESSAGE);
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
            String cin = txtCin.getText().trim().toUpperCase();
            Client existant = clientService.trouverClientParCin(cin);

            if (existant != null) {
                return existant;
            }

            Client nouveau = new Client();
            nouveau.setNom(txtNom.getText().trim().toUpperCase());
            nouveau.setPrenom(txtPrenom.getText().trim());
            nouveau.setEmail(txtEmail.getText().trim());
            nouveau.setTelephone(txtTel.getText().trim());
            nouveau.setCin(cin);

            boolean succes = clientService.enregistrerClient(nouveau);

            if (succes) {
                Client clientCree = clientService.trouverClientParCin(cin);
                if (clientCree != null) {
                    return clientCree;
                }
            }

            return null;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur client : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void chargerServices() {
        try {
            com.hotel.dao.ServiceSupplementaireDAOImpl dao = new com.hotel.dao.ServiceSupplementaireDAOImpl();
            servicesDisponibles = dao.listerTous();
            if (servicesDisponibles == null) {
                servicesDisponibles = new ArrayList<>();
            }
        } catch (Exception ex) {
            System.err.println("Erreur chargement services : " + ex.getMessage());
            servicesDisponibles = new ArrayList<>();
        }
    }
}