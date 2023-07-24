package me.blafexe.infoview;

import me.blafexe.event.PlayerJobEvent;
import me.blafexe.event.PlayerStatsUpdateEvent;
import me.blafexe.event.PlayerTransitionZoneEvent;
import me.blafexe.player.Stats;
import me.blafexe.player.StatsHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

//TODO Refactor
public class InfoviewHandler implements Listener {

    private final Plugin plugin;
    private final Map<Player, Infoview> infoviewMap;

    private ZoneDummy zoneDummy = new ZoneDummy();
    private StatsHandler statsHandler;

    public InfoviewHandler(Plugin plugin, StatsHandler statsHandler) {
        this.plugin = plugin;
        this.statsHandler = statsHandler;
        infoviewMap = new HashMap<>();
    }

    public Infoview getInfoview(Player player) {
        return infoviewMap.get(player);
    }

    private void initializeInfoview(Infoview infoview, Player player) {
        Stats stats = statsHandler.getStats(player);
        infoview.addElement(stats.getLevelView());
        infoview.addElement(stats.getMoneyView());
        infoview.addElement(zoneDummy);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Infoview infoview = new Infoview(Component.text("Provinglife").color(() -> 0x228B22), plugin.getServer().getScoreboardManager());
        infoview.setVisible(event.getPlayer());
        infoviewMap.put(event.getPlayer(), infoview);

        initializeInfoview(infoview, event.getPlayer());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        infoviewMap.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTransitionZone(PlayerTransitionZoneEvent event) {

        Infoview infoview = getInfoview(event.getPlayer());

        if (infoview == null) return;

        if (event.isInside()) {
            infoview.removeElement(zoneDummy);
            infoview.addElement(event.getZone());
        } else {
            infoview.removeElement(event.getZone());
            infoview.addElement(zoneDummy);
        }

    }

    @EventHandler
    public void onPlayerJob(PlayerJobEvent event) {

        Infoview infoview = getInfoview(event.getPlayer());
        if (infoview == null) return;

        switch (event.getStatus()) {
            case TAKE -> {
                infoview.addElement(event.getJob());
            }
            case FINISH -> {
                infoview.removeElement(event.getJob());
            }
            case UPDATE -> {
                infoview.render();
            }
        }

    }

    @EventHandler
    public void onPlayerStatsUpdate(PlayerStatsUpdateEvent event) {

        Infoview infoview = getInfoview(event.getPlayer());
        infoview.render();

    }

}
