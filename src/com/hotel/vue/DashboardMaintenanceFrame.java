package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        add(creerCorps(), BorderLayout.CENTER);
    }

    private JPanel creerHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeUtil.BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel("DÉPARTEMENT MAINTENANCE");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);
        ImageIcon ic = IconLoader.charger("icon_maintenance", 26);
        if (ic != null) { lblTitre.setIcon(ic); lblTitre.setIconTextGap(10); }

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        droite.setOpaque(false);

        JLabel lblUser = new JLabel(technicienConnecte.getNom() + " " + technicienConnecte.getPrenom());
        lblUser.setFont(ThemeUtil.POLICE_NORMALE);
        lblUser.setForeground(ThemeUtil.BLANC);
        ImageIcon icUser = IconLoader.charger("icon_user", 18);
        if (icUser != null) { lblUser.setIcon(icUser); lblUser.setIconTextGap(6); }

        JButton btnDeconnexion = new JButton("Déconnexion");
        ThemeUtil.appliquerThemeBoutonSuppression(btnDeconnexion);
        IconLoader.appliquerIcone(btnDeconnexion, "icon_logout");
        btnDeconnexion.addActionListener(e -> NavigationManager.naviguerVers(this, new LoginFrame()));

        droite.add(lblUser);
        droite.add(btnDeconnexion);

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(droite, BorderLayout.EAST);
        return panel;
    }

    private JPanel creerCorps() {
        JPanel corps = new JPanel(new BorderLayout());
        corps.setBackground(ThemeUtil.GRIS_FOND);
        corps.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Sous-titre / message d'accueil
        JLabel lblAccueil = new JLabel("Bienvenue " + technicienConnecte.getPrenom() + ", choisissez une action :");
        lblAccueil.setFont(ThemeUtil.POLICE_NORMAL);
        lblAccueil.setForeground(new Color(80, 80, 80));
        lblAccueil.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        corps.add(lblAccueil, BorderLayout.NORTH);

        JPanel grille = new JPanel(new GridLayout(1, 2, 30, 30));
        grille.setBackground(ThemeUtil.GRIS_FOND);

        grille.add(creerCarte(
                "INTERVENTIONS EN COURS",
                "Visualiser et clôturer les pannes en cours sur les chambres",
                "icon_maintenance", ThemeUtil.ROUGE_ERREUR,
                () -> NavigationManager.naviguerVers(this, new InterventionsFrame(technicienConnecte))));

        grille.add(creerCarte(
                "HISTORIQUE DES RÉPARATIONS",
                "Consulter l'historique complet des interventions de maintenance",
                "icon_facture", new Color(52, 152, 219),
                () -> NavigationManager.naviguerVers(this, new HistoriqueFrame(technicienConnecte))));

        corps.add(grille, BorderLayout.CENTER);
        return corps;
    }

    private JPanel creerCarte(String titre, String description, String nomIcone, Color couleur, Runnable action) {
        JPanel carte = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
            }
        };
        carte.setOpaque(false);
        carte.setBackground(couleur);
        carte.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        carte.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);

        ImageIcon ic = IconLoader.charger(nomIcone, 64);
        if (ic != null) {
            carte.add(new JLabel(ic), gbc);
            gbc.gridy++;
        }

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitre.setForeground(ThemeUtil.BLANC);
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        carte.add(lblTitre, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel lblDesc = new JLabel("<html><div style='text-align:center;'>" + description + "</div></html>");
        lblDesc.setFont(ThemeUtil.POLICE_PETIT);
        lblDesc.setForeground(new Color(240, 240, 240));
        carte.add(lblDesc, gbc);

        final Color origin = couleur;
        carte.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { carte.setBackground(assombrir(origin)); carte.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { carte.setBackground(origin); carte.repaint(); }
            @Override public void mouseClicked(MouseEvent e) { if (action != null) action.run(); }
        });
        return carte;
    }

    private Color assombrir(Color c) {
        float f = 0.2f;
        return new Color(
                Math.max(0, (int) (c.getRed()   * (1 - f))),
                Math.max(0, (int) (c.getGreen() * (1 - f))),
                Math.max(0, (int) (c.getBlue()  * (1 - f)))
        );
    }
}
