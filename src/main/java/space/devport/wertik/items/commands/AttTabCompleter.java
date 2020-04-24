package space.devport.wertik.items.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.system.AttributeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttTabCompleter implements TabCompleter {

    private final ItemsPlugin plugin;
    private final AttributeManager attributeManager;

    public AttTabCompleter() {
        plugin = ItemsPlugin.getInstance();

        attributeManager = plugin.getAttributeManager();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        List<String> tabComplete = new ArrayList<>();

        switch (args[0].toLowerCase()) {
            case "add":
            case "a":

                break;
            case "rem":
            case "remove":
            case "r":
                if (args.length == 2) {
                    if (!args[1].equals("")) {
                        if (!attributeManager.getAttributes(item).isEmpty())
                            for (String attributeName : attributeManager.getAttributes(item).keySet()) {
                                if (attributeName.toLowerCase().startsWith(args[1].toLowerCase()))
                                    tabComplete.add(attributeName);
                                if (attributeManager.getAttributes(item).get(attributeName).toLowerCase().startsWith(args[1].toLowerCase()))
                                    tabComplete.add(attributeManager.getAttributes(item).get(attributeName));
                            }
                    } else {
                        if (!attributeManager.getAttributes(item).isEmpty()) {
                            tabComplete.addAll(attributeManager.getAttributes(item).keySet());
                            tabComplete.addAll(attributeManager.getAttributes(item).values());
                        }
                    }

                    Collections.sort(tabComplete);

                    return tabComplete;
                }
                break;
        }
        return null;
    }
}