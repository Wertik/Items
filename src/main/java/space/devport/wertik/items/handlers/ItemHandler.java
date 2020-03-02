package space.devport.wertik.items.handlers;

import lombok.Getter;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.itemutil.ItemNBTEditor;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemHandler {

    // items.yml
    private final Configuration storage;

    // System name, item
    @Getter
    private final HashMap<String, ItemStack> items = new HashMap<>();

    public ItemHandler() {
        storage = new Configuration(Main.inst, "items");
    }

    // Check if an item has attributes attached
    public boolean isSpecial(ItemStack item) {
        return Main.inst.getActionNames().stream().anyMatch(a -> ItemNBTEditor.hasNBTKey(item, a.toUpperCase()));
    }

    // Load items from yaml
    public void load() {
        items.clear();
        storage.reload();

        for (String name : storage.getFileConfiguration().getKeys(false)) {

            // Parse from base64
            String base64 = storage.getFileConfiguration().getString(name);
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
        items.keySet().forEach(name -> storage.getFileConfiguration().set(name, Utils.itemStackToBase64(items.get(name))));
        storage.save();
    }

    // Add attribute to an item
    public ItemStack setAttribute(ItemStack item, String attributeName, String action) {
        return ItemNBTEditor.writeNBT(item, action, attributeName);
    }

    // Remove attribute by action
    public ItemStack removeAction(ItemStack item, String action) {
        return ItemNBTEditor.hasNBTKey(item, action) ? ItemNBTEditor.removeNBT(item, action) : item;
    }

    // Remove attribute from all actions
    public ItemStack removeAttribute(ItemStack item, String attributeName) {
        Map<String, String> nbt = getAttributes(item);

        for (String key : nbt.keySet())
            if (nbt.get(key).equalsIgnoreCase(attributeName))
                item = ItemNBTEditor.removeNBT(item, key);

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
        for (String key : ItemNBTEditor.getNBTTagMap(item).keySet())
            if (key.equalsIgnoreCase(clickType))
                return Main.inst.getAttributeHandler().get(ItemNBTEditor.getNBT(item, key));
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
    public Map<String, String> getAttributes(ItemStack item) {
        Map<String, String> actionMap = new HashMap<>();

        if (ItemNBTEditor.hasNBT(item))
            actionMap = ItemNBTEditor.getNBTTagMap(item);

        // Remove unwanted NBT
        for (String key : new ArrayList<>(actionMap.keySet()))
            if (!Main.inst.getActionNames().contains(key.toUpperCase()))
                actionMap.remove(key);

        return actionMap;
    }
}