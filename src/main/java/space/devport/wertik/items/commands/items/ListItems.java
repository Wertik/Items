package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.handlers.ItemManager;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;

public class ListItems extends SubCommand {

    private final ItemManager itemManager;

    public ListItems(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        language.getPrefixed("Items-List")
                .replace("%items%", Utils.listToString(new ArrayList<>(itemManager.getItems().keySet()), "&7, &f", "&cNo items saved."))
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% list";
    }

    @Override
    public String getDefaultDescription() {
        return "Lists stored items.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}