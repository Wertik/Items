package space.devport.wertik.items.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.DevportListener;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.system.ItemManager;

public class CraftListener extends DevportListener {

    private final ItemManager itemManager;

    public CraftListener() {
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null ||
                !event.getAction().toString().toUpperCase().contains("DROP"))
            return;

        ItemStack item = event.getCursor();

        if (itemManager.hasExtra(item, "uncraftable"))
            event.setCancelled(true);
    }
}