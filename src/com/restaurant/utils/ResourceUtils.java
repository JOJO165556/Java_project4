package com.restaurant.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Utilitaire pour localiser les ressources (DB, icônes) en dev et en mode packagé (jpackage)
public class ResourceUtils {
    private static final Logger logger = LogManager.getLogger(ResourceUtils.class);

    // Répertoire utilisateur dédié à l'app (toujours accessible en écriture)
    private static final File USER_APP_DIR = new File(System.getProperty("user.home"), ".gestionrestaurant");

    /**
     * Retourne le chemin absolu vers un fichier dans le dossier "data".
     *
     * Stratégie :
     * 1. Mode développement : utilise data/ dans le répertoire courant directement.
     * 2. Mode jpackage (app installée) : copie la DB dans ~/.gestionrestaurant/data/
     *    au premier lancement (car Program Files et /opt/ ne sont pas accessibles en écriture),
     *    puis retourne ce chemin utilisateur.
     */
    public static String getDataPath(String fileName) {
        // 1. Mode développement : répertoire de travail courant (accessible en écriture)
        File devFile = new File("data/" + fileName);
        if (devFile.exists()) {
            logger.debug("Ressource trouvée (dev) : " + devFile.getAbsolutePath());
            return devFile.getAbsolutePath();
        }

        // 2. Mode jpackage : répertoire utilisateur (toujours accessible en écriture)
        File userDataDir = new File(USER_APP_DIR, "data");
        File userFile = new File(userDataDir, fileName);

        // Si le fichier existe déjà dans le répertoire utilisateur, on l'utilise directement
        if (userFile.exists()) {
            logger.debug("Ressource trouvée (user home) : " + userFile.getAbsolutePath());
            return userFile.getAbsolutePath();
        }

        // Sinon : premier lancement — chercher la DB dans l'installation jpackage et la copier
        File sourceFile = findInInstallation(fileName);
        if (sourceFile != null && sourceFile.exists()) {
            try {
                userDataDir.mkdirs();
                Files.copy(sourceFile.toPath(), userFile.toPath());
                logger.info("DB initiale copiée vers : " + userFile.getAbsolutePath());
                return userFile.getAbsolutePath();
            } catch (IOException e) {
                logger.warn("Impossible de copier la DB vers le répertoire utilisateur : " + e.getMessage());
                // Fallback : utiliser le fichier source directement (peut échouer en écriture)
                return sourceFile.getAbsolutePath();
            }
        }

        // 3. Dernier recours : chemin relatif
        logger.warn("Ressource introuvable, fallback relatif : data/" + fileName);
        return devFile.getAbsolutePath();
    }

    /**
     * Cherche un fichier dans le répertoire d'installation jpackage
     * en remontant jusqu'à 5 niveaux depuis le JAR.
     */
    private static File findInInstallation(String fileName) {
        try {
            File jarFile = new File(ResourceUtils.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            File dir = jarFile.getParentFile();
            for (int level = 0; level < 5 && dir != null; level++) {
                File candidate = new File(dir, "data/" + fileName);
                if (candidate.exists()) {
                    logger.info("Ressource trouvée dans l'installation (niveau " + level + ") : " + candidate.getAbsolutePath());
                    return candidate;
                }
                dir = dir.getParentFile();
            }
        } catch (URISyntaxException e) {
            logger.warn("Impossible de résoudre le chemin du JAR : " + e.getMessage());
        }
        return null;
    }

    // Charge une ImageIcon de manière robuste (JAR ou fichier physique)
    public static ImageIcon getIcon(String path) {
        java.net.URL imgURL = ResourceUtils.class.getResource("/" + path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }

        String absPath = getDataPath(path.replace("data/", ""));
        File file = new File(absPath);
        if (file.exists()) {
            return new ImageIcon(absPath);
        }

        return null;
    }
}

