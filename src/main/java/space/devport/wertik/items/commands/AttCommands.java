package space.devport.wertik.items.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.messageutil.StringUtil;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.handlers.ItemHandler;
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

    private final Main plugin;
    private final AttributeHandler attributeHandler;
    private final ItemHandler itemHandler;

    public AttCommands() {
        plugin = Main.inst;
        attributeHandler = plugin.getAttributeHandler();
        itemHandler = plugin.getItemHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(StringUtil.color("&cOnly players."));
            return true;
        }

        if (!sender.hasPermission("items.control")) {
            sender.sendMessage(StringUtil.color("&cYou don't have permission to do this."));
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

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
                        sender.sendMessage(StringUtil.color("&cValids: &f" + Utils.listToString(new ArrayList<>(attributeHandler.getAttributeCache().keySet()), "&7, &f", "&cNo attributes configured.")));
                        return true;
                    }

                    try {
                        Action.valueOf(args[2].toUpperCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(StringUtil.color("&cThat click type is not valid."));
                        sender.sendMessage(StringUtil.color("&cValids: &f"));
                        for (Action a : Action.values()) {
                            sender.sendMessage(a.name());
                        }
                        return true;
                    }

                    player.setItemInHand(itemHandler.setAttribute(item, args[1], args[2].toUpperCase()));
                    sender.sendMessage(StringUtil.color("&eShould be added."));
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

                    if (!itemHandler.getAttributes(item).containsKey(args[1].toLowerCase()) && !itemHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {
                        sender.sendMessage(StringUtil.color("&cThis does not have any of this set."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att rem <name/clickType>"));
                        return true;
                    }

                    if (itemHandler.getAttributes(item).containsKey(args[1].toLowerCase())) {
                        player.setItemInHand(itemHandler.removeAttribute(item, args[1].toLowerCase()));
                        sender.sendMessage(StringUtil.color("&eAttribute removed."));
                        return true;
                    } else if (itemHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {
                        player.setItemInHand(itemHandler.removeAttribute(item, args[1].toLowerCase()));
                        sender.sendMessage(StringUtil.color("&eAttribute removed."));
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

                    player.setItemInHand(itemHandler.clearAttributes(item));
                    sender.sendMessage(StringUtil.color("&eAttributes cleared."));
                    break;
                case "list":
                case "l":
                    if (args.length > 1) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att list"));
                        return true;
                    }

                    plugin.cO.debug("Atts: " + attributeHandler.getAttributeCache().keySet());
                    sender.sendMessage(StringUtil.color("&eAttributes: &f" + Utils.listToString(new ArrayList<>(attributeHandler.getAttributeCache().keySet()), "&7, &f", "&cNo attributes saved.")));
                    break;
                case "listhand":
                case "lh":
                case "listh":
                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage(Main.inst.cO.getPrefix() + StringUtil.color("&cCannot help you with AIR."));
                        return true;
                    }

                    if (args.length > 1) {
                        sender.sendMessage(StringUtil.color("&cToo many arguments."));
                        sender.sendMessage(StringUtil.color("&cUsage: &7/att listHand"));
                        return true;
                    }

                    sender.sendMessage(StringUtil.color("&eAttributes:"));
                    Map<String, String> attributes = itemHandler.getAttributes(item);

                    for (String key : itemHandler.getAttributes(item).keySet())
                        sender.sendMessage(StringUtil.color("&8- &7" + key + "&f:&7" + attributes.get(key)));
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
                "\n&e/att list &8- &7Lists attributes from config." +
                "\n&e/att listHand &8- &7List attributes on item in your hand."));
    }
}