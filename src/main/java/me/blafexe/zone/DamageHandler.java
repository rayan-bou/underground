package me.blafexe.zone;

import me.blafexe.event.PlayerTransitionZoneEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Watches every damage event that occurs. Disables damage for protected players.
 */
public class DamageHandler implements Listener {

    private final Set<Player> protectedPlayerSet;

    public DamageHandler() {
        protectedPlayerSet = new HashSet<>();
    }

    /**
     * Grants the "protected"-status to a player. Protected players can't receive any damage.
     * @param player Player to grant "protected"-status.
     */
    public void addProtectedPlayer(Player player) {
        protectedPlayerSet.add(player);
    }

    /**
     * Removes the "protected"-status from a player. The player will be able to receive damage.
     * @param player Player to revoke "protected"-status from.
     */
    public void removeProtectedPlayer(Player player) {
        protectedPlayerSet.remove(player);
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

    @EventHandler
    public void onPlayerTransitionZone(PlayerTransitionZoneEvent event) {

        if (event.getZone().disableDamage()) {
            if (event.isInside()) {
                protectedPlayerSet.add(event.getPlayer());
            } else {
                protectedPlayerSet.remove(event.getPlayer());
            }
        }

    }

}
