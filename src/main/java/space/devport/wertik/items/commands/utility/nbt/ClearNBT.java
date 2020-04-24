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
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.utils.Utils;

public class ClearNBT extends SubCommand {

    public ClearNBT(String name) {
        super(name);
        this.preconditions = new Preconditions().permissions("items.utility.nbt.clear");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        if (!ItemNBTEditor.hasNBT(item)) {
            language.sendPrefixed(sender, "No-NBT");
            return CommandResult.FAILURE;
        }

        for (String key : ItemNBTEditor.getNBTTagMap(item).keySet()) {
            if (!ItemsPlugin.getInstance().getFilteredNBT().contains(key))
                item = ItemNBTEditor.removeNBT(item, key);
        }

        Utils.setItem(player, EquipmentSlot.HAND, item);
        language.sendPrefixed(sender, "NBT-Cleared");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% clear";
    }

    @Override
    public String getDefaultDescription() {
        return "Clear all custom NBT data from an item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}