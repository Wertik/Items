package space.devport.wertik.items.commands.items;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.system.ItemManager;

public class GiveItem extends SubCommand {

    private final ItemManager itemManager;

    public GiveItem(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[1])) return CommandResult.FAILURE;

        String argStr = String.join(" ", args);
        boolean raw = argStr.contains(" -r");

        String[] newArgs = raw ? argStr.replace(" -r", "").split(" ") : args;

        Player target = null;
        OfflinePlayer offlineTarget;

        if (newArgs.length > 2) {
            offlineTarget = CommandUtils.parsePlayer(newArgs[2]);

            if (offlineTarget != null) {
                target = offlineTarget.getPlayer();
            } else {
                if (newArgs.length > 3)
                    offlineTarget = CommandUtils.parsePlayer(newArgs[3]);

                if (offlineTarget != null)
                    target = offlineTarget.getPlayer();
            }
        }

        if (target == null) {
            if (!(sender instanceof Player)) return CommandResult.NO_CONSOLE;
            target = (Player) sender;
        }

        int amt = 0;

        if (newArgs.length > 2) {
            amt = CommandUtils.parseAmount(newArgs[2]);
            if (amt <= 0 && newArgs.length > 3) {

                amt = CommandUtils.parseAmount(newArgs[3]);

                if (amt <= 0) {
                    language.getPrefixed("Not-A-Number")
                            .replace("%param%", newArgs[3])
                            .send(sender);
                    return CommandResult.FAILURE;
                }
            }
        }

        int amount;
        ItemStack giveItem;

        if (amt <= 0)
            amount = 1;
        else amount = amt;

        // Raw item
        if (raw) {
            giveItem = itemManager.getItem(args[1]);
        } else giveItem = itemManager.prepareItem(args[1], target);

        for (int i = 0; i < amount; i++) target.getInventory().addItem(giveItem);

        language.getPrefixed(raw ? "Item-Given-Raw" : "Item-Given")
                .replace("%item%", args[1])
                .replace("%player%", target.getName())
                .replace("%amount%", "" + amount)
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% give <name> (playerName) (amount) (-r)";
    }

    @Override
    public String getDefaultDescription() {
        return "Give yourself or someone else an item. ( -r makes the item raw, unparsed, for editing )";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1, 4);
    }
}
