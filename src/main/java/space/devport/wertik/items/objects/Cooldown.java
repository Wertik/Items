package space.devport.wertik.items.objects;

import lombok.Getter;
import lombok.Setter;

public class Cooldown {

    // This class is only used for holding the time.
    
    // Attribute the cooldown is on.
    @Getter
    private final String attributeName;

    // Cd expire time
    @Getter
    @Setter
    private long time;

    public Cooldown(String attributeName, long time) {
        this.attributeName = attributeName;
        this.time = time;
    }
}