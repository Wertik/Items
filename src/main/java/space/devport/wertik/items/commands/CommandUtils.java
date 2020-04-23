package space.devport.wertik.items.commands;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.items.ItemsPlugin;

@UtilityClass
public class CommandUtils {
    public boolean checkAir(Player player, ItemStack item) {
        if (item.getType() == Material.AIR) {
            ItemsPlugin.getInstance().getLanguageManager().sendPrefixed(player, "Cannot-Help-With-Air");
            return true;
        } else return false;
    }

    public boolean checkItemStored(CommandSender sender, String name) {
        if (!ItemsPlugin.getInstance().getItemHandler().getItems().containsKey(name)) {
            ItemsPlugin.getInstance().getLanguageManager()
                    .getPrefixed("Item-Not-Valid")
                    .replace("%item%", name)
                    .send(sender);
            return true;
        } else return false;
    }

    public int parseAmount(String arg) {
        int amount = -1;
        try {
            amount = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
        }
        return amount;
    }

    public OfflinePlayer parsePlayer(String arg) {
        OfflinePlayer offlineTarget = Bukkit.getPlayer(arg);

        if (offlineTarget == null ||
                !offlineTarget.isOnline() ||
                offlineTarget.getPlayer() == null)
            return null;

        return offlineTarget;
    }
}