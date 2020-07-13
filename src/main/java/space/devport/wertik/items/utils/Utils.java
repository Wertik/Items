package space.devport.wertik.items.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.utility.reflection.ServerVersion;
import space.devport.utils.utility.reflection.SpigotHelper;
import space.devport.wertik.items.ItemsPlugin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class Utils {

    public String mapToString(Map<?, ?> map, String splitter, String separator, String ifEmpty) {
        StringBuilder str = ifEmpty == null ? new StringBuilder() : new StringBuilder(ifEmpty);

        if (!map.isEmpty()) {
            str = new StringBuilder();

            int n = 0;
            for (Object key : map.keySet()) {
                str.append(key.toString()).append(separator).append(map.get(key).toString());
                n++;
                if (n < map.size() - 1)
                    str.append(splitter);
            }
        }

        return str.toString();
    }

    public String listToString(List<String> list, String splitter, String ifEmpty) {
        StringBuilder stringList = ifEmpty == null ? new StringBuilder("§cNaN") : new StringBuilder(ifEmpty);

        if (list != null)
            if (!list.isEmpty()) {
                stringList = new StringBuilder(list.get(0));

                for (int i = 1; i < list.size(); i++)
                    stringList.insert(0, list.get(i) + splitter);
            }
        return stringList.toString();
    }

    public void consumeItem(Player player, @Nullable EquipmentSlot hand, ItemStack item) {
        if (item.getAmount() == 1)
            setItem(player, hand, null);
        else {
            item.setAmount(item.getAmount() - 1);
            setItem(player, hand, item);
        }
    }

    public void setItem(Player player, @Nullable EquipmentSlot hand, ItemStack item) {
        if (ServerVersion.isBelowCurrent(ServerVersion.v1_8))
            player.setItemInHand(item);
        else {
            if (hand == null)
                player.getInventory().setItemInOffHand(item);
            else
                player.getInventory().setItem(hand, item);
        }
    }

    public ItemStack getItemInHand(Player player) {
        if (SpigotHelper.getVersion().contains("1.7") || SpigotHelper.getVersion().contains("1.8"))
            return player.getItemInHand();
        else
            return player.getInventory().getItemInMainHand();
    }

    public ItemBuilder getBuilderInHand(Player player) {
        return new ItemBuilder(getItemInHand(player));
    }

    public String parsePlaceholders(String string, Player player) {
        if (ItemsPlugin.getInstance().usePlaceholderAPI) string = PlaceholderAPI.setPlaceholders(player, string);
        return string;
    }

    public List<String> parsePlaceholders(List<String> list, Player player) {
        if (!ItemsPlugin.getInstance().usePlaceholderAPI) return list;
        return list.stream().map(line -> PlaceholderAPI.setPlaceholders(player, line)).collect(Collectors.toList());
    }
}