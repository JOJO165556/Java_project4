package com.restaurant.utils;

import java.io.File;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Utilitaire pour localiser les ressources (DB, icônes) en dev et en mode packagé (jpackage)
public class ResourceUtils {
    private static final Logger logger = LogManager.getLogger(ResourceUtils.class);

    // Retourne le chemin absolu vers un fichier dans le dossier "data"
    public static String getDataPath(String fileName) {
        // Mode développement : cherche dans le répertoire courant
        File devFile = new File("data/" + fileName);
        if (devFile.exists()) {
            return devFile.getAbsolutePath();
        }

        // Mode jpackage : cherche par rapport au JAR
        try {
            File jarPath = new File(ResourceUtils.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            File jarDir = jarPath.getParentFile();

            if (jarDir != null) {
                // Cherche dans lib/app/data/ (structure jpackage)
                File packagedFile = new File(jarDir, "data/" + fileName);
                if (packagedFile.exists()) {
                    return packagedFile.getAbsolutePath();
                }

                // Cherche à la racine de l'application
                File rootDir = jarDir.getParentFile();
                if (rootDir != null) {
                    File dataInRoot = new File(rootDir, "data/" + fileName);
                    if (dataInRoot.exists()) {
                        return dataInRoot.getAbsolutePath();
                    }
                }
            }
        } catch (URISyntaxException e) {
            logger.warn("Impossible de résoudre le chemin du JAR : " + e.getMessage());
        }

        // Fallback : chemin relatif par défaut
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
