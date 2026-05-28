package com.hotel.vue;

import com.hotel.dao.impl.ServiceSupplementaireDAOImpl;
import com.hotel.model.*;
import com.hotel.service.*;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierReservationFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private ReservationService reservationService;
    private FacturationService facturationService;
    private ServiceSupplementaireDAOImpl serviceDAO;

    private JTextField txtIdReservation;
    private JPanel panelExtras;
    private Map<Integer, Integer> selectedExtras = new HashMap<>();
    private JLabel lblInfoResa;

    public ModifierReservationFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();
        this.serviceDAO = new ServiceSupplementaireDAOImpl();

        setTitle("Hotel Manager - Modifier Réservation");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JButton btnRetour = ThemeUtil.creerBoutonRetour(e -> retourDashboard());
        add(ThemeUtil.creerHeaderApp("MODIFIER RÉSERVATION", "icon_edit", btnRetour), BorderLayout.NORTH);

        add(new JScrollPane(creerPanelContenu()), BorderLayout.CENTER);
        add(creerPanelBoutons(), BorderLayout.SOUTH);
    }

    private void retourDashboard() {
        NavigationManager.retourVers(this, new DashboardReceptionnisteFrame(receptionnisteConnecte));
    }

    private JPanel creerPanelContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblId = new JLabel("ID Réservation :");
        lblId.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblId, gbc);

        txtIdReservation = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtIdReservation);
        gbc.gridx = 1;
        panel.add(txtIdReservation, gbc);

        JButton btnChercher = new JButton("Charger");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        IconLoader.appliquerIcone(btnChercher, "icon_search");
        btnChercher.addActionListener(e -> chargerReservation());
        gbc.gridx = 2;
        panel.add(btnChercher, gbc);

        lblInfoResa = new JLabel(" ");
        lblInfoResa.setFont(ThemeUtil.POLICE_NORMAL);
        lblInfoResa.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        panel.add(lblInfoResa, gbc);

        gbc.gridy = 2;
        panel.add(ThemeUtil.creerTitreSection("AJOUTER DES SERVICES SUPPLÉMENTAIRES"), gbc);

        panelExtras = new JPanel();
        panelExtras.setLayout(new BoxLayout(panelExtras, BoxLayout.Y_AXIS));
        panelExtras.setBackground(Color.WHITE);

        gbc.gridy = 3; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(panelExtras), gbc);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnSauvegarder = new JButton("Sauvegarder les modifications");
        ThemeUtil.appliquerThemeBoutonValider(btnSauvegarder);
        IconLoader.appliquerIcone(btnSauvegarder, "icon_check");
        btnSauvegarder.setPreferredSize(new Dimension(240, 42));
        btnSauvegarder.addActionListener(e -> sauvegarder());

        JButton btnAnnuler = new JButton("Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnuler);
        btnAnnuler.setPreferredSize(new Dimension(150, 42));
        btnAnnuler.addActionListener(e -> retourDashboard());

        panel.add(btnSauvegarder);
        panel.add(btnAnnuler);
        return panel;
    }

    private void chargerReservation() {
        try {
            int idResa = Integer.parseInt(txtIdReservation.getText().trim());
            Reservation resa = reservationService.obtenirDetailsReservation(idResa);

            // Vider la zone d'extras à chaque chargement
            panelExtras.removeAll();
            selectedExtras.clear();
            panelExtras.revalidate();
            panelExtras.repaint();

            if (resa == null) {
                lblInfoResa.setText(" ");
                JOptionPane.showMessageDialog(this,
                        "Réservation N° " + idResa + " introuvable",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ★ BLOCAGE : seules les réservations CONFIRMEES sont modifiables
            if (resa.getStatut() != com.hotel.model.enumeration.StatutReservation.CONFIRMEE) {
                lblInfoResa.setText("Réservation N° " + idResa + " — Statut : " + resa.getStatut());
                lblInfoResa.setForeground(ThemeUtil.ROUGE_ERREUR);

                String raison;
                switch (resa.getStatut()) {
                    case TERMINEE:
                        raison = "Cette réservation est déjà TERMINÉE.\n"
                                + "Le séjour du client est clôturé, plus aucun service ne peut être ajouté.";
                        break;
                    case ANNULEE:
                        raison = "Cette réservation a été ANNULÉE.\n"
                                + "Elle ne peut plus être modifiée.";
                        break;
                    default:
                        raison = "Cette réservation n'est pas dans un statut modifiable.";
                }

                JOptionPane.showMessageDialog(this,
                        raison + "\n\nSeules les réservations CONFIRMÉES peuvent être modifiées.",
                        "Modification impossible",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Réservation CONFIRMEE → édition autorisée
            lblInfoResa.setText("Réservation N° " + idResa + " — Statut : CONFIRMEE — Édition autorisée");
            lblInfoResa.setForeground(ThemeUtil.VERT_VALIDATION);
            creerPanelExtras();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID invalide", "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creerPanelExtras() {
        panelExtras.removeAll();
        selectedExtras.clear();

        List<ServiceSupplementaire> services = serviceDAO.listerTous();
        for (ServiceSupplementaire service : services) {
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
            JLabel lblQte = new JLabel("0");
            lblQte.setFont(ThemeUtil.POLICE_BOUTON);
            lblQte.setPreferredSize(new Dimension(30, 28));
            lblQte.setHorizontalAlignment(JLabel.CENTER);
            JButton btnPlus = new JButton("+");
            btnPlus.setPreferredSize(new Dimension(32, 28));
            btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 16));

            final int idService = service.getIdService();
            btnPlus.addActionListener(e -> {
                int q = Integer.parseInt(lblQte.getText()) + 1;
                lblQte.setText(String.valueOf(q));
                selectedExtras.put(idService, q);
            });
            btnMoins.addActionListener(e -> {
                int q = Math.max(0, Integer.parseInt(lblQte.getText()) - 1);
                lblQte.setText(String.valueOf(q));
                if (q == 0) selectedExtras.remove(idService);
                else selectedExtras.put(idService, q);
            });

            panelQte.add(btnMoins);
            panelQte.add(lblQte);
            panelQte.add(btnPlus);

            ligne.add(lblNom, BorderLayout.WEST);
            ligne.add(panelQte, BorderLayout.EAST);
            panelExtras.add(ligne);
            panelExtras.add(Box.createVerticalStrut(6));
        }
        panelExtras.revalidate();
        panelExtras.repaint();
    }

    private void sauvegarder() {
        try {
            int idResa = Integer.parseInt(txtIdReservation.getText().trim());

            Reservation verif = reservationService.obtenirDetailsReservation(idResa);
            if (verif == null
                    || verif.getStatut() != com.hotel.model.enumeration.StatutReservation.CONFIRMEE) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de modifier : la réservation n'est pas (ou plus) au statut CONFIRMEE.",
                        "Modification refusée",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedExtras.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun service à ajouter.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Map.Entry<Integer, Integer> e : selectedExtras.entrySet()) {
                if (e.getValue() > 0) {
                    ReservationServices rs = new ReservationServices();
                    rs.setIdReservation(idResa);
                    rs.setIdService(e.getKey());
                    rs.setQuantite(e.getValue());
                    rs.setDateConsommation(LocalDate.now());
                    facturationService.ajouterConsommation(rs);
                }
            }

            // Mise à jour du montant facture
            double nouveauTotal = facturationService.recalculerEtMettreAJourMontantFacture(idResa);

            JOptionPane.showMessageDialog(this,
                    "Services ajoutés avec succès.\nNouveau total facture : "
                            + String.format("%.2f MAD", nouveauTotal),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // ★ Retour au dashboard (au lieu de dispose() qui faisait quitter l'app)
            retourDashboard();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID invalide", "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
