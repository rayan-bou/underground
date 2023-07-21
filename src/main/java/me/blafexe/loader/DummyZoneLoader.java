package me.blafexe.loader;

import me.blafexe.zone.Area;
import me.blafexe.zone.Zone;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Set;

public class DummyZoneLoader implements ZoneLoader {

    public DummyZoneLoader() {}

    @Override
    public Collection<Zone> getZones() {
        return Set.of(new Zone(new Area(Bukkit.getWorld("world"), 0,0,0,10,200,10), false));
    }

}
