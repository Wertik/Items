package space.devport.wertik.items.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.utils.NBTEditor;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;

public class ItemsCommand implements CommandExecutor {

    private Main plugin;

    public ItemsCommand() {
        plugin = Main.inst;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("items.control")) {
            sender.sendMessage("&cYou don't have permission to do this.");
            return true;
        }

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
                        sender.sendMessage(Main.inst.cO.getPrefix() + "§cYou have to be a player.");
                        return true;
                    }

                    Player player = (Player) sender;

                    if (player.getItemInHand().getType().equals(Material.AIR)) {
                        sender.sendMessage(Main.inst.cO.getPrefix() + "§cCannot add air.");
                        return true;
                    }

                    plugin.getItemHandler().addItem(args[1], player.getItemInHand());
                    sender.sendMessage(Main.inst.cO.getPrefix() + "§eAdded item under name §f" + args[1]);
                    break;
                case "get":
                    if (args.length < 2) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i get <name> [amount]");
                        return true;
                    }

                    if (args.length > 3) {
                        sender.sendMessage("§cToo many arguments.");
                        sender.sendMessage("§cUsage: §7/i get <name> [amount]");
                        return true;
                    }

                    if (!plugin.getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage(Main.inst.cO.getPrefix() + "§cThat item is not saved.");
                        return true;
                    }

                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Main.inst.cO.getPrefix() + "§cYou have to be a player.");
                        return true;
                    }

                    int amount = 1;

                    if (args.length > 2)
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Main.inst.cO.getPrefix() + "§cAmount has to be a number.");
                        }

                    player = (Player) sender;

                    ItemStack item = plugin.getItemHandler().getItem(args[1]);
                    item.setAmount(amount);
                    player.getInventory().addItem(item);

                    sender.sendMessage(Main.inst.cO.getPrefix() + "§eItem §f" + args[1] + "§7x" + amount + " §eadded to your inventory.");
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

                    if (!plugin.getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage(Main.inst.cO.getPrefix() + "§cThsi item is not saved.");
                        return true;
                    }

                    plugin.getItemHandler().removeItem(args[1]);
                    sender.sendMessage(Main.inst.cO.getPrefix() + "§eRemoved item §f" + args[1]);
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
                        sender.sendMessage(Main.inst.cO.getPrefix() + "§cThat item is not saved.");
                        return true;
                    }

                    item = plugin.getItemHandler().getItem(args[1]);

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
                        NBTEditor.getNBTKeys(item).forEach(tag -> sender.sendMessage("§8- §7" + tag + "§f:§7" + NBTEditor.getNBT(item, tag)));
                    }
                    break;
                case "drop":
                    // /i drop <name> <worldName;x;y;z> (amount)
                    if (args.length < 3) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§cUsage: §7/i drop <name> <worldName;x;y;z> [amount]");
                        return true;
                    }

                    if (!plugin.getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage(Main.inst.cO.getPrefix() + "§cThat item is not saved.");
                        return true;
                    }

                    amount = 1;

                    if (args.length == 4) {
                        try {
                            amount = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Main.inst.cO.getPrefix() + "§cAmount should be a number.");
                            return true;
                        }
                    }

                    String[] locationString = args[2].split(";");

                    for (int i = 1; i < locationString.length; i++) {
                        try {
                            Integer.valueOf(locationString[i]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Main.inst.cO.getPrefix() + "§cCoordinates have to be provided in integers.");
                            return true;
                        }
                    }

                    item = plugin.getItemHandler().getItem(args[1]);

                    item.setAmount(amount);

                    Location location = new Location(plugin.getServer().getWorld(locationString[0]), Integer.parseInt(locationString[1]), Integer.parseInt(locationString[2]), Integer.parseInt(locationString[3]));

                    if (plugin.getConfig().getBoolean("drop-naturally"))
                        plugin.getServer().getWorld(locationString[0]).dropItemNaturally(location, item);
                    else
                        plugin.getServer().getWorld(locationString[0]).dropItem(location, item);
                    sender.sendMessage(Main.inst.cO.getPrefix() + "§eItem §f" + args[1] + "§7x" + amount + " §espawned §8@§7" + Utils.locationToString(location));
                    break;
                case "give":
                    if (args.length < 3) {
                        sender.sendMessage("§cNot enough arguments.");
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
                        sender.sendMessage("§cThat player is not online.");
                        return true;

                    }

                    Player target = plugin.getServer().getPlayerExact(args[2]);

                    amount = 1;

                    if (args.length == 4)
                        try {
                            amount = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§cThat is not a number.");
                            return true;
                        }

                    for (int i = 0; i < amount; i++) {
                        target.getInventory().addItem(plugin.getItemHandler().getItem(args[1]));
                    }

                    sender.sendMessage(Main.inst.cO.getPrefix() + "§eGave player §f" + target.getName() + " item §f" + args[1] + "§7x" + amount);
                    break;
                case "reload":
                    Main.inst.reload(sender);
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
        sender.sendMessage("§8§m--------§e Items §8§m--------" +
                "\n§e/i add <name> §8- §7Saves item in hand to db under given name." +
                "\n§e/i remove <name> §8- §7Removes item by name." +
                "\n§e/i list §8- §7Lists saved items." +
                "\n§e/i get <name> [amount] §8- §7Adds a saved item into your inventory." +
                "\n§e/i detail <name> §8- §7Displays info about an item in the db." +
                "\n§e/i drop <name> <worldName;x;y;z> (amount) §8- §7Drops item on a given location." +
                "\n§e/i give <name> <playerName> [amount] §8- §7Give player an item." +
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