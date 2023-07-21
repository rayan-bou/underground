package me.blafexe.scoreboard;

import me.blafexe.event.PlayerJobEvent;
import me.blafexe.event.PlayerTransitionZoneEvent;
import me.blafexe.scoreboard.infoview.InfoEntry;
import me.blafexe.scoreboard.infoview.Infoview;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class InfoviewHandler implements Listener {

    private final Plugin plugin;
    private final Map<Player, Infoview> infoviewMap;

    public InfoviewHandler(Plugin plugin) {
        this.plugin = plugin;
        infoviewMap = new HashMap<>();
    }

    public Infoview getInfoview(Player player) {
        return infoviewMap.get(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Infoview infoview = initializeDefaultInfoview();
        infoview.setVisible(event.getPlayer());
        infoviewMap.put(event.getPlayer(), infoview);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        infoviewMap.remove(event.getPlayer());
    }

    private Infoview initializeDefaultInfoview() {
        Infoview infoview = new Infoview(Component.text("Information").color(() -> 0xf5b942),
                plugin.getServer().getScoreboardManager());

        infoview.addInfoEntry(new InfoEntry("Player", "Level: 2", 2, "level"));
        infoview.addInfoEntry(new InfoEntry("Player", "Money: 0€", 1, "money"));
        infoview.addInfoEntry(new InfoEntry(ChatColor.RED + "Gebiet unsicher", "zoneSecurity"));
        return infoview;
    }

    @EventHandler
    public void onPlayerTransitionZone(PlayerTransitionZoneEvent event) {

        Infoview infoview = getInfoview(event.getPlayer());

        if (infoview == null) return;

        if (event.isInside() && !event.getZone().allowDamage()) {
            infoview.getById("zoneSecurity").ifPresent(infoEntry -> {
                infoEntry.setText(ChatColor.GREEN + "Gebiet sicher");
                infoview.update();
            });
        } else {
            infoview.getById("zoneSecurity").ifPresent(infoEntry -> {
                infoEntry.setText(ChatColor.RED + "Gebiet unsicher");
                infoview.update();
            });
        }

    }

    @EventHandler
    public void onPlayerJob(PlayerJobEvent event) {

        Infoview infoview = getInfoview(event.getPlayer());

        System.out.println("Caught Job Event " + event.getStatus());

        switch (event.getStatus()) {
            case TAKE -> {
                InfoEntry infoEntry = new InfoEntry(event.getJob());
                infoview.addInfoEntry(infoEntry);
            }
            case FINISH -> {
                infoview.getById(event.getJob().getId()).ifPresent(infoview::removeInfoEntry);
            }
            case UPDATE -> {
                infoview.update();
            }
        }

    }

}
