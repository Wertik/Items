package me.wertik.items.commands;

import me.wertik.items.Main;
import me.wertik.items.handlers.AttributeHandler;
import me.wertik.items.handlers.ItemHandler;
import me.wertik.items.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class AttCommands implements CommandExecutor {

    /*
     * /att add <name> <clickType>
     * /att rem <name/clickType>
     * /att list
     * /att clear
     * */

    private Main plugin;
    private AttributeHandler attributeHandler;
    private ItemHandler itemHandler;

    public AttCommands() {
        plugin = Main.getInstance();
        attributeHandler = plugin.getAttributeHandler();
        itemHandler = plugin.getItemHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players.");
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        if (args.length == 0) {
            help(sender);
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "add":
                case "a":
                    if (args.length < 3) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/att add <name> <clickType>");
                        return true;
                    }

                    if (args.length > 3) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/att add <name> <clickType>");
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage("§cCannot help you with AIR.");
                        return true;
                    }

                    if (!attributeHandler.getAttributes().containsKey(args[1])) {
                        sender.sendMessage("§cThat attribute is not valid.");
                        sender.sendMessage("§cValids: §f" + Utils.listToString(new ArrayList<>(attributeHandler.getAttributes().keySet()), "§7, §f", "§cNo attributes configured."));
                        return true;
                    }

                    try {
                        Action.valueOf(args[2].toUpperCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage("§cThat click type is not valid.");
                        sender.sendMessage("§cValids: §f");
                        for (Action a : Action.values()) {
                            sender.sendMessage(a.name());
                        }
                        return true;
                    }

                    player.setItemInHand(itemHandler.setAttribute(item, args[1], args[2].toUpperCase()));
                    sender.sendMessage("§eShould be added.");
                    break;
                case "rem":
                case "r":
                case "remove":
                    if (args.length < 2) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/att rem <name/clickType>");
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/att rem <name/clickType>");
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage("§cCannot help you with AIR.");
                        return true;
                    }

                    if (!itemHandler.getAttributes(item).containsKey(args[1].toLowerCase()) && !itemHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {
                        sender.sendMessage("§cThis does not have any of this set.");
                        sender.sendMessage("§cUsage: §7/att rem <name/clickType>");
                        return true;
                    }

                    if (itemHandler.getAttributes(item).containsKey(args[1].toLowerCase())) {
                        player.setItemInHand(itemHandler.removeAttribute(item, args[1].toLowerCase()));
                        sender.sendMessage("§eAttribute removed.");
                        return true;
                    } else if (itemHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {
                        player.setItemInHand(itemHandler.removeAttribute(args[1].toLowerCase(), item));
                        sender.sendMessage("§eAttribute removed.");
                        return true;
                    } else
                        sender.sendMessage("§cCould not remove attribute.");

                    break;
                case "clear":
                case "c":
                    if (args.length < 1) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/att clear");
                        return true;
                    }

                    if (args.length > 1) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/att clear");
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage("§cCannot help you with AIR.");
                        return true;
                    }

                    player.setItemInHand(itemHandler.clearAttributes(item));
                    sender.sendMessage("§eAttributes cleared.");
                    break;
                case "list":
                case "l":
                    if (args.length < 1) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/att list");
                        return true;
                    }

                    if (args.length > 1) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/att list");
                        return true;
                    }

                    plugin.cO.debug("Atts: " + attributeHandler.getAttributes().keySet());
                    sender.sendMessage("§eAttributes: §f" + Utils.listToString(new ArrayList<>(attributeHandler.getAttributes().keySet()), "§7, §f", "§cNo attributes saved."));
                    break;
                case "listhand":
                case "lh":
                case "listh":
                    if (args.length < 1) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/att listHand");
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage("§cCannot help you with AIR.");
                        return true;
                    }

                    if (args.length > 1) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/att listHand");
                        return true;
                    }

                    sender.sendMessage("§eAttributes:");
                    HashMap<String, String> attributes = itemHandler.getAttributes(item);
                    for (String key : itemHandler.getAttributes(item).keySet())
                        if (plugin.getActionNames().contains(key.toLowerCase()))
                            sender.sendMessage("§8- §7" + key + "§f:§7" + attributes.get(key));
                    break;
                case "help":
                case "h":
                default:
                    help(sender);
            }
        }
        return false;
    }

    private void help(CommandSender sender) {
        sender.sendMessage("§8§m----§r §eAttributes §8§m----" +
                "\n§e/att add <name> <clicktype> §8- §7Add attribute to an item." +
                "\n§e/att rem <name/clickType> §8- §7Remove attribute based on clickType/Name." +
                "\n§e/att clear §8- §7Clears attributes." +
                "\n§e/att list §8- §7Lists attributes from config." +
                "\n§e/att listHand §8- §7List attributes on item in your hand.");
    }
}
