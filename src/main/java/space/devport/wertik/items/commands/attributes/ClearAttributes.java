package space.devport.wertik.items.commands.attributes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.util.ItemUtil;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.commands.ItemsSubCommand;
import space.devport.wertik.items.system.attribute.AttributeManager;

public class ClearAttributes extends ItemsSubCommand {

    private final AttributeManager attributeManager;

    public ClearAttributes(ItemsPlugin plugin) {
        super("clear", plugin);
        this.preconditions = new Preconditions().permissions("items.attributes.clear");
        attributeManager = plugin.getAttributeManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;
        ItemStack item = ItemUtil.getItemInHand(player);

        if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

        ItemUtil.setItem(player, EquipmentSlot.HAND, attributeManager.clearAttributes(item));
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
