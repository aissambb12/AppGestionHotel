package com.hotel.ui;

import com.hotel.model.Chambre;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.service.ChambreService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChambrePanel extends JPanel {

    private JTextField txtId, txtNumero, txtPrix;
    private JComboBox<String> cbType;
    private JComboBox<StatutChambre> cbStatut;
    private JTable table;
    private DefaultTableModel model;

    private ChambreService chambreService = new ChambreService();

    public ChambrePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerChambres();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Gestion des chambres");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(330, 0));

        JLabel formTitle = new JLabel("Informations chambre");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(10, 1, 6, 4));
        form.setBackground(Color.WHITE);

        txtId = UITheme.createTextField();
        txtId.setEditable(false);

        txtNumero = UITheme.createTextField();
        txtPrix = UITheme.createTextField();

        cbType = new JComboBox<>(new String[]{"Simple", "Double", "Suite"});
        cbStatut = new JComboBox<>(StatutChambre.values());

        form.add(new JLabel("ID"));
        form.add(txtId);

        form.add(new JLabel("Numéro"));
        form.add(txtNumero);

        form.add(new JLabel("Type"));
        form.add(cbType);

        form.add(new JLabel("Prix par nuit"));
        form.add(txtPrix);

        form.add(new JLabel("Statut"));
        form.add(cbStatut);

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

        JLabel tableTitle = new JLabel("Liste des chambres");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Numéro", "Type", "Prix", "Statut"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        btnAjouter.addActionListener(e -> ajouterChambre());
        btnModifier.addActionListener(e -> modifierChambre());
        btnSupprimer.addActionListener(e -> supprimerChambre());
        btnVider.addActionListener(e -> viderChamps());

        table.getSelectionModel().addListSelectionListener(e -> remplirChamps());
    }

    private void ajouterChambre() {
        Chambre c = new Chambre();
        c.setNumero(txtNumero.getText());
        c.setType(cbType.getSelectedItem().toString());
        c.setPrixParNuit(Double.parseDouble(txtPrix.getText()));
        c.setStatut((StatutChambre) cbStatut.getSelectedItem());

        chambreService.ajouterChambre(c);
        chargerChambres();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Chambre ajoutée avec succès.");
    }

    private void modifierChambre() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une chambre.");
            return;
        }

        Chambre c = new Chambre();
        c.setIdChambre(Integer.parseInt(txtId.getText()));
        c.setNumero(txtNumero.getText());
        c.setType(cbType.getSelectedItem().toString());
        c.setPrixParNuit(Double.parseDouble(txtPrix.getText()));
        c.setStatut((StatutChambre) cbStatut.getSelectedItem());

        chambreService.modifierChambre(c);
        chargerChambres();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Chambre modifiée avec succès.");
    }

    private void supprimerChambre() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une chambre.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer cette chambre ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            chambreService.supprimerChambre(Integer.parseInt(txtId.getText()));
            chargerChambres();
            viderChamps();

            JOptionPane.showMessageDialog(this, "Chambre supprimée avec succès.");
        }
    }

    private void chargerChambres() {
        model.setRowCount(0);

        List<Chambre> chambres = chambreService.listerChambres();

        for (Chambre c : chambres) {
            model.addRow(new Object[]{
                    c.getIdChambre(),
                    c.getNumero(),
                    c.getType(),
                    c.getPrixParNuit(),
                    c.getStatut()
            });
        }
    }

    private void remplirChamps() {
        int ligne = table.getSelectedRow();

        if (ligne >= 0) {
            txtId.setText(model.getValueAt(ligne, 0).toString());
            txtNumero.setText(model.getValueAt(ligne, 1).toString());
            cbType.setSelectedItem(model.getValueAt(ligne, 2).toString());
            txtPrix.setText(model.getValueAt(ligne, 3).toString());
            cbStatut.setSelectedItem(StatutChambre.valueOf(model.getValueAt(ligne, 4).toString()));
        }
    }

    private void viderChamps() {
        txtId.setText("");
        txtNumero.setText("");
        txtPrix.setText("");
        cbType.setSelectedIndex(0);
        cbStatut.setSelectedIndex(0);
        table.clearSelection();
    }
}