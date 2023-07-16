package me.blafexe.zone;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ZoneHandler implements Listener {

    private final Plugin plugin;
    //Stores a world's name and the associated spaces
    private final Set<Zone> zoneSet;
    //Stores players, that cannot receive damage
    private final Set<Player> protectedPlayerSet;

    public ZoneHandler(Plugin plugin) {
        this.plugin = plugin;
        zoneSet = new HashSet<>();
        protectedPlayerSet = new HashSet<>();
    }

    public void addZone(Zone zone) {
        zoneSet.add(zone);
        System.out.println(zone);
    }

    public void removeZone(Zone zone) {
        zoneSet.remove(zone);
    }

    public void addProtectedPlayer(Player player) {
        protectedPlayerSet.add(player);
    }

    public void removeProtectedPlayer(Player player) {
        protectedPlayerSet.remove(player);
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

            Set<Zone> oldSpaces = getContainingSpaces(from);
            Set<Zone> newSpaces = getContainingSpaces(to);

            Set<Zone> tempOldSpaces = Set.copyOf(oldSpaces);
            oldSpaces.removeAll(newSpaces);
            newSpaces.removeAll(tempOldSpaces);

            oldSpaces.forEach(space -> {
                scheduler.runTask(plugin, () -> {
                    space.onLeave(player, from, to);
                });
            });
            newSpaces.forEach(space -> {
                scheduler.runTask(plugin, () -> {
                    space.onEnter(player, from, to);
                });
            });

        });
    }

    /**
     * Collects each zone, that contains the probe location.
     *
     * @param location Probe location.
     * @return A set of spaces that contain the location.
     */
    public Set<Zone> getContainingSpaces(@Nullable Location location) {

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

    /**
     * Is triggered by the <code>EntityDamageEvent</code>. Cancels the event, if the damaged player is contained within
     * the <code>protectedPlayers</code> set.
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {

        if (event.getEntity() instanceof Player player) {
            if (protectedPlayerSet.contains(player)) {
                event.setCancelled(true);
                if (event instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
                    if (entityDamageByEntityEvent.getDamager() instanceof Player damager) {
                        damager.sendMessage(Component.text("You cant deal damage here!"));
                    }
                }
            }
        }

    }
}
