package com.hotel;

import com.hotel.vue.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        // 1. Appliquer le design du système d'exploitation (Windows/Mac) pour que ce soit plus joli
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Impossible d'appliquer le thème du système : " + e.getMessage());
        }

        // 2. Lancer l'interface graphique de manière sécurisée (Recommandé par Java)
        SwingUtilities.invokeLater(() -> {
            // Lancer UNIQUEMENT la fenêtre de connexion au démarrage
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}