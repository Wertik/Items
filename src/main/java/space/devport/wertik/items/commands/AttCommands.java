package space.devport.wertik.items.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.utils.Language;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;

public class AttCommands implements CommandExecutor {

    private final AttributeHandler attributeHandler;

    public AttCommands() {
        attributeHandler = ItemsPlugin.getInstance().getAttributeHandler();
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
        ItemStack item = Utils.getItem(player);

        if (args.length == 0) {
            help(sender);
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "add":
                case "a":
                    if (args.length < 3) {
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " add <name> <clickType>")
                                .send(sender);
                        return true;
                    }

                    if (args.length > 3) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " add <name> <clickType>")
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

                    Utils.setItem(player, EquipmentSlot.HAND, attributeHandler.setAttribute(item, args[2], args[1]));
                    Language.ATTRIBUTE_ADDED.getPrefixed().fill("%attribute%", args[1]).send(sender);
                    break;
                case "rem":
                case "r":
                case "remove":
                    if (args.length < 2) {
                        Language.NOT_ENOUGH_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " remove <attribute/action>")
                                .send(sender);
                        return true;
                    }

                    if (args.length > 2) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " remove <attribute/action>")
                                .send(sender);
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
                        return true;
                    }

                    if (!attributeHandler.getAttributes(item).containsKey(args[1].toLowerCase()) &&
                            !attributeHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {

                        Language.ATTRIBUTE_INVALID_PARAM.getPrefixed()
                                .fill("%param%", args[1])
                                .fill("%usage%", "/" + label + " remove <name/clickType>")
                                .send(sender);
                        return true;
                    }

                    String param = args[1].toLowerCase();

                    if (attributeHandler.getAttributes(item).containsKey(param)) {
                        Utils.setItem(player, EquipmentSlot.HAND, attributeHandler.removeAttribute(item, param));
                        Language.ATTRIBUTE_REMOVED.getPrefixed().fill("%attribute%", param).send(sender);
                        return true;
                    } else if (attributeHandler.getAttributes(item).containsValue(param)) {
                        Utils.setItem(player, EquipmentSlot.HAND, attributeHandler.removeAttribute(item, param));
                        Language.ATTRIBUTE_REMOVED.getPrefixed().fill("%attribute%", param).send(sender);
                        return true;
                    } else
                        Language.ATTRIBUTE_COULD_NOT_REMOVE.sendPrefixed(sender);

                    break;
                case "clear":
                case "c":
                    if (args.length > 1) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " clear")
                                .send(sender);
                        return true;
                    }

                    if (item.getType().equals(Material.AIR)) {
                        Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
                        return true;
                    }

                    Utils.setItem(player, EquipmentSlot.HAND, attributeHandler.clearAttributes(item));
                    Language.ATTRIBUTES_CLEARED.sendPrefixed(sender);
                    break;
                case "list":
                case "l":
                    if (args.length > 2) {
                        Language.TOO_MANY_ARGUMENTS.getPrefixed()
                                .fill("%usage%", "/" + label + " list (hand)")
                                .send(sender);
                        return true;
                    }

                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("hand") || args[1].equalsIgnoreCase("h")) {
                            if (item.getType().equals(Material.AIR)) {
                                Language.CANNOT_HELP_WITH_AIR.sendPrefixed(sender);
                                return true;
                            }

                            Language.ATTRIBUTES_LIST.getPrefixed()
                                    .fill("%attributes%", "\n &7" + Utils.mapToString(attributeHandler.getAttributes(item), "\n &7", "&8 - &7", "&cNo attributes saved."))
                                    .send(sender);
                            break;
                        }
                    }

                    Language.ATTRIBUTES_LIST.getPrefixed()
                            .fill("%attributes%", Utils.listToString(new ArrayList<>(attributeHandler.getAttributeCache().keySet()), "&7, &f", "&cNo attributes saved."))
                            .send(sender);
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