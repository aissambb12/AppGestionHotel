package com.hotel.vue;

import com.hotel.dao.impl.ServiceSupplementaireDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.service.*;
import com.hotel.util.DatePickerUtil;
import com.hotel.util.IconLoader;
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

    private JTextField txtNom, txtPrenom, txtEmail, txtTel, txtCin;
    private JTextField txtDateArrivee, txtDateDepart;
    private JComboBox<String> comboCategorie;

    private JPanel panelChambresSelectionnees;
    private List<Chambre> chambresSelectionnees = new ArrayList<>();

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
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JButton btnRetour = ThemeUtil.creerBoutonRetour(e -> retourDashboard());
        add(ThemeUtil.creerHeaderApp("CRÉER UNE RÉSERVATION COMPLÈTE", "icon_reservations", btnRetour), BorderLayout.NORTH);

        JPanel contenu = new JPanel(new GridLayout(1, 3, 15, 15));
        contenu.setBackground(ThemeUtil.GRIS_FOND);
        contenu.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenu.add(new JScrollPane(creerColonneClient()));
        contenu.add(new JScrollPane(creerColonneChambres()));
        contenu.add(new JScrollPane(creerColonneExtras()));
        add(contenu, BorderLayout.CENTER);

        add(creerPanelBoutons(), BorderLayout.SOUTH);
    }

    private void retourDashboard() {
        NavigationManager.retourVers(this, new DashboardReceptionnisteFrame(receptionnisteConnecte));
    }

    private JPanel creerColonneClient() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(ThemeUtil.creerTitreSection("CLIENT"), gbc);

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
        ajouterLigne(panel, gbc, 1, "Nom :", txtNom);
        ajouterLigne(panel, gbc, 2, "Prénom :", txtPrenom);
        ajouterLigne(panel, gbc, 3, "Email :", txtEmail);
        ajouterLigne(panel, gbc, 4, "Tél :", txtTel);
        ajouterLigne(panel, gbc, 5, "CIN :", txtCin);

        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 7;
        panel.add(ThemeUtil.creerTitreSection("RÉSERVATION"), gbc);

        gbc.gridwidth = 1;
        txtDateArrivee = new JTextField(LocalDate.now().toString());
        ThemeUtil.appliquerThemeTextField(txtDateArrivee);
        ajouterLigneAvecCalendrier(panel, gbc, 8, "Arrivée :", txtDateArrivee);

        txtDateDepart = new JTextField(LocalDate.now().plusDays(1).toString());
        ThemeUtil.appliquerThemeTextField(txtDateDepart);
        ajouterLigneAvecCalendrier(panel, gbc, 9, "Départ :", txtDateDepart);

        comboCategorie = new JComboBox<>(new String[]{"SIMPLE", "DOUBLE", "SUITE"});
        comboCategorie.setFont(ThemeUtil.POLICE_NORMAL);
        ajouterLigne(panel, gbc, 10, "Catégorie :", comboCategorie);

        JButton btnChercher = new JButton("Chercher chambres disponibles");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        IconLoader.appliquerIcone(btnChercher, "icon_search");
        btnChercher.addActionListener(e -> rechercherChambres());
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnChercher, gbc);

        return panel;
    }

    private void ajouterLigne(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private void ajouterLigneAvecCalendrier(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lbl, gbc);

        JPanel wrapper = new JPanel(new BorderLayout(5, 0));
        wrapper.setBackground(Color.WHITE);
        JButton btnCal = new JButton("...");
        btnCal.setPreferredSize(new Dimension(38, 30));
        btnCal.setFocusPainted(false);
        btnCal.addActionListener(e -> afficherCalendrier(field));
        wrapper.add(field, BorderLayout.CENTER);
        wrapper.add(btnCal, BorderLayout.EAST);

        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(wrapper, gbc);
    }

    private JPanel creerColonneChambres() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)));

        JLabel lblTitre = ThemeUtil.creerTitreSection("CHAMBRES DISPONIBLES");
        ImageIcon ic = IconLoader.charger("icon_chambres", 22);
        if (ic != null) { lblTitre.setIcon(ic); lblTitre.setIconTextGap(8); }
        panel.add(lblTitre, BorderLayout.NORTH);

        panelChambresSelectionnees = new JPanel();
        panelChambresSelectionnees.setLayout(new BoxLayout(panelChambresSelectionnees, BoxLayout.Y_AXIS));
        panelChambresSelectionnees.setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel("Cliquez sur 'Chercher chambres disponibles'");
        lblInfo.setFont(ThemeUtil.POLICE_PETIT);
        lblInfo.setForeground(new Color(140, 140, 140));
        panelChambresSelectionnees.add(lblInfo);

        panel.add(new JScrollPane(panelChambresSelectionnees), BorderLayout.CENTER);
        return panel;
    }

    private JPanel creerColonneExtras() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)));

        JLabel lblTitre = ThemeUtil.creerTitreSection("SERVICES SUPPLÉMENTAIRES");
        ImageIcon ic = IconLoader.charger("icon_paiements", 22);
        if (ic != null) { lblTitre.setIcon(ic); lblTitre.setIconTextGap(8); }
        panel.add(lblTitre, BorderLayout.NORTH);

        panelExtras = new JPanel();
        panelExtras.setLayout(new BoxLayout(panelExtras, BoxLayout.Y_AXIS));
        panelExtras.setBackground(Color.WHITE);
        remplirPanelExtras();

        panel.add(new JScrollPane(panelExtras), BorderLayout.CENTER);
        return panel;
    }

    private void remplirPanelExtras() {
        panelExtras.removeAll();
        if (servicesDisponibles == null || servicesDisponibles.isEmpty()) {
            JLabel lbl = new JLabel("Aucun service disponible");
            lbl.setFont(ThemeUtil.POLICE_PETIT);
            panelExtras.add(lbl);
        } else {
            for (ServiceSupplementaire service : servicesDisponibles) {
                panelExtras.add(creerLigneExtra(service));
                panelExtras.add(Box.createVerticalStrut(6));
            }
        }
        panelExtras.revalidate();
        panelExtras.repaint();
    }

    private JPanel creerLigneExtra(ServiceSupplementaire service) {
        JPanel ligne = new JPanel(new BorderLayout(8, 0));
        ligne.setBackground(ThemeUtil.GRIS_FOND);
        ligne.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblNom = new JLabel(service.getNomService() + " - " + String.format("%.2f MAD", service.getPrixService()));
        lblNom.setFont(ThemeUtil.POLICE_NORMAL);

        JPanel panelQte = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelQte.setBackground(ThemeUtil.GRIS_FOND);

        JButton btnMoins = new JButton("-");
        btnMoins.setPreferredSize(new Dimension(32, 28));
        btnMoins.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnMoins.setFocusPainted(false);

        JLabel lblQte = new JLabel("0");
        lblQte.setFont(ThemeUtil.POLICE_BOUTON);
        lblQte.setPreferredSize(new Dimension(30, 28));
        lblQte.setHorizontalAlignment(JLabel.CENTER);

        JButton btnPlus = new JButton("+");
        btnPlus.setPreferredSize(new Dimension(32, 28));
        btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPlus.setFocusPainted(false);

        final int idService = service.getIdService();
        btnPlus.addActionListener(e -> {
            int qte = Integer.parseInt(lblQte.getText()) + 1;
            lblQte.setText(String.valueOf(qte));
            selectedExtras.put(idService, qte);
        });
        btnMoins.addActionListener(e -> {
            int qte = Math.max(0, Integer.parseInt(lblQte.getText()) - 1);
            lblQte.setText(String.valueOf(qte));
            if (qte == 0) selectedExtras.remove(idService);
            else selectedExtras.put(idService, qte);
        });

        panelQte.add(btnMoins);
        panelQte.add(lblQte);
        panelQte.add(btnPlus);

        ligne.add(lblNom, BorderLayout.WEST);
        ligne.add(panelQte, BorderLayout.EAST);
        return ligne;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnValider = new JButton("Confirmer la réservation");
        ThemeUtil.appliquerThemeBoutonValider(btnValider);
        IconLoader.appliquerIcone(btnValider, "icon_check");
        btnValider.setPreferredSize(new Dimension(260, 44));
        btnValider.addActionListener(e -> confirmerReservation());

        JButton btnAnnuler = new JButton("Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        btnAnnuler.setPreferredSize(new Dimension(150, 44));
        btnAnnuler.addActionListener(e -> retourDashboard());

        panel.add(btnValider);
        panel.add(btnAnnuler);
        return panel;
    }

    private void rechercherChambres() {
        try {
            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText().trim());
            LocalDate depart = LocalDate.parse(txtDateDepart.getText().trim());

            if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
                JOptionPane.showMessageDialog(this, "La date de départ doit être après l'arrivée", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String categorie = comboCategorie.getSelectedItem().toString();
            List<Chambre> chambres = chambreService.rechercherChambresDisponibles(arrivee, depart, categorie);

            panelChambresSelectionnees.removeAll();
            chambresSelectionnees.clear();

            if (chambres.isEmpty()) {
                JLabel lbl = new JLabel("Aucune chambre disponible pour ces dates");
                lbl.setFont(ThemeUtil.POLICE_NORMAL);
                lbl.setForeground(ThemeUtil.ROUGE_ERREUR);
                panelChambresSelectionnees.add(lbl);
            } else {
                for (Chambre chambre : chambres) {
                    JCheckBox chk = new JCheckBox(
                            "Chambre " + chambre.getNumero()
                                    + " - " + chambre.getCategorie()
                                    + " (" + String.format("%.2f", chambre.getPrixUnitaire()) + " MAD/nuit)"
                    );
                    chk.setFont(ThemeUtil.POLICE_NORMAL);
                    chk.setBackground(Color.WHITE);
                    chk.setAlignmentX(Component.LEFT_ALIGNMENT);

                    final Chambre f = chambre;
                    chk.addActionListener(e -> {
                        if (chk.isSelected()) {
                            if (!chambresSelectionnees.contains(f)) chambresSelectionnees.add(f);
                        } else {
                            chambresSelectionnees.remove(f);
                        }
                    });
                    panelChambresSelectionnees.add(chk);
                }
            }
            panelChambresSelectionnees.revalidate();
            panelChambresSelectionnees.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherCalendrier(JTextField field) {
        try {
            LocalDate dateActuelle = LocalDate.parse(field.getText().trim());
            JDialog dialog = new JDialog(this, "Sélectionner une date", true);
            dialog.setSize(420, 480);          // ← avant : 350x400
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            JPanel panelCal = DatePickerUtil.creerCalendrier(dateActuelle, selectedDate -> {
                field.setText(selectedDate.toString());
                dialog.dispose();
            });
            dialog.add(panelCal);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmerReservation() {
        try {
            if (!validerClient()) return;

            Client client = obtenirOuCreerClient();
            if (client == null) return;

            if (chambresSelectionnees.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner au moins une chambre",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate arrivee = LocalDate.parse(txtDateArrivee.getText().trim());
            LocalDate depart  = LocalDate.parse(txtDateDepart.getText().trim());

            if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
                JOptionPane.showMessageDialog(this,
                        "Dates invalides",
                        "Validation", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Création de la réservation
            Reservation resa = new Reservation();
            resa.setIdClient(client.getIdClient());
            resa.setIdUtilisateur(receptionnisteConnecte.getIdUtilisateur());
            resa.setDateCreation(LocalDateTime.now());
            resa.setStatut(StatutReservation.CONFIRMEE);

            boolean ok = reservationService.creerNouvelleReservation(
                    resa,
                    new ArrayList<>(chambresSelectionnees),
                    arrivee, depart);

            if (!ok || resa.getIdReservation() <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la création de la réservation",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ajout des extras sélectionnés (sans recalcul ni facture, ce sera au check-out)
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

            // Message de confirmation propre
            JOptionPane.showMessageDialog(this,
                    "Réservation N° " + resa.getIdReservation() + " créée avec succès.\n\n"
                            + "Client : " + client.getNom() + " " + client.getPrenom() + "\n"
                            + "Du " + arrivee + " au " + depart + "\n"
                            + "Chambres : " + chambresSelectionnees.size() + "\n\n",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            // Retour direct au dashboard receptionniste
            retourDashboard();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean validerClient() {
        if (ValidationUtil.estVide(txtNom.getText())) { msgValidation("Nom obligatoire"); return false; }
        if (ValidationUtil.estVide(txtPrenom.getText())) { msgValidation("Prénom obligatoire"); return false; }
        if (!ValidationUtil.estEmailValide(txtEmail.getText())) { msgValidation("Email invalide"); return false; }
        if (!ValidationUtil.estTelephoneValide(txtTel.getText())) { msgValidation("Téléphone invalide (10 chiffres)"); return false; }
        if (!ValidationUtil.estCinValide(txtCin.getText())) { msgValidation("CIN invalide"); return false; }
        return true;
    }

    private void msgValidation(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    private Client obtenirOuCreerClient() {
        try {
            String cin = txtCin.getText().trim().toUpperCase();
            Client existant = clientService.trouverClientParCin(cin);
            if (existant != null) return existant;

            Client nouveau = new Client();
            nouveau.setNom(txtNom.getText().trim().toUpperCase());
            nouveau.setPrenom(txtPrenom.getText().trim());
            nouveau.setEmail(txtEmail.getText().trim());
            nouveau.setTelephone(txtTel.getText().trim());
            nouveau.setCin(cin);

            if (clientService.enregistrerClient(nouveau)) {
                return clientService.trouverClientParCin(cin);
            }
            return null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur client : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void chargerServices() {
        try {
            servicesDisponibles = new ServiceSupplementaireDAOImpl().listerTous();
            if (servicesDisponibles == null) servicesDisponibles = new ArrayList<>();
        } catch (Exception ex) {
            servicesDisponibles = new ArrayList<>();
        }
    }
}
