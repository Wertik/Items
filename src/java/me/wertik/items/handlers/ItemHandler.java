package me.wertik.items.handlers;

import me.wertik.items.Main;
import me.wertik.items.objects.Attribute;
import me.wertik.items.utils.Configuration;
import me.wertik.items.utils.NBTEditor;
import me.wertik.items.utils.NBTUtils;
import me.wertik.items.utils.Utils;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ItemHandler {

    private Main plugin;

    private Configuration storage;

    // System name, item
    private HashMap<String, ItemStack> items;

    public ItemHandler() {
        plugin = Main.getInstance();

        items = new HashMap<>();

        storage = new Configuration(plugin, "items");
    }

    public boolean isSpecial(ItemStack item) {
        for (Action a : Action.values())
            if (NBTEditor.hasNBTTag(item, a.name().toLowerCase()))
                return true;

        return false;
    }

    public void loadItems() {
        items.clear();

        storage.reload();

        for (String name : storage.getYaml().getKeys(false)) {
            String base64 = storage.getYaml().getString(name);

            ItemStack item = Utils.itemStackFromBase64(base64);

            if (item.getAmount() == 0)
                item.setAmount(1);

            items.put(name, item);
        }
    }

    // Save to base64
    public void saveItems() {
        storage.clear();

        items.keySet().forEach(name -> storage.getYaml().set(name, Utils.itemStackToBase64(items.get(name))));

        storage.save();
    }

    public HashMap<String, ItemStack> getItems() {
        return items;
    }

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

    public ItemStack removeAttribute(ItemStack item, String clickType) {
        return NBTEditor.hasNBTTag(item, clickType) ? NBTEditor.removeNBT(item, clickType) : item;
    }

    public ItemStack removeAttribute(String attributeName, ItemStack item) {
        HashMap<String, String> nbt = getAttributes(item);

        for (String key : nbt.keySet())
            if (nbt.get(key).equalsIgnoreCase(attributeName))
                item = NBTEditor.removeNBT(item, key);

        return item;
    }

    public ItemStack clearAttributes(ItemStack item) {
        String[] clickTypes = {""};

        for (String clickType : clickTypes)
            item = removeAttribute(item, clickType);

        return item;
    }

    public Attribute getAttribute(ItemStack item, String clickType) {
        for (String key : NBTEditor.getNBTKeys(item))
            if (key.equalsIgnoreCase(clickType))
                return Main.getInstance().getAttributeHandler().getAttribute(NBTUtils.strip(NBTEditor.getNBT(item, key)));

        return null;
    }

    public ItemStack getItem(String name) {
        return items.get(name);
    }

    public void addItem(String name, ItemStack item) {
        items.put(name, item);
    }

    public void removeItem(String name) {
        items.remove(name);
    }

    public HashMap<String, String> getAttributes(ItemStack item) {
        return NBTEditor.getNBT(item);
    }
}
