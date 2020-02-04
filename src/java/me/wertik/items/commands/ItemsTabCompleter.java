package me.wertik.items.commands;

import me.wertik.items.Main;
import me.wertik.items.handlers.AttributeHandler;
import me.wertik.items.handlers.ItemHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemsTabCompleter implements TabCompleter {

    private Main plugin;

    private AttributeHandler attributeHandler;
    private ItemHandler itemHandler;

    public ItemsTabCompleter() {
        plugin = Main.getInstance();

        attributeHandler = plugin.getAttributeHandler();
        itemHandler = plugin.getItemHandler();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Main.getInstance().cO.debug("Att Tab Complete request");

        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        List<String> tabComplete = new ArrayList<>();

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length == 2) {

                    if (!args[1].equals("")) {

                    } else {

                    }

                    Collections.sort(tabComplete);

                    return tabComplete;
                }
        }
        return null;
    }
}
