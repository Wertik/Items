package space.devport.wertik.items.objects;

import lombok.Getter;
import lombok.Setter;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.utils.Utils;

public class Attribute {

    // Name of the attribute
    @Getter
    private final String name;

    // Attribute reward - commands and messages
    @Getter
    @Setter
    private Reward reward;

    // Attribute cooldown
    // In seconds
    @Getter
    @Setter
    private Long cooldown;

    // Remove on use?
    @Getter
    @Setter
    private boolean oneTime;

    // Custom cooldown message when the player tries to use the attribute
    @Setter
    private String cooldownMessage;

    // Custom cooldown message when the player can use the item again
    @Setter
    private String cooldownExpireMessage;

    // Default constructor
    public Attribute(String name) {
        this.name = name;
        this.reward = new Reward();
    }

    // Returns parsed and colored cooldown message
    // If it's absent, uses default
    public String getCooldownMessage(double time) {
        return Utils.color((cooldownMessage != null ? cooldownMessage : Main.inst.getCfg().getFileConfiguration().getString("Strings.cooldown-message"))
                .replace("%time%", String.valueOf(Utils.round(time, 1))));
    }

    // Returns parsed and colored cooldown expire message
    // If it's absent, uses default
    public String getCooldownExpireMessage() {
        return Utils.color((cooldownExpireMessage != null ? cooldownExpireMessage : Main.inst.getCfg().getFileConfiguration().getString("Strings.cooldown-expire-message"))
                .replace("%attributeName%", name));
    }

    // Returns whether or not the item has a cooldown configured
    public boolean hasCooldown() {
        return cooldown != 0;
    }
}