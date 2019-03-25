package me.wertik.items.handlers;

import me.wertik.items.Main;
import me.wertik.items.objects.Attribute;
import me.wertik.items.objects.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CooldownHandler {

    private Main plugin;
    private AttributeHandler attributeHandler;
    private ItemHandler itemHandler;

    // Todo add cooldown saving upon reload

    // uniqueID, (AttributeName, cooldownTime)
    private HashMap<String, List<Cooldown>> cooldowns;

    public CooldownHandler() {
        plugin = Main.getInstance();
        cooldowns = new HashMap<>();
        attributeHandler = plugin.getAttributeHandler();
        itemHandler = plugin.getItemHandler();

        if (plugin.getConfigLoader().getConfig().getBoolean("enable-cooldown-automatic")) {
            startChecker();
            plugin.cw.info("Cooldown checker started.");
        }
    }

    public void startChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String uniqueID : cooldowns.keySet()) {
                    for (Cooldown cooldown : cooldowns.get(uniqueID)) {
                        if (cooldown.getTime() <= System.currentTimeMillis()) {
                            Player player = Bukkit.getPlayer(UUID.fromString(uniqueID));
                            if (player.isOnline()) {
                                if (plugin.getConfigLoader().getConfig().getBoolean("inform-on-cd"))
                                    player.sendMessage("§eYou can use " + cooldown.getAttributeName() + " again!");
                                plugin.cw.debug("Removed from cooldown list " + uniqueID);
                                cooldowns.remove(uniqueID);
                                continue;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, plugin.getConfigLoader().getConfig().getInt("cooldown-checker-loop-time") * 20);
    }

    public void addCooldown(String uniqueID, Attribute attribute) {
        if (attribute.hasCooldown()) {
            Cooldown cooldown = new Cooldown(attribute.getName(), System.currentTimeMillis() + attribute.getCooldown());

            List<Cooldown> playerCooldowns = new ArrayList<>();

            if (cooldowns.containsKey(uniqueID))
                playerCooldowns = cooldowns.get(uniqueID);

            playerCooldowns.add(cooldown);
            cooldowns.put(uniqueID, playerCooldowns);
            plugin.cw.debug("Added to cooldown list " + uniqueID);

            for (Cooldown cooldown1 : cooldowns.get(uniqueID))
                plugin.cw.debug(cooldown1.getAttributeName() + " - " + cooldown1.getTime());
        }
    }

    public Cooldown getCooldown(String uniqueID, String attributeName) {
        if (cooldowns.containsKey(uniqueID)) {
            List<Cooldown> playerCooldowns = cooldowns.get(uniqueID);
            for (Cooldown cooldown : playerCooldowns) {
                if (cooldown.getAttributeName().equals(attributeName)) {
                    Main.getInstance().cw.debug("Return: " + cooldown.getAttributeName() + " - " + cooldown.getTime());
                    return cooldown;
                }
            }
        }
        return null;
    }

    public double getCooldownTime(String uniqueID, String attributeName) {
        Cooldown cooldown = getCooldown(uniqueID, attributeName);
        return (System.currentTimeMillis() - cooldown.getTime()) / -1000;
    }

    public boolean isUsable(String uniqueID, String attributeName) {
        if (cooldowns.containsKey(uniqueID)) {
            Cooldown cooldown = getCooldown(uniqueID, attributeName);
            if (System.currentTimeMillis() >= cooldown.getTime()) {
                plugin.cw.debug("Removed from cooldown list on usage " + uniqueID);
                cooldowns.remove(uniqueID);
                Main.getInstance().cw.debug("Is usable.");
                return true;
            } else {
                Main.getInstance().cw.debug("Is not usable.");
                return false;
            }
        }
        Main.getInstance().cw.debug("Is usable.");
        return true;
    }

// Todo

    public void saveCooldown(String uniqueID) {

    }

    public void loadCooldown(String uniqueID) {

    }
}
