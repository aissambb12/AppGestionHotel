package com.hotel.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Charge les icônes PNG depuis le classpath.
 * Essaie plusieurs noms de fichiers possibles : "icon_xxx.png", "icon_xxx_64.png", "icon_xxx_32.png", "icon_xxx_24.png"
 * pour que ça marche peu importe la taille du PNG fournie dans le dossier resources.
 */
public class IconLoader {

    private IconLoader() {}

    private static final String[] DOSSIERS = {
            "/resources/icons/",
            "/icons/",
            "/"
    };

    private static final String[] SUFFIXES = { "", "_64", "_32", "_24", "_128" };

    /**
     * Charge l'icône à la taille demandée. Retourne null silencieusement si introuvable.
     */
    public static ImageIcon charger(String nom, int taille) {
        try {
            URL url = trouverUrl(nom);
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

    private static URL trouverUrl(String nom) {
        for (String dossier : DOSSIERS) {
            for (String suffixe : SUFFIXES) {
                URL u = IconLoader.class.getResource(dossier + nom + suffixe + ".png");
                if (u != null) return u;
            }
        }
        return null;
    }

    /**
     * Applique une icône à un bouton SI elle existe.
     */
    public static void appliquerIcone(JButton bouton, String nom) {
        ImageIcon ic = charger(nom, 18);
        if (ic != null) {
            bouton.setIcon(ic);
            bouton.setIconTextGap(8);
        }
    }
}
