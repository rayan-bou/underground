package me.blafexe.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class TestScoreboardHandler implements Listener {

    private Plugin plugin;

    public TestScoreboardHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        EasyScoreboard easyScoreboard = new EasyScoreboard(Component.text("Example Scoreboard"), plugin.getServer().getScoreboardManager());
        easyScoreboard.addEntry("1", Component.text("Beispieltext").color(() -> 0x555500));
        easyScoreboard.setVisible(event.getPlayer());

    }

}
