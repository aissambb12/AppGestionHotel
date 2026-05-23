package com.hotel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    Class.forName("com.mysql.jdbc.Driver");
                }

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion réussie à la base de données hotel_db !");
            }
        } catch (Exception e) {
            System.err.println("Erreur critique de connexion : " + e.getMessage());
        }
        return connection;
    }
}