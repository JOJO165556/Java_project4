package com.restaurant.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseBackupService {

    private static final Logger logger = LogManager.getLogger(DatabaseBackupService.class);
    
    // Le chemin vers la base de données 
    private static final String DB_SOURCE_PATH = "data/gestion_restaurant.db";

    public DatabaseBackupService() {
        // Plus besoin de charger config.properties pour les backups !
    }

    // Sauvegarde la base de données SQLite en copiant simplement le fichier .db
    public boolean sauvegarderBaseDeDonnees(String cheminDestination) throws Exception {
        File source = new File(DB_SOURCE_PATH);
        File destination = new File(cheminDestination);

        if (!source.exists()) {
            throw new Exception("Le fichier de base de données source n'existe pas : " + DB_SOURCE_PATH);
        }

        try {
            // Copie physique du fichier
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Sauvegarde réussie vers : " + cheminDestination);
            return true;
        } catch (IOException e) {
            logger.error("Erreur lors de la sauvegarde SQLite : " + e.getMessage());
            throw new Exception("Erreur de copie de fichier : " + e.getMessage());
        }
    }

    //Restaure la base de données en remplaçant le fichier actuel par une sauvegarde
    public boolean restaurerBaseDeDonnees(String cheminSourceSql) throws Exception {
        File sourceBackup = new File(cheminSourceSql);
        File destinationActive = new File(DB_SOURCE_PATH);

        if (!sourceBackup.exists()) {
            throw new Exception("Le fichier de sauvegarde est introuvable.");
        }

        try {
            // On s'assure que le dossier data existe
            if (!destinationActive.getParentFile().exists()) {
                destinationActive.getParentFile().mkdirs();
            }

            // On remplace le fichier de travail par la sauvegarde
            Files.copy(sourceBackup.toPath(), destinationActive.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Restauration réussie depuis : " + cheminSourceSql);
            return true;
        } catch (IOException e) {
            logger.error("Erreur lors de la restauration SQLite : " + e.getMessage());
            throw new Exception("Erreur de remplacement du fichier .db : " + e.getMessage());
        }
    }
}