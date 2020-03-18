package space.devport.wertik.items.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import space.devport.wertik.items.ItemsPlugin;

import java.util.UUID;

public class Cooldown extends BukkitRunnable {

    // Attribute the cooldown
    @Getter
    private final String attributeName;

    @Getter
    private final UUID player;

    // Expire time
    @Getter
    @Setter
    private long time;

    public Cooldown(String attributeName, UUID player, long time) {
        this.attributeName = attributeName;
        this.player = player;
        this.time = time;
    }

    @Override
    public void run() {
        ItemsPlugin.getInstance().getCooldownHandler().removeCooldown(player, attributeName);
    }
}