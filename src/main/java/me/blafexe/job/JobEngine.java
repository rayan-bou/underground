package me.blafexe.job;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

/**
 * The JobEngine manages every active job any player has.
 */
public class JobEngine implements Listener {

    private final JobEventPipeline jobEventPipeline;

    public JobEngine() {
        jobEventPipeline = new JobEventPipeline();
    }

    public void giveJob(Player player, Job job) {

        if (jobEventPipeline.addToJobList(player, job)) {
            //TODO start job
        }

    }

    public void removeJob(Player player, Job job) {

        if (jobEventPipeline.removeFromJobList(player, job)) {
            //Dispose Job
        }

    }

    @EventHandler
    public void onEvent(PlayerEvent playerEvent) {
        jobEventPipeline.distributePlayerEvent(playerEvent);
    }


}
