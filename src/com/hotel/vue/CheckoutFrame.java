package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.FacturationService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;

public class CheckoutFrame extends JFrame {

    private Utilisateur receptionnisteConnecte;
    private FacturationService facturationService;

    public CheckoutFrame(Utilisateur receptionniste) {
        this.receptionnisteConnecte = receptionniste;
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Check-Out & Facturation");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JButton btnRetour = ThemeUtil.creerBoutonRetour(e -> retourDashboard());
        add(ThemeUtil.creerHeaderApp("CHECK-OUT & FACTURATION", "icon_facture", btnRetour), BorderLayout.NORTH);

        add(creerPanelContenu(), BorderLayout.CENTER);
    }

    private JPanel creerPanelContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitre = new JLabel("Finaliser le Check-Out");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE_PETIT);
        lblTitre.setForeground(ThemeUtil.BLEU_NUIT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitre, gbc);

        JLabel lblDesc = new JLabel("Entrez l'ID de la réservation pour procéder au check-out");
        lblDesc.setFont(ThemeUtil.POLICE_PETIT);
        gbc.gridy = 1;
        panel.add(lblDesc, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel lblId = new JLabel("ID Réservation :");
        lblId.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(lblId, gbc);

        JTextField txtId = new JTextField();
        ThemeUtil.appliquerThemeTextField(txtId);
        gbc.gridx = 1;
        panel.add(txtId, gbc);

        JButton btnCheckout = new JButton("Confirmer le check-out");
        ThemeUtil.appliquerThemeBoutonValider(btnCheckout);
        IconLoader.appliquerIcone(btnCheckout, "icon_check");
        btnCheckout.setPreferredSize(new Dimension(220, 44));
        btnCheckout.addActionListener(e -> effectuerCheckout(txtId.getText()));

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(35, 15, 15, 15);
        panel.add(btnCheckout, gbc);

        return panel;
    }

    private void effectuerCheckout(String idResaStr) {
        try {
            if (idResaStr == null || idResaStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer un ID de réservation",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int idResa = Integer.parseInt(idResaStr.trim());

            boolean succes = facturationService.realiserCheckOut(idResa);
            if (!succes) {
                JOptionPane.showMessageDialog(this,
                        "Échec du check-out. Vérifiez l'ID.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ouvre la facture pour le paiement
            new FactureFrame(idResa, facturationService, receptionnisteConnecte).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "ID invalide (nombre requis)",
                    "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            // Réservation déjà TERMINEE ou ANNULEE — message clair pour le réceptionniste
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Check-out impossible", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            // Réservation introuvable
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retourDashboard() {
        NavigationManager.retourVers(this, new DashboardReceptionnisteFrame(receptionnisteConnecte));
    }
}