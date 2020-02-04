package me.wertik.items.listeners;

import me.wertik.items.Main;
import me.wertik.items.handlers.CooldownHandler;
import me.wertik.items.handlers.ItemHandler;
import me.wertik.items.objects.Attribute;
import me.wertik.items.objects.Reward;
import me.wertik.items.utils.NBTEditor;
import me.wertik.items.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    private Main plugin;
    private CooldownHandler cooldownHandler;
    private ItemHandler itemHandler;

    public ItemListener() {
        plugin = Main.getInstance();
        cooldownHandler = plugin.getCooldownHandler();
        itemHandler = plugin.getItemHandler();
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        if (!e.getAction().equals(Action.PHYSICAL)) {

            ItemStack item = e.getPlayer().getInventory().getItemInHand();

            if (item == null)
                return;

            if (item.getType().equals(Material.AIR))
                return;

            if (!itemHandler.isSpecial(item))
                return;

            // Check for NBT
            Main.getInstance().cO.debug("Special item click");

            Main.getInstance().cO.debug(e.getAction().name());
            Main.getInstance().cO.debug(NBTEditor.getNBTKeys(item).toString());

            // Check action name
            if (NBTEditor.hasNBTTag(item, e.getAction().name().toLowerCase())) {
                Main.getInstance().cO.debug("Has attribute, proceed");

                Attribute attribute = Main.getInstance().getItemHandler().getAttribute(item, e.getAction().toString().toLowerCase());

                if (attribute == null)
                    return;

                Main.getInstance().cO.debug("Attribute: " + attribute.getName());

                // COOLDOWN CHECK
                if (attribute.hasCooldown())
                    if (!cooldownHandler.isUsable(e.getPlayer().getUniqueId().toString(), attribute.getName())) {
                        double cooldownTime = cooldownHandler.getCooldownTime(e.getPlayer().getUniqueId().toString(), attribute.getName());

                        e.getPlayer().sendMessage(attribute.getCooldownMessage(cooldownTime));
                        return;
                    }

                // ONE TIME USE
                if (attribute.isOneTime()) {
                    if (e.getPlayer().getInventory().getItemInHand().getAmount() == 1)
                        e.getPlayer().setItemInHand(null);
                    else
                        e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);

                    Main.getInstance().cO.debug("One time use removed");
                }

                // REWARDS
                Reward reward = attribute.getReward();

                reward.getConsoleCommands().forEach(command -> Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), Utils.parse(command, e.getPlayer())));
                reward.getPlayerCommands().forEach(command -> e.getPlayer().performCommand(Utils.parse(command, e.getPlayer())));

                for (String commandOP : reward.getPlayerOPCommands()) {
                    if (!e.getPlayer().isOp()) {
                        e.getPlayer().setOp(true);
                        e.getPlayer().performCommand(commandOP);
                        e.getPlayer().setOp(false);
                    } else e.getPlayer().performCommand(commandOP);
                }
                //

                cooldownHandler.addCooldown(e.getPlayer().getUniqueId().toString(), attribute);
            }
        }
    }
}