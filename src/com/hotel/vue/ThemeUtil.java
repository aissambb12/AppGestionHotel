package com.hotel.vue;

import javax.swing.*;
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
    public static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font POLICE_TITRE_PETIT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font POLICE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font POLICE_NORMALE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font POLICE_BOUTON = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font POLICE_PETIT = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font POLICE_LABEL = new Font("Segoe UI", Font.BOLD, 12);

    private ThemeUtil() {}

    // === BOUTONS - CORRECTIF TEXTE VISIBLE ===
    public static void appliquerThemeBoutonPrincipal(JButton bouton) {
        bouton.setBackground(DORE_LUXE);
        bouton.setForeground(BLANC);
        bouton.setFont(POLICE_BOUTON);
        bouton.setFocusPainted(false);
        bouton.setOpaque(true);
        bouton.setContentAreaFilled(true);
        bouton.setBorderPainted(true);
        bouton.setBorder(BorderFactory.createLineBorder(DORE_LUXE, 1));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void appliquerThemeBoutonSecondaire(JButton bouton) {
        bouton.setBackground(GRIS_CLAIR);
        bouton.setForeground(TEXTE_SOMBRE);
        bouton.setFont(POLICE_BOUTON);
        bouton.setFocusPainted(false);
        bouton.setOpaque(true);
        bouton.setContentAreaFilled(true);
        bouton.setBorderPainted(true);
        bouton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void appliquerThemeBoutonValider(JButton bouton) {
        bouton.setBackground(VERT_VALIDATION);
        bouton.setForeground(BLANC);
        bouton.setFont(POLICE_BOUTON);
        bouton.setFocusPainted(false);
        bouton.setOpaque(true);
        bouton.setContentAreaFilled(true);
        bouton.setBorderPainted(true);
        bouton.setBorder(BorderFactory.createLineBorder(VERT_VALIDATION, 1));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void appliquerThemeBoutonSuppression(JButton bouton) {
        bouton.setBackground(ROUGE_ERREUR);
        bouton.setForeground(BLANC);
        bouton.setFont(POLICE_BOUTON);
        bouton.setFocusPainted(false);
        bouton.setOpaque(true);
        bouton.setContentAreaFilled(true);
        bouton.setBorderPainted(true);
        bouton.setBorder(BorderFactory.createLineBorder(ROUGE_ERREUR, 1));
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
    public static void appliquerThemeTable(JTable table) {
        table.setFont(POLICE_NORMAL);
        table.setRowHeight(28);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(DORE_LUXE);
        table.setSelectionForeground(BLANC);
        table.setForeground(TEXTE_SOMBRE);
        table.setBackground(BLANC);
        table.getTableHeader().setFont(POLICE_LABEL);
        table.getTableHeader().setBackground(BLEU_NUIT);
        table.getTableHeader().setForeground(BLANC);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(BLEU_NUIT));
    }
}