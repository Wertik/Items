package me.wertik.items.objects;

import me.wertik.items.Main;
import me.wertik.items.utils.Utils;

public class Attribute {

    private String name;

    private Reward reward;

    // Cooldown, in millis
    private Long cooldown;

    private boolean oneTime;

    private String cooldownMessage;
    private String cooldownExpireMessage;

    public Attribute(String name) {
        this.name = name;

        this.reward = new Reward();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public void setCooldown(Long cooldown) {
        this.cooldown = cooldown;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public String getCooldownMessage(double time) {
        return Utils.color((cooldownMessage != null ? cooldownMessage :
                Main.getInstance().getCfg().getYaml().getString("Strings.cooldown-message"))
                .replace("%time%", String.valueOf(Utils.round(time, 1))));
    }

    public void setCooldownMessage(String cooldownMessage) {
        this.cooldownMessage = cooldownMessage;
    }

    public String getCooldownExpireMessage() {
        return Utils.color((cooldownExpireMessage != null ? cooldownExpireMessage :
                Main.getInstance().getCfg().getYaml().getString("Strings.cooldown-expire-message"))
                .replace("%attributeName%", name));
    }

    public void setCooldownExpireMessage(String cooldownExpireMessage) {
        this.cooldownExpireMessage = cooldownExpireMessage;
    }

    public boolean hasCooldown() {
        Main.getInstance().cO.debug("Has cooldown? " + (cooldown != 0));
        return cooldown != 0;
    }

    public String getName() {
        return name;
    }

    public Long getCooldown() {
        return cooldown;
    }

    public Reward getReward() {
        return reward;
    }

    public boolean isOneTime() {
        return oneTime;
    }
}
