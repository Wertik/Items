package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.handlers.ItemHandler;

public class Load extends SubCommand {

    private final ItemHandler itemHandler;

    public Load(String name) {
        super(name);
        itemHandler = ItemsPlugin.getInstance().getItemHandler();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            if (CommandUtils.checkItemStored(sender, args[1])) return CommandResult.FAILURE;

            itemHandler.loadItem(args[1]);

            language.getPrefixed("Item-Loaded")
                    .replace("%item%", args[1])
                    .send(sender);
            return CommandResult.SUCCESS;
        }

        ItemsPlugin.getInstance().getItemHandler().loadItems();
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