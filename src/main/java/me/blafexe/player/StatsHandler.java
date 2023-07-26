package me.blafexe.player;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import me.blafexe.event.PlayerStatsUpdateEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

/**
 * Handles the updating and conversion of player stats using the internal <code>PersistentDataContainer</code>.
 */
public class StatsHandler implements Listener {

    //Keys
    private static final NamespacedKey KEY_MONEY = new NamespacedKey("underground", "money");
    private static final NamespacedKey KEY_LEVEL = new NamespacedKey("underground", "level");

    /**
     * Takes <code>PlayerStats</code> and stores them in the player's <code>PersistentDataContainer</code>. Existing
     * stats will be overridden.
     * @param player The player.
     * @param stats The stats.
     */
    public static void packStats(Player player, PlayerStats stats) {

        PersistentDataContainer container = player.getPersistentDataContainer();

        double levelTruncated = stats.getLevel();
        levelTruncated *= 100;
        levelTruncated = (int) levelTruncated;
        levelTruncated /= 100;

        container.set(KEY_MONEY, PersistentDataType.INTEGER, stats.getMoney());
        container.set(KEY_LEVEL, PersistentDataType.DOUBLE, levelTruncated);

    }

    /**
     * Pulls the data regarding stats stored in a player's <code>PersistentDataContainer</code> and converts them into
     * a <code>PlayerStats</code> object.
     * @param player The player.
     * @return PlayerStats.
     */
    public static PlayerStats unpackStats(Player player) {

        PersistentDataContainer container = player.getPersistentDataContainer();

        int money = Optional.ofNullable(container.get(KEY_MONEY, PersistentDataType.INTEGER)).orElse(0);
        double level  = Optional.ofNullable(container.get(KEY_LEVEL, PersistentDataType.DOUBLE)).orElse(0d);

        return new PlayerStats(money, level);

    }

    private final Plugin plugin;

    public StatsHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPickupXP(PlayerPickupExperienceEvent event) {

        PlayerStats playerStats = unpackStats(event.getPlayer());
        playerStats.addLevelProgress(0.1);
        packStats(event.getPlayer(), playerStats);

        plugin.getServer().getPluginManager().callEvent(new PlayerStatsUpdateEvent(event.getPlayer()));


    }

}
