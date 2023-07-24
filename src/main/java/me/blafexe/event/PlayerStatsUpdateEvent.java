package me.blafexe.event;

import me.blafexe.player.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerStatsUpdateEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }


    private final Stats stats;

    public PlayerStatsUpdateEvent(Player player, Stats stats) {
        super(player);
        this.stats = stats;
    }

    public Stats getStats() {
        return stats;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
