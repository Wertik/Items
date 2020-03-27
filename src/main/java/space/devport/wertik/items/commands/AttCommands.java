package space.devport.wertik.items.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.messageutil.StringUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.utils.Language;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

public class AttCommands implements CommandExecutor {

    /*
     * /att add <name> <clickType>
     * /att rem <name/clickType>
     * /att list
     * /att clear
     * */

    private final ItemsPlugin plugin;
    private final AttributeHandler attributeHandler;

    public AttCommands() {
        plugin = ItemsPlugin.getInstance();
        attributeHandler = plugin.getAttributeHandler();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            Language.ONLY_PLAYERS.getPrefixed().send(sender);
            return true;
        }

        if (!sender.hasPermission("items.control")) {
            Language.NO_PERMS.getPrefixed().send(sender);
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (args.length == 0) {
            help(sender);
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "add":
                case "a":
                    if (args.length < 3) {
                        sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att add <name> <clickType>"));
                        return true;
                    }

                    if (args.length > 3) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att add <name> <clickType>"));
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage(StringUtil.color("&cCannot help you with AIR."));
                        return true;
                    }

                    if (!attributeHandler.getAttributeCache().containsKey(args[1])) {
                        sender.sendMessage(StringUtil.color("&cThat attribute is not valid."));
                        sender.sendMessage(StringUtil.color("&cValid ones: &f" + Utils.listToString(new ArrayList<>(attributeHandler.getAttributeCache().keySet()), "&7, &f", "&cNo attributes configured.")));
                        return true;
                    }

                    String action = args[2].toLowerCase();

                    if (!ItemsPlugin.getInstance().getActionNames().contains(action)) {
                        sender.sendMessage(StringUtil.color("&cThat click type is not valid."));
                        sender.sendMessage(StringUtil.color("&cValid ones: &f" + String.join(", ", ItemsPlugin.getInstance().getActionNames())));
                        return true;
                    }

                    player.getInventory().setItemInMainHand(attributeHandler.setAttribute(item, args[2], args[1]));
                    Language.ATTRIBUTE_ADDED.getPrefixed().fill("%attribute%", args[1]).send(sender);
                    break;
                case "rem":
                case "r":
                case "remove":
                    if (args.length < 2) {
                        sender.sendMessage(StringUtil.color("&cNot enough arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att rem <name/clickType>"));
                        return true;
                    }

                    if (args.length > 2) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att rem <name/clickType>"));
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage(StringUtil.color("&cCannot help you with AIR."));
                        return true;
                    }

                    if (!attributeHandler.getAttributes(item).containsKey(args[1].toLowerCase()) &&
                            !attributeHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {

                        sender.sendMessage(StringUtil.color("&cThis does not have any of this set."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att rem <name/clickType>"));
                        return true;
                    }

                    String param = args[1].toLowerCase();

                    if (attributeHandler.getAttributes(item).containsKey(param)) {
                        player.getInventory().setItemInMainHand(attributeHandler.removeAttribute(item, param));
                        Language.ATTRIBUTE_REMOVED.getPrefixed().fill("%attribute%", param).send(sender);
                        return true;
                    } else if (attributeHandler.getAttributes(item).containsValue(param)) {
                        player.getInventory().setItemInMainHand(attributeHandler.removeAttribute(item, param));
                        Language.ATTRIBUTE_REMOVED.getPrefixed().fill("%attribute%", param).send(sender);
                        return true;
                    } else
                        sender.sendMessage(StringUtil.color("&cCould not remove attribute."));

                    break;
                case "clear":
                case "c":
                    if (args.length > 1) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att clear"));
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage(StringUtil.color("&cCannot help you with AIR."));
                        return true;
                    }

                    player.getInventory().setItemInMainHand(attributeHandler.clearAttributes(item));
                    sender.sendMessage(StringUtil.color("&eAttributes cleared."));
                    break;
                case "list":
                case "l":
                    if (args.length > 2) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att list [hand]"));
                        return true;
                    }

                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("hand") || args[1].equalsIgnoreCase("h")) {
                            if (item.getType().equals(Material.AIR)) {
                                player.sendMessage(ItemsPlugin.getInstance().consoleOutput.getPrefix() + StringUtil.color("&cCannot help you with AIR."));
                                return true;
                            }

                            sender.sendMessage(StringUtil.color("&eAttributes:"));
                            Map<String, String> attributes = attributeHandler.getAttributes(item);

                            for (String key : attributeHandler.getAttributes(item).keySet())
                                sender.sendMessage(StringUtil.color("&8- &7" + key + "&f:&7" + attributes.get(key)));
                            break;
                        }
                    }

                    plugin.consoleOutput.debug("Attributes: " + attributeHandler.getAttributeCache().keySet());
                    sender.sendMessage(StringUtil.color("&eAttributes: &f" + Utils.listToString(new ArrayList<>(attributeHandler.getAttributeCache().keySet()), "&7, &f", "&cNo attributes saved.")));
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
        sender.sendMessage(StringUtil.color("&8&m--------&r &eAttributes &8&m--------" +
                "\n&e/att add <name> <action> &8- &7Add attribute to an item." +
                "\n&e/att rem <name/action> &8- &7Remove attribute based on clickType/Name." +
                "\n&e/att clear &8- &7Clears attributes." +
                "\n&e/att list [hand/h] &8- &7Lists attributes from config or on item in hand."));
    }
}