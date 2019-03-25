package me.wertik.items.handlers;

import me.wertik.items.Main;
import me.wertik.items.objects.Attribute;
import me.wertik.items.objects.Reward;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class AttributeHandler {

    private Main plugin;

    private File attFile;
    private FileConfiguration attYaml;

    private HashMap<String, Attribute> attributes;

    public AttributeHandler() {
        plugin = Main.getInstance();
        attributes = new HashMap<>();

        attFile = new File(plugin.getDataFolder(), "attributes.yml");
        if (!attFile.exists())
            plugin.saveResource("attributes.yml", false);
        attYaml = YamlConfiguration.loadConfiguration(attFile);

        loadAttributes();
    }

    public Attribute getAttribute(String name) {
        plugin.cw.debug("Attributes: " + attributes.keySet().toString());
        plugin.cw.debug("LF: " + name);
        return attributes.get(name);
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public void loadAttributes() {
        plugin.cw.debug("Loading attributes");
        for (String name : attYaml.getKeys(false)) {
            attributes.put(name, new Attribute(name, new Reward(attYaml.getStringList(name + ".console"),
                    attYaml.getStringList(name + ".player")), ((long) (attYaml.getInt(name + ".cooldown", 0) * 1000))));
        }
    }
}
