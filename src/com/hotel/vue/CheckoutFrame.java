package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.FacturationService;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;

public class CheckoutFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private FacturationService facturationService;

    public CheckoutFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Check-Out & Facturation");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenu = creerPanelContenu();
        add(panelContenu, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("💳 CHECK-OUT & FACTURATION");
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
        btnRetour.addActionListener(e ->
                NavigationManager.retourVers(this, new DashboardReceptionnisteFrame(receptionnisteConnecte)));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("Finaliser le Check-Out");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        JLabel lblDesc = new JLabel("Entrez l'ID de la réservation pour procéder au check-out");
        lblDesc.setFont(ThemeUtil.POLICE_PETIT);
        gbc.gridy = 1;
        panel.add(lblDesc, gbc);

        JLabel lblId = new JLabel("ID Réservation :");
        lblId.setFont(ThemeUtil.POLICE_LABEL);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(lblId, gbc);

        JTextField txtIdReservation = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtIdReservation);
        gbc.gridx = 1;
        panel.add(txtIdReservation, gbc);

        /**
         * IMAGE À AJOUTER : checkin.png (48x48px)
         * Description: Icône de clé d'hôtel ou porte d'accès
         */
        JButton btnCheckout = new JButton("📤 CONFIRMER CHECK-OUT");
        ThemeUtil.appliquerThemeBoutonValider(btnCheckout);
        btnCheckout.setPreferredSize(new Dimension(200, 50));
        btnCheckout.addActionListener(e -> effectuerCheckout(txtIdReservation.getText()));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(40, 20, 20, 20);
        panel.add(btnCheckout, gbc);

        return panel;
    }

    private void effectuerCheckout(String idResaStr) {
        try {
            if (idResaStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Veuillez entrer un ID de réservation", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idResa = Integer.parseInt(idResaStr);

            boolean succes = facturationService.realiserCheckOut(idResa);

            if (succes) {
                new FactureFrame(idResa, facturationService).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors du check-out. Vérifiez l'ID.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ ID invalide (nombre requis)", "Validation", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}