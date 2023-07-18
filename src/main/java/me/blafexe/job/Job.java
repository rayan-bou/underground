package me.blafexe.job;

import org.bukkit.event.Event;

/**
 * A job can be given to a player. The player has to fulfill a certain goal in order to complete the job.
 */
public interface Job {

    /**
     * Starts the job.
     */
    void start();

    /**
     * Ends the job.
     */
    void end();

    /**
     * @return True, if the jobs goal-condition was met.
     */
    boolean isFinished();

    /**
     * Disposes job resources.
     */
    void dispose();

    /**
     * Is usually called by the JobEngine whenever a player triggers an event.
     * @param event Any player-related event.
     */
    void handleEvent(Event event);

}
