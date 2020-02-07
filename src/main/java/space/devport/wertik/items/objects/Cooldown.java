package space.devport.wertik.items.objects;

import lombok.Getter;

public class Cooldown {

    // This class is only used for holding the time.
    
    // Attribute the cooldown is on.
    @Getter
    private String attributeName;

    // Cd expire time
    @Getter
    private long time;

    public Cooldown(String attributeName, long time) {
        this.attributeName = attributeName;
        this.time = time;
    }
}