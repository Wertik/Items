package space.devport.wertik.items.commands.items;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.utility.LocationUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.commands.ItemsSubCommand;
import space.devport.wertik.items.system.ItemManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DropItem extends ItemsSubCommand {

    private final ItemManager itemManager;

    public DropItem(ItemsPlugin plugin) {
        super("drop", plugin);
        this.preconditions = new Preconditions().permissions("items.manage.drop");
        itemManager = plugin.getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[0])) return CommandResult.FAILURE;

        int amount = 1;

        if (args.length == 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                language.sendPrefixed(sender, "Not-A-Number");
                return CommandResult.FAILURE;
            }
        }

        String[] locationString = args[1].split(";");

        for (int i = 1; i < locationString.length; i++) {
            try {
                Double.parseDouble(locationString[i]);
            } catch (NumberFormatException e) {
                language.sendPrefixed(sender, "Not-A-Number");
                return CommandResult.FAILURE;
            }
        }

        ItemStack giveItem = itemManager.getBuilder(args[0]).build();

        if (itemManager.hasExtra(giveItem, "unstackable"))
            giveItem = itemManager.setExtra(giveItem, "unstackable", UUID.randomUUID().toString());

        giveItem.setAmount(amount);

        Location location = new Location(ItemsPlugin.getInstance().getServer().getWorld(locationString[0]),
                Double.parseDouble(locationString[1]),
                Double.parseDouble(locationString[2]),
                Double.parseDouble(locationString[3]));

        if (location.getWorld() == null) return CommandResult.FAILURE;

        location.getWorld().dropItemNaturally(location, giveItem);

        language.getPrefixed("Spawned-At")
                .replace("%item%", args[0])
                .replace("%amount%", String.valueOf(amount))
                .replace("%location%", LocationUtil.locationToString(location, ", "))
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
        return "/%label% drop <name> <world;x;y;z> <amount>";
    }

    @Override
    public String getDefaultDescription() {
        return "Drops an item on specified location.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(3);
    }
}