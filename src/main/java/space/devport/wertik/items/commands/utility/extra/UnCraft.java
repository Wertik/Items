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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnCraft extends SubCommand {

    private final ItemManager itemManager;

    public UnCraft(ItemsPlugin plugin) {
        super("uncraft");
        this.itemManager = plugin.getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        ItemStack item = ItemUtil.getItemInHand(player);

        boolean unCraft = args.length > 0 ?
                Boolean.parseBoolean(args[0]) :
                !itemManager.hasExtra(item, "uncraftable");

        ItemUtil.setItem(player, EquipmentSlot.HAND, unCraft ?
                itemManager.setExtra(item, "uncraftable") :
                itemManager.removeExtra(item, "uncraftable"));

        language.getPrefixed("Set-Uncraftable")
                .replace("%state%", String.valueOf(unCraft))
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull List<String> requestTabComplete(CommandSender sender, String[] args) {
        return args.length == 0 ? Arrays.asList("true", "false") : new ArrayList<>();
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% uncraft (true/false)";
    }

    @Override
    public String getDefaultDescription() {
        return "Players won't be able to craft with this item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}