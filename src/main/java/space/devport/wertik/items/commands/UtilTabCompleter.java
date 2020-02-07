package space.devport.wertik.items.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtilTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        switch (cmd.getName().toLowerCase()) {
            case "addflag":
                if (args.length == 1) {
                    List<String> flags = new ArrayList<>();

                    if (!args[0].equals("")) {
                        for (ItemFlag flag : ItemFlag.values()) {
                            if (flag.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                                flags.add(flag.name());
                            }
                        }
                    } else {
                        for (ItemFlag flag : ItemFlag.values()) {
                            flags.add(flag.name());
                        }
                    }

                    Collections.sort(flags);

                    return flags;
                }
            case "remflag":
                if (args.length == 1) {
                    List<String> flags = new ArrayList<>();
                    if (!item.getItemMeta().getItemFlags().isEmpty()) {

                        if (!args[0].equals("")) {
                            for (ItemFlag flag : item.getItemMeta().getItemFlags()) {
                                if (flag.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                                    flags.add(flag.name());
                                }
                            }
                        } else {
                            for (ItemFlag flag : item.getItemMeta().getItemFlags()) {
                                flags.add(flag.name());
                            }
                        }
                    } else
                        flags.add("This item has no flags.");

                    Collections.sort(flags);

                    return flags;
                }

            case "addench":
                if (args.length == 1) {
                    ArrayList<String> enchants = new ArrayList<>();

                    if (!args[0].equals("")) {
                        for (Enchantment enchantment : Enchantment.values()) {
                            if (enchantment.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                                enchants.add(enchantment.getName());
                            }
                        }
                    } else {
                        for (Enchantment enchantment : Enchantment.values()) {
                            enchants.add(enchantment.getName());
                        }
                    }

                    Collections.sort(enchants);

                    return enchants;
                } else if (args.length == 2) {
                    ArrayList<String> levels = new ArrayList<>();

                    if (Enchantment.getByName(args[0]) != null) {

                        if (!args[1].equals("")) {
                            for (int i = 1; i <= Enchantment.getByName(args[0]).getMaxLevel(); i++)
                                if (String.valueOf(i).toLowerCase().startsWith(args[0].toLowerCase()))
                                    levels.add(String.valueOf(i));
                        } else {
                            for (int i = 1; i <= Enchantment.getByName(args[0]).getMaxLevel(); i++)
                                levels.add(String.valueOf(i));
                        }
                    } else
                        levels.add("That enchant is not valid.");

                    Collections.sort(levels);

                    return levels;
                }

            case "remench":
                if (args.length == 1) {
                    List<String> enchants = new ArrayList<>();

                    if (item.getItemMeta().hasEnchants()) {

                        if (!args[0].equals("")) {
                            for (Enchantment enchantment : item.getItemMeta().getEnchants().keySet()) {
                                if (enchantment.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                                    enchants.add(enchantment.getName());
                                }
                            }
                        } else {
                            for (Enchantment enchantment : item.getItemMeta().getEnchants().keySet()) {
                                enchants.add(enchantment.getName());
                            }
                        }
                    } else
                        enchants.add("This item has no enchants.");

                    Collections.sort(enchants);

                    return enchants;
                }
            case "setname":
                if ((args.length == 1) && args[0].equalsIgnoreCase("")) {
                    List<String> tip = new ArrayList<>();
                    tip.add("Names can contain spaces.");
                    return tip;
                }
        }
        return null;
    }
}