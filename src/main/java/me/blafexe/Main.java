package me.blafexe;

import me.blafexe.loader.DummyZoneLoader;
import me.blafexe.loader.ZoneLoader;
import me.blafexe.zone.ZoneHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ZoneHandler zoneHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        zoneHandler = new ZoneHandler(this);
        getServer().getPluginManager().registerEvents(zoneHandler, this);

        //Load Zones TODO Load from file
        ZoneLoader zoneLoader = new DummyZoneLoader(zoneHandler);
        zoneLoader.getZones().forEach(zone -> zoneHandler.addZone(zone));

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }



}