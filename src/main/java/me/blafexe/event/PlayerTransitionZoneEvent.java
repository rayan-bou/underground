package me.blafexe.event;

import me.blafexe.zone.Zone;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerTransitionZoneEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }


    private final Zone zone;
    private final boolean isInside;

    public PlayerTransitionZoneEvent(@NotNull Player who, @NotNull Zone zone, boolean isInside) {
        super(who);
        this.zone = zone;
        this.isInside = isInside;
    }

    public Zone getZone() {
        return zone;
    }

    public boolean isInside() {
        return isInside;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
