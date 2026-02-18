package com.restaurant.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_restaurant";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Mot de passe XAMPP (vide par défaut)

    private static Connection connection = null;

    // Retourne la connexion active, ou en crée une nouvelle
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
        return connection;
    }

    // Ferme la connexion proprement
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }
}