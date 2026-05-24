import com.hotel.vue.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Configuration Swing sur Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Appliquer le Look & Feel natif du système
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Créer et afficher la fenêtre de login
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);

                System.out.println("✓ Application démarrée avec succès");
            } catch (Exception e) {
                System.err.println("❌ Erreur au démarrage : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}