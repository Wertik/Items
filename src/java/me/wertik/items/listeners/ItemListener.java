package me.wertik.items.listeners;

import me.wertik.items.Main;
import me.wertik.items.handlers.ItemHandler;
import me.wertik.items.objects.Attribute;
import me.wertik.items.utils.NBTEditor;
import me.wertik.items.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        // Make sure there's only one click, not like MyItems tho -- Praya I thought you were better.
        if (!e.getAction().equals(Action.PHYSICAL))
            if (!e.getHand().equals(EquipmentSlot.HAND))
                return;

        ItemStack item = e.getPlayer().getInventory().getItemInHand();
        if (Main.getInstance().getItemHandler().isSpecial(item)) {
            // Check for NBT
            Main.getInstance().cw.debug("Special item click");
            Main.getInstance().cw.debug(e.getAction().name());
            Main.getInstance().cw.debug(NBTEditor.getNBTTags(item).toString());
            if (NBTEditor.hasNBTTag(item, e.getAction().name().toLowerCase())) {
                Main.getInstance().cw.debug("Has attribute, proceed");

                Attribute attribute = Main.getInstance().getItemHandler().getAttribute(item, e.getAction().toString().toLowerCase());
                Main.getInstance().cw.debug("Attribute: " + attribute.getName());

                if (attribute.hasCooldown())
                    if (!Main.getInstance().getCooldownHandler().isUsable(e.getPlayer().getUniqueId().toString(), attribute.getName())) {
                        e.getPlayer().sendMessage("§cYou need to wait " + Main.getInstance().getCooldownHandler().getCooldownTime(e.getPlayer().getUniqueId().toString(), attribute.getName()) + " more seconds until next usage.");
                        return;
                    }

                // REWARDS ---------------------------
                attribute.getReward().getConsoleCommands().forEach(command -> Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), Utils.parse(command, e.getPlayer())));
                attribute.getReward().getPlayerCommands().forEach(command -> e.getPlayer().performCommand(Utils.parse(command, e.getPlayer())));
                //

                Main.getInstance().getCooldownHandler().addCooldown(e.getPlayer().getUniqueId().toString(), attribute);
            }
        }
    }
}