package space.devport.wertik.items.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.utility.reflection.ServerVersion;

@UtilityClass
public class ItemUtil {

    public void consumeItem(Player player, EquipmentSlot hand, ItemStack item) {
        if (item.getAmount() == 1)
            setItem(player, hand, null);
        else {
            item.setAmount(item.getAmount() - 1);
            setItem(player, hand, item);
        }
    }

    @SuppressWarnings("deprecation")
    public void setItem(Player player, EquipmentSlot hand, ItemStack item) {
        PlayerInventory inventory = player.getInventory();

        if (ServerVersion.isCurrentBelow(ServerVersion.v1_8))
            player.setItemInHand(item);
        else if (ServerVersion.isCurrentAbove(ServerVersion.v1_16)) {
            inventory.setItem(hand, item);
        } else {
            if (hand == EquipmentSlot.HAND)
                inventory.setItemInMainHand(item);
            else if (hand == EquipmentSlot.OFF_HAND)
                inventory.setItemInOffHand(item);
        }
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemInHand(Player player) {
        if (ServerVersion.isCurrentBelow(ServerVersion.v1_8))
            return player.getItemInHand();
        else
            return player.getInventory().getItemInMainHand();
    }

    public ItemBuilder getBuilderInHand(Player player) {
        return new ItemBuilder(getItemInHand(player));
    }
}
