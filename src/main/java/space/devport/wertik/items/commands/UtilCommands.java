package space.devport.wertik.items.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.itemutil.ItemBuilder;
import space.devport.utils.messageutil.StringUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.utils.Language;
import space.devport.wertik.items.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class UtilCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {

        if (!(sender instanceof Player)) {
            Language.ONLY_PLAYERS.getPrefixed().send(sender);
            return true;
        }

        if (!sender.hasPermission("items.control")) {
            Language.NO_PERMS.getPrefixed().send(sender);
            return true;
        }

        Player player = (Player) sender;

        final ItemStack item = player.getInventory().getItemInMainHand();
        final ItemBuilder builder = new ItemBuilder(item);

        if (item.getType() == Material.AIR) {
            Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "setname":
                builder.displayName(StringUtil.color(String.join(" ", args)));

                player.getInventory().setItemInMainHand(builder.build());

                Language.ITEM_RENAMED.sendPrefixed(sender);
                break;
            case "detail":
                sender.sendMessage(StringUtil.color("&eName: &f" + (builder.getDisplayName().isEmpty() ? item.getType().toString() : builder.getDisplayName().toString())));
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
            case "lore":
                if (builder.getLore().getMessage().isEmpty()) {
                    sender.sendMessage("&eLore: &cNo lore.");
                    return true;
                }

                sender.sendMessage(StringUtil.color("&eLore:"));
                int i = 0;
                for (String line : builder.getLore().getMessage()) {
                    sender.sendMessage(StringUtil.color("&f " + i + " &8- &r" + line));
                    i++;
                }
                break;
            case "addlore":
                if (args.length < 1) {
                    sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                    sender.sendMessage(StringUtil.color("&cUsage: &7/addlore <line>"));
                    return true;
                }

                builder.addLine(String.join(" ", args));

                player.getInventory().setItemInMainHand(builder.build());
                Language.LINE_ADDED.getPrefixed().send(sender);
                break;
            case "remlore":
                if (args.length < 1) {
                    sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                    sender.sendMessage(StringUtil.color("&cUsage: &7/remlore <lineIndex>"));
                    return true;
                }

                int index;
                try {
                    index = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(StringUtil.color("&cThat is not a number."));
                    return true;
                }

                ItemMeta meta = item.getItemMeta();

                if (meta == null || !meta.hasLore()) {
                    sender.sendMessage(StringUtil.color("&cThat item does not have a lore."));
                    return true;
                }

                List<String> lore = meta.getLore();

                if (lore != null)
                    lore.remove(index);

                meta.setLore(lore);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                Language.LINE_REMOVED.getPrefixed().send(sender);
                break;
            case "flags":
                List<String> flags = builder.getFlags().stream().map(ItemFlag::name).collect(Collectors.toList());
                sender.sendMessage(StringUtil.color("&eFlags: &f" + Utils.listToString(flags, "&7, &f", "&cNo flags.")));
                break;
            case "addflag":
                if (args.length < 1) {
                    sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                    sender.sendMessage(StringUtil.color("&cUsage: &7/addflag <itemFlag>"));
                    return true;
                }

                ItemFlag flag;

                try {
                    flag = ItemFlag.valueOf(args[0].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage(StringUtil.color("&cFlag is invalid."));
                    return true;
                }

                builder.addFlag(flag);

                player.getInventory().setItemInMainHand(builder.build());

                Language.FLAG_ADDED.getPrefixed().send(sender);
                break;
            case "remflag":
                if (args.length < 1) {
                    sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                    sender.sendMessage(StringUtil.color("&cUsage: &7/addflag <itemFlag>"));
                    return true;
                }

                try {
                    flag = ItemFlag.valueOf(args[0].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage(StringUtil.color("&cThat flag is invalid."));
                    return true;
                }

                if (!builder.getFlags().contains(flag)) {
                    Language.NO_FLAG.getPrefixed().send(sender);
                    return true;
                }

                builder.addFlag(flag);

                player.getInventory().setItemInMainHand(builder.build());
                break;
            case "enchs":
                sender.sendMessage(StringUtil.color("&eEnchants: &f" + Utils.mapToString(builder.getEnchants(), "&7, &f", "&f:", "&cNo enchants.")));
                break;
            case "addench":
                if (args.length < 2) {
                    sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                    sender.sendMessage(StringUtil.color("&cUsage: &7/addench <enchantment> <level>"));
                    return true;
                }

                int level;
                try {
                    level = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(StringUtil.color("&cThat is not a number."));
                    return true;
                }

                Enchantment enchantment = null;
                try {
                    enchantment = Enchantment.getByName(args[0].toUpperCase());
                } catch (IllegalArgumentException ignore) {
                }

                if (enchantment == null) {
                    Language.INVALID_ENCHANT.getPrefixed().send(sender);
                    return true;
                }

                builder.addEnchant(enchantment, level);

                player.getInventory().setItemInMainHand(builder.build());
                Language.ENCHANT_ADDED.getPrefixed().send(sender);
                break;
            case "remench":
                if (args.length < 1) {
                    sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                    sender.sendMessage(StringUtil.color("&cUsage: &7/remench <enchantment>"));
                    return true;
                }

                enchantment = null;

                try {
                    enchantment = Enchantment.getByName(args[0].toUpperCase());
                } catch (IllegalArgumentException ignore) {
                }

                if (enchantment == null) {
                    Language.INVALID_ENCHANT.getPrefixed().send(sender);
                    return true;
                }

                builder.removeEnchant(enchantment);

                player.getInventory().setItemInMainHand(builder.build());
                Language.ENCHANT_REMOVED.getPrefixed().send(sender);
                break;
            default:
        }
        return false;
    }
}