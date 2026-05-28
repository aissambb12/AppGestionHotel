package com.hotel.vue;

import com.hotel.dao.impl.ServiceSupplementaireDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.ModePaiement;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.service.FacturationService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FactureFrame extends JFrame {

    private int idReservation;
    private FacturationService facturationService;
    private Facture facture;
    private List<ReservationServices> extras;
    private Utilisateur receptionnisteConnecte; // peut être null si la frame est ouverte ailleurs

    /** Nouveau constructeur préféré (pour retour dashboard). */
    public FactureFrame(int idReservation, FacturationService facturationService, Utilisateur receptionniste) {
        this.idReservation = idReservation;
        this.facturationService = facturationService;
        this.receptionnisteConnecte = receptionniste;
        this.facture = facturationService.obtenirFactureReservation(idReservation);
        this.extras = facturationService.obtenirDetailsConsommations(idReservation);

        setTitle("Hotel Manager - Facture N°" + idReservation);
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
    }

    /** Ancien constructeur conservé pour compatibilité avec les appels existants. */
    public FactureFrame(int idReservation, FacturationService facturationService) {
        this(idReservation, facturationService, null);
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());
        add(new JScrollPane(creerPanelFacture()), BorderLayout.CENTER);
        add(creerPanelPaiement(), BorderLayout.SOUTH);
    }

    private JPanel creerPanelFacture() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // En-tête
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(creerEnTeteFacture(), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(sep1, gbc);

        // Infos
        gbc.gridy = 2; gbc.gridwidth = 1;
        ajouterLigneInfo(panel, gbc, 2, "Réservation :", "N° " + idReservation);
        ajouterLigneInfo(panel, gbc, 3, "Date :", facture.getDateFacture() != null
                ? facture.getDateFacture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "N/A");
        ajouterLigneInfo(panel, gbc, 4, "Statut :", facture.getStatutFacture().toString());

        gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 6;
        panel.add(ThemeUtil.creerTitreSection("DÉTAILS DES FRAIS"), gbc);

        // Hébergement
        double prixHebergement = facture.getMontantTotal();
        gbc.gridy = 7; gbc.gridwidth = 1; gbc.gridx = 0;
        JLabel l1 = new JLabel("Hébergement (chambres + services)");
        l1.setFont(ThemeUtil.POLICE_NORMAL);
        panel.add(l1, gbc);

        JLabel l1v = new JLabel(String.format("%.2f MAD", prixHebergement));
        l1v.setFont(ThemeUtil.POLICE_NORMAL);
        l1v.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridx = 1;
        panel.add(l1v, gbc);

        int row = 8;
        if (extras != null && !extras.isEmpty()) {
            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
            JLabel sub = new JLabel("Détail des services consommés :");
            sub.setFont(ThemeUtil.POLICE_LABEL);
            panel.add(sub, gbc);
            row++;

            for (ReservationServices extra : extras) {
                ServiceSupplementaire s = chargerService(extra.getIdService());
                if (s == null) continue;
                double prix = s.getPrixService() * extra.getQuantite();
                gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = row;
                JLabel ls = new JLabel("  · " + s.getNomService() + "  x" + extra.getQuantite());
                ls.setFont(ThemeUtil.POLICE_PETIT);
                panel.add(ls, gbc);

                JLabel lp = new JLabel(String.format("%.2f MAD", prix));
                lp.setFont(ThemeUtil.POLICE_PETIT);
                gbc.gridx = 1;
                panel.add(lp, gbc);
                row++;
            }
        }

        // Total
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        JSeparator sep3 = new JSeparator();
        sep3.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(sep3, gbc);

        gbc.gridy = row; gbc.gridwidth = 1; gbc.gridx = 0;
        JLabel lblTotal = new JLabel("MONTANT TOTAL :");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblTotal, gbc);

        JLabel lblMontant = new JLabel(String.format("%.2f MAD", prixHebergement));
        lblMontant.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblMontant.setForeground(ThemeUtil.DORE_LUXE);
        gbc.gridx = 1;
        panel.add(lblMontant, gbc);

        return panel;
    }

    private void ajouterLigneInfo(JPanel panel, GridBagConstraints gbc, int row, String label, String valeur) {
        gbc.gridy = row; gbc.gridx = 0;
        JLabel l = new JLabel(label);
        l.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(l, gbc);
        gbc.gridx = 1;
        JLabel v = new JLabel(valeur);
        v.setFont(ThemeUtil.POLICE_NORMAL);
        panel.add(v, gbc);
    }

    private JPanel creerEnTeteFacture() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblLogo = new JLabel();
        ImageIcon logo = IconLoader.charger("app_logo", 50);
        if (logo != null) lblLogo.setIcon(logo);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ThemeUtil.BLEU_NUIT);

        JLabel lblTitre = new JLabel("FACTURE OFFICIELLE");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(ThemeUtil.BLANC);
        info.add(lblTitre);

        JLabel lblNum = new JLabel("N° " + facture.getIdFacture());
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNum.setForeground(ThemeUtil.DORE_LUXE);
        info.add(lblNum);

        panel.add(lblLogo, BorderLayout.WEST);
        panel.add(Box.createHorizontalStrut(20), BorderLayout.CENTER);
        panel.add(info, BorderLayout.EAST);

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrap.setBackground(ThemeUtil.BLEU_NUIT);
        wrap.add(lblLogo);
        wrap.add(Box.createHorizontalStrut(20));
        wrap.add(info);
        panel.add(wrap, BorderLayout.CENTER);
        return panel;
    }

    private JPanel creerPanelPaiement() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblMode = new JLabel("Mode de paiement :");
        lblMode.setFont(ThemeUtil.POLICE_LABEL);
        IconLoader.appliquerIcone(new JButton(), "icon_paiements"); // no-op si pas trouvé

        JComboBox<ModePaiement> comboMode = new JComboBox<>(ModePaiement.values());
        comboMode.setFont(ThemeUtil.POLICE_NORMAL);

        JButton btnPayer = new JButton("Paiement effectué");
        ThemeUtil.appliquerThemeBoutonValider(btnPayer);
        IconLoader.appliquerIcone(btnPayer, "icon_check");
        btnPayer.setPreferredSize(new Dimension(180, 38));
        btnPayer.addActionListener(e -> effectuerPaiement((ModePaiement) comboMode.getSelectedItem()));

        JButton btnAnnuler = new JButton("Annuler facture");
        ThemeUtil.appliquerThemeBoutonSuppression(btnAnnuler);
        IconLoader.appliquerIcone(btnAnnuler, "icon_delete");
        btnAnnuler.setPreferredSize(new Dimension(170, 38));
        btnAnnuler.addActionListener(e -> annulerFacture());

        panel.add(lblMode);
        panel.add(comboMode);
        panel.add(btnPayer);
        panel.add(btnAnnuler);
        return panel;
    }

    private void effectuerPaiement(ModePaiement modePaiement) {
        try {
            // 1. On enregistre le paiement
            Paiement paiement = new Paiement();
            paiement.setIdFacture(facture.getIdFacture());
            paiement.setMontantPaye(facture.getMontantTotal());
            paiement.setDatePaiement(LocalDateTime.now());
            paiement.setModePaiement(modePaiement);

            boolean succesPaiement = facturationService.enregistrerPaiement(paiement);
            if (!succesPaiement) {
                JOptionPane.showMessageDialog(this,
                        "Échec de l'enregistrement du paiement.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. ★ On PERSISTE le nouveau statut PAYEE en BDD (avant on ne le faisait qu'en mémoire)
            boolean succesStatut = facturationService.mettreAJourStatutFacture(
                    facture.getIdFacture(), StatutFacture.PAYEE);
            if (!succesStatut) {
                JOptionPane.showMessageDialog(this,
                        "Paiement enregistré mais le statut de la facture n'a pas pu être mis à jour.",
                        "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
            facture.setStatutFacture(StatutFacture.PAYEE);

            JOptionPane.showMessageDialog(this,
                    "Paiement effectué avec succès.\n\n"
                            + "Mode : " + modePaiement + "\n"
                            + "Montant : " + String.format("%.2f MAD", facture.getMontantTotal()) + "\n"
                            + "Statut facture : PAYEE",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            retourDashboard();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annulerFacture() {
        int rep = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment annuler cette facture ?\n"
                        + "Cette action est définitive (statut → ANNULEE).",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (rep != JOptionPane.YES_OPTION) return;

        // ★ On PERSISTE l'annulation en BDD (avant on ne le faisait qu'en mémoire)
        boolean ok = facturationService.mettreAJourStatutFacture(
                facture.getIdFacture(), StatutFacture.ANNULEE);

        if (ok) {
            facture.setStatutFacture(StatutFacture.ANNULEE);
            JOptionPane.showMessageDialog(this,
                    "Facture annulée.\nStatut mis à jour : ANNULEE",
                    "Annulation", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Échec de l'annulation en base de données.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        retourDashboard();
    }

    private void retourDashboard() {
        if (receptionnisteConnecte != null) {
            NavigationManager.retourVers(this, new DashboardReceptionnisteFrame(receptionnisteConnecte));
        } else {
            dispose();
        }
    }

    private ServiceSupplementaire chargerService(int idService) {
        try {
            return new ServiceSupplementaireDAOImpl().trouverParId(idService);
        } catch (Exception ex) {
            return null;
        }
    }
}
