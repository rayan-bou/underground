package me.blafexe.player;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import me.blafexe.event.PlayerStatsUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class StatsHandler implements Listener {

    private Map<Player, Stats> statsMap;
    private Plugin plugin;

    public StatsHandler(Plugin plugin) {
        this.plugin = plugin;
        statsMap = new HashMap<>();
    }

    public Stats getStats(Player player) {
        return statsMap.get(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        statsMap.put(event.getPlayer(), new Stats(this));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        statsMap.remove(event.getPlayer());

    }

    @EventHandler
    public void onPlayerPickupXP(PlayerPickupExperienceEvent event) {

        Stats stats = getStats(event.getPlayer());

        if (stats != null) {
            stats.setLevel(stats.getLevel() + 1);
        }

    }

    protected void reportUpdate(Stats stats) {

        for (Map.Entry<Player, Stats> playerStatsEntry : statsMap.entrySet()) {
            if (playerStatsEntry.getValue().equals(stats)) {
                PlayerStatsUpdateEvent event = new PlayerStatsUpdateEvent(playerStatsEntry.getKey(), stats);
                plugin.getServer().getPluginManager().callEvent(event);
                break;
            }
        }

    }

}
