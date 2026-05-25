package com.hotel.util;

import javax.swing.*;

/**
 * Gestionnaire centralisé de navigation entre les frames
 * Évite les problèmes de dispose et de fermeture d'application
 */
public class NavigationManager {

    /**
     * Ouvre une nouvelle fenêtre et ferme l'ancienne proprement
     * Utilisé pour naviguer d'un dashboard à un autre
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
     * Ferme la fenêtre actuelle sans en ouvrir une autre
     * Utilisé pour les sous-écrans
     */
    public static void fermerFenetre(JFrame frame) {
        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     * Retour à la fenêtre précédente
     */
    public static void retourner(JFrame frameActuelle) {
        if (frameActuelle != null) {
            frameActuelle.dispose();
        }
    }
}