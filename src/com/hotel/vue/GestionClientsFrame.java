package com.hotel.vue;

import com.hotel.model.Client;
import com.hotel.model.Utilisateur;
import com.hotel.service.ClientService;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
        import javax.swing.table.DefaultTableModel;
import java.awt.*;
        import java.util.List;

public class GestionClientsFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private ClientService clientService;
    private DefaultTableModel modeleClients;
    private JTable tableClients;
    private JTextField txtNom, txtPrenom, txtEmail, txtTel, txtCin;

    public GestionClientsFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.clientService = new ClientService();

        setTitle("Hotel Manager - Gestion Clients");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        // === EN-TÊTE ===
        JPanel panelHeader = creerHeader();
        add(panelHeader, BorderLayout.NORTH);

        // === FORMULAIRE ===
        JPanel panelForm = creerFormulaire();
        add(panelForm, BorderLayout.CENTER);

        // === TABLE ===
        JPanel panelTable = creerPanelTable();
        add(panelTable, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("👥 GESTION DES CLIENTS");
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
        btnRetour.addActionListener(e -> {
            new DashboardReceptionnisteFrame(receptionnisteConnecte).setVisible(true);
            dispose();
        });

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerFormulaire() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel lblTitre = new JLabel("Enregistrer un Nouveau Client");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        panel.add(lblTitre, gbc);

        // Champs
        txtNom = new JTextField(12);
        txtPrenom = new JTextField(12);
        txtEmail = new JTextField(15);
        txtTel = new JTextField(12);
        txtCin = new JTextField(12);

        ThemeUtil.appliquerThemeTextField(txtNom);
        ThemeUtil.appliquerThemeTextField(txtPrenom);
        ThemeUtil.appliquerThemeTextField(txtEmail);
        ThemeUtil.appliquerThemeTextField(txtTel);
        ThemeUtil.appliquerThemeTextField(txtCin);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        ajouterChamp(panel, gbc, "Nom :", txtNom, 0);
        ajouterChamp(panel, gbc, "Prénom :", txtPrenom, 1);
        ajouterChamp(panel, gbc, "Email :", txtEmail, 2);
        ajouterChamp(panel, gbc, "Tél :", txtTel, 3);
        ajouterChamp(panel, gbc, "CIN :", txtCin, 4);

        // Bouton
        JButton btnAjouter = new JButton("➕ Enregistrer Client");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);
        btnAjouter.addActionListener(e -> ajouterClient());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        panel.add(btnAjouter, gbc);

        return panel;
    }

    private void ajouterChamp(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int col) {
        gbc.gridx = col;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridy = 1;
        panel.add(field, gbc);
    }

    private JPanel creerPanelTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(0, 300));

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBoutons.setBackground(Color.WHITE);

        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panelBoutons.add(btnRafraichir);
        panel.add(panelBoutons, BorderLayout.NORTH);

        // Table
        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Téléphone", "CIN"};
        modeleClients = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableClients = new JTable(modeleClients);
        ThemeUtil.appliquerThemeTable(tableClients);

        JScrollPane scrollPane = new JScrollPane(tableClients);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void ajouterClient() {
        try {
            if (ValidationUtil.estVide(txtNom.getText()) || ValidationUtil.estVide(txtPrenom.getText())) {
                JOptionPane.showMessageDialog(this, "❌ Nom et Prénom obligatoires", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!ValidationUtil.estEmailValide(txtEmail.getText())) {
                JOptionPane.showMessageDialog(this, "❌ Email invalide", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!ValidationUtil.estTelephoneValide(txtTel.getText())) {
                JOptionPane.showMessageDialog(this, "❌ Téléphone invalide (10 chiffres)", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!ValidationUtil.estCinValide(txtCin.getText())) {
                JOptionPane.showMessageDialog(this, "❌ CIN invalide", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Client client = new Client();
            client.setNom(txtNom.getText());
            client.setPrenom(txtPrenom.getText());
            client.setEmail(txtEmail.getText());
            client.setTelephone(txtTel.getText());
            client.setCin(txtCin.getText());

            boolean succes = clientService.enregistrerClient(client);
            if (succes) {
                JOptionPane.showMessageDialog(this, "✓ Client enregistré avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderFormulaire();
                chargerDonnees();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viderFormulaire() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtCin.setText("");
    }

    private void chargerDonnees() {
        modeleClients.setRowCount(0);
        List<Client> clients = clientService.obtenirTousLesClients();
        for (Client c : clients) {
            modeleClients.addRow(new Object[]{
                    c.getIdClient(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getEmail(),
                    c.getTelephone(),
                    c.getCin()
            });
        }
    }
}
