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

public class AddNBT extends SubCommand {

    public AddNBT(String name) {
        super(name);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        String key = args[0];

        String value = "";
        if (args.length > 1) {
            value = args[1];
        }

        Utils.setItem(player, EquipmentSlot.HAND, ItemNBTEditor.writeNBT(item, key, value));
        language.getPrefixed("NBT-Added")
                .replace("%key%", key)
                .replace("%value%", "'" + value + "'")
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% add <key> (value)";
    }

    @Override
    public String getDefaultDescription() {
        return "Add an NBT entry to item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1, 2);
    }
}