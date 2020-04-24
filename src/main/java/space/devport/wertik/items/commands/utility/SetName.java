package space.devport.wertik.items.commands.utility;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.utils.Utils;

public class SetName extends MainCommand {

    public SetName(String name) {
        super(name);
        this.preconditions = new Preconditions().playerOnly();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        if (CommandUtils.checkAir(player, Utils.getItemInHand(player))) return CommandResult.FAILURE;

        ItemBuilder builder = Utils.getBuilderInHand(player);

        builder.displayName(StringUtil.color(String.join(" ", args)));
        Utils.setItem(player, EquipmentSlot.HAND, builder.build());

        language.sendPrefixed(sender, "Item-Renamed");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return null;
    }

    @Override
    public String getDefaultDescription() {
        return null;
    }
}
