package com.hotel.vue;

import com.hotel.model.Utilisateur;
import com.hotel.service.UtilisateurService;
import com.hotel.util.IconLoader;
import com.hotel.util.NavigationManager;

import javax.swing.*;
import java.awt.*;

public class DashboardAdminFrame extends JFrame {

    private Utilisateur adminConnecte;
    private UtilisateurService utilisateurService;

    public DashboardAdminFrame(Utilisateur admin) {
        this.adminConnecte = admin;
        this.utilisateurService = new UtilisateurService();

        setTitle("Hotel Manager - Administration");
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

        JLabel lblTitre = new JLabel("ADMINISTRATION");
        lblTitre.setFont(ThemeUtil.POLICE_TITRE);
        lblTitre.setForeground(ThemeUtil.DORE_LUXE);
        ImageIcon icAdmin = IconLoader.charger("icon_dashboard", 24);
        if (icAdmin != null) { lblTitre.setIcon(icAdmin); lblTitre.setIconTextGap(10); }

        JPanel panelDroite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelDroite.setOpaque(false);

        JLabel lblUtilisateur = new JLabel(adminConnecte.getNom() + " " + adminConnecte.getPrenom());
        lblUtilisateur.setFont(ThemeUtil.POLICE_NORMALE);
        lblUtilisateur.setForeground(ThemeUtil.BLANC);
        ImageIcon icUser = IconLoader.charger("icon_user", 18);
        if (icUser != null) { lblUtilisateur.setIcon(icUser); lblUtilisateur.setIconTextGap(6); }

        JButton btnDeconnexion = new JButton("Déconnexion");
        ThemeUtil.appliquerThemeBoutonSuppression(btnDeconnexion);
        IconLoader.appliquerIcone(btnDeconnexion, "icon_logout");
        btnDeconnexion.addActionListener(e -> NavigationManager.naviguerVers(this, new LoginFrame()));

        panelDroite.add(lblUtilisateur);
        panelDroite.add(btnDeconnexion);

        panel.add(lblTitre, BorderLayout.WEST);
        panel.add(panelDroite, BorderLayout.EAST);

        return panel;
    }

    private JPanel creerPanelIcones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(ThemeUtil.GRIS_FOND);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        panel.add(creerCarteAction(
                "GESTION DU PERSONNEL", "icon_clients", ThemeUtil.BLEU_NUIT,
                () -> NavigationManager.naviguerVers(this, new GestionPersonnelFrame(adminConnecte))));

        panel.add(creerCarteAction(
                "PARC DE CHAMBRES", "icon_chambres", new Color(52, 152, 219),
                () -> NavigationManager.naviguerVers(this, new GestionChambresFrame(adminConnecte))));

        panel.add(creerCarteAction(
                "RÉSERVATIONS ET EXTRAS", "icon_reservations", new Color(46, 204, 113),
                () -> NavigationManager.naviguerVers(this, new GestionReservationsFrame(adminConnecte))));

        panel.add(creerCarteAction(
                "CHIFFRE D'AFFAIRES", "icon_paiements", new Color(155, 89, 182),
                () -> NavigationManager.naviguerVers(this, new StatistiquesFrame(adminConnecte))));

        return panel;
    }

    /**
     * Carte cliquable avec icône PNG (ou rien si introuvable) + texte.
     */
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
            JLabel lblIcone = new JLabel(ic);
            btn.add(lblIcone, gbc);
            gbc.gridy = 1;
        }

        JLabel lblTexte = new JLabel("<html><center>" + texte + "</center></html>");
        lblTexte.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTexte.setForeground(ThemeUtil.BLANC);
        btn.add(lblTexte, gbc);

        // Hover + click
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
