package space.devport.wertik.items.handlers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.objects.Cooldown;
import space.devport.wertik.items.utils.Language;

import java.util.*;

public class CooldownHandler {

    // UUID, Cooldowns
    @Getter
    private final Map<UUID, List<Cooldown>> cooldownCache = new HashMap<>();

    public void addCooldown(Player player, Attribute attribute) {
        addCooldown(player.getUniqueId(), attribute);
    }

    // Start cooldown for a player
    public void addCooldown(UUID uniqueID, Attribute attribute) {

        if (!attribute.hasCooldown()) {
            return;
        }

        // Add a new cooldown, or update already existing ones
        List<Cooldown> cooldowns = cooldownCache.containsKey(uniqueID) ? cooldownCache.get(uniqueID) : new ArrayList<>();

        Cooldown cooldown = new Cooldown(attribute.getName(), uniqueID, System.currentTimeMillis() + attribute.getCooldown());

        cooldowns.add(cooldown);

        this.cooldownCache.put(uniqueID, cooldowns);

        cooldown.runTaskLaterAsynchronously(ItemsPlugin.getInstance(), attribute.getCooldown() * 20);
    }

    public void removeCooldown(Player player, String attributeName) {
        removeCooldown(player.getUniqueId(), attributeName);
    }

    // Remove cooldown
    public void removeCooldown(UUID uniqueID, String attributeName) {

        // Just to make sure there's something to remove.
        if (!cooldownCache.containsKey(uniqueID)) {
            return;
        }

        List<Cooldown> cooldowns = cooldownCache.get(uniqueID);

        // Loop through and find the cooldown, remove it.
        cooldowns.stream()
                .filter(cd -> cd.getAttributeName().equals(attributeName))
                .findFirst()
                .ifPresent(cooldowns::remove);

        // Remove if empty
        if (cooldowns.isEmpty()) {
            this.cooldownCache.remove(uniqueID);
            return;
        }

        // Update
        this.cooldownCache.put(uniqueID, cooldowns);

        // Message the player, if online
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uniqueID);

        if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null)
            Language.COOLDOWN_EXPIRED.get().send(offlinePlayer.getPlayer());
    }

    public Cooldown getCooldown(Player player, String attributeName) {
        return getCooldown(player.getUniqueId(), attributeName);
    }

    // Get a cooldown
    public Cooldown getCooldown(UUID uniqueID, String attributeName) {

        if (!cooldownCache.containsKey(uniqueID)) {
            return null;
        }

        Optional<Cooldown> first = cooldownCache.get(uniqueID)
                .stream()
                .filter(cd -> cd.getAttributeName().equals(attributeName))
                .findFirst();

        return first.orElse(null);
    }

    public double getTimeRemaining(Player player, String attributeName) {
        return getTimeRemaining(player.getUniqueId(), attributeName);
    }

    public double getTimeRemaining(UUID uniqueID, String attributeName) {
        return (getCooldown(uniqueID, attributeName).getTime() - System.currentTimeMillis());
    }

    public boolean isUsable(Player player, String attributeName) {
        return isUsable(player.getUniqueId(), attributeName);
    }

    // Return whether or not the player can use the attribute
    public boolean isUsable(UUID uniqueID, String attributeName) {
        if (!cooldownCache.containsKey(uniqueID)) {
            return true;
        }

        return this.cooldownCache.get(uniqueID)
                .stream()
                .noneMatch(cd -> cd.getAttributeName().equals(attributeName));
    }
}