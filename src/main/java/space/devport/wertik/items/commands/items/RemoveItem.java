package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.commands.ItemsSubCommand;
import space.devport.wertik.items.system.ItemManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoveItem extends ItemsSubCommand {

    private final ItemManager itemManager;

    public RemoveItem(ItemsPlugin plugin) {
        super("remove", plugin);
        this.preconditions = new Preconditions().permissions("items.manage.remove");
        itemManager = plugin.getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[0])) return CommandResult.FAILURE;

        itemManager.removeItem(args[0]);
        language.getPrefixed("Item-Removed")
                .replace("%item%", args[0])
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            suggestions.addAll(itemManager.getItems().keySet());
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% remove <name>";
    }

    @Override
    public String getDefaultDescription() {
        return "Removes an item from storage.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}