package me.blafexe.scoreboard.infoview;

import net.kyori.adventure.text.Component;

public class InfoEntry {

    private String category;
    private String text;
    private int importance;
    private final String id;

    public InfoEntry(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public InfoEntry(String category, String text, int importance, String id) {
        this.category = category;
        this.text = text;
        this.importance = importance;
        this.id = id;
    }

    public InfoEntry(Infoviewable infoviewable) {
        this.category = infoviewable.getCategory();
        this.text = infoviewable.getText();
        this.importance = infoviewable.getImportance();
        this.id = infoviewable.getId();
    }

    public String getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImportance() {
        return importance;
    }

    public String getId() {
        return id;
    }
}
