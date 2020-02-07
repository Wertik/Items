package space.devport.wertik.items.handlers;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.objects.Reward;
import space.devport.wertik.items.utils.Configuration;

import java.util.HashMap;

public class AttributeHandler {

    // attributes.yml
    private Configuration storage;

    // Attribute cache keyed by name
    @Getter
    private HashMap<String, Attribute> attributeCache = new HashMap<>();

    public AttributeHandler() {
        storage = new Configuration(Main.inst, "attributes");
    }

    // Get an attribute, return null if invalid.
    public Attribute get(String attributeName) {
        return attributeCache.getOrDefault(attributeName, null);
    }

    // Load all attributes from attributes.yml
    public void load() {

        attributeCache.clear();
        storage.reload();

        FileConfiguration attYaml = storage.getYaml();

        for (String name : attYaml.getKeys(false)) {
            ConfigurationSection section = attYaml.getConfigurationSection(name);

            Attribute attribute = new Attribute(name);

            // Cooldown and one time use
            attribute.setOneTime(section.getBoolean("one-time-use", false));
            attribute.setCooldown(section.getLong("cooldown", 0) * 1000L);

            // Load attribute reward
            Reward reward = new Reward();

            // Commands
            if (section.contains("commands")) reward.setCommands(section.getStringList("commands"));

            // Messages
            if (section.contains("inform")) reward.setInformMessage(section.getStringList("inform"));
            if (section.contains("broadcast")) reward.setBroadcastMessage(section.getStringList("broadcast"));

            attribute.setReward(reward);

            // Load cooldown messages
            if (section.contains("cooldown-message"))
                attribute.setCooldownMessage(section.getString("cooldown-message"));
            if (section.contains("cooldown-expire-message"))
                attribute.setCooldownExpireMessage(section.getString("cooldown-expire-message"));

            // Add to attribute cache
            attributeCache.put(name, attribute);
            Main.inst.cO.debug("Loaded " + attribute.getName());
        }
    }
}