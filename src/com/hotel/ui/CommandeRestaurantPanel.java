package com.hotel.ui;

import com.hotel.model.CommandeRestaurant;
import com.hotel.model.LigneCommandeRestaurant;
import com.hotel.service.RestaurantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CommandeRestaurantPanel extends JPanel {

    private JTextField txtIdReservation, txtIdCommande, txtIdPlat, txtQuantite;
    private JTable tableCommandes, tableLignes;
    private DefaultTableModel modelCommandes, modelLignes;

    private RestaurantService restaurantService = new RestaurantService();

    public CommandeRestaurantPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerCommandes();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Commandes restaurant");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(330, 0));

        JLabel formTitle = new JLabel("Nouvelle commande");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(8, 1, 6, 4));
        form.setBackground(Color.WHITE);

        txtIdReservation = UITheme.createTextField();
        txtIdCommande = UITheme.createTextField();
        txtIdPlat = UITheme.createTextField();
        txtQuantite = UITheme.createTextField();

        form.add(new JLabel("ID Réservation"));
        form.add(txtIdReservation);

        form.add(new JLabel("ID Commande"));
        form.add(txtIdCommande);

        form.add(new JLabel("ID Plat"));
        form.add(txtIdPlat);

        form.add(new JLabel("Quantité"));
        form.add(txtQuantite);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton btnNouvelleCommande = UITheme.createPrimaryButton("Nouvelle commande");
        JButton btnAjouterPlat = UITheme.createPrimaryButton("Ajouter plat");
        JButton btnActualiser = new JButton("Actualiser");

        btnActualiser.setFont(UITheme.BUTTON_FONT);

        buttons.add(btnNouvelleCommande);
        buttons.add(btnAjouterPlat);
        buttons.add(btnActualiser);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);
        formCard.add(buttons, BorderLayout.SOUTH);

        add(formCard, BorderLayout.WEST);

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablesPanel.setBackground(UITheme.BACKGROUND);

        JPanel commandesCard = UITheme.createCardPanel();
        commandesCard.setLayout(new BorderLayout(10, 10));

        JLabel commandesTitle = new JLabel("Liste des commandes");
        commandesTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        commandesTitle.setForeground(UITheme.PRIMARY_DARK);

        modelCommandes = new DefaultTableModel(
                new String[]{"ID", "Réservation", "Date", "Statut"},
                0
        );

        tableCommandes = new JTable(modelCommandes);
        UITheme.styleTable(tableCommandes);

        commandesCard.add(commandesTitle, BorderLayout.NORTH);
        commandesCard.add(new JScrollPane(tableCommandes), BorderLayout.CENTER);

        JPanel lignesCard = UITheme.createCardPanel();
        lignesCard.setLayout(new BorderLayout(10, 10));

        JLabel lignesTitle = new JLabel("Lignes de commande sélectionnée");
        lignesTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lignesTitle.setForeground(UITheme.PRIMARY_DARK);

        modelLignes = new DefaultTableModel(
                new String[]{"ID", "Commande", "Plat", "Quantité", "Prix unitaire"},
                0
        );

        tableLignes = new JTable(modelLignes);
        UITheme.styleTable(tableLignes);

        lignesCard.add(lignesTitle, BorderLayout.NORTH);
        lignesCard.add(new JScrollPane(tableLignes), BorderLayout.CENTER);

        tablesPanel.add(commandesCard);
        tablesPanel.add(lignesCard);

        add(tablesPanel, BorderLayout.CENTER);

        btnNouvelleCommande.addActionListener(e -> creerCommande());
        btnAjouterPlat.addActionListener(e -> ajouterPlatCommande());
        btnActualiser.addActionListener(e -> chargerCommandes());

        tableCommandes.getSelectionModel().addListSelectionListener(e -> chargerLignesCommande());
    }

    private void creerCommande() {
        int idReservation = Integer.parseInt(txtIdReservation.getText());

        restaurantService.creerCommandeRestaurant(idReservation);
        chargerCommandes();

        JOptionPane.showMessageDialog(this, "Commande créée avec succès.");
    }

    private void ajouterPlatCommande() {
        int idCommande = Integer.parseInt(txtIdCommande.getText());
        int idPlat = Integer.parseInt(txtIdPlat.getText());
        int quantite = Integer.parseInt(txtQuantite.getText());

        restaurantService.ajouterPlatCommande(idCommande, idPlat, quantite);
        chargerLignesParIdCommande(idCommande);

        JOptionPane.showMessageDialog(this, "Plat ajouté à la commande.");
    }

    private void chargerCommandes() {
        modelCommandes.setRowCount(0);

        List<CommandeRestaurant> commandes = restaurantService.listerCommandes();

        for (CommandeRestaurant c : commandes) {
            modelCommandes.addRow(new Object[]{
                    c.getIdCommande(),
                    c.getReservation().getIdReservation(),
                    c.getDateCommande(),
                    c.getStatut()
            });
        }
    }

    private void chargerLignesCommande() {
        int ligne = tableCommandes.getSelectedRow();

        if (ligne >= 0) {
            int idCommande = Integer.parseInt(modelCommandes.getValueAt(ligne, 0).toString());
            txtIdCommande.setText(String.valueOf(idCommande));
            chargerLignesParIdCommande(idCommande);
        }
    }

    private void chargerLignesParIdCommande(int idCommande) {
        modelLignes.setRowCount(0);

        List<LigneCommandeRestaurant> lignes = restaurantService.listerLignesParCommande(idCommande);

        for (LigneCommandeRestaurant l : lignes) {
            modelLignes.addRow(new Object[]{
                    l.getIdLigneCommande(),
                    l.getCommandeRestaurant().getIdCommande(),
                    l.getPlat().getIdPlat(),
                    l.getQuantite(),
                    l.getPrixUnitaire()
            });
        }
    }
}