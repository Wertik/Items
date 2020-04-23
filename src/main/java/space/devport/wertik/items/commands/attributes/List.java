package space.devport.wertik.items.commands.attributes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;

public class List extends SubCommand {

    private final AttributeHandler attributeHandler;

    public List(String name) {
        super(name);
        attributeHandler = ItemsPlugin.getInstance().getAttributeHandler();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("hand") || args[1].equalsIgnoreCase("h")) {
                ItemStack item = Utils.getItemInHand(player);

                if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

                language.getPrefixed("Attributes")
                        .replace("%attributes%", "\n &7" + Utils.mapToString(attributeHandler.getAttributes(item), "\n &7", "&8 - &7", "&cNo attributes saved."))
                        .send(sender);
                return CommandResult.SUCCESS;
            }
        }

        language.getPrefixed("Attributes")
                .replace("%attributes%", Utils.listToString(new ArrayList<>(attributeHandler.getAttributeCache().keySet()), "&7, &f", "&cNo attributes saved."))
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
