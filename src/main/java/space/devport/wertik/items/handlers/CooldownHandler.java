package space.devport.wertik.items.handlers;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.objects.Attribute;
import space.devport.wertik.items.objects.Cooldown;
import space.devport.wertik.items.utils.Utils;

import java.util.*;

public class CooldownHandler {

    // uniqueID, Cooldowns
    private HashMap<String, List<Cooldown>> cooldownCache = new HashMap<>();

    // Remove all cooldowns on reload
    // TODO Add persist later
    public void reload() {
        cooldownCache.clear();
    }

    // Start a cooldown for a player
    public void addCooldown(UUID uniqueID, Attribute attribute) {

        // No need to start if it has no cooldown time configured.
        if (!attribute.hasCooldown())
            return;

        // Add a new cooldown
        List<Cooldown> cooldowns = cooldownCache.containsKey(uniqueID.toString()) ? cooldownCache.get(uniqueID.toString()) : new ArrayList<>();

        // Add a new cooldown to the list, so we can pull the time later.
        cooldowns.add(new Cooldown(attribute.getName(), System.currentTimeMillis() + (attribute.getCooldown() * 1000)));
        cooldownCache.put(uniqueID.toString(), cooldowns);

        // Start a runnable to remove it
        new BukkitRunnable() {
            @Override
            public void run() {
                removeCooldown(uniqueID, attribute.getName());
            }
        }.runTaskLaterAsynchronously(Main.inst, attribute.getCooldown() * 20);
    }

    // Remove cooldown from a player
    public void removeCooldown(UUID uniqueID, String attributeName) {

        // Just to make sure there's something to remove.
        if (!cooldownCache.containsKey(uniqueID.toString()))
            return;

        List<Cooldown> cooldowns = cooldownCache.get(uniqueID.toString());

        // If there is only one cooldown or none, remove altogether.
        if (cooldowns.size() == 1 || cooldowns.isEmpty()) {
            Main.inst.cO.debug("Removing " + uniqueID.toString() + " from cooldowns");
            cooldownCache.remove(uniqueID.toString());
            return;
        }

        // Loop through and find the cooldown, remove it.
        Optional<Cooldown> first = cooldowns.stream()
                .filter(cd -> cd.getAttributeName().equals(attributeName))
                .findFirst();
        first.ifPresent(cooldowns::remove);

        // If there is only one cooldown, remove altogether.
        if (cooldowns.isEmpty()) {
            Main.inst.cO.debug("Removing " + uniqueID.toString() + " from cooldowns");
            cooldownCache.remove(uniqueID.toString());
            return;
        }

        cooldownCache.put(uniqueID.toString(), cooldowns);
        Main.inst.cO.debug("Updated " + uniqueID.toString() + " to cooldown size " + cooldowns.size());

        // Message the player
        OfflinePlayer player = Main.inst.getServer().getOfflinePlayer(uniqueID);
        if (player.isOnline())
            player.getPlayer().sendMessage(Utils.parse(Main.inst.getAttributeHandler().get(attributeName).getCooldownExpireMessage(), player.getPlayer()));
    }

    // Get a cooldown
    public Cooldown getCooldown(String uniqueID, String attributeName) {

        // He has no cooldowns at all
        if (!cooldownCache.containsKey(uniqueID)) return null;

        // Find desired cooldown
        Optional<Cooldown> first = cooldownCache.get(uniqueID).stream().filter(cd -> cd.getAttributeName().equals(attributeName)).findFirst();

        // Return it if present
        return first.orElse(null);
    }

    // Return cooldown time left in seconds
    public double getCooldownTime(String uniqueID, String attributeName) {
        return (getCooldown(uniqueID, attributeName).getTime() - System.currentTimeMillis()) / 1000D;
    }

    // Return whether or not the player can use the attribute
    public boolean isUsable(String uniqueID, String attributeName) {
        if (!cooldownCache.containsKey(uniqueID))
            return true;

        // Stream through the cooldowns the player has, if we find any with correct attribute name, return false, has cooldown.
        return cooldownCache.get(uniqueID).stream().noneMatch(cd -> cd.getAttributeName().equals(attributeName));
    }
}