package me.blafexe.job;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import java.util.*;

/**
 * Stores jobs for every player respectively and distributes events to corresponding jobs when they occur.
 */
public class JobEventPipeline {

    //Stores every player's jobs in their respective list
    private final Map<UUID, List<Job>> playerJobListMap;
    private final List<Job> removedJobs;

    public JobEventPipeline() {
        playerJobListMap = new HashMap<>();
        removedJobs = new LinkedList<>();
    }

    /**
     * Takes in an event and distributes it to the respective player's jobs.
     *
     * @param event PlayerEvent to be distributed.
     * @param jobEngine JobEngine that caught the event.
     */
    public void distributeEvent(Event event, JobEngine jobEngine) {

        //Get player
        UUID uuid;
        if (event instanceof PlayerEvent playerEvent) {
            uuid = playerEvent.getPlayer().getUniqueId();
        } else {
            return;
        }

        //Get player's job list from uuid
        List<Job> jobList = playerJobListMap.get(uuid);

        //Return if player has no jobs
        if (jobList == null) return;

        //Pass event to each job
        jobList.forEach(job -> job.handleEvent(event, jobEngine));
        jobList.removeAll(removedJobs);
        removedJobs.clear();

    }

    /**
     * Adds a job to the player's job list.
     * @param player Player.
     * @param job Job to add.
     * @return True, if the job was added, false if the job has already been is the list.
     */
    public boolean addToJobList(Player player, Job job) {

        //Adds a new entry for the player if not exists and inserts the job
        playerJobListMap.computeIfAbsent(player.getUniqueId(), uuid -> new ArrayList<>());
        if (playerJobListMap.get(player.getUniqueId()).contains(job)) return false;
        playerJobListMap.get(player.getUniqueId()).add(job);
        return true;

    }

    /**
     * Removes a job from the players job list. A removed job will no longer handle events.
     * @param player Player.
     * @param job Job that is to be removed.
     * @return True, if the job was removed, false if the job was not in the list.
     */
    public boolean removeFromJobList(Player player, Job job) {

        boolean jobIsContained = false;

        if (playerJobListMap.get(player.getUniqueId()) == null) return false;

        List<Job> jobList = playerJobListMap.get(player.getUniqueId());
        jobIsContained = jobList.contains(job);
        removedJobs.add(job);

        if (jobList.size() < 1) playerJobListMap.remove(player.getUniqueId());

        return jobIsContained;

    }

}
