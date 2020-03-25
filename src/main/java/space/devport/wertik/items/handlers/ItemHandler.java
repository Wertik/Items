package space.devport.wertik.items.handlers;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.itemutil.ItemBuilder;
import space.devport.utils.itemutil.ItemNBTEditor;
import space.devport.wertik.items.ItemsPlugin;

import java.util.HashMap;
import java.util.Map;

public class ItemHandler {

    // System name, item
    @Getter
    private final Map<String, ItemBuilder> items = new HashMap<>();

    // Check if an item has attributes attached
    public boolean hasAttribute(ItemStack item) {
        return ItemsPlugin.getInstance().getActionNames()
                .stream()
                .anyMatch(action -> ItemNBTEditor.hasNBTKey(item, action.toLowerCase()));
    }

    // Load items from yaml
    public void loadItems() {
        Configuration storage = new Configuration(ItemsPlugin.getInstance(), "items");

        items.clear();

        for (String name : storage.getFileConfiguration().getKeys(false)) {

            ItemBuilder item = storage.loadItemBuilder(name);

            // Filter 0 amount items
            if (item.getAmount() > 0)
                this.items.put(name, item);
        }
    }

    // Save all items
    public void saveItems() {
        Configuration storage = new Configuration(ItemsPlugin.getInstance(), "items");

        storage.clear();

        for (String itemName : this.items.keySet()) {
            ItemBuilder item = this.items.get(itemName);

            storage.setItemBuilder(itemName, item);
        }

        storage.save();
        this.items.clear();
    }

    // Get item from cache
    public ItemBuilder getItem(String name) {
        return this.items.get(name);
    }

    public boolean isValid(String name) {
        return this.items.containsKey(name);
    }

    // Add a new item to cache
    public void addItem(String name, ItemStack item) {
        this.items.put(name, new ItemBuilder(item));
    }

    // Remove an item from cache
    public void removeItem(String name) {
        this.items.remove(name);
    }
}