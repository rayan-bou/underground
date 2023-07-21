package me.blafexe.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

/**
 * Is used to easily access scoreboard functions without the need to create objectives or scores.
 * Scoreboards, that have not been created/modified using this wrapper might not work correctly.
 */
public class ScoreboardWrapper {

    private Scoreboard scoreboard;
    private Objective objective;
    private List<String> entries;

    public ScoreboardWrapper(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        this.objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        entries = new ArrayList<>(scoreboard.getEntries());
    }

    public ScoreboardWrapper(Component title, ScoreboardManager scoreboardManager) {
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("objective", Criteria.DUMMY, title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        entries = new ArrayList<>();
    }

    public void setVisible(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void setRow(int row, Component text) {
        String s = LegacyComponentSerializer.legacy('§').serialize(text);
        entries.add(row, s);
    }

    public void addRow(Component text) {
        setRow(entries.size(), text);
    }

    public void replaceRow(int row, Component text) {
        removeRow(row);
        setRow(row, text);
    }

    public void removeRow(int row) {
        entries.remove(row);
    }

    public void update() {
        //Set of removed elements
        Set<String> removedEntries = new HashSet<>(scoreboard.getEntries());
        removedEntries.removeAll(entries);
        removedEntries.forEach(scoreboard::resetScores);
        for (int i = 0; i < entries.size(); i++) {
            String entry = entries.get(i);
            Score score = objective.getScore(entry);
            score.setScore(entries.size() - i - 1);
        }
    }



}
