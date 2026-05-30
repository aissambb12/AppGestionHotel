import com.hotel.util.DatabaseConnection;
import com.hotel.vue.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::fermerConnexion));

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);

                System.out.println("[OK] Application demarree");
            } catch (Exception e) {
                System.err.println("[ERREUR] Demarrage : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
