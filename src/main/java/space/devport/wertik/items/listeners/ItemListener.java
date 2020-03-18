package space.devport.wertik.items.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.itemutil.ItemNBTEditor;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.utils.Messages;

public class ItemListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        // ignore physical action == button/plate interaction
        if (event.getAction().equals(Action.PHYSICAL)) {
            return;
        }

        String action = event.getAction().toString().toLowerCase();
        Player player = event.getPlayer();

        // TODO: Update DevportUtils to allow per-version methods
        ItemStack item = player.getInventory().getItemInHand();

        // Ignore air and non-special items
        if (item.getType().equals(Material.AIR) || !ItemsPlugin.getInstance().getItemHandler().hasAttribute(item)) {
            return;
        }

        // Returns if there are no attributes set for this action
        if (!ItemNBTEditor.hasNBTKey(item, action)) {
            return;
        }

        // Get attribute from item
        Attribute attribute = ItemsPlugin.getInstance().getAttributeHandler().getAttribute(item, action);

        // Attribute might be invalid
        if (attribute == null) {
            return;
        }

        // If the item is not usable, send a message and return
        if (!ItemsPlugin.getInstance().getCooldownHandler().isUsable(player, attribute.getName())) {

            double cooldownTime = ItemsPlugin.getInstance().getCooldownHandler().getTimeRemaining(player, attribute.getName());

            Messages.ITEM_ON_COOLDOWN.getPrefixed()
                    .fill("%time%", String.valueOf(cooldownTime))
                    .send(player);
            return;
        }

        // TODO: Use limit implementation

        // Reward the player
        attribute.getReward().give(event.getPlayer());

        ItemsPlugin.getInstance().getCooldownHandler().addCooldown(player, attribute);
    }
}