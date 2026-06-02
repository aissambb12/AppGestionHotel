package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.FacturationService;
import com.hotel.util.IconLoader;
import com.hotel.util.ValidationUtil;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class StatistiquesFrame extends JFrame {

    private FacturationService facturationService;
    private Utilisateur adminConnecte;

    public StatistiquesFrame(Utilisateur admin) {
        this.facturationService = new FacturationService();
        this.adminConnecte = admin;

        setTitle("Hotel Manager - Statistiques & Chiffre d'Affaires");
        setSize(800, 600);
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

        JLabel lblTitre = new JLabel("CHIFFRE D'AFFAIRES");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);


        JButton btnRetour = new JButton("Retour");
        btnRetour.setBackground(ThemeUtil.GRIS_CLAIR);
        btnRetour.setForeground(ThemeUtil.TEXTE_SOMBRE);
        btnRetour.setFont(ThemeUtil.POLICE_BOUTON);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setContentAreaFilled(true);
        btnRetour.setBorderPainted(true);
        btnRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        IconLoader.appliquerIcone(btnRetour , "icon_back");
        btnRetour.addActionListener(e ->
                NavigationManager.retourVers(this, new DashboardAdminFrame(adminConnecte)));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Titre
        JLabel lblTitre = new JLabel("Calcul du Chiffre d'Affaires");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        // Descriptif
        JLabel lblDesc = new JLabel("Sélectionnez une période pour calculer le CA sur les factures PAYÉES");
        lblDesc.setFont(ThemeUtil.POLICE_PETIT);
        gbc.gridy = 1;
        panel.add(lblDesc, gbc);

        // Date début
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        JLabel lblDebut = new JLabel("Du :");
        lblDebut.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblDebut, gbc);

        JTextField txtDebut = new JTextField(LocalDate.now().minusMonths(1).toString());
        ThemeUtil.appliquerThemeTextField(txtDebut);
        gbc.gridx = 1;
        panel.add(txtDebut, gbc);

        // Date fin
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblFin = new JLabel("Au :");
        lblFin.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblFin, gbc);

        JTextField txtFin = new JTextField(LocalDate.now().toString());
        ThemeUtil.appliquerThemeTextField(txtFin);
        gbc.gridx = 1;
        panel.add(txtFin, gbc);

        // Bouton Calculer
        JButton btnCalculer = new JButton(" CALCULER ");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnCalculer);
        btnCalculer.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(btnCalculer, gbc);

        // Résultat
        JLabel lblResultat = new JLabel("Total : 0.00 MAD");
        lblResultat.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblResultat.setForeground(ThemeUtil.DORE_LUXE);
        lblResultat.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 5;
        gbc.insets = new Insets(40, 15, 15, 15);
        panel.add(lblResultat, gbc);

        // Message info
        JLabel lblInfo = new JLabel("Résultat agrégé des factures PAYÉES sur la période");
        lblInfo.setFont(ThemeUtil.POLICE_PETIT);
        lblInfo.setForeground(new Color(100, 100, 100));
        lblInfo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 15, 15, 15);
        panel.add(lblInfo, gbc);

        btnCalculer.addActionListener(e -> {
            try {
                String debut = txtDebut.getText().trim();
                String fin = txtFin.getText().trim();

                if (ValidationUtil.estVide(debut) || ValidationUtil.estVide(fin)) {
                    JOptionPane.showMessageDialog(this, "❌ Les deux dates sont obligatoires", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                LocalDate dateDebut = LocalDate.parse(debut);
                LocalDate dateFin = LocalDate.parse(fin);

                if (dateDebut.isAfter(dateFin)) {
                    JOptionPane.showMessageDialog(this, "❌ La date de début doit être avant la date de fin", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double ca = facturationService.obtenirChiffreAffaires(dateDebut, dateFin);
                lblResultat.setText(String.format("Total : %.2f MAD", ca));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "❌ Format de date invalide\n\nFormat requis : YYYY-MM-DD\nExemple : 2026-01-15",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                lblResultat.setText("Total : 0.00 MAD");
            }
        });

        return panel;
    }
}