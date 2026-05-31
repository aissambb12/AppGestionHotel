package com.hotel.util;

import javax.swing.*;

/**
 * Gestionnaire centralisé de navigation entre les frames.
 * Évite les problèmes de dispose et de fermeture inattendue de l'application.
 */
public class NavigationManager {

    private NavigationManager() {}


    public static void naviguerVers(JFrame frameActuelle, JFrame frameNouvelle) {
        if (frameNouvelle != null) {
            frameNouvelle.setVisible(true);
        }
        if (frameActuelle != null) {
            frameActuelle.dispose();
        }
    }

    public static void retourVers(JFrame frameActuelle, JFrame dashboardParent) {
        if (dashboardParent != null) {
            dashboardParent.setVisible(true);
        }
        if (frameActuelle != null) {
            frameActuelle.dispose();
        }
    }

    /**
     * Ferme la fenêtre actuelle sans en ouvrir une autre (à éviter pour les sous-écrans).
     */
    public static void fermerFenetre(JFrame frame) {
        if (frame != null) {
            frame.dispose();
        }
    }
}
