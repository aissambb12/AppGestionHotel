package com.hotel.ui;

import com.hotel.model.Plat;
import com.hotel.service.RestaurantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PlatPanel extends JPanel {

    private JTextField txtId, txtNom, txtDescription, txtPrix;
    private JCheckBox chkDisponible;
    private JTable table;
    private DefaultTableModel model;

    private RestaurantService restaurantService = new RestaurantService();

    public PlatPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerPlats();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Gestion des plats");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(330, 0));

        JLabel formTitle = new JLabel("Informations plat");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(10, 1, 6, 4));
        form.setBackground(Color.WHITE);

        txtId = UITheme.createTextField();
        txtId.setEditable(false);

        txtNom = UITheme.createTextField();
        txtDescription = UITheme.createTextField();
        txtPrix = UITheme.createTextField();
        chkDisponible = new JCheckBox("Disponible");
        chkDisponible.setBackground(Color.WHITE);
        chkDisponible.setFont(UITheme.NORMAL_FONT);

        form.add(new JLabel("ID"));
        form.add(txtId);

        form.add(new JLabel("Nom"));
        form.add(txtNom);

        form.add(new JLabel("Description"));
        form.add(txtDescription);

        form.add(new JLabel("Prix"));
        form.add(txtPrix);

        form.add(new JLabel("Disponibilité"));
        form.add(chkDisponible);

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

        JLabel tableTitle = new JLabel("Liste des plats");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Nom", "Description", "Prix", "Disponible"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        btnAjouter.addActionListener(e -> ajouterPlat());
        btnModifier.addActionListener(e -> modifierPlat());
        btnSupprimer.addActionListener(e -> supprimerPlat());
        btnVider.addActionListener(e -> viderChamps());

        table.getSelectionModel().addListSelectionListener(e -> remplirChamps());
    }

    private void ajouterPlat() {
        Plat p = new Plat();
        p.setNom(txtNom.getText());
        p.setDescription(txtDescription.getText());
        p.setPrix(Double.parseDouble(txtPrix.getText()));
        p.setDisponible(chkDisponible.isSelected());

        restaurantService.ajouterPlat(p);
        chargerPlats();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Plat ajouté avec succès.");
    }

    private void modifierPlat() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un plat.");
            return;
        }

        Plat p = new Plat();
        p.setIdPlat(Integer.parseInt(txtId.getText()));
        p.setNom(txtNom.getText());
        p.setDescription(txtDescription.getText());
        p.setPrix(Double.parseDouble(txtPrix.getText()));
        p.setDisponible(chkDisponible.isSelected());

        restaurantService.modifierPlat(p);
        chargerPlats();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Plat modifié avec succès.");
    }

    private void supprimerPlat() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un plat.");
            return;
        }

        restaurantService.supprimerPlat(Integer.parseInt(txtId.getText()));
        chargerPlats();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Plat supprimé avec succès.");
    }

    private void chargerPlats() {
        model.setRowCount(0);

        List<Plat> plats = restaurantService.listerPlats();

        for (Plat p : plats) {
            model.addRow(new Object[]{
                    p.getIdPlat(),
                    p.getNom(),
                    p.getDescription(),
                    p.getPrix(),
                    p.isDisponible()
            });
        }
    }

    private void remplirChamps() {
        int ligne = table.getSelectedRow();

        if (ligne >= 0) {
            txtId.setText(model.getValueAt(ligne, 0).toString());
            txtNom.setText(model.getValueAt(ligne, 1).toString());
            txtDescription.setText(model.getValueAt(ligne, 2).toString());
            txtPrix.setText(model.getValueAt(ligne, 3).toString());
            chkDisponible.setSelected(Boolean.parseBoolean(model.getValueAt(ligne, 4).toString()));
        }
    }

    private void viderChamps() {
        txtId.setText("");
        txtNom.setText("");
        txtDescription.setText("");
        txtPrix.setText("");
        chkDisponible.setSelected(false);
        table.clearSelection();
    }
}