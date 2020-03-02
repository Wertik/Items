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
import space.devport.utils.itemutil.ItemNBTEditor;
import space.devport.utils.messageutil.StringUtil;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class UtilCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StringUtil.color("&cOnly for players."));
            return true;
        }

        if (!sender.hasPermission("items.control")) {
            sender.sendMessage(StringUtil.color("&cYou don't have permission to do this."));
            return true;
        }

        Player player = (Player) sender;

        if (player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
            sender.sendMessage(StringUtil.color("&cHow can I help you with AIR. lol."));
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("setname")) {
            String name = "";
            for (String arg : args) {
                name = name + arg + " ";
            }
            name = name.trim();
            ItemStack item = player.getItemInHand();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.color(name));
            item.setItemMeta(meta);
            player.setItemInHand(item);
            player.sendMessage(StringUtil.color("&eRenamed."));
        } else if (cmd.getName().equalsIgnoreCase("detail")) {
            ItemStack item = ((Player) sender).getItemInHand();

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
            // NBT!
            if (ItemNBTEditor.hasNBT(item)) {
                sender.sendMessage(StringUtil.color("&eNBT:"));
                for (String tag : ItemNBTEditor.getNBTTagMap(item).keySet()) {
                    sender.sendMessage(StringUtil.color("&8- &7" + tag + "&f:&7" + ItemNBTEditor.getNBT(item, tag)));
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("addlore")) {
            if (args.length < 1) {
                sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                sender.sendMessage(StringUtil.color("&cUsage: &7/addlore <line>"));
                return true;
            }
            String line = "";
            for (String arg : args) {
                line = line + " " + arg;
            }
            line = line.trim();
            ItemStack item = player.getItemInHand();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (meta.hasLore())
                lore = meta.getLore();
            lore.add(Utils.color(line));
            meta.setLore(lore);
            item.setItemMeta(meta);
            player.setItemInHand(item);
        } else if (cmd.getName().equalsIgnoreCase("remlore")) {
            if (args.length < 1) {
                sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                sender.sendMessage(StringUtil.color("&cUsage: &7/remlore <lineIndex>"));
                return true;
            }

            int index;
            try {
                index = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(StringUtil.color("&cThat is not a number."));
                return true;
            }

            ItemStack item = player.getItemInHand();
            ItemMeta meta = item.getItemMeta();
            if (!meta.hasLore()) {
                sender.sendMessage(StringUtil.color("&cThat item does not have a lore."));
                return true;
            }
            List<String> lore = meta.getLore();
            lore.remove(index);
            meta.setLore(lore);
            item.setItemMeta(meta);
            player.setItemInHand(item);
        } else if (cmd.getName().equalsIgnoreCase("addflag")) {
            if (args.length < 1) {
                sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                sender.sendMessage(StringUtil.color("&cUsage: &7/addflag <itemFlag>"));
                return true;
            }
            try {
                ItemFlag.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(StringUtil.color("&cThat flag is invalid."));
                return true;
            }

            ItemStack item = player.getItemInHand();
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.valueOf(args[0].toUpperCase()));
            item.setItemMeta(meta);
            player.setItemInHand(item);
        } else if (cmd.getName().equalsIgnoreCase("remflag")) {
            if (args.length < 1) {
                sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                sender.sendMessage(StringUtil.color("&cUsage: &7/addflag <itemFlag>"));
                return true;
            }
            try {
                ItemFlag.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(StringUtil.color("&cThat flag is invalid."));
                return true;
            }

            ItemStack item = player.getItemInHand();
            ItemMeta meta = item.getItemMeta();
            meta.removeItemFlags(ItemFlag.valueOf(args[0].toUpperCase()));
            item.setItemMeta(meta);
            player.setItemInHand(item);
        } else if (cmd.getName().equalsIgnoreCase("flags")) {
            ItemStack item = player.getItemInHand();
            List<String> flags = new ArrayList<>();
            item.getItemMeta().getItemFlags().forEach(flag -> flags.add(flag.name()));
            sender.sendMessage(StringUtil.color("&eFlags: &f" + Utils.listToString(flags, "&7, &f", "&cNo flags.")));
        } else if (cmd.getName().equalsIgnoreCase("lore")) {
            ItemStack item = player.getItemInHand();
            sender.sendMessage(StringUtil.color(StringUtil.color("&eLore: &f" + Utils.listToString(item.getItemMeta().getLore(), "&7, &f", "&cNo lore."))));
        } else if (cmd.getName().equalsIgnoreCase("addench")) {
            ItemStack item = player.getItemInHand();
            if (args.length < 2) {
                sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                sender.sendMessage(StringUtil.color("&cUsage: &7/addench <enchantment> <level>"));
                return true;
            }

            int level;
            try {
                level = Integer.valueOf(args[1].toUpperCase());
            } catch (NumberFormatException e) {
                sender.sendMessage(StringUtil.color("&cThat is not a number."));
                return true;
            }

            Enchantment enchantment;
            try {
                enchantment = Enchantment.getByName(args[0].toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(StringUtil.color("&cThat's not a valid enchantment."));
                return true;
            }

            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(enchantment, level, true);
            item.setItemMeta(meta);
            player.setItemInHand(item);
        } else if (cmd.getName().equalsIgnoreCase("remench")) {
            ItemStack item = player.getItemInHand();
            if (args.length < 1) {
                sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                sender.sendMessage(StringUtil.color("&cUsage: &7/remench <enchantment>"));
                return true;
            }

            Enchantment enchantment;
            try {
                enchantment = Enchantment.getByName(args[0].toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(StringUtil.color("&cThat's not a valid enchantment."));
                return true;
            }

            ItemMeta meta = item.getItemMeta();
            meta.removeEnchant(enchantment);
            item.setItemMeta(meta);
            player.setItemInHand(item);
        } else if (cmd.getName().equalsIgnoreCase("enchs")) {
            ItemStack item = player.getItemInHand();
            sender.sendMessage(StringUtil.color("&eEnchants: &f" + Utils.mapToString(item.getItemMeta().getEnchants(), "&7, &f", "&f:", "&cNo enchants.")));
        }

        return false;
    }
}