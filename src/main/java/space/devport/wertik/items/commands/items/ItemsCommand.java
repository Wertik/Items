package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;

public class ItemsCommand extends MainCommand {

    public ItemsCommand(ItemsPlugin plugin) {
        super("items");
        setPermissions("items.manage");
        addSubCommand(new Detail(plugin));
        addSubCommand(new DropItem(plugin));
        addSubCommand(new GiveItem(plugin));
        addSubCommand(new ListItems(plugin));
        addSubCommand(new LoadItem(plugin));
        addSubCommand(new Reload(plugin));
        addSubCommand(new RemoveItem(plugin));
        addSubCommand(new SaveItem(plugin));
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        return super.perform(sender, label, args);
    }

    @Override
    public String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public String getDefaultDescription() {
        return "Displays this.";
    }
}