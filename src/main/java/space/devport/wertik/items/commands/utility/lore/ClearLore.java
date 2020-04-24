package space.devport.wertik.items.commands.utility.lore;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.message.Message;
import space.devport.wertik.items.utils.Utils;

public class ClearLore extends SubCommand {

    public ClearLore(String name) {
        super(name);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        ItemBuilder builder = Utils.getBuilderInHand(player);

        builder.lore(new Message());

        Utils.setItem(player, EquipmentSlot.HAND, builder.build());
        language.sendPrefixed(sender, "Lore-Cleared");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% clear";
    }

    @Override
    public String getDefaultDescription() {
        return "Clear lore from item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}