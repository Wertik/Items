package space.devport.wertik.items.commands.utility.enchants;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.xseries.XEnchantment;
import space.devport.wertik.items.util.ItemUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveEnchant extends SubCommand {

    public RemoveEnchant(String name) {
        super(name);
        this.preconditions = new Preconditions().permissions("items.utility.enchants.remove");
    }

    @SuppressWarnings("deprecation")
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

        ItemBuilder builder = ItemUtil.getBuilderInHand((Player) sender);

        builder.removeEnchant(enchantment);

        ItemUtil.setItem((Player) sender, EquipmentSlot.HAND, builder.build());
        language.sendPrefixed(sender, "Enchant-Removed");
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            ItemBuilder builder = ItemUtil.getBuilderInHand((Player) sender);
            suggestions = builder.getEnchants().keySet().stream().map(XEnchantment::name).collect(Collectors.toList());
        }

        Collections.sort(suggestions);
        return suggestions;
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