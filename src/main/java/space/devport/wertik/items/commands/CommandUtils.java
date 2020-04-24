package space.devport.wertik.items.commands;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.utils.Utils;

@UtilityClass
public class CommandUtils {
    public boolean checkAir(CommandSender sender, ItemStack item) {
        if (item.getType() == Material.AIR) {
            ItemsPlugin.getInstance().getLanguageManager().sendPrefixed(sender, "Cannot-Help-With-Air");
            return true;
        } else return false;
    }

    public boolean checkAir(Player player) {
        return checkAir(player, Utils.getItemInHand(player));
    }

    public boolean checkAir(CommandSender sender) {
        return checkAir((Player) sender);
    }

    public boolean checkItemStored(CommandSender sender, String name) {
        if (!ItemsPlugin.getInstance().getItemManager().getItems().containsKey(name)) {
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

    public void sendDetail(CommandSender sender) {
        sendDetail(sender, Utils.getBuilderInHand((Player) sender));
    }

    public void sendDetail(CommandSender sender, ItemBuilder builder) {
        sender.sendMessage(StringUtil.color("&eName: &f" + (builder.getDisplayName().isEmpty() ? builder.getMaterial().toString() : builder.getDisplayName().toString())));
        sender.sendMessage(StringUtil.color("&eMaterial: &f" + builder.getMaterial().name()));
        sender.sendMessage(StringUtil.color("&eAmount: &f" + builder.getAmount()));

        // Lore
        if (!builder.getLore().getMessage().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eLore:"));
            int i = 0;
            for (String line : builder.getLore().getMessage()) {
                sender.sendMessage(StringUtil.color("&f " + i + " &8- &r" + line));
                i++;
            }
        }

        // Enchants
        if (!builder.getEnchants().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eEnchants:"));
            builder.getEnchants().forEach((enchantment, level) -> sender.sendMessage(StringUtil.color(" &8- &7" + enchantment.toString() + "&f;&7" + level)));
        }

        // Flags
        if (!builder.getFlags().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eFlags:"));
            builder.getFlags().forEach(flag -> sender.sendMessage(StringUtil.color(" &8- &7" + flag.toString())));
        }

        // NBT
        if (!builder.getNBT().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eNBT:"));

            for (String key : builder.getNBT().keySet()) {
                if (!ItemsPlugin.getInstance().getFilteredNBT().contains(key))
                    sender.sendMessage(StringUtil.color(" &8- &7" + key + "&f:&7" + builder.getNBT().get(key)));
            }
        }
    }

    public void sendDetail(CommandSender sender, ItemStack item) {
        sendDetail(sender, new ItemBuilder(item));
    }
}