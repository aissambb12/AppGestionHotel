package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.FacturationService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class StatistiquesFrame extends JFrame {

    private FacturationService facturationService;
    private Utilisateur adminConnecte;

    public StatistiquesFrame(Utilisateur admin) {
        this.facturationService = new FacturationService();
        this.adminConnecte = admin;

        setTitle("Hotel Manager - Statistiques");
        setSize(700, 500);
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
        JPanel panelContenu = creerPanelContenu();
        add(panelContenu, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("📊 CHIFFRE D'AFFAIRES");
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

    private JPanel creerPanelContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Titre
        JLabel lblTitre = new JLabel("Calcul du Chiffre d'Affaires");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        // Date début
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel lblDebut = new JLabel("Du :");
        lblDebut.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblDebut, gbc);

        JTextField txtDebut = new JTextField(LocalDate.now().toString());
        ThemeUtil.appliquerThemeTextField(txtDebut);
        gbc.gridx = 1;
        panel.add(txtDebut, gbc);

        // Date fin
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblFin = new JLabel("Au :");
        lblFin.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblFin, gbc);

        JTextField txtFin = new JTextField(LocalDate.now().toString());
        ThemeUtil.appliquerThemeTextField(txtFin);
        gbc.gridx = 1;
        panel.add(txtFin, gbc);

        // Bouton Calculer
        JButton btnCalculer = new JButton("📊 Calculer");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnCalculer);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnCalculer, gbc);

        // Résultat
        JLabel lblResultat = new JLabel("Total : 0.00 MAD");
        lblResultat.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblResultat.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridy = 4;
        panel.add(lblResultat, gbc);

        btnCalculer.addActionListener(e -> {
            try {
                LocalDate debut = LocalDate.parse(txtDebut.getText());
                LocalDate fin = LocalDate.parse(txtFin.getText());
                double ca = facturationService.obtenirChiffreAffaires(debut, fin);
                lblResultat.setText(String.format("Total : %.2f MAD", ca));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Format de date invalide (YYYY-MM-DD)", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
}