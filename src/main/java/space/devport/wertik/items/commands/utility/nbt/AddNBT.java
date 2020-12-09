package space.devport.wertik.items.commands.utility.nbt;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemNBTEditor;
import space.devport.wertik.items.util.ItemUtil;

public class AddNBT extends SubCommand {

    public AddNBT() {
        super("add");
        this.preconditions = new Preconditions()
                .permissions("items.utility.nbt.add");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;
        ItemStack item = ItemUtil.getItemInHand(player);

        String key = args[0];

        String value = "";
        if (args.length > 1) {
            value = args[1];
        }

        ItemUtil.setItem(player, EquipmentSlot.HAND, ItemNBTEditor.writeNBT(item, key, value));
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