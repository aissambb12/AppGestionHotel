package com.hotel.ui;

import com.hotel.model.Client;
import com.hotel.service.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {

    private JTextField txtId, txtNom, txtPrenom, txtCin, txtTelephone, txtEmail;
    private JTable table;
    private DefaultTableModel model;

    private ClientService clientService = new ClientService();

    public ClientPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);

        initComponents();
        chargerClients();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Gestion des clients");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);

        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(520, 0));

        JLabel formTitle = new JLabel("Informations client");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(12, 1, -3, -3));
        form.setBackground(Color.WHITE);

        txtId = UITheme.createTextField();
        txtId.setEditable(false);

        txtNom = UITheme.createTextField();
        txtPrenom = UITheme.createTextField();
        txtCin = UITheme.createTextField();
        txtTelephone = UITheme.createTextField();
        txtEmail = UITheme.createTextField();

        form.add(new JLabel("ID"));
        form.add(txtId);


        form.add(new JLabel("Nom"));
        form.add(txtNom);


        form.add(new JLabel("Prénom"));
        form.add(txtPrenom);


        form.add(new JLabel("CIN"));
        form.add(txtCin);


        form.add(new JLabel("Téléphone"));
        form.add(txtTelephone);


        form.add(new JLabel("Email"));
        form.add(txtEmail);


        JPanel buttons = new JPanel(new GridLayout(2, 2, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton btnAjouter = UITheme.createPrimaryButton("Ajouter");
        JButton btnModifier = UITheme.createPrimaryButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnSupprimer.setFont(UITheme.BUTTON_FONT);
        btnVider.setFont(UITheme.BUTTON_FONT);

        buttons.add(btnAjouter);
        buttons.add(btnModifier);
        buttons.add(btnSupprimer);
        buttons.add(btnVider);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);
        formCard.add(buttons, BorderLayout.SOUTH);

        add(formCard, BorderLayout.WEST);

        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JLabel tableTitle = new JLabel("Liste des clients");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Nom", "Prénom", "CIN", "Téléphone", "Email"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        btnAjouter.addActionListener(e -> ajouterClient());
        btnModifier.addActionListener(e -> modifierClient());
        btnSupprimer.addActionListener(e -> supprimerClient());
        btnVider.addActionListener(e -> viderChamps());

        table.getSelectionModel().addListSelectionListener(e -> remplirChamps());
    }

    private void ajouterClient() {
        Client c = new Client();
        c.setNom(txtNom.getText());
        c.setPrenom(txtPrenom.getText());
        c.setCin(txtCin.getText());
        c.setTelephone(txtTelephone.getText());
        c.setEmail(txtEmail.getText());

        clientService.ajouterClient(c);
        chargerClients();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Client ajouté avec succès.");
    }

    private void modifierClient() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client.");
            return;
        }

        Client c = new Client();
        c.setIdClient(Integer.parseInt(txtId.getText()));
        c.setNom(txtNom.getText());
        c.setPrenom(txtPrenom.getText());
        c.setCin(txtCin.getText());
        c.setTelephone(txtTelephone.getText());
        c.setEmail(txtEmail.getText());

        clientService.modifierClient(c);
        chargerClients();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Client modifié avec succès.");
    }

    private void supprimerClient() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer ce client ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            clientService.supprimerClient(id);
            chargerClients();
            viderChamps();

            JOptionPane.showMessageDialog(this, "Client supprimé avec succès.");
        }
    }

    private void chargerClients() {
        model.setRowCount(0);

        List<Client> clients = clientService.listerClients();

        for (Client c : clients) {
            model.addRow(new Object[]{
                    c.getIdClient(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getCin(),
                    c.getTelephone(),
                    c.getEmail()
            });
        }
    }

    private void remplirChamps() {
        int ligne = table.getSelectedRow();

        if (ligne >= 0) {
            txtId.setText(model.getValueAt(ligne, 0).toString());
            txtNom.setText(model.getValueAt(ligne, 1).toString());
            txtPrenom.setText(model.getValueAt(ligne, 2).toString());
            txtCin.setText(model.getValueAt(ligne, 3).toString());
            txtTelephone.setText(model.getValueAt(ligne, 4).toString());
            txtEmail.setText(model.getValueAt(ligne, 5).toString());
        }
    }

    private void viderChamps() {
        txtId.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtCin.setText("");
        txtTelephone.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }
}