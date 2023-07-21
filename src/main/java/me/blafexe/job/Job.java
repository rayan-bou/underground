package me.blafexe.job;

import me.blafexe.infoview.InfoviewElement;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * A job can be given to a player. The player has to fulfill a certain goal in order to complete the job.
 * Requires a <code>JobEngine</code> object in order to assign the job to a player.
 */
public interface Job extends InfoviewElement {

    /**
     * Starts the job.
     */
    void start(Player player);

    /**
     * Ends the job.
     */
    void end(Player player);

    /**
     * Is usually called by the JobEngine whenever a player triggers an event.
     *
     * @param event     Any player-related event.
     * @param jobEngine
     */
    void handleEvent(Event event, JobEngine jobEngine);

}
