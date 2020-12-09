package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.commands.ItemsSubCommand;
import space.devport.wertik.items.system.item.ItemManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadItem extends ItemsSubCommand {

    private final ItemManager itemManager;

    public LoadItem(ItemsPlugin plugin) {
        super("load", plugin);
        this.preconditions = new Preconditions().permissions("items.manage.load");
        itemManager = plugin.getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            if (CommandUtils.checkItemStored(sender, args[1])) return CommandResult.FAILURE;

            itemManager.loadItem(args[1]);

            language.getPrefixed("Item-Loaded")
                    .replace("%item%", args[1])
                    .send(sender);
            return CommandResult.SUCCESS;
        }

        itemManager.getStorage().load();
        itemManager.loadItems();
        language.sendPrefixed(sender, "Items-Loaded");
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            suggestions.addAll(itemManager.getItems().keySet());
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% load (name)";
    }

    @Override
    public String getDefaultDescription() {
        return "Load all, or a single item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}