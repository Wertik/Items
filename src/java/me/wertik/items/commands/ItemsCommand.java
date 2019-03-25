package me.wertik.items.commands;

import me.wertik.items.Main;
import me.wertik.items.utils.NBTEditor;
import me.wertik.items.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemsCommand implements CommandExecutor {

    /*Commands:
     * /setname <coloredName>
     * /addlore <coloredLine>
     * /remlore <line>
     * /detail (with NBT and values)
     * /i add <name>
     * /i get <name>
     * /i remove <name>
     * /i detail <name>
     * /att add/rem/clear (attributeType) (clickType) (cooldown)
     * /att list
     * */

    private Main plugin;

    public ItemsCommand() {
        plugin = Main.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            help(sender);
            return true;
        } else if (args.length > 4) {
            help(sender);
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "list":
                case "l":
                    sender.sendMessage("§eItems: §f" + Utils.listToString(new ArrayList<>(plugin.getItemHandler().getItems().keySet()), "§7, §f", "§cNo items saved."));
                    break;
                case "add":
                case "a":
                    if (args.length < 2) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i add <name>");
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/i add <name>");
                        return true;
                    }

                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cYou have to be a player.");
                        return true;
                    }

                    Player player = (Player) sender;

                    if (player.getItemInHand().getType().equals(Material.AIR)) {
                        sender.sendMessage("§cCannot add air.");
                        return true;
                    }

                    plugin.getItemHandler().addItem(args[1], player.getItemInHand());
                    sender.sendMessage("§eAdded!");
                    break;
                case "get":
                    if (args.length < 2) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i get <name>");
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/i get <name>");
                        return true;
                    }

                    if (!plugin.getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage("§cThat item is not saved.");
                        return true;
                    }

                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cYou have to be a player.");
                        return true;
                    }

                    player = (Player) sender;

                    player.getInventory().addItem(plugin.getItemHandler().getItem(args[1]));
                    sender.sendMessage("§eGiven!");
                    break;
                case "remove":
                case "rem":
                case "r":
                    if (args.length < 2) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i rem <name>");
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/i rem <name>");
                        return true;
                    }

                    if (!plugin.getItemHandler().getItems().keySet().contains(args[1])) {
                        sender.sendMessage("§cThsi item is not saved.");
                        return true;
                    }

                    plugin.getItemHandler().remItem(args[1]);
                    sender.sendMessage("§eRemoved!");
                    break;
                case "detail":
                case "d":
                    if (args.length < 2) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i detail <name>");
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/i detail <name>");
                        return true;
                    }

                    if (!plugin.getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage("§cThat item is not saved.");
                        return true;
                    }

                    ItemStack item = plugin.getItemHandler().getItem(args[1]);

                    sender.sendMessage("§eName: §f" + item.getItemMeta().getDisplayName());
                    sender.sendMessage("§eMaterial: §f" + item.getType().toString());
                    sender.sendMessage("§eAmount: §f" + item.getAmount());
                    if (item.getItemMeta().hasLore()) {
                        sender.sendMessage("§eLore:");
                        item.getItemMeta().getLore().forEach(line -> sender.sendMessage("§8- §r" + line));
                    }
                    if (item.getItemMeta().hasEnchants()) {
                        sender.sendMessage("§eEnchants:");
                        item.getEnchantments().keySet().forEach(ench -> sender.sendMessage("§8- §7" + ench.getName() + "§f;§7" + item.getItemMeta().getEnchantLevel(ench)));
                    }
                    if (!item.getItemMeta().getItemFlags().isEmpty()) {
                        sender.sendMessage("§eFlags:");
                        item.getItemMeta().getItemFlags().forEach(flag -> sender.sendMessage("§8- §7" + flag.name()));
                    }
                    // NBT!
                    if (NBTEditor.hasNBT(item)) {
                        sender.sendMessage("§eNBT:");
                        NBTEditor.getNBTTags(item).forEach(tag -> sender.sendMessage("§8- §7" + tag + "§f:§7" + NBTEditor.getNBT(item, tag)));
                    }
                    break;
                case "drop":
                    // /i drop <name> <worldName;x;y;z> (amount)
                    if (args.length < 3) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i drop <name> <worldName;x;y;z> (amount)");
                        return true;
                    }

                    if (args.length > 4) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/i drop <name> <worldName;x;y;z> (amount)");
                        return true;
                    }

                    if (!plugin.getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage("§cThat item is not saved.");
                        return true;
                    }

                    int amount = 1;

                    if (args.length == 4) {
                        try {
                            amount = Integer.valueOf(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§cAmount should be a number.");
                            return true;
                        }
                    }

                    String[] locationString = args[2].split(";");

                    for (int i = 1; i < locationString.length; i++) {
                        try {
                            Integer.valueOf(locationString[i]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§cCoordinates have to be provided in integers.");
                            return true;
                        }
                    }

                    for (int i = 0; i < amount; i++) {
                        if (plugin.getConfigLoader().getConfig().getBoolean("drop-naturally"))
                            plugin.getServer().getWorld(locationString[0]).dropItemNaturally(new Location(plugin.getServer().getWorld(locationString[0]), Integer.valueOf(locationString[1]), Integer.valueOf(locationString[2]), Integer.valueOf(locationString[3])), plugin.getItemHandler().getItem(args[1]));
                        else
                            plugin.getServer().getWorld(locationString[0]).dropItem(new Location(plugin.getServer().getWorld(locationString[0]), Integer.valueOf(locationString[1]), Integer.valueOf(locationString[2]), Integer.valueOf(locationString[3])), plugin.getItemHandler().getItem(args[1]));
                    }
                    sender.sendMessage("§eItem spawned.");
                    break;
                case "give":
                    if (args.length < 3) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i give <name> <playerName> (amount)");
                        return true;
                    }

                    if (args.length > 4) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/i give <name> <playerName> (amount)");
                        return true;
                    }

                    if (!plugin.getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage("§cThat item is not saved.");
                        return true;
                    }

                    try {
                        if (!plugin.getServer().getPlayerExact(args[2]).isOnline()) {
                            sender.sendMessage("§cThat player is not online.");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage("§cThat player is not online.");
                        return true;

                    }

                    Player target = plugin.getServer().getPlayerExact(args[2]);

                    amount = 1;

                    if (args.length == 4) {
                        try {
                            amount = Integer.valueOf(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§cThat is not a number.");
                            return true;
                        }
                    }

                    for (int i = 0; i < amount; i++) {
                        target.getInventory().addItem(plugin.getItemHandler().getItem(args[1]));
                    }

                    if (!plugin.getConfigLoader().getConfig().getBoolean("silent-give"))
                        target.sendMessage("§eYou received an item.");
                    sender.sendMessage("§eItem given.");
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
        sender.sendMessage("§8§m----§e Items §8§m----" +
                "\n§e/i add <name> §8- §7Saves item in hand." +
                "\n§e/i remove <name> §8- §7Removes item." +
                "\n§e/i list §8- §7Lists saved items." +
                "\n§e/i get <name> §8- §7Gets you saved item." +
                "\n§e/i detail <name> §8- §7Displays info about saved item." +
                "\n§e/i drop <name> <worldName;x;y;z> (amount) §8- §7Drop item somewhere in the world." +
                "\n§e/i give <name> <playerName> §8- §7Give player an item." +
                "\n§e/att help §8- §7Help page regarding attributes." + "" +
                "\n§e/setname <name> §8- §7Set display name of an item." +
                "\n§e/lore §8- §7List item lore." +
                "\n§e/addlore <line> §8- §7Add a line of lore." +
                "\n§e/remlore <lineINdex> §8- §7Remove a line from item lore." +
                "\n§e/flags §8- §7Display flags on an item." +
                "\n§e/addflag <flagName> §8- §7Add itemFlag to an item." +
                "\n§e/remflag <flagName> §8- §7Remove itemFlag from an item." +
                "\n§e/enchs");
    }
}
