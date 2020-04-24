package space.devport.wertik.items.commands.utility.nbt;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.item.ItemNBTEditor;
import space.devport.wertik.items.utils.Utils;

public class RemoveNBT extends SubCommand {

    public RemoveNBT(String name) {
        super(name);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        if (!ItemNBTEditor.hasNBT(item)) {
            language.sendPrefixed(sender, "No-NBT");
            return CommandResult.FAILURE;
        }

        if (!ItemNBTEditor.hasNBTKey(item, args[0])) {
            language.getPrefixed("No-Key")
                    .replace("%key%", "'" + args[0] + "'")
                    .send(sender);
            return CommandResult.FAILURE;
        }

        Utils.setItem(player, EquipmentSlot.HAND, ItemNBTEditor.removeNBT(item, args[0]));
        language.getPrefixed("NBT-Removed")
                .replace("%key%", "'" + args[0] + "'")
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% remove <key>";
    }

    @Override
    public String getDefaultDescription() {
        return "Remove an NBT entry from item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}