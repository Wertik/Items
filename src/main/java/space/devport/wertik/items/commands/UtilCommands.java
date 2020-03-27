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
import space.devport.utils.itemutil.ItemNBTEditor;
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

        if (item.getType() == Material.AIR) {
            Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "setname":
                StringBuilder name = new StringBuilder();

                for (String arg : args) {
                    name.append(arg).append(" ");
                }

                ItemBuilder builder = new ItemBuilder(item);

                builder.displayName(StringUtil.color(name.toString()));

                player.getInventory().setItemInMainHand(builder.build());

                player.sendMessage(StringUtil.color("&eRenamed."));
                break;
            case "detail":
                sender.sendMessage(StringUtil.color("&eName: &f" + item.getItemMeta().getDisplayName()));
                sender.sendMessage(StringUtil.color("&eMaterial: &f" + item.getType().toString()));
                sender.sendMessage(StringUtil.color("&eAmount: &f" + item.getAmount()));

                if (item.getItemMeta().hasLore()) {
                    sender.sendMessage(StringUtil.color("&eLore:"));
                    int i = 0;
                    for (String line : item.getItemMeta().getLore()) {
                        sender.sendMessage(StringUtil.color("&f " + i + "&8- &r" + line));
                        i++;
                    }
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
            case "addlore":
                if (args.length < 1) {
                    sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                    sender.sendMessage(StringUtil.color("&cUsage: &7/addlore <line>"));
                    return true;
                }

                builder = new ItemBuilder(item);

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

                builder = new ItemBuilder(player.getInventory().getItemInMainHand());

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

                builder = new ItemBuilder(player.getInventory().getItemInMainHand());

                if (!builder.getFlags().contains(flag)) {
                    Language.NO_FLAG.getPrefixed().send(sender);
                    return true;
                }

                builder.addFlag(flag);

                player.getInventory().setItemInMainHand(builder.build());
                break;
            case "flags":
                builder = new ItemBuilder(item);
                List<String> flags = builder.getFlags().stream().map(ItemFlag::name).collect(Collectors.toList());
                sender.sendMessage(StringUtil.color("&eFlags: &f" + Utils.listToString(flags, "&7, &f", "&cNo flags.")));
                break;
            case "lore":
                builder = new ItemBuilder(item);

                sender.sendMessage(StringUtil.color("&eLore:"));
                int i = 0;
                for (String line : builder.getLore().getMessage()) {
                    sender.sendMessage(StringUtil.color("&f " + i + " &8- &r" + line));
                    i++;
                }
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

                builder = new ItemBuilder(player.getInventory().getItemInMainHand())
                        .addEnchant(enchantment, level);

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

                builder = new ItemBuilder(player.getInventory().getItemInMainHand())
                        .removeEnchant(enchantment);

                player.getInventory().setItemInMainHand(builder.build());
                Language.ENCHANT_REMOVED.getPrefixed().send(sender);
                break;
            case "enchs":
                builder = new ItemBuilder(player.getInventory().getItemInMainHand());
                sender.sendMessage(StringUtil.color("&eEnchants: &f" + Utils.mapToString(builder.getEnchants(), "&7, &f", "&f:", "&cNo enchants.")));
                break;
        }
        return false;
    }
}