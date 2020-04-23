package space.devport.wertik.items.handlers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.configuration.Configuration;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.item.ItemNBTEditor;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.utils.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemHandler {

    private final Map<String, ItemBuilder> items = new HashMap<>();

    @Getter
    private final Configuration storage;

    public ItemHandler() {
        storage = new Configuration(ItemsPlugin.getInstance(), "items");
    }

    // Load items from yaml
    public void loadItems() {
        storage.load();
        this.items.clear();

        for (String name : storage.getFileConfiguration().getKeys(false)) {
            ItemBuilder item = storage.getItemBuilder(name);

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
        ItemBuilder item = getBuilder(name);

        if (item == null)
            return;

        storage.setItemBuilder(name, item);
        storage.save();
    }

    public void loadItem(String name) {
        storage.load();

        ItemBuilder itemBuilder = storage.getItemBuilder(name);

        if (itemBuilder == null) return;

        addItem(name, itemBuilder);
    }

    public boolean checkItemStorage(String name) {
        storage.load();
        return storage.getFileConfiguration().contains(name);
    }

    // Get item from cache
    public ItemBuilder getBuilder(String name) {
        return this.items.get(name);
    }

    public ItemStack getItem(String name) {
        return getBuilder(name).build();
    }

    public ItemBuilder prepareBuilder(String name) {
        return prepareBuilder(name, null);
    }

    public ItemBuilder prepareBuilder(String name, Player player) {
        ItemBuilder builder = getBuilder(name);

        // Parse displayname
        // TODO: Sort this out or make it possible to parse custom placeholders in every message
        builder.getDisplayName().set(Utils.parsePlaceholders(builder.getDisplayName().getWorkingMessage(), player));

        // Parse lore
        builder.getLore().setWorkingMessage(Utils.parsePlaceholders(builder.getLore().getWorkingMessage(), player));

        // Update unstackable uuid
        if (builder.getNBT().containsKey("items_unstackable")) {
            builder.removeNBT("items_unstackable").addNBT("items_unstackable", UUID.randomUUID().toString());
        }

        return builder;
    }

    public ItemStack prepareItem(String name, Player player) {
        return prepareBuilder(name, player).build();
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

    public ItemStack setUnplaceable(ItemStack item, boolean b) {
        if (item == null) return null;

        if (b) {
            return ItemNBTEditor.writeNBT(item, "items_unplaceable", "");
        } else return ItemNBTEditor.removeNBT(item, "items_unplaceable");
    }

    public boolean isUnplaceable(ItemStack item) {
        if (item == null) return false;

        if (!ItemNBTEditor.hasNBT(item)) return false;

        return ItemNBTEditor.hasNBTKey(item, "items_unplaceable");
    }
}