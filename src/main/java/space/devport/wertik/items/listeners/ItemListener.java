package space.devport.wertik.items.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.utils.Language;
import space.devport.wertik.items.utils.Utils;

public class ItemListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        // ignore physical action == button/plate interaction
        if (event.getAction() == Action.PHYSICAL) return;

        String action = null;
        for (String a : ItemsPlugin.getInstance().getActionNames()) {
            if (event.getAction().toString().toLowerCase().contains(a)) {
                action = a;
                break;
            }
        }

        if (action == null) return;

        Player player = event.getPlayer();

        ItemStack item = event.getItem();

        if (item == null) return;

        // Ignore air and non-special items
        if (item.getType() == Material.AIR) return;

        // Get attribute from item
        Attribute attribute = ItemsPlugin.getInstance().getAttributeHandler().getAttribute(item, action);

        if (attribute == null) return;

        event.setCancelled(true);

        // If the item is not usable, send a message and return
        if (!ItemsPlugin.getInstance().getCooldownHandler().isUsable(player, attribute.getName())) {

            double cooldownTime = ItemsPlugin.getInstance().getCooldownHandler().getTimeRemaining(player, attribute.getName()) / 1000D;

            Language.ITEM_ON_COOLDOWN.getPrefixed()
                    .fill("%time%", String.valueOf(cooldownTime))
                    .send(player);
            return;
        }

        // 0 == unlimited
        if (attribute.getUseLimit() > 0) {
            // Consume if above
            if ((ItemsPlugin.getInstance().getAttributeHandler().getUses(item, attribute) + 1) > attribute.getUseLimit()) {
                Language.ITEM_USE_LIMIT.getPrefixed().send(player);
                Utils.consumeItem(player, event.getHand(), item);
            } else
                Utils.consumeItem(player, event.getHand(), ItemsPlugin.getInstance().getAttributeHandler().addUse(item, attribute));
        }

        // Reward the player
        attribute.getReward().give(event.getPlayer());

        ItemsPlugin.getInstance().getCooldownHandler().addCooldown(player, attribute);
    }
}