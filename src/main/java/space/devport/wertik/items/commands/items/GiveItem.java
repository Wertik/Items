package space.devport.wertik.items.commands.items;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GiveItem extends SubCommand {

    private final ItemManager itemManager;

    public GiveItem(String name) {
        super(name);
        this.preconditions = new Preconditions().permissions("items.manage.give");

        setAliases("get");
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[0])) return CommandResult.FAILURE;

        String argStr = String.join(" ", args);
        boolean raw = argStr.contains(" -r");

        String[] newArgs = raw ? argStr.replace(" -r", "").split(" ") : args;

        Player target = null;
        OfflinePlayer offlineTarget;

        if (newArgs.length > 1) {
            offlineTarget = CommandUtils.parsePlayer(newArgs[1]);

            if (offlineTarget != null) {
                target = offlineTarget.getPlayer();
            } else {
                if (newArgs.length > 2)
                    offlineTarget = CommandUtils.parsePlayer(newArgs[2]);

                if (offlineTarget != null)
                    target = offlineTarget.getPlayer();
            }
        }

        if (target == null) {
            if (!(sender instanceof Player)) return CommandResult.NO_CONSOLE;
            target = (Player) sender;
        }

        int amt = 0;

        if (newArgs.length > 1) {
            amt = CommandUtils.parseAmount(newArgs[1]);

            if (amt <= 0 && newArgs.length > 2) {

                amt = CommandUtils.parseAmount(newArgs[2]);

                if (amt <= 0) {
                    language.getPrefixed("Not-A-Number")
                            .replace("%param%", newArgs[1])
                            .send(sender);
                    return CommandResult.FAILURE;
                }
            }
        }

        int amount;
        if (amt <= 0)
            amount = 1;
        else amount = amt;

        ItemStack giveItem;
        if (raw) {
            giveItem = itemManager.getItem(args[0]);
        } else giveItem = itemManager.prepareItem(args[0], target);

        for (int i = 0; i < amount; i++) target.getInventory().addItem(giveItem);

        language.getPrefixed(raw ? "Item-Given-Raw" : "Item-Given")
                .replace("%item%", args[0])
                .replace("%player%", target.getName())
                .replace("%amount%", amount)
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            suggestions.addAll(itemManager.getItems().keySet());
        } else if (args.length == 1) {
            suggestions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        }

        Collections.sort(suggestions);
        return suggestions;
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
