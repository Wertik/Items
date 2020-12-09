package space.devport.wertik.items.commands.utility.extra;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.util.ItemUtil;
import space.devport.wertik.items.system.item.ItemManager;
import space.devport.wertik.items.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UnStack extends SubCommand {

    private final ItemManager itemManager;

    public UnStack(ItemsPlugin plugin) {
        super("unstack");
        this.itemManager = plugin.getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        ItemStack item = ItemUtil.getItemInHand(player);

        boolean unStack = args.length > 0 ?
                Boolean.parseBoolean(args[0]) :
                !itemManager.hasExtra(item, "unstackable");

        ItemUtil.setItem(player, EquipmentSlot.HAND, unStack ?
                itemManager.setExtra(item, "unstackable", UUID.randomUUID().toString()) :
                itemManager.removeExtra(item, "unstackable"));

        language.getPrefixed("Set-Unstackable")
                .replace("%state%", String.valueOf(unStack))
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull List<String> requestTabComplete(CommandSender sender, String[] args) {
        return args.length == 0 ? Arrays.asList("true", "false") : new ArrayList<>();
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% unstack (true/false)";
    }

    @Override
    public String getDefaultDescription() {
        return "Make an item not stack with anything else.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}