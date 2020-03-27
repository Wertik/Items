package space.devport.wertik.items.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.itemutil.ItemNBTEditor;
import space.devport.utils.messageutil.StringUtil;
import space.devport.utils.regionutil.LocationUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.utils.Language;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;

public class ItemsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!sender.hasPermission("items.control")) {
            Language.NO_PERMS.getPrefixed().send(sender);
            return true;
        }

        if (args.length < 1) {
            help(sender, label);
            return true;
        } else if (args.length > 4) {
            help(sender, label);
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "list":
                case "l":
                    sender.sendMessage(StringUtil.color("&eItems: &f" +
                            Utils.listToString(new ArrayList<>(ItemsPlugin.getInstance().getItemHandler().getItems().keySet()), "&7, &f", "&cNo items saved.")));
                    break;
                case "add":
                case "a":
                    if (args.length < 2) {
                        sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i add <name>"));
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i add <name>"));
                        return true;
                    }

                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&cYou have to be a player."));
                        return true;
                    }

                    Player player = (Player) sender;

                    if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&cCannot add air."));
                        return true;
                    }

                    ItemsPlugin.getInstance().getItemHandler().addItem(args[1], player.getInventory().getItemInMainHand());
                    sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&eAdded item under name &f" + args[1]));
                    break;
                case "remove":
                case "rem":
                case "r":
                    if (args.length < 2) {
                        sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i rem <name>"));
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i rem <name>"));
                        return true;
                    }

                    if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&cThsi item is not saved."));
                        return true;
                    }

                    ItemsPlugin.getInstance().getItemHandler().removeItem(args[1]);
                    sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&eRemoved item &f" + args[1]));
                    break;
                case "detail":
                case "d":
                    if (args.length < 2) {
                        sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i detail <name>"));
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i detail <name>"));
                        return true;
                    }

                    if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&cThat item is not saved."));
                        return true;
                    }

                    ItemStack item = ItemsPlugin.getInstance().getItemHandler().getItem(args[1]).build();

                    sender.sendMessage(StringUtil.color("&eName: &f" + item.getItemMeta().getDisplayName()));
                    sender.sendMessage(StringUtil.color("&eMaterial: &f" + item.getType().toString()));
                    sender.sendMessage(StringUtil.color("&eAmount: &f" + item.getAmount()));

                    if (item.getItemMeta().hasLore()) {
                        sender.sendMessage(StringUtil.color("&eLore:"));
                        item.getItemMeta().getLore().forEach(line -> sender.sendMessage(StringUtil.color("&8- &r" + line)));
                    }

                    if (item.getItemMeta().hasEnchants()) {
                        sender.sendMessage(StringUtil.color("&eEnchants:"));
                        item.getEnchantments().keySet().forEach(ench -> sender.sendMessage(StringUtil.color("&8- &7" + ench.getName() + "&f;&7" + item.getItemMeta().getEnchantLevel(ench))));
                    }

                    if (!item.getItemMeta().getItemFlags().isEmpty()) {
                        sender.sendMessage(StringUtil.color("&eFlags:"));
                        item.getItemMeta().getItemFlags().forEach(flag -> sender.sendMessage(StringUtil.color("&8- &7" + flag.name())));
                    }

                    // NBT
                    if (ItemNBTEditor.hasNBT(item)) {
                        sender.sendMessage(StringUtil.color("&eNBT:"));

                        for (String tag : ItemNBTEditor.getNBTTagMap(item).keySet()) {
                            if (!ItemsPlugin.getInstance().getFilteredNBT().contains(tag))
                                sender.sendMessage(StringUtil.color("&8- &7" + tag + "&f:&7" + ItemNBTEditor.getNBT(item, tag)));
                        }
                    }
                    break;
                case "drop":
                    // /i drop <name> <worldName;x;y;z> (amount)
                    if (args.length < 3) {
                        sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i drop <name> <worldName;x;y;z> [amount]"));
                        return true;
                    }

                    if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(args[1])) {
                        sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&cThat item is not saved."));
                        return true;
                    }

                    int amount = 1;

                    if (args.length == 4) {
                        try {
                            amount = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&cAmount should be a number."));
                            return true;
                        }
                    }

                    String[] locationString = args[2].split(";");

                    for (int i = 1; i < locationString.length; i++) {
                        try {
                            Double.parseDouble(locationString[i]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + StringUtil.color("&cCoordinates have to be provided in numbers."));
                            return true;
                        }
                    }

                    item = ItemsPlugin.getInstance().getItemHandler().getItem(args[1]).build();

                    item.setAmount(amount);

                    Location location = new Location(ItemsPlugin.getInstance().getServer().getWorld(locationString[0]),
                            Double.parseDouble(locationString[1]),
                            Double.parseDouble(locationString[2]),
                            Double.parseDouble(locationString[3]));

                    if (location.getWorld() == null) {
                        // World not loaded or invalid
                        return true;
                    }

                    location.getWorld().dropItemNaturally(location, item);

                    Language.SPAWNED_AT.getPrefixed()
                            .fill("%item%", args[1])
                            .fill("%amount%", String.valueOf(amount))
                            .fill("%location%", LocationUtil.locationToString(location, ", "))
                            .send(sender);
                    break;
                case "give":
                    if (args.length < 2) {
                        sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/i give <name> (playerName) (amount)"));
                        return true;
                    }

                    if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(args[1])) {
                        Language.ITEM_NOT_VALID.getPrefixed().fill("%item%", args[1]).send(sender);
                        return true;
                    }

                    Player target;
                    if (args.length == 3) {
                        OfflinePlayer offlineTarget = Bukkit.getPlayer(args[2]);

                        if (offlineTarget == null || !offlineTarget.isOnline() || offlineTarget.getPlayer() == null) {
                            Language.PLAYER_OFFLINE.getPrefixed().send(sender);
                            return true;
                        }

                        target = offlineTarget.getPlayer();
                    } else {
                        if (!(sender instanceof Player)) {
                            Language.ONLY_PLAYERS.sendPrefixed(sender);
                            return true;
                        }

                        target = (Player) sender;
                    }

                    amount = 1;

                    if (args.length == 4)
                        try {
                            amount = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(StringUtil.color("&cThat is not a number."));
                            return true;
                        }

                    for (int i = 0; i < amount; i++) {
                        target.getInventory().addItem(ItemsPlugin.getInstance().getItemHandler().getItem(args[1]).build());
                    }

                    Language.ITEM_GIVEN.getPrefixed()
                            .fill("%item%", args[1])
                            .fill("%player%", target.getName())
                            .fill("%amount%", "" + amount)
                            .send(sender);
                    break;
                case "reload":
                    ItemsPlugin.getInstance().reload(sender);
                    break;
                case "load":
                    if (args.length > 1) {
                        if (!ItemsPlugin.getInstance().getItemHandler().checkItemStorage(args[1])) {
                            Language.ITEM_NOT_VALID.getPrefixed().fill("%item%", args[1]).send(sender);
                            return true;
                        }

                        ItemsPlugin.getInstance().getItemHandler().loadItem(args[1]);
                        Language.ITEM_LOADED.getPrefixed().fill("%item%", args[1]).send(sender);
                        return true;
                    }

                    ItemsPlugin.getInstance().getItemHandler().loadItems();
                    Language.ITEMS_LOADED.getPrefixed().send(sender);
                    break;
                case "save":
                    if (args.length > 1) {
                        if (ItemsPlugin.getInstance().getItemHandler().getItem(args[1]) == null) {
                            Language.ITEM_NOT_VALID.getPrefixed().fill("%item%", args[1]).send(sender);
                            return true;
                        }

                        ItemsPlugin.getInstance().getItemHandler().saveItem(args[1]);
                        Language.ITEM_SAVED.getPrefixed().fill("%item%", args[1]).send(sender);
                        return true;
                    }
                    ItemsPlugin.getInstance().getItemHandler().saveItems();
                    Language.ITEMS_SAVED.getPrefixed().send(sender);
                    break;
                case "help":
                case "h":
                default:
                    help(sender, label);
            }
        }
        return false;
    }

    private void help(CommandSender sender, String label) {
        sender.sendMessage(StringUtil.color("&8&m--------&e Items &8&m--------" +
                "\n&e/" + label + " save [item] &8- &7Save all items, or by name." +
                "\n&e/" + label + " load [item] &8- &7Load all items, or by name." +
                "\n&e/" + label + " add <name> &8- &7Saves item in hand to db under given name." +
                "\n&e/" + label + " remove <name> &8- &7Removes item by name." +
                "\n&e/" + label + " list &8- &7Lists saved items." +
                "\n&e/" + label + " detail <name> &8- &7Displays info about an item in the db." +
                "\n&e/" + label + " drop <name> <worldName;x;y;z> (amount) &8- &7Drops item on a given location." +
                "\n&e/" + label + " give <name> (playerName) (amount) &8- &7Give player an item." +
                "\n&e/att help &8- &7Help page regarding attributes." +
                "\n&e/setname <name> &8- &7Set display name of an item." +
                "\n&e/lore &8- &7List item lore." +
                "\n&e/addlore <line> &8- &7Add a line of lore." +
                "\n&e/remlore <lineINdex> &8- &7Remove a line from item lore." +
                "\n&e/flags &8- &7Display flags on an item." +
                "\n&e/addflag <flagName> &8- &7Add itemFlag to an item." +
                "\n&e/remflag <flagName> &8- &7Remove itemFlag from an item." +
                "\n&e/enchs &8- &7List enchantments on item." +
                "\n&e/addench <enchantment> <level> &8- &7Add enchant to item." +
                "\n&e/remench <enchantment> &8- &7Remove enchantment from item."));
    }
}