package space.devport.wertik.items.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.DevportListener;
import space.devport.utils.utility.reflection.SpigotHelper;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.objects.Reward;
import space.devport.wertik.items.utils.Utils;

public class ItemListener extends DevportListener {

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        // ignore physical action == button/plate interaction
        if (event.getAction() == Action.PHYSICAL) return;

        ItemStack item = event.getItem();

        if (item == null ||
                item.getType() == Material.AIR) return;

        // Unplaceable check
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                ItemsPlugin.getInstance().getItemManager().hasExtra(item, "unplaceable")) {
            event.setCancelled(true);
        }

        if (!ItemsPlugin.getInstance().getAttributeManager().hasAttribute(item)) return;

        event.setCancelled(true);

        // Now get to attributes

        Player player = event.getPlayer();

        String action = event.getAction().toString().toLowerCase().replace("_block", "").replace("_air", "");
        if (player.isSneaking()) action = "shift_" + action;

        ItemsPlugin.getInstance().getConsoleOutput().debug("Action: " + action);

        if (!ItemsPlugin.getInstance().getActionNames().contains(action))
            return;

        Attribute attribute = ItemsPlugin.getInstance().getAttributeManager().getAttribute(item, action);

        if (attribute == null) return;

        // If the item is not usable, send a message and return
        if (!ItemsPlugin.getInstance().getCooldownManager().isUsable(player, attribute.getName())) {

            double cooldownTime = ItemsPlugin.getInstance().getCooldownManager().getTimeRemaining(player, attribute.getName()) / 1000D;

            ItemsPlugin.getInstance().getLanguageManager().getPrefixed("Item-Cooldown")
                    .replace("%time%", String.valueOf(cooldownTime))
                    .send(player);
            return;
        }

        EquipmentSlot hand = SpigotHelper.getVersion().contains("1.8") || SpigotHelper.getVersion().contains("1.7") ? null : event.getHand();

        int uses = ItemsPlugin.getInstance().getAttributeManager().getUses(item, attribute.getName());

        // 0 == unlimited
        if (attribute.getUseLimit() > 0) {
            // Consume if above
            if ((uses + 1) >= attribute.getUseLimit()) {
                ItemsPlugin.getInstance().getLanguageManager().sendPrefixed(player, "Item-Use-Limit");
                Utils.consumeItem(player, hand, item);
            } else
                Utils.setItem(player, hand, ItemsPlugin.getInstance().getAttributeManager().addUse(item, attribute.getName()));

            uses++;
        }

        // Reward the player
        Reward reward = attribute.getReward();

        reward.getPlaceholders()
                .add("%uses%", String.valueOf(uses))
                .add("%use_limit%", String.valueOf(attribute.getUseLimit() == 0 ?
                        ItemsPlugin.getInstance().getLanguageManager().get("Unlimited").color().toString() :
                        attribute.getUseLimit()))
                .add("%cooldown%", String.valueOf(attribute.getCooldown() / 1000D));

        reward.give(event.getPlayer());

        ItemsPlugin.getInstance().getCooldownManager().addCooldown(player, attribute);
    }
}