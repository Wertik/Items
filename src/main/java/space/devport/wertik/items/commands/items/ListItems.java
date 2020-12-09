package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.ItemsSubCommand;
import space.devport.wertik.items.system.item.ItemManager;
import space.devport.wertik.items.util.StringUtil;

import java.util.ArrayList;

public class ListItems extends ItemsSubCommand {

    private final ItemManager itemManager;

    public ListItems(ItemsPlugin plugin) {
        super("list", plugin);
        this.preconditions = new Preconditions().permissions("items.manage.list");
        itemManager = plugin.getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        language.getPrefixed("Items-List")
                .replace("%items%",
                        StringUtil.listToString(new ArrayList<>(itemManager.getItems().keySet()),
                                language.get("List-Splitter").color().toString(),
                                language.get("No-Items-Saved").color().toString()))
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