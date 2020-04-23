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
import space.devport.wertik.items.handlers.ItemHandler;
import space.devport.wertik.items.utils.Utils;

public class Save extends SubCommand {

    private final ItemHandler itemHandler;

    public Save(String name) {
        super(name);
        itemHandler = ItemsPlugin.getInstance().getItemHandler();
        this.preconditions = new Preconditions().playerOnly();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack item = Utils.getItemInHand(player);

        if (CommandUtils.checkAir(player, item)) return CommandResult.FAILURE;

        itemHandler.addItem(args[1], item);
        language.getPrefixed(itemHandler.checkItemStorage(args[1]) ? "Item-Updated" : "Item-Saved")
                .replace("%item%", args[1])
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return null;
    }

    @Override
    public String getDefaultDescription() {
        return null;
    }

    @Override
    public ArgumentRange getRange() {
        return null;
    }
}
