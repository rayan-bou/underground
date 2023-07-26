package me.blafexe.infoview.wrapper;

import me.blafexe.infoview.InfoviewElement;
import me.blafexe.zone.Zone;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ZoneWrapper implements InfoviewElement {

    private Zone zone;

    public void setZone(Zone zone) {
        this.zone = zone;
    }

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
        if (zone == null || !zone.disableDamage()) return "Nicht sicher";
        else return "Sicher";
    }

    @Override
    public @NotNull String getId() {
        return "zone";
    }
}
