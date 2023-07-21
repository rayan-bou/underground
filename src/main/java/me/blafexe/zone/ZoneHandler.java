package me.blafexe.zone;

import me.blafexe.event.PlayerTransitionZoneEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ZoneHandler implements Listener {

    private final Plugin plugin;
    //Stores all zones
    private final Set<Zone> zoneSet;

    public ZoneHandler(Plugin plugin) {
        this.plugin = plugin;
        zoneSet = new HashSet<>();
    }

    public void addZone(Zone zone) {
        zoneSet.add(zone);
        System.out.println(zone);
    }

    public void removeZone(Zone zone) {
        zoneSet.remove(zone);
    }

    /**
     * Asynchronously handles a players movement into or out of zones and passes the movement information to the affected
     * zones. Utilizes the spigot async api.
     *
     * @param player Player that performs the movement.
     * @param from   Location the player came from.
     * @param to     Location the player moved to.
     */
    private void asyncHandleMovement(@NotNull Player player, @Nullable Location from, @Nullable Location to) {

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        scheduler.runTaskAsynchronously(plugin, () -> {

            Set<Zone> oldZones = getContainingZones(from);
            Set<Zone> newZones = getContainingZones(to);

            Set<Zone> tempOldSpaces = Set.copyOf(oldZones);
            oldZones.removeAll(newZones);
            newZones.removeAll(tempOldSpaces);

            oldZones.forEach(zone -> scheduler.runTask(plugin, () -> {
                zone.onLeave(player, from, to);
                PlayerTransitionZoneEvent zoneEvent = new PlayerTransitionZoneEvent(player, zone, false);
                plugin.getServer().getPluginManager().callEvent(zoneEvent);
            }));
            newZones.forEach(zone -> scheduler.runTask(plugin, () -> {
                zone.onEnter(player, from, to);
                PlayerTransitionZoneEvent zoneEvent = new PlayerTransitionZoneEvent(player, zone, true);
                plugin.getServer().getPluginManager().callEvent(zoneEvent);
            }));

        });
    }

    /**
     * Collects each zone, that contains the probe location.
     *
     * @param location Probe location.
     * @return A set of spaces that contain the location.
     */
    public Set<Zone> getContainingZones(@Nullable Location location) {

        if (location == null) return Collections.emptySet();

        return zoneSet.stream()
                .filter(zone -> zone.isInside(location))
                .collect(Collectors.toSet());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        asyncHandleMovement(event.getPlayer(), null, event.getPlayer().getLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        asyncHandleMovement(event.getPlayer(), event.getPlayer().getLocation(), null);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        asyncHandleMovement(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        asyncHandleMovement(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent event) {
        asyncHandleMovement(event.getPlayer(), event.getPlayer().getLastDeathLocation(), event.getRespawnLocation());
    }

}
