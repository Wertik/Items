package space.devport.wertik.items.commands.attributes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.utils.Utils;

public class Remove extends SubCommand {

    private final AttributeHandler attributeHandler;

    public Remove(String name) {
        super(name);
        attributeHandler = ItemsPlugin.getInstance().getAttributeHandler();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

        if (!attributeHandler.getAttributes(item).containsKey(args[1].toLowerCase()) &&
                !attributeHandler.getAttributes(item).containsValue(args[1].toLowerCase())) {

            language.getPrefixed("Attribute-Invalid-Param")
                    .replace("%param%", args[1])
                    .replace("%usage%", "/" + label + " remove <name/clickType>")
                    .send(sender);
            return CommandResult.FAILURE;
        }

        String param = args[1].toLowerCase();

        if (attributeHandler.getAttributes(item).containsKey(param)) {
            Utils.setItem(player, EquipmentSlot.HAND, attributeHandler.removeAttribute(item, param));
            language.getPrefixed("Attribute-Removed").replace("%attribute%", param).send(sender);
            return CommandResult.SUCCESS;
        }

        if (attributeHandler.getAttributes(item).containsValue(param)) {
            Utils.setItem(player, EquipmentSlot.HAND, attributeHandler.removeAttribute(item, param));
            language.getPrefixed("Attribute-Removed").replace("%attribute%", param).send(sender);
            return CommandResult.SUCCESS;
        }

        language.sendPrefixed(sender, "Could-Not-Remove-Attribute");
        return CommandResult.FAILURE;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% remove <attribute/action>";
    }

    @Override
    public String getDefaultDescription() {
        return "Remove an attribute from item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}