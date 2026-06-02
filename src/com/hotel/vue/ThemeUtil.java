package com.hotel.vue;

import com.hotel.util.IconLoader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ThemeUtil {

    // === COULEURS ===
    public static final Color BLEU_NUIT = new Color(44, 62, 80);
    public static final Color DORE_LUXE = new Color(212, 175, 55);
    public static final Color GRIS_FOND = new Color(245, 246, 250);
    public static final Color GRIS_CLAIR = new Color(240, 240, 240);
    public static final Color BLANC = Color.WHITE;
    public static final Color TEXTE_SOMBRE = new Color(50, 50, 50);
    public static final Color VERT_VALIDATION = new Color(39, 174, 96);
    public static final Color ROUGE_ERREUR = new Color(220, 53, 69);
    public static final Color ORANGE_ATTENTION = new Color(255, 159, 64);

    // === POLICES ===
    public static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font POLICE_TITRE_PETIT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font POLICE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font POLICE_NORMALE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font POLICE_BOUTON = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font POLICE_PETIT = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font POLICE_LABEL = new Font("Segoe UI", Font.BOLD, 12);

    private ThemeUtil() {}

    // === BOUTONS ===
    public static void appliquerThemeBoutonPrincipal(JButton bouton) {
        styleBouton(bouton, DORE_LUXE, BLEU_NUIT, DORE_LUXE);
    }

    public static void appliquerThemeBoutonSecondaire(JButton bouton) {
        styleBouton(bouton, GRIS_CLAIR, TEXTE_SOMBRE, new Color(200, 200, 200));
    }

    public static void appliquerThemeBoutonValider(JButton bouton) {
        styleBouton(bouton, VERT_VALIDATION, BLEU_NUIT, VERT_VALIDATION);
    }

    public static void appliquerThemeBoutonSuppression(JButton bouton) {
        styleBouton(bouton, ROUGE_ERREUR, BLEU_NUIT, ROUGE_ERREUR);
    }

    public static void appliquerThemeBoutonAttention(JButton bouton) {
        styleBouton(bouton, ORANGE_ATTENTION, BLEU_NUIT, ORANGE_ATTENTION);
    }

    private static void styleBouton(JButton bouton, Color fond, Color texte, Color bordure) {
        bouton.setBackground(fond);
        bouton.setForeground(texte);
        bouton.setFont(POLICE_BOUTON);
        bouton.setFocusPainted(false);
        bouton.setOpaque(true);
        bouton.setContentAreaFilled(true);
        bouton.setBorderPainted(true);
        bouton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordure, 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // === CHAMPS DE TEXTE ===
    public static void appliquerThemeTextField(JTextField field) {
        field.setFont(POLICE_NORMAL);
        field.setBackground(BLANC);
        field.setForeground(TEXTE_SOMBRE);
        field.setCaretColor(BLEU_NUIT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    // === JTABLE ===
    private static class EnteteRenderer extends DefaultTableCellRenderer {
        public EnteteRenderer() {
            setHorizontalAlignment(SwingConstants.LEFT);
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setBackground(BLEU_NUIT);
            lbl.setForeground(BLANC);
            lbl.setFont(POLICE_LABEL);
            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(30, 45, 60)),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            return lbl;
        }
    }

    public static void appliquerThemeTable(JTable table) {
        table.setFont(POLICE_NORMAL);
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(DORE_LUXE);
        table.setSelectionForeground(BLANC);
        table.setForeground(TEXTE_SOMBRE);
        table.setBackground(BLANC);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Renderer pour TOUTES les colonnes de l'entête
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 36));
        TableCellRenderer renderer = new EnteteRenderer();
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(renderer);
        }
        header.setDefaultRenderer(renderer);
    }

    // === HELPERS UI ===
    public static Border bordureCarte() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    public static JLabel creerTitreSection(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(POLICE_TITRE_PETIT);
        lbl.setForeground(BLEU_NUIT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return lbl;
    }

    public static void appliquerIconeFenetre(JFrame frame) {
        ImageIcon logo = IconLoader.charger("app_logo", 64);
        if (logo != null) {
            frame.setIconImage(logo.getImage());
        }
    }

    public static JPanel creerHeaderApp(String titre, String nomIcone, JButton btnDroite) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BLEU_NUIT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(POLICE_TITRE);
        lblTitre.setForeground(DORE_LUXE);
        if (nomIcone != null) {
            ImageIcon ic = IconLoader.charger(nomIcone, 26);
            if (ic != null) {
                lblTitre.setIcon(ic);
                lblTitre.setIconTextGap(10);
            }
        }
        panel.add(lblTitre, BorderLayout.WEST);
        if (btnDroite != null) {
            panel.add(btnDroite, BorderLayout.EAST);
        }
        return panel;
    }

    /**
     * Crée un bouton "Retour" prêt à l'emploi avec icône PNG.
     */
    public static JButton creerBoutonRetour(java.awt.event.ActionListener action) {
        JButton btn = new JButton("Retour");
        appliquerThemeBoutonSecondaire(btn);
        IconLoader.appliquerIcone(btn, "icon_back");
        if (action != null) btn.addActionListener(action);
        return btn;
    }
}
