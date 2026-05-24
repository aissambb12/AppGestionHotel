package com.hotel.util;

import com.hotel.vue.ThemeUtil;

import javax.swing.*;
import java.awt.*;

public class UIUtil {

    private UIUtil() {}

    /**
     * Crée un JPanel avec bordure arrondie et ombre
     */
    public static JPanel creerPanelModerne(Color couleur) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(couleur);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g);
            }
        };
        panel.setBackground(couleur);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    /**
     * Crée un label stylisé
     */
    public static JLabel creerLabel(String texte, Font font, Color couleur) {
        JLabel label = new JLabel(texte);
        label.setFont(font);
        label.setForeground(couleur);
        return label;
    }

    /**
     * Crée une séparation visuelle
     */
    public static JSeparator creerSeparateur() {
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(220, 220, 220));
        return sep;
    }

    /**
     * Affiche une boîte de dialogue d'erreur stylisée
     */
    public static void afficherErreur(JFrame parent, String titre, String message) {
        JOptionPane.showMessageDialog(parent, "❌ " + message, "⚠ " + titre, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Affiche une boîte de dialogue de succès
     */
    public static void afficherSucces(JFrame parent, String titre, String message) {
        JOptionPane.showMessageDialog(parent, "✓ " + message, "✓ " + titre, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Affiche une boîte de dialogue de confirmation
     */
    public static int afficherConfirmation(JFrame parent, String titre, String message) {
        return JOptionPane.showConfirmDialog(parent, message, titre, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Crée un JComboBox stylisé
     */
    public static JComboBox<String> creerComboBoxStyilisee(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(ThemeUtil.POLICE_NORMALE);
        combo.setBackground(Color.WHITE);
        combo.setForeground(ThemeUtil.TEXTE_SOMBRE);
        return combo;
    }
}