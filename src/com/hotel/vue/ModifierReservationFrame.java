package com.hotel.vue;

import com.hotel.model.*;
import com.hotel.service.*;
import com.hotel.util.ValidationUtil;

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
    private com.hotel.dao.ServiceSupplementaireDAOImpl serviceDAO;

    private JTextField txtIdReservation;
    private JPanel panelExtras;
    private Map<Integer, Integer> selectedExtras = new HashMap<>();
    private Facture factureActuelle;

    public ModifierReservationFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();
        this.serviceDAO = new com.hotel.dao.ServiceSupplementaireDAOImpl();

        setTitle("Hotel Manager - Modifier Réservation");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenu = creerPanelContenu();
        add(new JScrollPane(panelContenu), BorderLayout.CENTER);

        JPanel panelBoutons = creerPanelBoutons();
        add(panelBoutons, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("✏️ MODIFIER RÉSERVATION");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);

        /**
         * IMAGE À AJOUTER : back.png (48x48px)
         * Description: Icône d'une flèche gauche
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

    private JPanel creerPanelContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Recherche réservation
        JLabel lblId = new JLabel("ID Réservation :");
        lblId.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblId, gbc);

        txtIdReservation = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtIdReservation);
        gbc.gridx = 1;
        panel.add(txtIdReservation, gbc);

        /**
         * IMAGE À AJOUTER : refresh.png (48x48px)
         * Description: Icône d'une flèche circulaire
         */
        JButton btnChercher = new JButton("🔍 Charger");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        btnChercher.addActionListener(e -> chargerReservation());
        gbc.gridx = 2;
        panel.add(btnChercher, gbc);

        // Extras
        JLabel lblExtras = new JLabel("☕ AJOUTER SERVICES SUPPLÉMENTAIRES");
        lblExtras.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblExtras.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(lblExtras, gbc);

        panelExtras = new JPanel(new GridLayout(0, 1, 5, 5));
        panelExtras.setBackground(Color.WHITE);
        gbc.gridy = 2;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(panelExtras), gbc);

        return panel;
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /**
         * IMAGE À AJOUTER : save.png (48x48px)
         * Description: Icône de sauvegarde verte
         */
        JButton btnSauvegarder = new JButton("✓ SAUVEGARDER");
        ThemeUtil.appliquerThemeBoutonValider(btnSauvegarder);
        btnSauvegarder.setPreferredSize(new Dimension(150, 40));
        btnSauvegarder.addActionListener(e -> sauvegarder());

        /**
         * IMAGE À AJOUTER : cancel.png (48x48px)
         * Description: Icône d'une croix rouge
         */
        JButton btnRetour = new JButton("✕ Annuler");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRetour);
        btnRetour.setPreferredSize(new Dimension(150, 40));
        btnRetour.addActionListener(e -> dispose());

        panel.add(btnSauvegarder);
        panel.add(btnRetour);

        return panel;
    }

    private void chargerReservation() {
        try {
            int idResa = Integer.parseInt(txtIdReservation.getText());
            Reservation resa = reservationService.obtenirDetailsReservation(idResa);
            this.factureActuelle = facturationService.obtenirFactureReservation(idResa);

            if (resa == null) {
                JOptionPane.showMessageDialog(this, "❌ Réservation introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            creerPanelExtras();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ ID invalide (nombre requis)", "Validation", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creerPanelExtras() {
        panelExtras.removeAll();
        selectedExtras.clear();

        List<ServiceSupplementaire> services = serviceDAO.listerTous();

        for (ServiceSupplementaire service : services) {
            JPanel panelExtra = new JPanel(new BorderLayout());
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

        panelExtras.revalidate();
        panelExtras.repaint();
    }

    private void sauvegarder() {
        try {
            int idResa = Integer.parseInt(txtIdReservation.getText());

            for (Map.Entry<Integer, Integer> extra : selectedExtras.entrySet()) {
                if (extra.getValue() > 0) {
                    ReservationServices rs = new ReservationServices();
                    rs.setIdReservation(idResa);
                    rs.setIdService(extra.getKey());
                    rs.setQuantite(extra.getValue());
                    rs.setDateConsommation(LocalDate.now());
                    facturationService.ajouterConsommation(rs);
                }
            }

            JOptionPane.showMessageDialog(this, "✓ Services ajoutés avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ ID invalide", "Validation", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}