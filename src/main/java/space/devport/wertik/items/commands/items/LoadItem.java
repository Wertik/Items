package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.handlers.ItemManager;

public class LoadItem extends SubCommand {

    private final ItemManager itemManager;

    public LoadItem(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
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

        ItemsPlugin.getInstance().getItemManager().loadItems();
        language.sendPrefixed(sender, "Items-Loaded");
        return CommandResult.SUCCESS;
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