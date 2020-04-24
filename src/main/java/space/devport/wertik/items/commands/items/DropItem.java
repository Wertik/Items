package space.devport.wertik.items.commands.items;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.utility.LocationUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.handlers.ItemManager;

public class DropItem extends SubCommand {

    private final ItemManager itemManager;

    public DropItem(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[1])) return CommandResult.FAILURE;

        int amount = 1;

        if (args.length == 4) {
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                language.sendPrefixed(sender, "Not-A-Number");
                return CommandResult.FAILURE;
            }
        }

        String[] locationString = args[2].split(";");

        for (int i = 1; i < locationString.length; i++) {
            try {
                Double.parseDouble(locationString[i]);
            } catch (NumberFormatException e) {
                language.sendPrefixed(sender, "Not-A-Number");
                return CommandResult.FAILURE;
            }
        }

        ItemStack giveItem = itemManager.getBuilder(args[1]).build();

        if (itemManager.isUnstackable(giveItem))
            giveItem = itemManager.setUnstackable(giveItem, true);

        giveItem.setAmount(amount);

        Location location = new Location(ItemsPlugin.getInstance().getServer().getWorld(locationString[0]),
                Double.parseDouble(locationString[1]),
                Double.parseDouble(locationString[2]),
                Double.parseDouble(locationString[3]));

        if (location.getWorld() == null) return CommandResult.FAILURE;

        location.getWorld().dropItemNaturally(location, giveItem);

        language.getPrefixed("Spawned-At")
                .replace("%item%", args[1])
                .replace("%amount%", String.valueOf(amount))
                .replace("%location%", LocationUtil.locationToString(location, ", "))
                .send(sender);
        return CommandResult.SUCCESS;
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