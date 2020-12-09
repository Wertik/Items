package space.devport.wertik.items.system.cooldown.struct;

import lombok.Getter;
import lombok.Setter;
import space.devport.wertik.items.system.attribute.struct.Attribute;

import java.util.UUID;

public class Cooldown {

    @Getter
    private final String attributeName;

    @Getter
    private final UUID uniqueID;

    @Getter
    @Setter
    private long time;

    public Cooldown(Attribute attribute, UUID uniqueID) {
        this.attributeName = attribute.getName();
        this.uniqueID = uniqueID;
        this.time = System.currentTimeMillis() + attribute.getCooldown();
    }
}