package space.devport.wertik.items.commands.utility.flags;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveFlag extends SubCommand {

    public RemoveFlag(String name) {
        super(name);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        ItemFlag flag;

        try {
            flag = ItemFlag.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            language.getPrefixed("Flag-Invalid")
                    .replace("%flag%", args[0])
                    .send(sender);
            return CommandResult.FAILURE;
        }

        ItemBuilder builder = Utils.getBuilderInHand((Player) sender);

        if (!builder.getFlags().contains(flag)) {
            language.sendPrefixed(sender, "No-Flag");
            return CommandResult.FAILURE;
        }

        builder.addFlag(flag);
        Utils.setItem((Player) sender, EquipmentSlot.HAND, builder.build());

        language.sendPrefixed(sender, "Flag-Removed");
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            Player player = (Player) sender;
            ItemBuilder item = Utils.getBuilderInHand(player);

            suggestions = item.getFlags().stream().map(ItemFlag::toString).collect(Collectors.toList());
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% remove <flag>";
    }

    @Override
    public String getDefaultDescription() {
        return "Remove a flag from item in hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}