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
    private JButton btnEnregistrer;
    private JLabel lblTitreForm;

    // État : id du client en cours d'édition (0 = mode ajout, >0 = mode modification)
    private int idClientEdition = 0;

    public GestionClientsFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.clientService = new ClientService();

        setTitle("Hotel Manager - Gestion Clients");
        setSize(1150, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JButton btnRetour = ThemeUtil.creerBoutonRetour(e ->
                NavigationManager.retourVers(this, new DashboardReceptionnisteFrame(receptionnisteConnecte)));
        add(ThemeUtil.creerHeaderApp("GESTION DES CLIENTS", "icon_clients", btnRetour), BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(0, 0));
        centre.setBackground(ThemeUtil.GRIS_FOND);
        centre.add(creerFormulaire(), BorderLayout.NORTH);
        centre.add(creerPanelTable(), BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);
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

        lblTitreForm = ThemeUtil.creerTitreSection("Enregistrer un Nouveau Client");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 6;
        panel.add(lblTitreForm, gbc);
        gbc.gridwidth = 1;

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

        gbc.gridy = 1; gbc.weightx = 0;
        cellule(panel, gbc, 0, labelChamp("Nom *"));
        cellule(panel, gbc, 1, labelChamp("Prénom *"));
        cellule(panel, gbc, 2, labelChamp("CIN *"));
        cellule(panel, gbc, 3, labelChamp("Email"));
        cellule(panel, gbc, 4, labelChamp("Téléphone"));

        gbc.gridy = 2; gbc.weightx = 1.0;
        cellule(panel, gbc, 0, txtNom);
        cellule(panel, gbc, 1, txtPrenom);
        cellule(panel, gbc, 2, txtCin);
        cellule(panel, gbc, 3, txtEmail);
        cellule(panel, gbc, 4, txtTel);

        btnEnregistrer = new JButton("Enregistrer");
        ThemeUtil.appliquerThemeBoutonPrincipal(btnEnregistrer);
        IconLoader.appliquerIcone(btnEnregistrer, "icon_add");
        btnEnregistrer.addActionListener(e -> validerFormulaire());

        gbc.gridy = 2; gbc.gridx = 5; gbc.weightx = 0;
        panel.add(btnEnregistrer, gbc);

        // Petit bouton "Annuler édition" sous le bouton principal
        JButton btnAnnulerEdit = new JButton("Annuler édition");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnAnnulerEdit);
        btnAnnulerEdit.addActionListener(e -> repasserEnModeAjout());
        gbc.gridy = 3;
        panel.add(btnAnnulerEdit, gbc);

        return panel;
    }

    private JLabel labelChamp(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(ThemeUtil.POLICE_LABEL);
        lbl.setForeground(ThemeUtil.BLEU_NUIT);
        return lbl;
    }

    private void cellule(JPanel panel, GridBagConstraints gbc, int col, JComponent comp) {
        gbc.gridx = col;
        panel.add(comp, gbc);
    }

    private JPanel creerPanelTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JPanel barre = new JPanel(new BorderLayout(10, 0));
        barre.setBackground(Color.WHITE);
        barre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

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

        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelActions.setBackground(Color.WHITE);

        JButton btnModifier = new JButton("Modifier");
        ThemeUtil.appliquerThemeBoutonAttention(btnModifier);
        IconLoader.appliquerIcone(btnModifier, "icon_edit");
        btnModifier.addActionListener(e -> chargerSelectionnePourEdition());

        JButton btnSupprimer = new JButton("Supprimer");
        ThemeUtil.appliquerThemeBoutonSuppression(btnSupprimer);
        IconLoader.appliquerIcone(btnSupprimer, "icon_delete");
        btnSupprimer.addActionListener(e -> supprimerClient());

        JButton btnRafraichir = new JButton("Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir, "icon_refresh");
        btnRafraichir.addActionListener(e -> { txtRecherche.setText(""); chargerDonnees(); });

        panelActions.add(btnModifier);
        panelActions.add(btnSupprimer);
        panelActions.add(btnRafraichir);

        barre.add(panelRecherche, BorderLayout.WEST);
        barre.add(panelActions, BorderLayout.EAST);
        panel.add(barre, BorderLayout.NORTH);

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

    private void chargerDonnees() {
        modeleClients.setRowCount(0);
        for (Client c : clientService.obtenirTousLesClients()) {
            modeleClients.addRow(new Object[]{
                    c.getIdClient(), c.getNom(), c.getPrenom(), c.getCin(), c.getEmail(), c.getTelephone()
            });
        }
    }

    // ★ Charge la ligne sélectionnée dans le formulaire pour modification
    private void chargerSelectionnePourEdition() {
        int ligne = tableClients.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner un client dans la liste.", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        idClientEdition = (Integer) modeleClients.getValueAt(ligne, 0);
        txtNom.setText(String.valueOf(modeleClients.getValueAt(ligne, 1)));
        txtPrenom.setText(String.valueOf(modeleClients.getValueAt(ligne, 2)));
        txtCin.setText(String.valueOf(modeleClients.getValueAt(ligne, 3)));
        Object email = modeleClients.getValueAt(ligne, 4);
        Object tel   = modeleClients.getValueAt(ligne, 5);
        txtEmail.setText(email == null ? "" : email.toString());
        txtTel.setText(tel == null ? "" : tel.toString());

        // Mode édition
        lblTitreForm.setText("Modifier le Client #" + idClientEdition);
        btnEnregistrer.setText("Mettre à jour");
        IconLoader.appliquerIcone(btnEnregistrer, "icon_edit");
    }

    private void repasserEnModeAjout() {
        idClientEdition = 0;
        viderFormulaire();
        lblTitreForm.setText("Enregistrer un Nouveau Client");
        btnEnregistrer.setText("Enregistrer");
        IconLoader.appliquerIcone(btnEnregistrer, "icon_add");
    }

    private void validerFormulaire() {
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

            Client c = new Client();
            c.setNom(txtNom.getText().trim().toUpperCase());
            c.setPrenom(txtPrenom.getText().trim());
            c.setCin(txtCin.getText().trim().toUpperCase());
            c.setEmail(txtEmail.getText().trim());
            c.setTelephone(txtTel.getText().trim());

            if (idClientEdition == 0) {
                // AJOUT
                if (clientService.enregistrerClient(c)) {
                    JOptionPane.showMessageDialog(this, "Client enregistré avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    repasserEnModeAjout();
                    chargerDonnees();
                }
            } else {
                // MODIFICATION
                c.setIdClient(idClientEdition);
                if (clientService.modifierClient(c)) {
                    JOptionPane.showMessageDialog(this, "Client mis à jour avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    repasserEnModeAjout();
                    chargerDonnees();
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de la mise à jour (CIN ou email en doublon ?)", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
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
                repasserEnModeAjout();
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
}
