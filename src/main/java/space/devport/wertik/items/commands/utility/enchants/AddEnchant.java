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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AddEnchant extends SubCommand {

    public AddEnchant(String name) {
        super(name);
        this.preconditions = new Preconditions().permissions("items.utility.enchants.add");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        int level = 1;

        if (args.length > 1)
            try {
                level = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                language.getPrefixed("Not-A-Number")
                        .replace("%param%", args[1])
                        .send(sender);
                return CommandResult.FAILURE;
            }

        Enchantment enchantment = null;
        try {
            enchantment = Enchantment.getByName(args[0].toUpperCase());
        } catch (IllegalArgumentException ignore) {
        }

        if (enchantment == null) {
            language.sendPrefixed(sender, "Invalid-Enchant");
            return CommandResult.FAILURE;
        }

        ItemBuilder builder = Utils.getBuilderInHand((Player) sender);

        builder.addEnchant(enchantment, level);

        Utils.setItem((Player) sender, EquipmentSlot.HAND, builder.build());
        language.sendPrefixed(sender, "Enchant-Added");
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            suggestions = Arrays.stream(Enchantment.values()).map(Enchantment::getName).collect(Collectors.toList());
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% add <enchantment> (level)";
    }

    @Override
    public String getDefaultDescription() {
        return "Add an enchantment to item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}