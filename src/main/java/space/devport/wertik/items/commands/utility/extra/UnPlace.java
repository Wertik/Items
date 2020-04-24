package space.devport.wertik.items.commands.utility.extra;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.handlers.ItemManager;
import space.devport.wertik.items.utils.Utils;

public class UnPlace extends SubCommand {

    private final ItemManager itemManager;

    public UnPlace(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        boolean unPlace = args.length > 0 ?
                Boolean.parseBoolean(args[0]) :
                !itemManager.isUnplaceable(Utils.getItemInHand(player));

        Utils.setItem(player, EquipmentSlot.HAND, itemManager.setUnplaceable(Utils.getItemInHand(player), unPlace));
        language.getPrefixed("Set-Unplaceable")
                .replace("%state%", String.valueOf(unPlace))
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% unplace (true/false)";
    }

    @Override
    public String getDefaultDescription() {
        return "Make an item unplace-able.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}