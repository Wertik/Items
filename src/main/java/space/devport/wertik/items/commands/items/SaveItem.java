package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.system.ItemManager;
import space.devport.wertik.items.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveItem extends SubCommand {

    private final ItemManager itemManager;

    public SaveItem(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
        this.preconditions = new Preconditions()
                .playerOnly()
                .permissions("items.manage.give");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

        itemManager.addItem(args[0], item);
        language.getPrefixed(itemManager.checkItemStorage(args[0]) ? "Item-Updated" : "Item-Saved")
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
        return "/%label% save <name>";
    }

    @Override
    public String getDefaultDescription() {
        return "Save an item to the storage.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}
