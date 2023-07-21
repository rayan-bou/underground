package me.blafexe.infoview;

import me.blafexe.event.PlayerJobEvent;
import me.blafexe.event.PlayerTransitionZoneEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//TODO REfactor
public class InfoviewHandler implements Listener {

    private final Plugin plugin;
    private final Map<Player, Infoview> infoviewMap;

    private ZoneDummy zoneDummy = new ZoneDummy();

    public InfoviewHandler(Plugin plugin) {
        this.plugin = plugin;
        infoviewMap = new HashMap<>();
    }

    public Infoview getInfoview(Player player) {
        return infoviewMap.get(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Infoview infoview = new Infoview(Component.text("Provinglife").color(() -> 0x228B22), plugin.getServer().getScoreboardManager());
        infoview.setVisible(event.getPlayer());
        infoviewMap.put(event.getPlayer(), infoview);

        //TODO Remove
        infoview.addElement(new InfoviewElement() {
            @Override
            public int getImportance() {
                return 10;
            }

            @Override
            public Optional<String> getCategory() {
                return Optional.of("Level");
            }

            @Override
            public @NotNull String getText() {
                return "[|||||||| 4 ||||||||]";
            }

            @Override
            public @NotNull String getId() {
                return "level";
            }
        });
        infoview.addElement(new InfoviewElement() {
            @Override
            public int getImportance() {
                return 9;
            }

            @Override
            public Optional<String> getCategory() {
                return Optional.of("Geld");
            }

            @Override
            public @NotNull String getText() {
                return "1239€";
            }

            @Override
            public @NotNull String getId() {
                return "money";
            }
        });

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        infoviewMap.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTransitionZone(PlayerTransitionZoneEvent event) {

        Infoview infoview = getInfoview(event.getPlayer());

        if (infoview == null) return;

        if (event.isInside()) {
            infoview.removeElement(zoneDummy);
            infoview.addElement(event.getZone());
        } else {
            infoview.removeElement(event.getZone());
            infoview.addElement(zoneDummy);
        }

    }

    @EventHandler
    public void onPlayerJob(PlayerJobEvent event) {

        Infoview infoview = getInfoview(event.getPlayer());

        switch (event.getStatus()) {
            case TAKE -> {
                infoview.addElement(event.getJob());
            }
            case FINISH -> {
                infoview.removeElement(event.getJob());
            }
            case UPDATE -> {
                infoview.render();
            }
        }

    }

}
