package com.hotel.util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class IconLoader {

    private IconLoader() {}

    /** Emplacements possibles dans le classpath (avec et sans slash initial). */
    private static final String[] DOSSIERS = {
            "/resources/icons/", "resources/icons/",
            "/icons/",           "icons/",
            "/resources/images/icons/", "resources/images/icons/",
            "/",                 ""
    };

    /** Suffixes de taille essayes pour chaque nom. */
    private static final String[] SUFFIXES = { "", "_64", "_32", "_24", "_128", "_16", "_48" };

    /** Emplacements possibles sur le disque (repli si le classpath echoue). */
    private static final String[] DOSSIERS_DISQUE = {
            "src/resources/icons/",
            "resources/icons/",
            "src/resources/images/icons/",
            "out/production/AppGestionHotel/icons/"
    };

    /**
     * Charge l'icone a la taille demandee.
     * Retourne null si elle est vraiment introuvable (avec un message d'aide en console).
     */
    public static ImageIcon charger(String nom, int taille) {
        try {
            URL url = trouverUrl(nom);
            if (url != null) {
                return redimensionner(new ImageIcon(url), taille);
            }

            File fichier = trouverFichier(nom);
            if (fichier != null) {
                return redimensionner(new ImageIcon(fichier.getAbsolutePath()), taille);
            }
        } catch (Exception e) {
            System.err.println("[IconLoader] Erreur lors du chargement de '" + nom + "' : " + e.getMessage());
            return null;
        }

        System.err.println("[IconLoader] ICONE INTROUVABLE : '" + nom + "'."
                + " Verifie que 'src/resources' est marque 'Resources Root' dans IntelliJ,"
                + " puis fais Build > Rebuild Project.");
        return null;
    }

    public static ImageIcon charger(String nom) {
        return charger(nom, 20);
    }

    private static ImageIcon redimensionner(ImageIcon source, int taille) {
        Image img = source.getImage().getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /** Cherche l'icone dans le classpath via les deux class loaders disponibles. */
    private static URL trouverUrl(String nom) {
        ClassLoader contexte = Thread.currentThread().getContextClassLoader();
        for (String dossier : DOSSIERS) {
            for (String suffixe : SUFFIXES) {
                String chemin = dossier + nom + suffixe + ".png";

                URL u = IconLoader.class.getResource(chemin);
                if (u != null) return u;

                if (contexte != null) {
                    String sansSlash = chemin.startsWith("/") ? chemin.substring(1) : chemin;
                    u = contexte.getResource(sansSlash);
                    if (u != null) return u;
                }
            }
        }
        return null;
    }

    /** Cherche l'icone directement sur le disque (repli). */
    private static File trouverFichier(String nom) {
        for (String dossier : DOSSIERS_DISQUE) {
            for (String suffixe : SUFFIXES) {
                File f = new File(dossier + nom + suffixe + ".png");
                if (f.exists()) return f;
            }
        }
        return null;
    }

    /** Applique une icone a un bouton SI elle existe. */
    public static void appliquerIcone(JButton bouton, String nom) {
        ImageIcon ic = charger(nom, 18);
        if (ic != null) {
            bouton.setIcon(ic);
            bouton.setIconTextGap(8);
        }
    }
}