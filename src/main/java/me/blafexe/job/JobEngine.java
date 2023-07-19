package me.blafexe.job;

import me.blafexe.job.courier.CourierJob;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * The JobEngine assigns jobs and manages every active job any player has.
 */
public class JobEngine implements Listener {

    private final JobEventPipeline jobEventPipeline;

    public JobEngine() {
        jobEventPipeline = new JobEventPipeline();
    }

    /**
     * Assigns a job to a player.
     * @param player
     * @param job
     */
    public void giveJob(Player player, Job job) {

        if (jobEventPipeline.addToJobList(player, job)) {
            job.start(player);
            player.sendMessage(Component.text("Du hast einen neuen Job erhalten!"));
        }

    }

    /**
     * Removes a job from a player.
     * @param player
     * @param job
     */
    public boolean removeJob(Player player, Job job) {

        if (jobEventPipeline.removeFromJobList(player, job)) {
            job.end(player);
            player.sendMessage(Component.text("Du hast einen Job abgeschlossen!"));
            return true;
        }
        return false;

    }

    /**
     * Finished a players job and marks it as successful.
     * @param player
     * @param job
     */
    public void finishJob(Player player, Job job) {

        if (removeJob(player, job)) {
            //TODO success etc...
        }

    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent playerEvent) {
        jobEventPipeline.distributeEvent(playerEvent, this);
    }

    //TODO Remove
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Job job = new CourierJob(new Location(Bukkit.getWorld("world"), 0, 5, 0));
        giveJob(event.getPlayer(), job);
    }


}
