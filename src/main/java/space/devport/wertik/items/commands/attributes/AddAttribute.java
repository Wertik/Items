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
import space.devport.wertik.items.handlers.AttributeManager;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;

public class AddAttribute extends SubCommand {

    private final AttributeManager attributeManager;

    public AddAttribute(String name) {
        super(name);
        attributeManager = ItemsPlugin.getInstance().getAttributeManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        if (CommandUtils.checkAir(player, item)) {
            return CommandResult.FAILURE;
        }

        if (!attributeManager.getAttributeCache().containsKey(args[1])) {
            language.getPrefixed("Attribute-Invalid")
                    .replace("%attribute%", args[1])
                    .replace("%valid%", Utils.listToString(new ArrayList<>(attributeManager.getAttributeCache().keySet()), "&7, &f", "&cNo attributes configured."))
                    .send(sender);
            return CommandResult.FAILURE;
        }

        String action = args[2].toLowerCase();

        if (!ItemsPlugin.getInstance().getActionNames().contains(action)) {
            language.getPrefixed("Click-Type-Invalid")
                    .replace("%action%", args[2])
                    .replace("%valid%", String.join(", ", ItemsPlugin.getInstance().getActionNames()))
                    .send(sender);
            return CommandResult.FAILURE;
        }

        Utils.setItem(player, EquipmentSlot.HAND, attributeManager.setAttribute(item, args[2], args[1]));
        language.getPrefixed("Attribute-Added").replace("%attribute%", args[1]).send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% add <attribute> <action>";
    }

    @Override
    public String getDefaultDescription() {
        return "Add an attribute to item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(2);
    }
}