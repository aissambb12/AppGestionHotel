package com.hotel.vue;

import javax.swing.*;
import java.awt.*;

public class ThemeUtil {

    // Couleurs de l'hôtel
    public static final Color BLEU_NUIT = new Color(44, 62, 80); // Pour les barres de menu et textes importants
    public static final Color DORE_LUXE = new Color(212, 175, 55); // Pour les boutons d'action (Valider, Connexion)
    public static final Color GRIS_FOND = new Color(245, 246, 250); // Pour le fond de l'application
    public static final Color BLANC = Color.WHITE;

    // Polices d'écriture (Fonts)
    public static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font POLICE_NORMALE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font POLICE_BOUTON = new Font("Segoe UI", Font.BOLD, 14);

    // Empêcher l'instanciation
    private ThemeUtil() {}

    /**
     * Applique le design "Hôtel" à un bouton Swing.
     */
    public static void appliquerThemeBoutonPrincipal(JButton bouton) {
        bouton.setBackground(DORE_LUXE);
        bouton.setForeground(BLANC);
        bouton.setFont(POLICE_BOUTON);
        bouton.setFocusPainted(false);

        // --- LIGNES À AJOUTER POUR FORCER LA COULEUR ---
        bouton.setOpaque(true);
        bouton.setBorderPainted(false); // Enlève la bordure Windows qui bloque la couleur

        bouton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}