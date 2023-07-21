package me.blafexe.zone;

import me.blafexe.infoview.InfoviewElement;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * An area that can specifically set to enable or disable player damage.
 */
public class Zone implements InfoviewElement {

    private final Area area;
    private final boolean allowDamage;

    public Zone(Area area, boolean allowDamage) {
        this.area = area;
        this.allowDamage = allowDamage;
    }

    public boolean isInside(Location location) {
        return area.isInside(location);
    }

    public boolean disableDamage() {
        return !allowDamage;
    }

    public void onEnter(Player player, Location from, Location to) {

    }

    public void onLeave(Player player, Location from, Location to) {

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
        return disableDamage() ? "Sicher" : "Nicht sicher";
    }

    @Override
    public @NotNull String getId() {
        return "zone";
    }
}