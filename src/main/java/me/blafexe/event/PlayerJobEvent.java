package me.blafexe.event;

import me.blafexe.job.Job;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJobEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }


    private final Job job;
    private final Status status;

    public PlayerJobEvent(@NotNull Player who, @NotNull Job job, @NotNull Status status) {
        super(who);
        this.job = job;
        this.status = status;
    }

    public Job getJob() {
        return job;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public enum Status {
        TAKE,
        FINISH,
        UPDATE
    }
}
