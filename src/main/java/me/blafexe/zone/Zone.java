package me.blafexe.zone;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An area that can specifically set to enable or disable player damage.
 */
public class Zone {

    private final Area area;
    private final boolean allowDamage;
    //Reference of DamageHandler to enable player protection
    private final DamageHandler damageHandler;

    public Zone(Area area, boolean allowDamage, DamageHandler damageHandler) {
        this.area = area;
        this.allowDamage = allowDamage;
        this.damageHandler = damageHandler;
    }

    public boolean isInside(Location location) {
        return area.isInside(location);
    }

    public void onEnter(Player player, Location from, Location to) {
        if (!allowDamage) {
            damageHandler.addProtectedPlayer(player);
            player.sendMessage(Component.text("Damage is disabled!").color(() -> 0x00ff00));
        } else {
            player.sendMessage(Component.text("Damage is enabled!").color(() -> 0xff0000));
        }
    }

    public void onLeave(Player player, Location from, Location to) {
        if (!allowDamage) damageHandler.removeProtectedPlayer(player);
    }

}