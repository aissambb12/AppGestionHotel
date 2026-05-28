package com.hotel.vue;

import com.hotel.dao.impl.ReservationChambreDAOImpl;
import com.hotel.dao.impl.ServiceSupplementaireDAOImpl;
import com.hotel.model.ReservationChambre;
import com.hotel.model.ReservationServices;
import com.hotel.model.ServiceSupplementaire;
import com.hotel.service.FacturationService;
import com.hotel.util.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class DetailsReservationFrame extends JFrame {

    private int idReservation;
    private FacturationService facturationService;
    private ReservationChambreDAOImpl reservationChambreDAO;
    private ServiceSupplementaireDAOImpl serviceDAO;

    public DetailsReservationFrame(int idReservation, FacturationService facturationService) {
        this.idReservation = idReservation;
        this.facturationService = facturationService;
        this.reservationChambreDAO = new ReservationChambreDAOImpl();
        this.serviceDAO = new ServiceSupplementaireDAOImpl();

        setTitle("Hotel Manager - Détails Réservation N°" + idReservation);
        setSize(720, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JButton btnFermer = new JButton("Fermer");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnFermer);
        IconLoader.appliquerIcone(btnFermer, "icon_back");
        btnFermer.addActionListener(e -> dispose());

        add(ThemeUtil.creerHeaderApp("DÉTAILS RÉSERVATION N°" + idReservation, "icon_reservations", btnFermer), BorderLayout.NORTH);
        add(new JScrollPane(creerContenu()), BorderLayout.CENTER);
    }

    private JPanel creerContenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // INFOS RÉSERVATION
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(ThemeUtil.creerTitreSection("INFORMATIONS RÉSERVATION"), gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        ajouterLigne(panel, gbc, 1, "ID Réservation :", String.valueOf(idReservation));
        ajouterLigne(panel, gbc, 2, "Statut :", "CONFIRMÉE");
        ajouterLigne(panel, gbc, 3, "Date Création :", LocalDate.now().toString());

        // CHAMBRES
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);
        gbc.gridy = 5;
        panel.add(ThemeUtil.creerTitreSection("CHAMBRES RÉSERVÉES"), gbc);

        int row = 6;
        try {
            List<ReservationChambre> chambres = reservationChambreDAO.listerParReservation(idReservation);
            if (chambres == null || chambres.isEmpty()) {
                gbc.gridy = row++;
                JLabel l = new JLabel("Aucune chambre trouvée");
                l.setFont(ThemeUtil.POLICE_PETIT);
                panel.add(l, gbc);
            } else {
                for (ReservationChambre rc : chambres) {
                    gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = row;
                    JLabel l1 = new JLabel("• Chambre ID " + rc.getIdChambre());
                    l1.setFont(ThemeUtil.POLICE_NORMAL);
                    panel.add(l1, gbc);

                    JLabel l2 = new JLabel(String.format("%.2f MAD", rc.getPrixApplique()));
                    l2.setFont(ThemeUtil.POLICE_NORMAL);
                    gbc.gridx = 1;
                    panel.add(l2, gbc);
                    row++;

                    gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
                    JLabel l3 = new JLabel("  Du " + rc.getDateArrivee() + " au " + rc.getDateDepart());
                    l3.setFont(ThemeUtil.POLICE_PETIT);
                    l3.setForeground(new Color(120, 120, 120));
                    panel.add(l3, gbc);
                    row++;
                }
            }
        } catch (Exception ex) {
            gbc.gridy = row++;
            JLabel l = new JLabel("Erreur : " + ex.getMessage());
            l.setForeground(ThemeUtil.ROUGE_ERREUR);
            panel.add(l, gbc);
        }

        // SERVICES
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);
        gbc.gridy = row++;
        panel.add(ThemeUtil.creerTitreSection("SERVICES SUPPLÉMENTAIRES"), gbc);

        try {
            List<ReservationServices> extras = facturationService.obtenirDetailsConsommations(idReservation);
            if (extras == null || extras.isEmpty()) {
                gbc.gridy = row++; gbc.gridwidth = 2;
                JLabel l = new JLabel("Aucun service supplémentaire");
                l.setFont(ThemeUtil.POLICE_PETIT);
                panel.add(l, gbc);
            } else {
                for (ReservationServices extra : extras) {
                    // ★ CHARGEMENT DU NOM DU SERVICE (au lieu de l'ID)
                    ServiceSupplementaire s = serviceDAO.trouverParId(extra.getIdService());
                    String nom = (s != null) ? s.getNomService() : ("Service #" + extra.getIdService());
                    double prixUnit = (s != null) ? s.getPrixService() : 0.0;
                    double sousTotal = prixUnit * extra.getQuantite();

                    gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = row;
                    JLabel l1 = new JLabel("• " + nom + "  x" + extra.getQuantite());
                    l1.setFont(ThemeUtil.POLICE_NORMAL);
                    panel.add(l1, gbc);

                    gbc.gridx = 1;
                    JLabel l2 = new JLabel(String.format("%.2f MAD", sousTotal));
                    l2.setFont(ThemeUtil.POLICE_NORMAL);
                    panel.add(l2, gbc);
                    row++;
                }
            }
        } catch (Exception ex) {
            gbc.gridy = row++; gbc.gridwidth = 2;
            JLabel l = new JLabel("Erreur : " + ex.getMessage());
            l.setForeground(ThemeUtil.ROUGE_ERREUR);
            panel.add(l, gbc);
        }

        // MONTANT TOTAL
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeUtil.DORE_LUXE);
        panel.add(sep, gbc);

        gbc.gridy = row; gbc.gridwidth = 1; gbc.gridx = 0;
        JLabel lblTotal = new JLabel("MONTANT TOTAL FACTURE :");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblTotal, gbc);

        try {
            double total = facturationService.obtenirFactureReservation(idReservation).getMontantTotal();
            JLabel l = new JLabel(String.format("%.2f MAD", total));
            l.setFont(new Font("Segoe UI", Font.BOLD, 20));
            l.setForeground(ThemeUtil.DORE_LUXE);
            gbc.gridx = 1;
            panel.add(l, gbc);
        } catch (Exception ex) {
            JLabel l = new JLabel("N/A");
            gbc.gridx = 1;
            panel.add(l, gbc);
        }
        return panel;
    }

    private void ajouterLigne(JPanel panel, GridBagConstraints gbc, int row, String label, String valeur) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel l1 = new JLabel(label);
        l1.setFont(ThemeUtil.POLICE_LABEL);
        panel.add(l1, gbc);
        gbc.gridx = 1;
        JLabel l2 = new JLabel(valeur);
        l2.setFont(ThemeUtil.POLICE_NORMAL);
        panel.add(l2, gbc);
    }
}
