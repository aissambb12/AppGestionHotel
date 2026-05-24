package com.hotel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;
    private static final Object LOCK = new Object();

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private DatabaseConnection() {}

    public static Connection getConnection() {
        synchronized (LOCK) {
            try {
                if (connection == null || connection.isClosed()) {
                    chargerDriver();
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("✓ Connexion BDD réussie (hotel_db)");
                }
            } catch (SQLException e) {
                System.err.println("✗ Erreur connexion BDD : " + e.getMessage());
                e.printStackTrace();
            }
            return connection;
        }
    }

    private static void chargerDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.err.println("✗ Driver JDBC non trouvé");
                ex.printStackTrace();
            }
        }
    }

    public static void fermerConnexion() {
        synchronized (LOCK) {
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                    System.out.println("✓ Connexion BDD fermée");
                } catch (SQLException e) {
                    System.err.println("✗ Erreur fermeture BDD : " + e.getMessage());
                }
            }
        }
    }
}