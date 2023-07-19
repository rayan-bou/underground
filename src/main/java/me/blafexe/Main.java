package me.blafexe;

import me.blafexe.job.JobEngine;
import me.blafexe.loader.DummyZoneLoader;
import me.blafexe.loader.ZoneLoader;
import me.blafexe.scoreboard.TestScoreboardHandler;
import me.blafexe.zone.DamageHandler;
import me.blafexe.zone.ZoneHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        ZoneHandler zoneHandler = new ZoneHandler(this);
        getServer().getPluginManager().registerEvents(zoneHandler, this);

        DamageHandler damageHandler = new DamageHandler();
        getServer().getPluginManager().registerEvents(damageHandler, this);

        //Load Zones TODO Load from file
        ZoneLoader zoneLoader = new DummyZoneLoader(damageHandler);
        zoneLoader.getZones().forEach(zoneHandler::addZone);

        JobEngine jobEngine = new JobEngine();
        getServer().getPluginManager().registerEvents(jobEngine, this);

        getServer().getPluginManager().registerEvents(new TestScoreboardHandler(this), this);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }



}