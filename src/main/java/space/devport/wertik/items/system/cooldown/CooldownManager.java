package space.devport.wertik.items.system.cooldown;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import space.devport.utils.text.language.LanguageManager;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.system.attribute.struct.Attribute;
import space.devport.wertik.items.system.cooldown.struct.Cooldown;

import java.util.*;

public class CooldownManager {

    private final ItemsPlugin plugin;

    @Getter
    private final Map<UUID, Set<Cooldown>> cooldownCache = new HashMap<>();

    public CooldownManager(ItemsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean triggerCooldown(Player player, Attribute attribute) {
        if (!isUsable(player, attribute.getName()))
            return false;

        startCooldown(player, attribute);
        return true;
    }

    public void startCooldown(Player player, Attribute attribute) {
        startCooldown(player.getUniqueId(), attribute);
    }

    // Start cooldown for a player
    public void startCooldown(UUID uniqueID, Attribute attribute) {

        if (!attribute.hasCooldown()) {
            return;
        }

        Set<Cooldown> cooldowns = cooldownCache.getOrDefault(uniqueID, new HashSet<>());

        Cooldown cooldown = new Cooldown(attribute, uniqueID);

        cooldowns.add(cooldown);

        this.cooldownCache.put(uniqueID, cooldowns);

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,
                () -> clearCooldown(uniqueID, attribute.getName()),
                attribute.getCooldown() * 20L);
    }

    // Remove cooldown
    public void clearCooldown(UUID uniqueID, String attributeName) {

        // Just to make sure there's something to remove.
        if (!cooldownCache.containsKey(uniqueID)) {
            return;
        }

        Set<Cooldown> cooldowns = cooldownCache.get(uniqueID);

        // Loop through and find the cooldown, remove it.
        cooldowns.removeIf(cd -> cd.getAttributeName().equals(attributeName));

        // Remove if empty
        if (cooldowns.isEmpty()) {
            this.cooldownCache.remove(uniqueID);
            return;
        }

        // Message the player, if online
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uniqueID);

        if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null)
            plugin.getManager(LanguageManager.class).sendPrefixed(offlinePlayer.getPlayer(), "Cooldown-Expired");
    }

    public Optional<Cooldown> getCooldown(UUID uniqueID, String attributeName) {
        return cooldownCache.containsKey(uniqueID) ? cooldownCache.get(uniqueID).stream()
                .filter(cd -> cd.getAttributeName().equals(attributeName))
                .findFirst() : Optional.empty();
    }

    public double getTimeRemaining(Player player, String attributeName) {
        return getTimeRemaining(player.getUniqueId(), attributeName);
    }

    public double getTimeRemaining(UUID uniqueID, String attributeName) {
        return getCooldown(uniqueID, attributeName)
                .map(value -> value.getTime() - System.currentTimeMillis())
                .orElse(0L);
    }

    public boolean isUsable(Player player, String attributeName) {
        return isUsable(player.getUniqueId(), attributeName);
    }

    // Return whether or not the player can use the attribute
    public boolean isUsable(UUID uniqueID, String attributeName) {
        if (!cooldownCache.containsKey(uniqueID)) {
            return true;
        }

        Optional<Cooldown> cd = this.cooldownCache.get(uniqueID).stream()
                .filter(i -> i.getAttributeName().equalsIgnoreCase(attributeName))
                .findAny();

        if (!cd.isPresent())
            return true;

        if (System.currentTimeMillis() > cd.get().getTime()) {
            clearCooldown(uniqueID, attributeName);
            return true;
        }

        return false;
    }
}