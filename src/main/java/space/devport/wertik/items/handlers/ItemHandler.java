package space.devport.wertik.items.handlers;

import org.bukkit.inventory.ItemStack;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.itemutil.ItemBuilder;
import space.devport.utils.itemutil.ItemNBTEditor;
import space.devport.wertik.items.ItemsPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemHandler {

    // System name, item
    private final Map<String, ItemBuilder> items = new HashMap<>();

    private final Configuration storage;

    public ItemHandler() {
        storage = new Configuration(ItemsPlugin.getInstance(), "items");
    }

    // Load items from yaml
    public void loadItems() {
        storage.reload();
        this.items.clear();

        for (String name : storage.getFileConfiguration().getKeys(false)) {

            ItemBuilder item = storage.loadItemBuilder(name);

            // Filter 0 amount items
            if (item.getAmount() > 0)
                this.items.put(name, item);
        }
    }

    // Save all items
    public void saveItems() {

        for (String itemName : this.items.keySet()) {
            ItemBuilder item = this.items.get(itemName);

            storage.setItemBuilder(itemName, item);
        }

        storage.save();
    }

    public void saveItem(String name) {
        ItemBuilder item = getItem(name);

        if (item == null)
            return;

        storage.setItemBuilder(name, item);
        storage.save();
    }

    public void loadItem(String name) {
        storage.reload();

        if (!storage.getFileConfiguration().contains(name))
            return;

        addItem(name, storage.loadItemBuilder(name));
    }

    public boolean checkItemStorage(String name) {
        storage.reload();

        return storage.getFileConfiguration().contains(name);
    }

    // Get item from cache
    public ItemBuilder getItem(String name) {
        return this.items.get(name);
    }

    public void addItem(String name, ItemBuilder builder) {
        this.items.put(name, builder);
        saveItem(name);
    }

    // Add a new item to cache
    public void addItem(String name, ItemStack item) {
        addItem(name, new ItemBuilder(item));
    }

    // Remove an item from cache
    public void removeItem(String name) {
        this.items.remove(name);

        storage.reload();
        storage.getFileConfiguration().set(name, null);
        storage.save();
    }

    public Map<String, ItemBuilder> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    // Item Manipulation

    public ItemStack setUnstackable(ItemStack item, boolean b) {
        // Throw it back at 'em
        if (item == null) return null;

        if (b) {
            // Assign a new random UUID
            String uniqueID = UUID.randomUUID().toString();
            return ItemNBTEditor.writeNBT(item, "items_unstackable", uniqueID);
        } else return ItemNBTEditor.removeNBT(item, "items_unstackable");
    }

    public boolean isUnstackable(ItemStack item) {
        if (item == null) return false;

        if (!ItemNBTEditor.hasNBT(item)) return false;

        return ItemNBTEditor.hasNBTKey(item, "items_unstackable");
    }
}