package space.devport.wertik.items.commands.utility.enchants;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.utils.Utils;

public class RemoveEnchant extends SubCommand {

    public RemoveEnchant(String name) {
        super(name);
        this.preconditions = new Preconditions().permissions("items.utility.enchants.remove");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Enchantment enchantment;
        try {
            enchantment = Enchantment.getByName(args[0].toUpperCase());
        } catch (IllegalArgumentException ignore) {
            language.sendPrefixed(sender, "Invalid-Enchant");
            return CommandResult.FAILURE;
        }

        if (enchantment == null) {
            language.sendPrefixed(sender, "Invalid-Enchant");
            return CommandResult.FAILURE;
        }

        ItemBuilder builder = Utils.getBuilderInHand((Player) sender);

        builder.removeEnchant(enchantment);

        Utils.setItem((Player) sender, EquipmentSlot.HAND, builder.build());
        language.sendPrefixed(sender, "Enchant-Removed");
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% remove <enchantment>";
    }

    @Override
    public String getDefaultDescription() {
        return "Remove an enchant from item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}