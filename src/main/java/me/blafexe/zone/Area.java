package me.blafexe.zone;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a 3-dimensional area from 2 corner points.
 */
public class Area {

    private final World world;
    private final double x1, x2, y1, y2, z1, z2;

    public Area(World world, double x1, double y1, double z1, double x2, double y2, double z2) {
        this.world = world;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    /**
     * @param location Location
     * @return True, if the given location is within the area's bounds (inclusive).
     */
    public boolean isInside(Location location) {

        if (!location.getWorld().equals(world)) return false;
        return isInside(location.getX(), location.getY(), location.getZ());

    }

    /**
     * @return True, if a 3-dimensional location is within the area's bounds (bounds inclusive).
     */
    public boolean isInside(double x, double y, double z) {
        return isInRange(x, x1, x2)
                && isInRange(y, y1, y2)
                && isInRange(z, z1, z2);
    }

    /**
     * @return True, if a given number is between two bounds (bounds inclusive).
     */
    private boolean isInRange(double d, double bound1, double bound2) {
        double d1 = Math.min(bound1, bound2);
        double d2 = Math.max(bound1, bound2);

        return d >= d1 && d <= d2;
    }

    @Override
    public String toString() {
        return "A (" + x1 + " " + y1 + " " + z1 + ")\nB (" + x2 + " " + y2 + " " + z2 + ")";
    }
}
