package space.devport.wertik.items.handlers;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.itemutil.ItemNBTEditor;
import space.devport.utils.messageutil.MessageBuilder;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.objects.Reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttributeHandler {

    // Attribute cache
    @Getter
    private final Map<String, Attribute> attributeCache = new HashMap<>();

    public Attribute getAttribute(String attributeName) {
        return this.attributeCache.get(attributeName);
    }

    public boolean isValid(String attributeName) {
        return this.attributeCache.containsKey(attributeName);
    }

    public void load() {

        attributeCache.clear();
        Configuration storage = new Configuration(ItemsPlugin.getInstance(), "attributes");

        FileConfiguration yaml = storage.getFileConfiguration();

        for (String name : yaml.getKeys(false)) {
            ConfigurationSection section = yaml.getConfigurationSection(name);

            if (section == null) {
                continue;
            }

            Attribute attribute = new Attribute(name);

            // Cooldown and one time use
            attribute.setUseLimit(section.getInt("use-limit", 0));
            attribute.setCooldown(section.getLong("cooldown", 0) * 1000);

            // Load attribute reward
            Reward reward = new Reward();

            // Commands
            storage.getStringList("commands", new ArrayList<>());

            // Messages
            reward.setBroadcast(storage.loadMessageBuilder("broadcast", new MessageBuilder()));
            reward.setInform(storage.loadMessageBuilder("inform", new MessageBuilder()));

            attribute.setReward(reward);

            // Add to attribute cache
            this.attributeCache.put(name, attribute);
        }
    }

    // Get all attributes on an item
    public Map<String, String> getAttributes(ItemStack item) {
        Map<String, String> actionMap = ItemNBTEditor.hasNBT(item) ? ItemNBTEditor.getNBTTagMap(item) : new HashMap<>();

        // Remove unwanted NBT data
        for (String key : new ArrayList<>(actionMap.keySet())) {
            if (!ItemsPlugin.getInstance().getActionNames().contains(key.toLowerCase())) {
                actionMap.remove(key);
            }
        }

        return actionMap;
    }

    public ItemStack setAttribute(ItemStack item, String action, String attribute) {
        return ItemNBTEditor.writeNBT(item, action, attribute);
    }

    // Remove attribute by action
    public ItemStack removeAction(ItemStack item, String action) {
        return ItemNBTEditor.removeNBT(item, action);
    }

    // Remove attribute from all actions
    public ItemStack removeAttribute(ItemStack item, String attribute) {
        Map<String, String> nbt = getAttributes(item);

        for (String key : nbt.keySet()) {
            if (nbt.get(key).equalsIgnoreCase(attribute)) {
                item = ItemNBTEditor.removeNBT(item, key);
            }
        }

        return item;
    }

    // Clear all attributes
    public ItemStack clearAttributes(ItemStack item) {
        for (String action : ItemsPlugin.getInstance().getActionNames()) {
            item = removeAttribute(item, action);
        }

        return item;
    }

    // Get attribute from item by action
    public Attribute getAttribute(ItemStack item, String action) {
        for (String key : ItemNBTEditor.getNBTTagMap(item).keySet()) {
            if (key.equalsIgnoreCase(action)) {
                return ItemsPlugin.getInstance().getAttributeHandler().getAttribute(ItemNBTEditor.getNBT(item, key));
            }
        }

        return null;
    }
}