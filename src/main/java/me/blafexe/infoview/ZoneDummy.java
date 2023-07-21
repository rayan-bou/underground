package me.blafexe.infoview;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ZoneDummy implements InfoviewElement {
    @Override
    public int getImportance() {
        return 0;
    }

    @Override
    public Optional<String> getCategory() {
        return Optional.of("§b§lGebiet");
    }

    @Override
    public @NotNull String getText() {
        return "Nicht sicher";
    }

    @Override
    public @NotNull String getId() {
        return "zone";
    }
}
