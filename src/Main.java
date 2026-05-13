import com.hotel.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        try {
            Connection connection = DatabaseConnection.getConnection();

            if (connection != null) {
                System.out.println("Connexion réussie à la base hotel_db !");
            }
            connection.close();

        } catch (Exception e) {
            System.out.println("Erreur de connexion à la base de données !");
            e.printStackTrace();
        }

        Connection conn = DatabaseConnection.getConnection() ;
        try (
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM client");
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id_client") + " | " +
                                rs.getString("nom") + " | " +
                                rs.getString("prenom") + " | " +
                                rs.getString("cin") + " | " +
                                rs.getString("telephone") + " | " +
                                rs.getString("email")
                );
            }

        } catch (Exception e) {
            System.out.println("Il y a un problème.");
            e.printStackTrace();
        }

    }
}
