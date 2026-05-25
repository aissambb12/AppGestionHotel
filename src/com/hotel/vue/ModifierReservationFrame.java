package com.hotel.vue;

import com.hotel.model.*;
import com.hotel.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private Facture factureActuelle;

    public ModifierReservationFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.reservationService = new ReservationService();
        this.facturationService = new FacturationService();
        this.serviceDAO = new ServiceSupplementaireDAOImpl();

        setTitle("Hotel Manager - Modifier Réservation");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === CONTENU ===
        JPanel panelContenu = new JPanel(new GridBagLayout());
        panelContenu.setBackground(Color.WHITE);
        panelContenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Recherche réservation
        JLabel lblId = new JLabel("ID Réservation :");
        lblId.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridx = 0; gbc.gridy = 0;
        panelContenu.add(lblId, gbc);

        txtIdReservation = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtIdReservation);
        gbc.gridx = 1;
        panelContenu.add(txtIdReservation, gbc);

        JButton btnChercher = new JButton("🔍 Charger");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnChercher);
        btnChercher.addActionListener(e -> chargerReservation());
        gbc.gridx = 2;
        panelContenu.add(btnChercher, gbc);

        // Extras
        JLabel lblExtras = new JLabel("AJOUTER EXTRAS");
        lblExtras.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        panelContenu.add(lblExtras, gbc);

        panelExtras = new JPanel(new GridLayout(0, 1, 5, 5));
        panelExtras.setBackground(Color.WHITE);
        gbc.gridy = 2; gbc.weighty = 0.8;
        panelContenu.add(panelExtras, gbc);

        // === BOUTONS ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBoutons.setBackground(Color.WHITE);

        JButton btnSauvegarder = new JButton("✓ Sauvegarder");
        ThemeUtil.appliquerThemeBoutonValider(btnSauvegarder);
        btnSauvegarder.addActionListener(e -> sauvegarder());

        JButton btnRetour = new JButton("← Retour");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRetour);
        btnRetour.addActionListener(e -> dispose());

        panelBoutons.add(btnSauvegarder);
        panelBoutons.add(btnRetour);

        add(new JScrollPane(panelContenu), BorderLayout.CENTER);
        add(panelBoutons, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("✏️ MODIFIER RÉSERVATION");
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

            JOptionPane.showMessageDialog(this, "✓ Extras ajoutés avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ServiceSupplementaireDAOImpl extends com.hotel.dao.ServiceSupplementaireDAOImpl {
    }
}