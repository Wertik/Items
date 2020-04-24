package space.devport.wertik.items.commands.attributes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.system.AttributeManager;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;

public class ListAttributes extends SubCommand {

    private final AttributeManager attributeManager;

    public ListAttributes(String name) {
        super(name);
        this.preconditions = new Preconditions().permissions("items.attributes.list");
        attributeManager = ItemsPlugin.getInstance().getAttributeManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("hand") || args[1].equalsIgnoreCase("h")) {
                ItemStack item = Utils.getItemInHand(player);

                if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

                language.getPrefixed("Attributes")
                        .replace("%attributes%", "\n &7" + Utils.mapToString(attributeManager.getAttributes(item), "\n &7", "&8 - &7", "&cNo attributes saved."))
                        .send(sender);
                return CommandResult.SUCCESS;
            }
        }

        language.getPrefixed("Attributes")
                .replace("%attributes%", Utils.listToString(new ArrayList<>(attributeManager.getAttributeCache().keySet()), "&7, &f", "&cNo attributes saved."))
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% list (hand)";
    }

    @Override
    public String getDefaultDescription() {
        return "List all attributes, or those on an item in your hand.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}
