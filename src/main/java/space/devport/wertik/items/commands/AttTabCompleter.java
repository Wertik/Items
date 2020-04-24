package space.devport.wertik.items.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.handlers.AttributeManager;

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

        if (args.length == 1) {
            String[] subs = {"add", "remove", "list", "clear", "help"};
            if (!args[0].equals("")) {
                for (String sub : subs)
                    if (sub.toLowerCase().startsWith(args[0].toLowerCase()))
                        tabComplete.add(sub);
            } else
                tabComplete.addAll(Arrays.asList(subs));

            Collections.sort(tabComplete);

            return tabComplete;
        }

        switch (args[0].toLowerCase()) {
            case "add":
            case "a":
                if (args.length == 2) {
                    if (!attributeManager.getAttributeCache().isEmpty()) {
                        if (!args[1].equals("")) {
                            for (String attributeName : attributeManager.getAttributeCache().keySet())
                                if (attributeName.toLowerCase().startsWith(args[1].toLowerCase()))
                                    tabComplete.add(attributeName);
                        } else
                            tabComplete.addAll(attributeManager.getAttributeCache().keySet());

                    } else {
                        tabComplete.add("There are no attributes configured.");
                    }

                    Collections.sort(tabComplete);

                    return tabComplete;
                } else if (args.length == 3) {
                    if (!args[2].equals("")) {
                        for (String actionName : plugin.getActionNames())
                            if (actionName.toLowerCase().startsWith(args[2].toLowerCase()))
                                tabComplete.add(actionName.toLowerCase());
                    } else
                        tabComplete.addAll(plugin.getActionNames());

                    Collections.sort(tabComplete);

                    return tabComplete;
                }
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