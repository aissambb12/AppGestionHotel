package com.hotel.util;

import javax.swing.*;

/**
 * Gestionnaire centralisé de navigation entre les frames.
 * Évite les problèmes de dispose et de fermeture inattendue de l'application.
 */
public class NavigationManager {

    private NavigationManager() {}

    /**
     * Ouvre une nouvelle fenêtre et ferme l'ancienne proprement.
     * Utilisé pour la navigation principale (dashboard → écran enfant, ou déconnexion).
     */
    public static void naviguerVers(JFrame frameActuelle, JFrame frameNouvelle) {
        if (frameNouvelle != null) {
            frameNouvelle.setVisible(true);
        }
        if (frameActuelle != null) {
            frameActuelle.dispose();
        }
    }

    /**
     * Retour depuis un sous-écran vers son dashboard parent.
     * Recrée la fenêtre parente (puisqu'elle a été disposée à l'ouverture).
     */
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
