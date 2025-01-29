package tv.ghostvone.sleepingfasternight.manager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ConfigManager {
    public static FileConfiguration config;

    public static void setupConfig(JavaPlugin main) {
        // Save le fichier si il n'existe pas
        main.saveDefaultConfig();

        // Récupère les valeurs du conf server et les confs par défaut du plugin si elles n'existent pas côté serveur
        main.getConfig().options().copyDefaults(true);


        // On re-save le fichier conf pour insérer les clés manquantes
        main.saveConfig();

        ConfigManager.config = main.getConfig();
    }

    public static void reloadConfig(JavaPlugin main) {
        main.reloadConfig();
        ConfigManager.config = main.getConfig();
    }

    public static String getString(String key) {
        return ChatColor.translateAlternateColorCodes('&', ConfigManager.config.getString(key));
    }

    public static Integer getInt(String key) {
        return ConfigManager.config.getInt(key);
    }

    public static Boolean getBoolean(String key) {
        return ConfigManager.config.getBoolean(key);
    }

    public static String replaceWord(String source, String key, String value) {
        return source.replace(key, value);
    }

    public static String replaceWords(String source, HashMap<String, String> replacements) {
        String formated_source = source;
        for (String key : replacements.keySet()) {
            formated_source = formated_source.replace(key, replacements.get(key));
        }
        return formated_source;
    }
}
