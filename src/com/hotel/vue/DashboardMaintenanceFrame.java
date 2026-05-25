package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;

public class DashboardMaintenanceFrame extends JFrame {

    private Utilisateur technicienConnecte;

    public DashboardMaintenanceFrame(Utilisateur technicien) {
        this.technicienConnecte = technicien;

        setTitle("Hotel Manager - Maintenance");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        ThemeUtil.appliquerIconeFenetre(this);

        initialiserComposants();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout());
        add(creerHeader(), BorderLayout.NORTH);
        add(creerPanelIcones(), BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("DÉPARTEMENT MAINTENANCE");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);
        ImageIcon ic = IconLoader.charger("icon_maintenance", 24);
        if (ic != null) { lblTitre.setIcon(ic); lblTitre.setIconTextGap(10); }

        JPanel panelDroite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelDroite.setOpaque(false);

        JLabel lblUser = new JLabel(technicienConnecte.getNom() + " " + technicienConnecte.getPrenom());
        lblUser.setFont(ThemeUtil.POLICE_NORMALE);
        lblUser.setForeground(ThemeUtil.BLANC);
        ImageIcon icUser = IconLoader.charger("icon_user", 18);
        if (icUser != null) { lblUser.setIcon(icUser); lblUser.setIconTextGap(6); }

        JButton btnDeconnexion = new JButton("Déconnexion");
        ThemeUtil.appliquerThemeBoutonSuppression(btnDeconnexion);
        IconLoader.appliquerIcone(btnDeconnexion, "icon_logout");
        btnDeconnexion.addActionListener(e -> NavigationManager.naviguerVers(this, new LoginFrame()));

        panelDroite.add(lblUser);
        panelDroite.add(btnDeconnexion);

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(panelDroite, BorderLayout.EAST);
        return panel;
    }

    private JPanel creerPanelIcones() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 30));
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        panel.add(creerCarteAction("INTERVENTIONS\nEN COURS", "icon_maintenance", ThemeUtil.ROUGE_ERREUR,
                () -> NavigationManager.naviguerVers(this, new InterventionsFrame(technicienConnecte))));

        panel.add(creerCarteAction("HISTORIQUE DES\nRÉPARATIONS", "icon_facture", new Color(52, 152, 219),
                () -> NavigationManager.naviguerVers(this, new HistoriqueFrame(technicienConnecte))));

        return panel;
    }

    private JPanel creerCarteAction(String texte, String nomIcone, Color couleur, Runnable action) {
        JPanel btn = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
            }
        };
        btn.setBackground(couleur);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);

        ImageIcon ic = IconLoader.charger(nomIcone, 56);
        if (ic != null) {
            btn.add(new JLabel(ic), gbc);
            gbc.gridy = 1;
        }
        JLabel lblTexte = new JLabel("<html><center>" + texte + "</center></html>");
        lblTexte.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTexte.setForeground(ThemeUtil.BLANC);
        btn.add(lblTexte, gbc);

        final Color couleurOrigin = couleur;
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(assombrir(couleurOrigin, 0.2f)); btn.repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleurOrigin); btn.repaint();
            }
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (action != null) action.run();
            }
        });
        return btn;
    }

    private Color assombrir(Color c, float f) {
        return new Color(
                (int) (c.getRed()   * (1 - f)),
                (int) (c.getGreen() * (1 - f)),
                (int) (c.getBlue()  * (1 - f))
        );
    }
}
