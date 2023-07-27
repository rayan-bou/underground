package me.blafexe.infoview;

import me.blafexe.event.PlayerStatsUpdateEvent;
import me.blafexe.event.PlayerTransitionZoneEvent;
import me.blafexe.infoview.wrapper.LevelWrapper;
import me.blafexe.infoview.wrapper.MoneyWrapper;
import me.blafexe.infoview.wrapper.ZoneWrapper;
import me.blafexe.player.StatsHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The <code>InfoviewHandler</code> keeps track of all currently active (= visible to one or more players) info views. It
 * listens for events, that might alter the info displayed by any info view and sends an update-trigger to the corresponding
 * info view.
 */
public class InfoviewHandler implements Listener {

    private final Plugin plugin;
    private final Map<Player, Infoview> infoviewMap;

    public InfoviewHandler(Plugin plugin) {
        this.plugin = plugin;
        infoviewMap = new HashMap<>();
    }

    /**
     * @param player The player.
     * @return An optional containing the specified player's info view. Empty if player has no displayed info view.
     */
    public Optional<Infoview> getInfoview(Player player) {
        return Optional.ofNullable(infoviewMap.get(player));
    }

    /**
     * Initializes a fresh info view with default elements.
     * @param player The player the info view is created for.
     * @return A fresh info view.
     */
    private Infoview initializeInfoview(Player player) {

        Infoview infoview = new Infoview(
                Component.text("Provinglife").decorate(TextDecoration.BOLD).color(() -> 0x40b840),
                plugin.getServer().getScoreboardManager()
        );

        //Add default elements
        infoview.addElement(new ZoneWrapper());
        infoview.addElement(new LevelWrapper(player));
        infoview.addElement(new MoneyWrapper(player));

        //Set visible
        infoview.setVisible(player);

        return infoview;
    }

    /**
     * Creates a fresh info view for any player that joins.
     * @param event A PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        infoviewMap.put(event.getPlayer(), initializeInfoview(event.getPlayer()));
    }

    /**
     * Removes a leaving player's info view.
     * @param event A PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        infoviewMap.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTransitionZone(PlayerTransitionZoneEvent event) {

        getInfoview(event.getPlayer()).ifPresent(infoview -> {
            infoview.getElement("zone").ifPresent(infoviewElement -> {
                if (infoviewElement instanceof ZoneWrapper zoneWrapper) {
                    if (event.isInside()) zoneWrapper.setZone(event.getZone());
                    else zoneWrapper.setZone(null);
                    infoview.render();
                }
            });
        });

    }

    @EventHandler
    public void onPlayerStatsUpdate(PlayerStatsUpdateEvent event) {
        getInfoview(event.getPlayer()).ifPresent(Infoview::render);
    }

}
