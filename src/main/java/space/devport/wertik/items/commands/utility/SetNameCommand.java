package space.devport.wertik.items.commands.utility;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.util.ItemUtil;

public class SetNameCommand extends MainCommand {

    public SetNameCommand() {
        super("setname");
        this.preconditions = new Preconditions()
                .permissions("items.utility.setname")
                .playerOnly();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        if (CommandUtils.checkAir(player, ItemUtil.getItemInHand(player))) return CommandResult.FAILURE;

        ItemBuilder builder = ItemUtil.getBuilderInHand(player);

        builder.displayName(space.devport.utils.text.StringUtil.color(String.join(" ", args)));
        ItemUtil.setItem(player, EquipmentSlot.HAND, builder.build());

        language.sendPrefixed(sender, "Item-Renamed");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% (name)";
    }

    @Override
    public String getDefaultDescription() {
        return "Change the name of an item in hand.";
    }
}
