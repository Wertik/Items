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

public class ClearAttributes extends SubCommand {

    private final AttributeManager attributeManager;

    public ClearAttributes(String name) {
        super(name);
        attributeManager = ItemsPlugin.getInstance().getAttributeManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

        Utils.setItem(player, EquipmentSlot.HAND, attributeManager.clearAttributes(item));
        language.sendPrefixed(sender, "Attributes-Cleared");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% clear";
    }

    @Override
    public String getDefaultDescription() {
        return "Clear attributes from item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}
