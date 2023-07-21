package me.blafexe.zone;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An area that can specifically set to enable or disable player damage.
 */
public class Zone {

    private final Area area;
    private final boolean allowDamage;

    public Zone(Area area, boolean allowDamage) {
        this.area = area;
        this.allowDamage = allowDamage;
    }

    public boolean isInside(Location location) {
        return area.isInside(location);
    }

    public boolean allowDamage() {
        return allowDamage;
    }

    public void onEnter(Player player, Location from, Location to) {

    }

    public void onLeave(Player player, Location from, Location to) {

    }

}