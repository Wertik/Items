package space.devport.wertik.items.handlers;

import lombok.Getter;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.utils.Configuration;
import space.devport.wertik.items.utils.NBTEditor;
import space.devport.wertik.items.utils.Utils;

import java.util.HashMap;

public class ItemHandler {

    // items.yml
    private Configuration storage;

    // System name, item
    @Getter
    private HashMap<String, ItemStack> items = new HashMap<>();

    public ItemHandler() {
        storage = new Configuration(Main.inst, "items");
    }

    // Check if an item has attributes attached
    public boolean isSpecial(ItemStack item) {
        for (Action action : Action.values())
            if (NBTEditor.hasNBTTag(item, action.name().toLowerCase()))
                return true;
        return false;
    }

    // Load items from yaml
    public void load() {
        items.clear();
        storage.reload();

        for (String name : storage.getYaml().getKeys(false)) {

            // Parse from base64
            String base64 = storage.getYaml().getString(name);
            ItemStack item = Utils.itemStackFromBase64(base64);

            // No 0 amount items please.
            if (item.getAmount() == 0)
                item.setAmount(1);

            // Load into cache
            items.put(name, item);
            Main.inst.cO.debug("Loaded " + name);
        }
    }

    // Save all items
    public void saveItems() {
        storage.clear();
        items.keySet().forEach(name -> storage.getYaml().set(name, Utils.itemStackToBase64(items.get(name))));
        storage.save();
    }

    // Add attribute to an item
    public ItemStack setAttribute(ItemStack item, String attributeName, String action) {
        return NBTEditor.writeNBT(item, action, attributeName);
    }

    // Remove attribute by action
    public ItemStack removeAction(ItemStack item, String action) {
        return NBTEditor.hasNBTTag(item, action) ? NBTEditor.removeNBT(item, action) : item;
    }

    // Remove attribute from all actions
    public ItemStack removeAttribute(ItemStack item, String attributeName) {
        HashMap<String, String> nbt = getAttributes(item);

        for (String key : nbt.keySet())
            if (nbt.get(key).equalsIgnoreCase(attributeName))
                item = NBTEditor.removeNBT(item, key);

        return item;
    }

    // Clear all attributes
    public ItemStack clearAttributes(ItemStack item) {
        for (String action : Main.inst.getActionNames())
            item = removeAttribute(item, action);
        return item;
    }

    // Get attribute by action type
    public Attribute getAttribute(ItemStack item, String clickType) {
        for (String key : NBTEditor.getNBTKeys(item))
            if (key.equalsIgnoreCase(clickType))
                return Main.inst.getAttributeHandler().get(NBTEditor.getNBT(item, key));
        return null;
    }

    // Get item from cache
    public ItemStack getItem(String name) {
        return items.getOrDefault(name, null);
    }

    // Add a new item to cache
    public void addItem(String name, ItemStack item) {
        items.put(name, item);
    }

    // Remove an item from cache
    public void removeItem(String name) {
        items.remove(name);
    }

    // Get all attributes on an item
    public HashMap<String, String> getAttributes(ItemStack item) {
        HashMap<String, String> actionMap = new HashMap<>();

        if (NBTEditor.hasNBT(item))
            actionMap = NBTEditor.getNBT(item);

        // Remove unwanted NBT
        for (String next : actionMap.keySet())
            if (!Main.inst.getActionNames().contains(next))
                actionMap.remove(next);

        return actionMap;
    }
}