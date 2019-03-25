package me.wertik.items.handlers;

import me.wertik.items.Main;
import me.wertik.items.objects.Attribute;
import me.wertik.items.utils.NBTEditor;
import me.wertik.items.utils.NBTUtils;
import me.wertik.items.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ItemHandler {

    private Main plugin;

    private File itemsFile;
    private FileConfiguration itemsYaml;

    // System name, item
    private HashMap<String, ItemStack> items;

    public ItemHandler() {
        plugin = Main.getInstance();
        items = new HashMap<>();

        itemsFile = new File(plugin.getDataFolder(), "items.yml");

        if (!itemsFile.exists())
            plugin.saveResource("items.yml", false);

        itemsYaml = YamlConfiguration.loadConfiguration(itemsFile);

        loadItems();
    }

    public boolean isSpecial(ItemStack item) {
        for (Action a : Action.values()) {
            if (NBTEditor.hasNBTTag(item, a.name().toLowerCase()))
                return true;
        }
        return false;
    }

    public ItemStack getItem(String name) {
        return items.get(name);
    }

    public void addItem(String name, ItemStack item) {
        items.put(name, item);
    }

    public void remItem(String name) {
        items.remove(name);
    }

    public void reloadItems() {
        saveItems();
        loadItems();
    }

    public void loadItems() {
        itemsYaml.getKeys(false).forEach(name -> items.put(name, Utils.itemStackFromBase64(itemsYaml.getString(name))));
    }

    public void saveItems() {
        items.keySet().forEach(name -> itemsYaml.set(name, Utils.itemStackToBase64(items.get(name))));
        saveFile();
    }

    public HashMap<String, ItemStack> getItems() {
        return items;
    }

    public void saveFile() {
        try {
            itemsYaml.save(itemsFile);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    // Todo multiple attributes on one clickType support?

    /*
     * NBT SYNTAX:
     *
     * clickType: attributeName
     *
     *
     * */

    public ItemStack setAttribute(ItemStack item, String attributeName, String clickType) {
        item = NBTEditor.writeNBT(item, clickType, attributeName);
        return item;
    }

    public ItemStack remAttribute(ItemStack item, String clickType) {
        if (NBTEditor.hasNBTTag(item, clickType)) {
            item = NBTEditor.removeNBT(item, clickType);
        }
        return item;
    }

    public ItemStack remAttribute(String attributeName, ItemStack item) {
        HashMap<String, String> nbt = getAttributes(item);
        for (String key : nbt.keySet()) {
            if (nbt.get(key).equalsIgnoreCase(attributeName)) {
                item = NBTEditor.removeNBT(item, key);
            }
        }
        return item;
    }

    // Todo add clickTypes

    public ItemStack clearAttributes(ItemStack item) {

        String[] clickTypes = {""};

        for (String clickType : clickTypes) {
            item = remAttribute(item, clickType);
        }

        return item;
    }

    public Attribute getAttribute(ItemStack item, String clickType) {
        for (String key : NBTEditor.getNBTTags(item)) {
            if (key.equalsIgnoreCase(clickType)) {
                return Main.getInstance().getAttributeHandler().getAttribute(NBTUtils.strip(NBTEditor.getNBT(item, key)));
            }
        }
        return null;
    }

    public HashMap<String, String> getAttributes(ItemStack item) {
        return NBTEditor.getNBT(item);
    }
}
