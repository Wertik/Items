package space.devport.wertik.items.commands.attributes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.text.message.Message;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.commands.ItemsSubCommand;
import space.devport.wertik.items.system.AttributeManager;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListAttributes extends ItemsSubCommand {

    private final AttributeManager attributeManager;

    public ListAttributes(ItemsPlugin plugin) {
        super("list", plugin);
        this.preconditions = new Preconditions().permissions("items.attributes.list");
        attributeManager = plugin.getAttributeManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("hand") || args[0].equalsIgnoreCase("h")) {
                ItemStack item = Utils.getItemInHand(player);

                if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

                Message attributes = new Message(language.getPrefixed("Attributes-List"));

                for (Map.Entry<String, String> entry : attributeManager.getAttributes(item).entrySet()) {
                    attributes.append(language.get("Attributes-List-Line")
                            .replace("%action%", entry.getKey())
                            .replace("%attribute%", entry.getValue()));
                }

                attributes.send(sender);
                return CommandResult.SUCCESS;
            }
        }

        language.getPrefixed("Attributes")
                .replace("%attributes%",
                        Utils.listToString(new ArrayList<>(attributeManager.getAttributeCache().keySet()),
                                language.get("List-Splitter").color().toString(),
                                language.get("No-Attributes").color().toString()))
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        return args.length == 0 ? Collections.singletonList("hand") : new ArrayList<>();
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
