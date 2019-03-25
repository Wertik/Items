package me.wertik.items.objects;

public class Cooldown {

    // Assigned attribute
    private String attributeName;

    // Final time -- when you can use it again.
    private long time;

    public Cooldown(String attributeName, long time) {
        this.attributeName = attributeName;
        this.time = time;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public long getTime() {
        return time;
    }
}
