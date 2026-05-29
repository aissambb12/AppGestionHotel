package com.hotel.vue;

import com.hotel.model.Facture;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.service.FacturationService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FacturesAnnuleesFrame extends JFrame {

    private Utilisateur utilisateurConnecte;
    private FacturationService facturationService;
    private DefaultTableModel modele;
    private JTable table;

    /**
     * Le dashboard de retour dépend du rôle : ADMIN → DashboardAdmin, RECEPTIONNISTE → DashboardReceptionniste.
     */
    public FacturesAnnuleesFrame(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.facturationService = new FacturationService();

        setTitle("Hotel Manager - Factures Annulées");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
        chargerDonnees();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());

        JButton btnRetour = ThemeUtil.creerBoutonRetour(e -> retourDashboard());
        add(ThemeUtil.creerHeaderApp("FACTURES ANNULÉES", "icon_facture", btnRetour), BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(ThemeUtil.GRIS_FOND);
        centre.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        centre.add(creerPanelBoutons(), BorderLayout.NORTH);
        centre.add(creerPanelTable(), BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);
    }

    private void retourDashboard() {
        NavigationManager.retourVers(this, new DashboardAdminFrame(utilisateurConnecte));
    }

    private JPanel creerPanelBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton btnReactiverPayee = new JButton("Réactiver en PAYEE");
        ThemeUtil.appliquerThemeBoutonValider(btnReactiverPayee);
        IconLoader.appliquerIcone(btnReactiverPayee, "icon_check");
        btnReactiverPayee.addActionListener(e -> reactiver(StatutFacture.PAYEE));

        JButton btnReactiverAttente = new JButton("Remettre EN ATTENTE");
        ThemeUtil.appliquerThemeBoutonAttention(btnReactiverAttente);
        IconLoader.appliquerIcone(btnReactiverAttente, "icon_refresh");
        btnReactiverAttente.addActionListener(e -> reactiver(StatutFacture.EN_ATTENTE));

        JButton btnRafraichir = new JButton("Rafraîchir");
        ThemeUtil.appliquerThemeBoutonSecondaire(btnRafraichir);
        IconLoader.appliquerIcone(btnRafraichir, "icon_refresh");
        btnRafraichir.addActionListener(e -> chargerDonnees());

        panel.add(btnReactiverPayee);
        panel.add(btnReactiverAttente);
        panel.add(btnRafraichir);
        return panel;
    }

    private JScrollPane creerPanelTable() {
        String[] colonnes = {"ID Facture", "ID Réservation", "Montant", "Date facture", "Statut"};
        modele = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(modele);
        ThemeUtil.appliquerThemeTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected && value != null) {
                    c.setForeground(ThemeUtil.ROUGE_ERREUR);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        return scroll;
    }

    private void chargerDonnees() {
        modele.setRowCount(0);
        try {
            List<Facture> liste = facturationService.listerFacturesParStatut(StatutFacture.ANNULEE);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Facture f : liste) {
                modele.addRow(new Object[]{
                        f.getIdFacture(),
                        f.getIdReservation(),
                        String.format("%.2f MAD", f.getMontantTotal()),
                        f.getDateFacture() != null ? f.getDateFacture().format(fmt) : "N/A",
                        f.getStatutFacture()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reactiver(StatutFacture nouveauStatut) {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une facture", "Sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idFacture = (Integer) modele.getValueAt(ligne, 0);

        String message = (nouveauStatut == StatutFacture.PAYEE)
                ? "Confirmer la réactivation de la facture #" + idFacture + " en PAYEE ?\n"
                + "Elle sera recomptée dans le chiffre d'affaires."
                : "Confirmer la remise EN_ATTENTE de la facture #" + idFacture + " ?";

        int rep = JOptionPane.showConfirmDialog(this, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (rep != JOptionPane.YES_OPTION) return;

        try {
            if (facturationService.reactiverFacture(idFacture, nouveauStatut)) {
                JOptionPane.showMessageDialog(this, "Facture mise à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerDonnees();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la mise à jour.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
