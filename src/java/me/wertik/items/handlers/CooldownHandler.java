package me.wertik.items.handlers;

import me.wertik.items.Main;
import me.wertik.items.objects.Attribute;
import me.wertik.items.objects.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CooldownHandler {

    private Main plugin;
    private AttributeHandler attributeHandler;

    // uniqueID, (AttributeName, cooldownTime)
    private HashMap<String, List<Cooldown>> cooldowns;

    private BukkitTask checker;

    public CooldownHandler() {
        plugin = Main.getInstance();

        cooldowns = new HashMap<>();

        attributeHandler = plugin.getAttributeHandler();

        if (plugin.getConfig().getBoolean("enable-cooldown-automatic")) {
            startChecker();
            plugin.cO.info("Cooldown checker started.");
        }
    }

    public void reload() {
        if (checker != null) {
            checker.cancel();
            checker = null;
        }

        cooldowns.clear();

        if (plugin.getConfig().getBoolean("enable-cooldown-automatic")) {
            startChecker();
            plugin.cO.info("Cooldown checker started.");
        }
    }

    public void startChecker() {
        checker = new BukkitRunnable() {
            @Override
            public void run() {
                for (String uniqueID : cooldowns.keySet()) {
                    for (Cooldown cooldown : cooldowns.get(uniqueID)) {

                        if (cooldown.getTime() <= System.currentTimeMillis()) {

                            if (Bukkit.getPlayer(UUID.fromString(uniqueID)) == null) {
                                plugin.cO.debug("Removed offline player from cooldown list " + uniqueID);
                                cooldowns.remove(uniqueID);
                                continue;
                            }

                            Player player = Bukkit.getPlayer(UUID.fromString(uniqueID));
                            Attribute attribute = attributeHandler.getAttribute(cooldown.getAttributeName());

                            if (!player.isOnline())
                                continue;

                            if (plugin.getConfig().getBoolean("inform-on-cd"))
                                player.sendMessage(attribute.getCooldownExpireMessage().replace("%attributeName%", cooldown.getAttributeName()));

                            plugin.cO.debug("Removed from cooldown list " + uniqueID);
                            cooldowns.remove(uniqueID);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, plugin.getConfig().getInt("cooldown-checker-loop-time") * 20);
    }

    public void addCooldown(String uniqueID, Attribute attribute) {
        if (attribute.hasCooldown()) {
            Cooldown cooldown = new Cooldown(attribute.getName(), System.currentTimeMillis() + attribute.getCooldown());

            List<Cooldown> playerCooldowns = new ArrayList<>();

            if (cooldowns.containsKey(uniqueID))
                playerCooldowns = cooldowns.get(uniqueID);

            playerCooldowns.add(cooldown);

            cooldowns.put(uniqueID, playerCooldowns);
            plugin.cO.debug("Added to cooldown list " + uniqueID);

            for (Cooldown cooldown1 : cooldowns.get(uniqueID))
                plugin.cO.debug(cooldown1.getAttributeName() + " - " + cooldown1.getTime());
        }
    }

    public Cooldown getCooldown(String uniqueID, String attributeName) {
        List<Cooldown> playerCooldowns = cooldowns.get(uniqueID);

        for (Cooldown cooldown : playerCooldowns) {
            if (cooldown.getAttributeName().equals(attributeName)) {
                Main.getInstance().cO.debug("Return: " + cooldown.getAttributeName() + " - " + cooldown.getTime());
                return cooldown;
            }
        }

        return null;
    }

    public double getCooldownTime(String uniqueID, String attributeName) {
        Cooldown cooldown = getCooldown(uniqueID, attributeName);
        return (cooldown.getTime() - System.currentTimeMillis()) / 1000D;
    }

    public boolean isUsable(String uniqueID, String attributeName) {
        if (cooldowns.containsKey(uniqueID)) {
            Cooldown cooldown = getCooldown(uniqueID, attributeName);

            if (cooldown == null)
                return true;

            if (cooldown.getTime() <= System.currentTimeMillis()) {
                plugin.cO.debug("Removed from cooldown list on usage " + uniqueID);
                cooldowns.remove(uniqueID);
                plugin.cO.debug("Is usable.");
                return true;
            } else {
                Main.getInstance().cO.debug("Is not usable.");
                return false;
            }
        }

        Main.getInstance().cO.debug("Is usable.");
        return true;
    }
}
