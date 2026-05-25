package com.hotel.vue;

import com.hotel.model.Client;
import com.hotel.model.Utilisateur;
import com.hotel.service.ClientService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;
import com.hotel.util.ValidationUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionClientsFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private ClientService clientService;
    private DefaultTableModel modeleClients;
    private JTable tableClients;
    private JTextField txtNom, txtPrenom, txtEmail, txtTel, txtCin;
    private JTextField txtRecherche;

    public GestionClientsFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.clientService = new ClientService();

        setTitle("Hotel Manager - Gestion Clients");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());
        add(creerHeader(), BorderLayout.NORTH);

        // Panel central : formulaire en haut, table en dessous, dans un seul JSplitPane-like avec BorderLayout interne.
        JPanel centre = new JPanel(new BorderLayout(0, 0));
        centre.setBackground(ThemeUtil.GRIS_FOND);
        centre.add(creerFormulaire(), BorderLayout.NORTH);
        centre.add(creerPanelTable(), BorderLayout.CENTER);

        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("GESTION DES CLIENTS");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);
        ImageIcon ic = IconLoader.charger("icon_clients", 24);
        if (ic != null) { lblTitre.setIcon(ic); lblTitre.setIconTextGap(10); }

        JButton btnRetour = new JButton("Retour");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRetour);
        IconLoader.appliquerIcone(btnRetour, "icon_back");
        btnRetour.addActionListener(e ->
                NavigationManager.retourVers(this, new DashboardReceptionnisteFrame(receptionnisteConnecte)));

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(btnRetour, BorderLayout.EAST);
        return panel;
    }

    private JPanel creerFormulaire() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 5, 15),
                ThemeUtil.bordureCarte()
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Titre de section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        panel.add(ThemeUtil.creerTitreSection("Enregistrer un Nouveau Client"), gbc);
        gbc.gridwidth = 1;

        // Création des champs
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

        // Ligne 1 : labels
        gbc.gridy = 1;
        gbc.weightx = 0;
        ajouterCellule(panel, gbc, 0, labelChamp("Nom *"));
        ajouterCellule(panel, gbc, 1, labelChamp("Prénom *"));
        ajouterCellule(panel, gbc, 2, labelChamp("CIN *"));
        ajouterCellule(panel, gbc, 3, labelChamp("Email"));
        ajouterCellule(panel, gbc, 4, labelChamp("Téléphone"));

        // Ligne 2 : champs
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        ajouterCellule(panel, gbc, 0, txtNom);
        ajouterCellule(panel, gbc, 1, txtPrenom);
        ajouterCellule(panel, gbc, 2, txtCin);
        ajouterCellule(panel, gbc, 3, txtEmail);
        ajouterCellule(panel, gbc, 4, txtTel);

        // Bouton "Enregistrer"
        JButton btnAjouter = new JButton("Enregistrer Client");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnAjouter);
        IconLoader.appliquerIcone(btnAjouter, "icon_add");
        btnAjouter.addActionListener(e -> ajouterClient());

        gbc.gridy = 2;
        gbc.gridx = 5;
        gbc.weightx = 0;
        panel.add(btnAjouter, gbc);

        return panel;
    }

    private JLabel labelChamp(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(ThemeUtil.POLICE_LABEL);
        lbl.setForeground(ThemeUtil.BLEU_NUIT);
        return lbl;
    }

    private void ajouterCellule(JPanel panel, GridBagConstraints gbc, int col, JComponent comp) {
        gbc.gridx = col;
        panel.add(comp, gbc);
    }

    private JPanel creerPanelTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Barre d'outils : recherche + boutons
        JPanel panelBoutons = new JPanel(new BorderLayout(10, 0));
        panelBoutons.setBackground(Color.WHITE);
        panelBoutons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Recherche dynamique
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelRecherche.setBackground(Color.WHITE);
        JLabel lblRecherche = new JLabel("Rechercher :");
        lblRecherche.setFont(ThemeUtil.POLICE_LABEL);
        ImageIcon icSearch = IconLoader.charger("icon_search", 16);
        if (icSearch != null) { lblRecherche.setIcon(icSearch); lblRecherche.setIconTextGap(6); }
        txtRecherche = new JTextField(20);
        ThemeUtil.appliquerThemeTextField(txtRecherche);
        txtRecherche.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filtrer(); }
            public void removeUpdate(DocumentEvent e)  { filtrer(); }
            public void changedUpdate(DocumentEvent e) { filtrer(); }
        });
        panelRecherche.add(lblRecherche);
        panelRecherche.add(txtRecherche);

        // Actions à droite
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelActions.setBackground(Color.WHITE);

        JButton btnSupprimer = new JButton("Supprimer");
        ThemeUtil.appliquerThemeBoutonSuppression(btnSupprimer);
        IconLoader.appliquerIcone(btnSupprimer, "icon_delete");
        btnSupprimer.addActionListener(e -> supprimerClient());

        JButton btnRafraichir = new JButton("Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir, "icon_refresh");
        btnRafraichir.addActionListener(e -> { txtRecherche.setText(""); chargerDonnees(); });

        panelActions.add(btnSupprimer);
        panelActions.add(btnRafraichir);

        panelBoutons.add(panelRecherche, BorderLayout.WEST);
        panelBoutons.add(panelActions, BorderLayout.EAST);
        panel.add(panelBoutons, BorderLayout.NORTH);

        // Table
        String[] colonnes = {"ID", "Nom", "Prénom", "CIN", "Email", "Téléphone"};
        modeleClients = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tableClients = new JTable(modeleClients);
        ThemeUtil.appliquerThemeTable(tableClients);
        tableClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tableClients);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void filtrer() {
        String motCle = txtRecherche.getText().trim();
        modeleClients.setRowCount(0);
        List<Client> liste = clientService.rechercherClients(motCle);
        for (Client c : liste) {
            modeleClients.addRow(new Object[]{
                    c.getIdClient(), c.getNom(), c.getPrenom(), c.getCin(), c.getEmail(), c.getTelephone()
            });
        }
    }

    private void ajouterClient() {
        try {
            if (ValidationUtil.estVide(txtNom.getText()) || ValidationUtil.estVide(txtPrenom.getText())) {
                JOptionPane.showMessageDialog(this, "Nom et Prénom obligatoires", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!ValidationUtil.estCinValide(txtCin.getText())) {
                JOptionPane.showMessageDialog(this, "CIN invalide", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!ValidationUtil.estVide(txtEmail.getText())
                    && !ValidationUtil.estEmailValide(txtEmail.getText())) {
                JOptionPane.showMessageDialog(this, "Email invalide", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!ValidationUtil.estVide(txtTel.getText())
                    && !ValidationUtil.estTelephoneValide(txtTel.getText())) {
                JOptionPane.showMessageDialog(this, "Téléphone invalide (10 chiffres)", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Client client = new Client();
            client.setNom(txtNom.getText());
            client.setPrenom(txtPrenom.getText());
            client.setEmail(txtEmail.getText());
            client.setTelephone(txtTel.getText());
            client.setCin(txtCin.getText());

            if (clientService.enregistrerClient(client)) {
                JOptionPane.showMessageDialog(this, "Client enregistré avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderFormulaire();
                chargerDonnees();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerClient() {
        int ligne = tableClients.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) modeleClients.getValueAt(ligne, 0);
        int rep = JOptionPane.showConfirmDialog(this,
                "Confirmer la suppression du client #" + id + " ?",
                "Suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (rep != JOptionPane.YES_OPTION) return;
        try {
            if (clientService.supprimerClient(id)) {
                chargerDonnees();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Suppression impossible (client lié à une réservation existante).",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
                    c.getIdClient(), c.getNom(), c.getPrenom(), c.getCin(), c.getEmail(), c.getTelephone()
            });
        }
    }
}
