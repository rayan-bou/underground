package me.blafexe.zone;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An area that can specifically set to enable or disable player damage (except for self-inflicted damage).
 */
public class Zone {

    private static final BossBar damageEnabledBar = BossBar.bossBar(
            Component.text("Damage enabled"),
            0f, BossBar.Color.RED, BossBar.Overlay.PROGRESS);
    private static final BossBar damageDisabledBar = BossBar.bossBar(
            Component.text("Damage disabled"),
            0f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);

    private final Area area;
    private final boolean allowDamage;
    //Reference of ZoneHandler to enable player protection
    private final ZoneHandler zoneHandler;

    public Zone(Area area, boolean allowDamage, ZoneHandler zoneHandler) {
        this.area = area;
        this.allowDamage = allowDamage;
        this.zoneHandler = zoneHandler;
    }

    public boolean isInside(Location location) {
        return area.isInside(location);
    }

    public void onEnter(Player player, Location from, Location to) {
        if (!allowDamage) {
            zoneHandler.addProtectedPlayer(player);
            Audience.audience(player).showBossBar(damageDisabledBar);
        } else {
            Audience.audience(player).showBossBar(damageEnabledBar);
        }
        player.sendMessage(Component.text("Hallo"));
    }

    public void onLeave(Player player, Location from, Location to) {
        if (!allowDamage) {
            zoneHandler.removeProtectedPlayer(player);
            Audience.audience(player).hideBossBar(damageDisabledBar);
        } else {
            Audience.audience(player).hideBossBar(damageEnabledBar);
        }
    }

    @Override
    public String toString() {
        return area.toString();
    }
}