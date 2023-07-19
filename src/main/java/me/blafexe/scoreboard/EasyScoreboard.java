package me.blafexe.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class EasyScoreboard {

    private final Scoreboard scoreboard;
    private final Map<String, String> entryMap;

    public EasyScoreboard(Component title, ScoreboardManager scoreboardManager) {
        scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("objective", Criteria.DUMMY, title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        entryMap = new HashMap<>();
    }

    public void setVisible(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void addEntry(String id, Component text) {

        //Set title
        String string = LegacyComponentSerializer.legacy('§').serialize(text);
        Score score = scoreboard.getObjective("objective").getScore(string);
        score.setScore(entryMap.size());
        //Add to map
        entryMap.put(id, string);

    }

}
