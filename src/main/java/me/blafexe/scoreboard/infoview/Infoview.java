package me.blafexe.scoreboard.infoview;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

/**
 * Is used to present any information to the player on-screen using text. The infoview is displayed in the player's sidebar.
 */
public class Infoview {

    private List<InfoEntry> infoEntries;
    private Scoreboard scoreboard;
    private Objective objective;

    public Infoview(Component title, ScoreboardManager scoreboardManager) {
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("objective", Criteria.DUMMY, title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        infoEntries = new ArrayList<>();
    }

    public void setVisible(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void addInfoEntry(InfoEntry infoEntry) {
        infoEntries.add(infoEntry);
        groupInfoEntries();
        update();
    }

    public void removeInfoEntry(InfoEntry infoEntry) {
        infoEntries.remove(infoEntry);
        groupInfoEntries();
        update();
    }

    public Optional<InfoEntry> getById(String id) {
        return infoEntries.stream()
                .filter(infoEntry -> infoEntry.getId().equals(id))
                .findFirst();
    }

    public void groupInfoEntries() {
        //Collect categories according to their importance
        List<String> sortedCategories = infoEntries.stream()
                .sorted(Comparator.comparingInt(InfoEntry::getImportance).reversed())
                .map(InfoEntry::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        //Add Entries to category
        List<InfoEntry> sortedEntries = new ArrayList<>();
        sortedCategories.forEach(category -> {
            sortedEntries.addAll(
                    infoEntries.stream()
                            .filter(infoEntry -> category.equals(infoEntry.getCategory()))
                            .sorted(Comparator.comparingInt(InfoEntry::getImportance))
                            .toList()
            );
        });
        //Add uncategorized entries
        sortedEntries.addAll(
                infoEntries.stream()
                        .filter(infoEntry -> infoEntry.getCategory() == null)
                        .sorted(Comparator.comparingInt(InfoEntry::getImportance))
                        .toList()
        );

        infoEntries = sortedEntries;
    }

    public void update() {

        scoreboard.getEntries().forEach(s -> scoreboard.resetScores(s));

        int i = 0;
        int spacers = 1;
        String lastCategory = null;
        for (InfoEntry infoEntry : infoEntries) {

            //Create Category if not present
            if (!Objects.equals(infoEntry.getCategory(), lastCategory)) {
                if (lastCategory != null) {
                    Score score = objective.getScore(" ".repeat(spacers++));
                    score.setScore(i--);
                }
                if (infoEntry.getCategory() != null) {
                    Score score = objective.getScore(ChatColor.YELLOW + infoEntry.getCategory());
                    score.setScore(i--);
                    lastCategory = infoEntry.getCategory();
                }
            }

            Score scoreText = objective.getScore(infoEntry.getText());
            scoreText.setScore(i--);

        }

        int entriesOffset = scoreboard.getEntries().size() - 1;
        scoreboard.getEntries().forEach(s -> {
            Score score = objective.getScore(s);
            score.setScore(score.getScore() + entriesOffset);
        });

    }

}
