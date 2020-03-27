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
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/att add <name> <clickType>")
                                .send(sender);
                        return true;
                    }

                    if (args.length > 3) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/att add <name> <clickType>")
                                .send(sender);
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
                        return true;
                    }

                    if (!attributeHandler.getAttributeCache().containsKey(args[1])) {
                        Language.ATTRIBUTE_INVALID.getPrefixed()
                                .fill("%attribute%", args[1])
                                .fill("%valid%", Utils.listToString(new ArrayList<>(attributeHandler.getAttributeCache().keySet()), "&7, &f", "&cNo attributes configured."))
                                .send(sender);
                        return true;
                    }

                    String action = args[2].toLowerCase();

                    if (!ItemsPlugin.getInstance().getActionNames().contains(action)) {
                        Language.CLICK_TYPE_INVALID.getPrefixed()
                                .fill("%action%", args[2])
                                .fill("%valid%", String.join(", ", ItemsPlugin.getInstance().getActionNames()))
                                .send(sender);
                        return true;
                    }

                    player.getInventory().setItemInMainHand(attributeHandler.setAttribute(item, args[2], args[1]));
                    Language.ATTRIBUTE_ADDED.getPrefixed().fill("%attribute%", args[1]).send(sender);
                    break;
                case "rem":
                case "r":
                case "remove":
                    if (args.length < 2) {
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/att remove <attribute/action>")
                                .send(sender);
                        return true;
                    }

                    if (args.length > 2) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/att remove <attribute/action>")
                                .send(sender);
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
                        return true;
                    }

                    if (!attributeHandler.getAttributes(item).containsKey(args[1].toLowerCase()) &&
                            !attributeHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {

                        sender.sendMessage(StringUtil.color("&cThis item does not have any of this set."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att remove <name/clickType>"));
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
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/att clear")
                                .send(sender);
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
                        return true;
                    }

                    player.getInventory().setItemInMainHand(attributeHandler.clearAttributes(item));
                    Language.ATTRIBUTES_CLEARED.sendPrefixed(sender);
                    break;
                case "list":
                case "l":
                    if (args.length > 2) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/att list (hand)")
                                .send(sender);
                        return true;
                    }

                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("hand") || args[1].equalsIgnoreCase("h")) {
                            if (item.getType().equals(Material.AIR)) {
                                Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
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
        Language.ATTRIBUTES_HELP.send(sender);
    }
}