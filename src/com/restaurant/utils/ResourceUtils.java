package com.restaurant.utils;

import java.io.File;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Utilitaire pour localiser les ressources (DB, icônes) en dev et en mode packagé (jpackage)
public class ResourceUtils {
    private static final Logger logger = LogManager.getLogger(ResourceUtils.class);

    /**
     * Retourne le chemin absolu vers un fichier dans le dossier "data".
     * Fonctionne en mode développement ET en mode jpackage (Windows exe, Linux deb/rpm, macOS dmg).
     *
     * Structure jpackage typique :
     *   Windows : InstallDir\app\GestionRestaurant.jar  → data\ est dans app\data\
     *   Linux   : /opt/gestionrestaurant/lib/app/GestionRestaurant.jar → data/ est dans app/data/
     */
    public static String getDataPath(String fileName) {
        // 1. Mode développement : répertoire de travail courant
        File devFile = new File("data/" + fileName);
        if (devFile.exists()) {
            logger.debug("Ressource trouvée (dev) : " + devFile.getAbsolutePath());
            return devFile.getAbsolutePath();
        }

        // 2. Mode jpackage : remonte jusqu'à 4 niveaux depuis le JAR
        try {
            File jarFile = new File(ResourceUtils.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            // Commence depuis le répertoire contenant le JAR
            File dir = jarFile.getParentFile();
            for (int level = 0; level < 4 && dir != null; level++) {
                File candidate = new File(dir, "data/" + fileName);
                if (candidate.exists()) {
                    logger.info("Ressource trouvée (jpackage niveau " + level + ") : " + candidate.getAbsolutePath());
                    return candidate.getAbsolutePath();
                }
                dir = dir.getParentFile();
            }
        } catch (URISyntaxException e) {
            logger.warn("Impossible de résoudre le chemin du JAR : " + e.getMessage());
        }

        // 3. Fallback : répertoire home utilisateur (portable, toujours accessible en écriture)
        File homeData = new File(System.getProperty("user.home"), ".gestionrestaurant/data/" + fileName);
        if (homeData.exists()) {
            logger.info("Ressource trouvée (home) : " + homeData.getAbsolutePath());
            return homeData.getAbsolutePath();
        }

        // 4. Dernier recours : chemin relatif (peut échouer si le répertoire courant est invalide)
        logger.warn("Ressource introuvable, fallback relatif : data/" + fileName);
        return devFile.getAbsolutePath();
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
