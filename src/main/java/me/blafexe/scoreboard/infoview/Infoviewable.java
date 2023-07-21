package me.blafexe.scoreboard.infoview;

public interface Infoviewable {

    default String getCategory() {
        return null;
    }

    default int getImportance() {
        return 0;
    }

    String getId();

    String getText();

}
