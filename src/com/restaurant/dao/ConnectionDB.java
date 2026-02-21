package com.restaurant.dao;

import com.restaurant.utils.ResourceUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionDB {

    private static final Logger logger = LogManager.getLogger(ConnectionDB.class);

    private static Connection connection = null;

    // Singleton pour récupérer la connexion active
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Chargement du driver SQLite
                Class.forName("org.sqlite.JDBC");

                // Résolution robuste du chemin (Mode Dev ou jpackage)
                String absolutePath = ResourceUtils.getDataPath("gestion_restaurant.db");

                logger.info("Connexion SQLite vers : " + absolutePath);

                connection = DriverManager.getConnection("jdbc:sqlite:" + absolutePath);
                logger.info("Connexion SQLite établie avec succès.");
            }
        } catch (ClassNotFoundException e) {
            logger.error("Driver SQLite introuvable : " + e.getMessage());
        } catch (SQLException e) {
            logger.error("Erreur de connexion (CANTOPEN): " + e.getMessage() + " [Path="
                    + ResourceUtils.getDataPath("gestion_restaurant.db") + "]");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
}