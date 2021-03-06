package space.devport.wertik.items.commands.utility.flags;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.util.ItemUtil;
import space.devport.wertik.items.util.StringUtil;

public class ClearFlags extends SubCommand {

    public ClearFlags() {
        super("clear");
        this.preconditions = new Preconditions()
                .permissions("items.utility.flags.clear");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        ItemBuilder builder = ItemUtil.getBuilderInHand(player);

        builder.clearFlags();

        ItemUtil.setItem(player, EquipmentSlot.HAND, builder.build());

        language.sendPrefixed(sender, "Flags-Cleared");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% clear";
    }

    @Override
    public String getDefaultDescription() {
        return "Clear flags from item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}