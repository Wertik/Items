package space.devport.wertik.items.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.DevportListener;
import space.devport.wertik.items.ItemsPlugin;

public class CraftListener extends DevportListener {

    private final ItemsPlugin plugin;

    public CraftListener(ItemsPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(CraftItemEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) return;

        for (ItemStack item : clickedInventory.getContents())
            if (plugin.getItemManager().hasExtra(item, "uncraftable") || plugin.getAttributeManager().hasAttribute(item))
                event.setResult(Event.Result.DENY);
    }
}