package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.system.ItemManager;

public class RemoveItem extends SubCommand {

    private final ItemManager itemManager;

    public RemoveItem(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[1])) return CommandResult.FAILURE;

        itemManager.removeItem(args[1]);
        language.getPrefixed("Item-Removed")
                .replace("%item%", args[1])
                .send(sender);
        return CommandResult.SUCCESS;
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