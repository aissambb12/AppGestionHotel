package com.hotel.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Utilitaire simple pour charger les icônes PNG depuis src/resources/icons.
 * Retourne null silencieusement si l'icône est introuvable (les boutons gardent alors leur emoji).
 */
public class IconLoader {

    private IconLoader() {}

    /**
     * Charge l'icône à la taille demandée (par défaut 20x20).
     * Exemple : IconLoader.charger("icon_add", 20)
     */
    public static ImageIcon charger(String nom, int taille) {
        try {
            URL url = IconLoader.class.getResource("/resources/icons/" + nom + ".png");
            if (url == null) {
                url = IconLoader.class.getResource("/icons/" + nom + ".png");
            }
            if (url == null) return null;

            ImageIcon icone = new ImageIcon(url);
            Image img = icone.getImage().getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    public static ImageIcon charger(String nom) {
        return charger(nom, 20);
    }

    /**
     * Applique une icône à un bouton SI elle existe (sinon on garde le texte tel quel).
     */
    public static void appliquerIcone(JButton bouton, String nom) {
        ImageIcon ic = charger(nom, 18);
        if (ic != null) {
            bouton.setIcon(ic);
            bouton.setIconTextGap(8);
        }
    }
}
