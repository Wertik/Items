package space.devport.wertik.items.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.handlers.CooldownHandler;
import space.devport.wertik.items.handlers.ItemHandler;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.objects.Reward;
import space.devport.wertik.items.utils.NBTEditor;

public class ItemListener implements Listener {

    private CooldownHandler cooldownHandler;
    private ItemHandler itemHandler;

    public ItemListener() {
        cooldownHandler = Main.inst.getCooldownHandler();
        itemHandler = Main.inst.getItemHandler();
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        // ignore physical action
        if (e.getAction().equals(Action.PHYSICAL))
            return;

        Action action = e.getAction();
        Player player = e.getPlayer();

        ItemStack item = player.getInventory().getItemInHand();

        if (item == null)
            return;

        // Ignore air and non-special items
        if (item.getType().equals(Material.AIR) || !itemHandler.isSpecial(item))
            return;

        Main.inst.cO.debug("Special item clicked");

        // Returns if there are no attributes set for this action
        if (!NBTEditor.hasNBTTag(item, e.getAction().name().toLowerCase()))
            return;

        Main.inst.cO.debug("Has attribute with this action");

        // Get attribute from item
        Attribute attribute = Main.inst.getItemHandler().getAttribute(item, action.toString().toLowerCase());

        // Attribute might be invalid
        if (attribute == null)
            return;

        Main.inst.cO.debug("Attribute: " + attribute.getName());

        // Cooldown check
        if (attribute.hasCooldown()) {
            if (!cooldownHandler.isUsable(player.getUniqueId().toString(), attribute.getName())) {

                // If the item is not usable, send a message and return
                double cooldownTime = cooldownHandler.getCooldownTime(player.getUniqueId().toString(), attribute.getName());

                player.sendMessage(attribute.getCooldownMessage(cooldownTime));
                return;
            }
        }

        // One time use remove one item
        if (attribute.isOneTime()) {
            if (player.getInventory().getItemInHand().getAmount() == 1)
                player.setItemInHand(null);
            else
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            Main.inst.cO.debug("One time use removed");
        }

        // Reward the player
        Reward reward = attribute.getReward();
        reward.reward(e.getPlayer());

        // Add to cooldowns, start a runnable
        cooldownHandler.addCooldown(player.getUniqueId(), attribute);
    }
}