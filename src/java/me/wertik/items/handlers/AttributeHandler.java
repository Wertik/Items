package me.wertik.items.handlers;

import me.wertik.items.Main;
import me.wertik.items.objects.Attribute;
import me.wertik.items.objects.Reward;
import me.wertik.items.utils.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class AttributeHandler {

    private Main plugin;

    private Configuration storage;

    // List of attributes
    private HashMap<String, Attribute> attributes;

    public AttributeHandler() {
        plugin = Main.getInstance();

        attributes = new HashMap<>();

        storage = new Configuration(plugin, "attributes");
    }

    public Attribute getAttribute(String name) {
        plugin.cO.debug("Attributes: " + attributes.keySet().toString());
        plugin.cO.debug("LF: " + name);

        return attributes.get(name);
    }

    public void loadAttributes() {
        plugin.cO.debug("Loading attributes..");

        storage.reload();

        attributes.clear();

        FileConfiguration attYaml = storage.getYaml();

        for (String name : attYaml.getKeys(false)) {

            ConfigurationSection section = attYaml.getConfigurationSection(name);

            Attribute attribute = new Attribute(name);

            if (section.contains("cooldown-message"))
                attribute.setCooldownMessage(section.getString("cooldown-message"));

            if (section.contains("cooldown-expire-message"))
                attribute.setCooldownExpireMessage(section.getString("cooldown-expire-message"));

            attribute.setOneTime(section.getBoolean("one-time-use", false));

            attribute.setCooldown(section.getLong("cooldown", 0) * 1000L);

            Reward reward = new Reward();

            if (section.contains("console"))
                reward.setConsoleCommands(section.getStringList("console"));

            if (section.contains("player"))
                reward.setPlayerCommands(section.getStringList("player"));

            if (section.contains("playerOP"))
                reward.setPlayerOPCommands(section.getStringList("playerOP"));

            attribute.setReward(reward);

            attributes.put(name, attribute);

            plugin.cO.debug("Loaded " + attribute.getName());
        }
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }
}
