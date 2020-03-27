package space.devport.wertik.items.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.devport.utils.SpigotHelper;

import java.util.List;
import java.util.Map;

@UtilityClass
public class Utils {

    public String mapToString(Map<?, ?> map, String splitter, String separator, String ifEmpty) {
        StringBuilder str = ifEmpty == null ? null : new StringBuilder(ifEmpty);

        if (!map.isEmpty()) {
            str = new StringBuilder();
            for (Object key : map.keySet()) {
                str.append(key.toString()).append(separator).append(map.get(key).toString()).append(splitter);
            }
        }

        return str == null ? null : str.toString();
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

    public void consumeItem(Player player, EquipmentSlot hand, ItemStack item) {
        if (item.getAmount() == 1)
            setItem(player, hand, null);
        else {
            item.setAmount(item.getAmount() - 1);
            setItem(player, hand, item);
        }
    }

    public void setItem(Player player, EquipmentSlot hand, ItemStack item) {
        if (SpigotHelper.getVersion().contains("1.7") || SpigotHelper.getVersion().contains("1.8"))
            player.setItemInHand(item);
        else {
            if (hand == EquipmentSlot.HAND)
                player.getInventory().setItemInMainHand(item);
            else
                player.getInventory().setItemInOffHand(item);
        }
    }

    public ItemStack getItem(Player player) {
        if (SpigotHelper.getVersion().contains("1.7") || SpigotHelper.getVersion().contains("1.8"))
            return player.getItemInHand();
        else
            return player.getInventory().getItemInMainHand();
    }
}