package me.wertik.items.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class AttributeFireEvent extends PlayerEvent {
    public AttributeFireEvent(Player who) {
        super(who);
    }

    @Override
    public String getEventName() {
        return super.getEventName();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private HandlerList handlerList;

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
