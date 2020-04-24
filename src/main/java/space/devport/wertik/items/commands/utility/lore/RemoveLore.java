package space.devport.wertik.items.commands.utility.lore;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoveLore extends SubCommand {

    public RemoveLore(String name) {
        super(name);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        ItemBuilder builder = Utils.getBuilderInHand(player);

        int index;
        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            language.getPrefixed("Not-A-Number")
                    .replace("%param%", args[0])
                    .send(sender);
            return CommandResult.FAILURE;
        }

        if (builder.getLore().isEmpty()) {
            language.sendPrefixed(sender, "No-Lore");
            return CommandResult.FAILURE;
        }

        List<String> workLore = builder.getLore().getMessage();

        if (index >= workLore.size()) {
            language.get("Index-Out-Of-Bounds")
                    .replace("%param%", String.valueOf(index))
                    .replace("%max%", String.valueOf(workLore.size() - 1))
                    .send(sender);
            return CommandResult.FAILURE;
        }

        workLore.remove(index);
        builder.getLore().set(workLore);

        Utils.setItem(player, EquipmentSlot.HAND, builder.build());
        language.sendPrefixed(sender, "Line-Removed");
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            Player player = (Player) sender;
            ItemBuilder builder = Utils.getBuilderInHand(player);
            for (int i = 0; i < builder.getLore().getOriginal().size(); i++) suggestions.add(String.valueOf(i));
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0);
    }

    @Override
    public boolean checkRange() {
        return false;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% remove <index>";
    }

    @Override
    public String getDefaultDescription() {
        return "Remove a line of lore from item in hand by index.";
    }
}