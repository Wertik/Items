package space.devport.wertik.items.system;

import com.google.common.base.Strings;
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

public class ItemManager {

    private final Map<String, ItemBuilder> items = new HashMap<>();

    @Getter
    private final Configuration storage;

    public ItemManager() {
        storage = new Configuration(ItemsPlugin.getInstance(), "items");
    }

    public void loadItems() {
        items.clear();

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

    // TODO: Support player == null
    public ItemBuilder prepareBuilder(String name, Player player) {
        ItemBuilder builder = getBuilder(name);

        builder.getDisplayName().setMessage(Utils.parsePlaceholders(builder.getDisplayName().getOriginal(), player));

        builder.getLore().setMessage(Utils.parsePlaceholders(builder.getLore().getMessage(), player));

        if (builder.getNBT().containsKey("items_unstackable")) {
            builder.removeNBT("items_unstackable")
                    .addNBT("items_unstackable", UUID.randomUUID().toString());
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

        storage.load();
        storage.getFileConfiguration().set(name, null);
        storage.save();
    }

    public Map<String, ItemBuilder> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    public ItemStack setExtra(ItemStack item, String key, String... value) {
        if (item == null || Strings.isNullOrEmpty(key)) return null;
        return ItemNBTEditor.writeNBT(item, "items_" + key, value.length > 0 ? value[0] : "");
    }

    public ItemStack removeExtra(ItemStack item, String key) {
        if (item == null || Strings.isNullOrEmpty(key)) return null;
        return ItemNBTEditor.removeNBT(item, key);
    }

    public boolean hasExtra(ItemStack item, String key) {
        if (item == null || !ItemNBTEditor.hasNBT(item) || Strings.isNullOrEmpty(key)) return false;
        return ItemNBTEditor.hasNBTKey(item, "items_" + key);
    }
}