package com.hotel.ui;

import com.hotel.model.enumeration.ModePaiement;
import com.hotel.model.Paiement;
import com.hotel.service.PaiementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PaiementPanel extends JPanel {

    private JTextField txtIdFacture, txtMontant;
    private JComboBox<ModePaiement> cbModePaiement;
    private JTable table;
    private DefaultTableModel model;

    private PaiementService paiementService = new PaiementService();

    public PaiementPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        initComponents();
        chargerPaiements();
    }

    private void initComponents() {
        JLabel titre = new JLabel("Gestion des paiements");
        titre.setFont(UITheme.TITLE_FONT);
        titre.setForeground(UITheme.TEXT_DARK);
        add(titre, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(330, 0));

        JLabel formTitle = new JLabel("Nouveau paiement");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(UITheme.PRIMARY_DARK);

        JPanel form = new JPanel(new GridLayout(6, 1, 6, 4));
        form.setBackground(Color.WHITE);

        txtIdFacture = UITheme.createTextField();
        txtMontant = UITheme.createTextField();
        cbModePaiement = new JComboBox<>(ModePaiement.values());

        form.add(new JLabel("ID Facture"));
        form.add(txtIdFacture);

        form.add(new JLabel("Montant"));
        form.add(txtMontant);

        form.add(new JLabel("Mode paiement"));
        form.add(cbModePaiement);

        JPanel buttons = new JPanel(new GridLayout(2, 1, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton btnPayer = UITheme.createPrimaryButton("Enregistrer paiement");
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setFont(UITheme.BUTTON_FONT);

        buttons.add(btnPayer);
        buttons.add(btnActualiser);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);
        formCard.add(buttons, BorderLayout.SOUTH);

        add(formCard, BorderLayout.WEST);

        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JLabel tableTitle = new JLabel("Liste des paiements");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(UITheme.PRIMARY_DARK);

        model = new DefaultTableModel(
                new String[]{"ID", "Facture", "Date", "Montant", "Mode"},
                0
        );

        table = new JTable(model);
        UITheme.styleTable(table);

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        btnPayer.addActionListener(e -> enregistrerPaiement());
        btnActualiser.addActionListener(e -> chargerPaiements());
    }

    private void enregistrerPaiement() {
        int idFacture = Integer.parseInt(txtIdFacture.getText());
        double montant = Double.parseDouble(txtMontant.getText());
        ModePaiement mode = (ModePaiement) cbModePaiement.getSelectedItem();

        paiementService.enregistrerPaiement(idFacture, montant, mode);

        chargerPaiements();
        viderChamps();

        JOptionPane.showMessageDialog(this, "Paiement enregistré avec succès.");
    }

    private void chargerPaiements() {
        model.setRowCount(0);

        List<Paiement> paiements = paiementService.listerPaiements();

        for (Paiement p : paiements) {
            model.addRow(new Object[]{
                    p.getIdPaiement(),
                    p.getFacture().getIdFacture(),
                    p.getDatePaiement(),
                    p.getMontant(),
                    p.getModePaiement()
            });
        }
    }

    private void viderChamps() {
        txtIdFacture.setText("");
        txtMontant.setText("");
        cbModePaiement.setSelectedIndex(0);
    }
}