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
import space.devport.utils.itemutil.ItemBuilder;
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

        ItemStack item;
        ItemBuilder builder;

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
                    Language.ITEMS_LIST.getPrefixed()
                            .fill("%items%", Utils.listToString(new ArrayList<>(ItemsPlugin.getInstance().getItemHandler().getItems().keySet()), "&7, &f", "&cNo items saved."))
                            .send(sender);
                    break;
                case "remove":
                case "rem":
                case "r":
                    if (args.length < 2) {
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " remove <name>")
                                .send(sender);
                        return true;
                    }

                    if (args.length > 2) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " remove <name>")
                                .send(sender);
                        return true;
                    }

                    if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(args[1])) {
                        Language.ITEM_NOT_VALID.getPrefixed().fill("%item%", args[1]).send(sender);
                        return true;
                    }

                    ItemsPlugin.getInstance().getItemHandler().removeItem(args[1]);
                    Language.ITEM_REMOVED.getPrefixed().fill("%item%", args[1]).send(sender);
                    break;
                case "detail":
                case "d":
                    if (args.length < 2) {
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " detail <name>")
                                .send(sender);
                        return true;
                    }

                    if (args.length > 2) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " detail <name>")
                                .send(sender);
                        return true;
                    }

                    if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(args[1])) {
                        Language.ITEM_NOT_VALID.getPrefixed().fill("%item%", args[1]).send(sender);
                        return true;
                    }

                    builder = ItemsPlugin.getInstance().getItemHandler().getItem(args[1]);

                    sender.sendMessage(StringUtil.color("&eName: &f" + (builder.getDisplayName().isEmpty() ? builder.getMaterial().toString() : builder.getDisplayName().toString())));
                    sender.sendMessage(StringUtil.color("&eMaterial: &f" + builder.getMaterial().name()));
                    sender.sendMessage(StringUtil.color("&eAmount: &f" + builder.getAmount()));

                    // Lore
                    if (!builder.getLore().getMessage().isEmpty()) {
                        sender.sendMessage(StringUtil.color("&eLore:"));
                        int i = 0;
                        for (String line : builder.getLore().getMessage()) {
                            sender.sendMessage(StringUtil.color("&f " + i + " &8- &r" + line));
                            i++;
                        }
                    }

                    // Enchants
                    if (!builder.getEnchants().isEmpty()) {
                        sender.sendMessage(StringUtil.color("&eEnchants:"));
                        builder.getEnchants().forEach((enchantment, level) -> sender.sendMessage(StringUtil.color(" &8- &7" + enchantment.toString() + "&f;&7" + level)));
                    }

                    // Flags
                    if (!builder.getFlags().isEmpty()) {
                        sender.sendMessage(StringUtil.color("&eFlags:"));
                        builder.getFlags().forEach(flag -> sender.sendMessage(StringUtil.color(" &8- &7" + flag.toString())));
                    }

                    // NBT
                    if (!builder.getNBT().isEmpty()) {
                        sender.sendMessage(StringUtil.color("&eNBT:"));

                        for (String key : builder.getNBT().keySet()) {
                            if (!ItemsPlugin.getInstance().getFilteredNBT().contains(key))
                                sender.sendMessage(StringUtil.color(" &8- &7" + key + "&f:&7" + builder.getNBT().get(key)));
                        }
                    }
                    break;
                case "drop":
                    if (args.length < 3) {
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " drop <name> <worldName;x;y;z> (amount)")
                                .send(sender);
                        return true;
                    }

                    if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(args[1])) {
                        Language.ITEM_NOT_VALID.getPrefixed().fill("%item%", args[1]).send(sender);
                        return true;
                    }

                    int amount = 1;

                    if (args.length == 4) {
                        try {
                            amount = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            Language.NOT_A_NUMBER.sendPrefixed(sender);
                            return true;
                        }
                    }

                    String[] locationString = args[2].split(";");

                    for (int i = 1; i < locationString.length; i++) {
                        try {
                            Double.parseDouble(locationString[i]);
                        } catch (NumberFormatException e) {
                            Language.NOT_A_NUMBER.sendPrefixed(sender);
                            return true;
                        }
                    }

                    ItemStack giveItem = ItemsPlugin.getInstance().getItemHandler().getItem(args[1]).build();

                    // Update unstackable property
                    if (ItemsPlugin.getInstance().getItemHandler().isUnstackable(giveItem))
                        giveItem = ItemsPlugin.getInstance().getItemHandler().setUnstackable(giveItem, true);

                    giveItem.setAmount(amount);

                    Location location = new Location(ItemsPlugin.getInstance().getServer().getWorld(locationString[0]),
                            Double.parseDouble(locationString[1]),
                            Double.parseDouble(locationString[2]),
                            Double.parseDouble(locationString[3]));

                    if (location.getWorld() == null) {
                        // World not loaded or invalid
                        return true;
                    }

                    location.getWorld().dropItemNaturally(location, giveItem);

                    Language.ITEM_SPAWNED_AT.getPrefixed()
                            .fill("%item%", args[1])
                            .fill("%amount%", String.valueOf(amount))
                            .fill("%location%", LocationUtil.locationToString(location, ", "))
                            .send(sender);
                    break;
                case "give":
                    if (args.length < 2) {
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " give <name> (playerName) (amount)")
                                .send(sender);
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
                            Language.NOT_A_NUMBER.getPrefixed()
                                    .fill("%param%", args[3])
                                    .send(sender);
                            return true;
                        }

                    for (int i = 0; i < amount; i++) {
                        giveItem = ItemsPlugin.getInstance().getItemHandler().getItem(args[1]).build();

                        // Update unstackable property
                        if (ItemsPlugin.getInstance().getItemHandler().isUnstackable(giveItem))
                            giveItem = ItemsPlugin.getInstance().getItemHandler().setUnstackable(giveItem, true);

                        target.getInventory().addItem(giveItem);
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
                        if (args.length > 2) {
                            Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                    .fill("%usage%", "/" + label + " save (name)")
                                    .send(sender);
                            return true;
                        }

                        if (!(sender instanceof Player)) {
                            Language.ONLY_PLAYERS.sendPrefixed(sender);
                            return true;
                        }

                        Player player = (Player) sender;

                        if (Utils.getItem(player).getType().equals(Material.AIR)) {
                            Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
                            return true;
                        }

                        if (ItemsPlugin.getInstance().getItemHandler().getItem(args[1]) == null)
                            Language.ITEM_SAVED.getPrefixed().fill("%item%", args[1]).send(sender);
                        else
                            Language.ITEM_UPDATED.getPrefixed().fill("%item%", args[1]).send(sender);

                        ItemsPlugin.getInstance().getItemHandler().addItem(args[1], Utils.getItem(player));
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
        Language.ITEMS_HELP.get().fill("%label%", label).send(sender);
    }
}