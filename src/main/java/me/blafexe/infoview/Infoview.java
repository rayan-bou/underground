package me.blafexe.infoview;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An object that is capable of displaying information for a single or multiple players.<br>
 * It uses Minecraft's scoreboard system to display text.
 */
public class Infoview {

    //List, order of elements is important
    private List<InfoviewElement> elements;
    private final InfoviewRenderer renderer;
    private final Scoreboard scoreboard;
    private final Objective objective;

    public Infoview(@NotNull Component title, @NotNull ScoreboardManager scoreboardManager) {
        scoreboard = scoreboardManager.getNewScoreboard();

        objective = scoreboard.registerNewObjective("objective", Criteria.DUMMY, title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        elements = new LinkedList<>();
        renderer = new InfoviewRenderer();
    }

    /**
     * Sets the infoview visible to a give player.
     * @param player Player
     */
    public void setVisible(@NotNull Player player) {
        player.setScoreboard(scoreboard);
    }

    /**
     * Adds a new element to the infoview. Adding a new element will automatically resort the elements and render the
     * changes.
     * @param infoviewElement New Element to be displayed.
     */
    public void addElement(@NotNull InfoviewElement infoviewElement) {

        elements.add(infoviewElement);
        sortElements();
        render();

    }

    /**
     * Removes an element from the infoview. Removing an element will automatically resort the elements and render the
     * changes.
     * @param infoviewElement Element to be removed.
     */
    public void removeElement(@NotNull InfoviewElement infoviewElement) {

        elements.remove(infoviewElement);
        sortElements();
        render();

    }

    /**
     * Searches the elements for any element with a specified id.
     * @param id The id.
     * @return An Optional, containing an element with the specified id.
     */
    public Optional<InfoviewElement> getElement(@NotNull String id) {

        return elements.stream()
                .filter(infoviewElement -> infoviewElement.getId().equals(id))
                .findFirst();

    }

    /**
     * Renders the elements and any changes that may have occurred since the last call onto the scoreboard.
     * Uses the current element sorting.
     */
    public void render() {

        //Reset scores of scoreboard
        scoreboard.getEntries().forEach(scoreboard::resetScores);

        //Create Renderer
        List<String> strings = renderer.render(elements);
        for (int i = 0; i < strings.size(); i++) {
            Score score = objective.getScore(strings.get(i));
            score.setScore(strings.size() - i - 1);
        }

    }

    /**
     * Sorts the element list. Elements with higher importance will be put at the start of the list while elements with
     * lower or no priority will be put at the end. Elements from the same category will be grouped together, regarding
     * their respective importance.
     */
    private void sortElements() {

        //Sort list using each element's importance
        elements = elements.stream()
                .sorted(Comparator.comparingInt(InfoviewElement::getImportance).reversed())
                .toList();

        //Collect all categories by their importance
        List<String> categories = elements.stream()
                .filter(infoviewElement -> infoviewElement.getCategory().isPresent())
                .map(infoviewElement -> infoviewElement.getCategory().get())
                .distinct()
                .toList();

        //Put all categorized elements in new list
        List<InfoviewElement> groupedElements = new LinkedList<>();
        categories.forEach(category -> {
            groupedElements.addAll(
                    elements.stream()
                            .filter(infoviewElement -> {
                                Optional<String> optional = infoviewElement.getCategory();
                                return optional.map(s -> s.equals(category)).orElse(false);
                            })
                            .sorted(Comparator.comparingInt(InfoviewElement::getImportance).reversed())
                            .toList()
            );
        });

        //Add all uncategorized elements to the new list
        groupedElements.addAll(
                elements.stream()
                        .filter(infoviewElement -> infoviewElement.getCategory().isEmpty())
                        .sorted(Comparator.comparingInt(InfoviewElement::getImportance).reversed())
                        .toList()
        );

        //Set elements to be the new, sorted list
        elements = groupedElements;

    }

}
