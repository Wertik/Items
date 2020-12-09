package space.devport.wertik.items.commands;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.language.LanguageManager;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.util.ItemUtil;

@UtilityClass
public class CommandUtils {

    public boolean checkAir(CommandSender sender, ItemStack item) {
        if (item.getType() == Material.AIR) {
            ItemsPlugin.getInstance().getManager(LanguageManager.class).sendPrefixed(sender, "Cannot-Help-With-Air");
            return true;
        } else return false;
    }

    public boolean checkAir(Player player) {
        return checkAir(player, ItemUtil.getItemInHand(player));
    }

    public boolean checkAir(CommandSender sender) {
        return checkAir((Player) sender);
    }

    public boolean checkItemStored(CommandSender sender, String name) {
        if (!ItemsPlugin.getInstance().getItemManager().getItems().containsKey(name)) {
            ItemsPlugin.getInstance().getManager(LanguageManager.class)
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
        sendDetail(sender, ItemUtil.getBuilderInHand((Player) sender));
    }

    public void sendDetail(CommandSender sender, ItemBuilder builder) {
        sender.sendMessage(space.devport.utils.text.StringUtil.color("&eName: &f" + (builder.getDisplayName().isEmpty() ? builder.getMaterial().toString() : builder.getDisplayName().toString())));
        sender.sendMessage(space.devport.utils.text.StringUtil.color("&eMaterial: &f" + builder.getMaterial().name()));
        sender.sendMessage(space.devport.utils.text.StringUtil.color("&eAmount: &f" + builder.getAmount()));

        // Lore
        if (!builder.getLore().getMessage().isEmpty()) {
            sender.sendMessage(space.devport.utils.text.StringUtil.color("&eLore:"));
            int i = 0;
            for (String line : builder.getLore().getMessage()) {
                sender.sendMessage(space.devport.utils.text.StringUtil.color("&f " + i + " &8- &r" + line));
                i++;
            }
        }

        // Enchants
        if (!builder.getEnchants().isEmpty()) {
            sender.sendMessage(space.devport.utils.text.StringUtil.color("&eEnchants:"));
            builder.getEnchants().forEach((enchantment, level) -> sender.sendMessage(space.devport.utils.text.StringUtil.color(" &8- &7" + enchantment.name() + "&f;&7" + level)));
        }

        // Flags
        if (!builder.getFlags().isEmpty()) {
            sender.sendMessage(space.devport.utils.text.StringUtil.color("&eFlags:"));
            builder.getFlags().forEach(flag -> sender.sendMessage(space.devport.utils.text.StringUtil.color(" &8- &7" + flag.toString())));
        }

        // NBT
        if (!builder.getNBT().isEmpty()) {
            sender.sendMessage(space.devport.utils.text.StringUtil.color("&eNBT:"));

            for (String key : builder.getNBT().keySet()) {
                if (!ItemBuilder.FILTERED_NBT.contains(key))
                    sender.sendMessage(space.devport.utils.text.StringUtil.color(" &8- &7" + key + "&f:&7" + builder.getNBT().get(key)));
            }
        }
    }
}