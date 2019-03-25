package me.wertik.items;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    private Main plugin;

    private File configFile;
    private FileConfiguration config;

    public ConfigLoader() {
        plugin = Main.getInstance();

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists())
            plugin.saveResource("config.yml", false);

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config.yml");
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
