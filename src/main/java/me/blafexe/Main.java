package me.blafexe;

import me.blafexe.job.JobEngine;
import me.blafexe.loader.DummyZoneLoader;
import me.blafexe.loader.ZoneLoader;
import me.blafexe.infoview.InfoviewHandler;
import me.blafexe.player.StatsHandler;
import me.blafexe.zone.DamageHandler;
import me.blafexe.zone.ZoneHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        //Infoview
        InfoviewHandler infoviewHandler = new InfoviewHandler(this);
        getServer().getPluginManager().registerEvents(infoviewHandler, this);

        StatsHandler statsHandler = new StatsHandler(this);
        getServer().getPluginManager().registerEvents(statsHandler, this);

        ZoneHandler zoneHandler = new ZoneHandler(this);
        getServer().getPluginManager().registerEvents(zoneHandler, this);

        DamageHandler damageHandler = new DamageHandler();
        getServer().getPluginManager().registerEvents(damageHandler, this);

        //Load Zones TODO Load from file
        ZoneLoader zoneLoader = new DummyZoneLoader();
        zoneLoader.getZones().forEach(zoneHandler::addZone);

        JobEngine jobEngine = new JobEngine(this);
        getServer().getPluginManager().registerEvents(jobEngine, this);


    }

    @Override
    public void onDisable() {
        super.onDisable();
    }



}