package me.wertik.items.objects;

import me.wertik.items.Main;

public class Attribute {

    private String name;

    // Req and cost will be used later, mby.
    private Requirement requirement;
    private Cost cost;

    private Reward reward;

    // Cooldown, in millis
    private Long cooldown;

    // Todo Add particles, somehow

    public Attribute(String name, Reward reward, Long cooldown) {
        this.name = name;
        this.reward = reward;
        this.cooldown = cooldown;
    }

    public boolean hasCooldown() {
        Main.getInstance().cw.debug("Has cooldown? " + (cooldown != 0));
        return cooldown != 0;
    }

    public String getName() {
        return name;
    }

    public Long getCooldown() {
        return cooldown;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public Cost getCost() {
        return cost;
    }

    public Reward getReward() {
        return reward;
    }
}
