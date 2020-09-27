package space.devport.wertik.items.system;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.configuration.Configuration;
import space.devport.utils.item.ItemNBTEditor;
import space.devport.utils.text.message.CachedMessage;
import space.devport.utils.text.message.Message;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.objects.Reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttributeManager {

    private final ItemsPlugin plugin;

    @Getter
    private final Map<String, Attribute> attributeCache = new HashMap<>();

    public Attribute getAttribute(String attributeName) {
        return this.attributeCache.get(attributeName);
    }

    public AttributeManager(ItemsPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {

        attributeCache.clear();
        Configuration storage = new Configuration(plugin, "attributes");

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
            reward.setCommands(storage.getStringList(name + ".commands", new ArrayList<>()));

            // Messages
            reward.setBroadcast(new CachedMessage(storage.getMessage(name + ".broadcast", new Message())));
            reward.setInform(new CachedMessage(storage.getMessage(name + ".inform", new Message())));

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
        return ItemNBTEditor.writeNBT(item, action.toLowerCase(), attribute);
    }

    public ItemStack removeAction(ItemStack item, String action) {
        for (String key : getAttributes(item).keySet()) {
            if (key.equalsIgnoreCase(action))
                item = ItemNBTEditor.removeNBT(item, key);
        }
        return item;
    }

    public ItemStack removeAttribute(ItemStack item, String attribute) {
        for (Map.Entry<String, String> entry : getAttributes(item).entrySet()) {
            if (entry.getValue().equalsIgnoreCase(attribute))
                item = ItemNBTEditor.removeNBT(item, entry.getKey());
        }
        return item;
    }

    public ItemStack clearAttributes(ItemStack item) {
        for (String action : getAttributes(item).keySet()) {
            item = ItemNBTEditor.removeNBT(item, action);
        }

        return item;
    }

    // Get attribute from item by action
    public Attribute getAttribute(ItemStack item, String action) {
        for (String key : ItemNBTEditor.getNBTTagMap(item).keySet()) {
            if (key.equalsIgnoreCase(action)) {
                return plugin.getAttributeManager().getAttribute(ItemNBTEditor.getString(item, key));
            }
        }
        return null;
    }

    public boolean hasAttribute(ItemStack item) {
        return plugin.getActionNames().stream().anyMatch(key -> ItemNBTEditor.hasNBTKey(item, key));
    }

    // items_uses : "<attribute>:<uses>;<attribute>:<uses>"
    public ItemStack addUse(ItemStack item, String attribute) {
        if (!ItemNBTEditor.hasNBTKey(item, "items_uses"))
            return setUses(item, attribute, 1);
        else
            return setUses(item, attribute, getUses(item, attribute) + 1);
    }

    public HashMap<String, Integer> getUses(ItemStack item) {
        HashMap<String, Integer> usesMap = new HashMap<>();

        if (!ItemNBTEditor.hasNBTKey(item, "items_uses"))
            return usesMap;

        String dataString = ItemNBTEditor.getString(item, "items_uses");

        if (!dataString.contains(":"))
            return usesMap;

        for (String str : dataString.split(";")) {
            Attribute attribute = getAttribute(str.split(":")[0]);

            if (attribute == null)
                continue;

            int uses;
            try {
                uses = Integer.parseInt(str.split(":")[1].trim());
            } catch (NumberFormatException e) {
                continue;
            }

            usesMap.put(attribute.getName(), uses);
        }

        return usesMap;
    }

    public int getUses(ItemStack item, String attributeName) {
        return getUses(item).getOrDefault(attributeName, 0);
    }

    public ItemStack setUses(ItemStack item, String attributeName, int uses) {

        if (ItemNBTEditor.hasNBTKey(item, "items_uses")) {

            if (!getUses(item).containsKey(attributeName)) {
                // Append the attribute uses
                item = ItemNBTEditor.writeNBT(item, "items_uses", ItemNBTEditor.getString(item, "items_uses") + ";" + attributeName + ":" + uses);
            } else {
                // Replace with new value
                item = ItemNBTEditor.writeNBT(item, "items_uses", ItemNBTEditor.getString(item, "items_uses")
                        .replace(attributeName + ":" + getUses(item, attributeName), attributeName + ":" + uses));
            }
        } else {
            item = ItemNBTEditor.writeNBT(item, "items_uses", attributeName + ":" + uses);
        }

        return item;
    }
}