package space.devport.wertik.items.commands.utility.lore;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.util.ItemUtil;

import java.util.List;

public class AddLore extends SubCommand {

    public AddLore() {
        super("add");
        this.preconditions = new Preconditions()
                .permissions("items.utility.lore.add");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        ItemBuilder builder = ItemUtil.getBuilderInHand(player);

        List<String> lore = builder.getLore().getMessage();
        lore.add(String.join(" ", args));
        builder.lore(lore);

        ItemUtil.setItem(player, EquipmentSlot.HAND, builder.build());
        language.sendPrefixed(sender, "Line-Added");
        return CommandResult.SUCCESS;
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
        return "/%label% add <line>";
    }

    @Override
    public String getDefaultDescription() {
        return "Add a line of lore to the item in hand.";
    }
}