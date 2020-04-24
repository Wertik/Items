package space.devport.wertik.items.commands.utility.flags;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.utils.Utils;

public class AddFlag extends SubCommand {

    public AddFlag(String name) {
        super(name);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        ItemBuilder builder = Utils.getBuilderInHand(player);

        ItemFlag flag;

        try {
            flag = ItemFlag.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            language.getPrefixed("Flag-Invalid")
                    .replace("%flag%", args[0])
                    .send(sender);
            return CommandResult.FAILURE;
        }

        builder.addFlag(flag);
        Utils.setItem(player, EquipmentSlot.HAND, builder.build());
        language.sendPrefixed(sender, "Flag-Added");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% add <flag>";
    }

    @Override
    public String getDefaultDescription() {
        return "Add a flag to item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}