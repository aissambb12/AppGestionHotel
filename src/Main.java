import com.hotel.util.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

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
    }
}
