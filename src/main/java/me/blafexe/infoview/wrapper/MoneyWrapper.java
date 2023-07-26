package me.blafexe.infoview.wrapper;

import me.blafexe.infoview.InfoviewElement;
import me.blafexe.player.PlayerStats;
import me.blafexe.player.StatsHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MoneyWrapper implements InfoviewElement {

    private final Player player;

    public MoneyWrapper(Player player) {
        this.player = player;
    }

    @Override
    public int getImportance() {
        return 9;
    }

    @Override
    public Optional<String> getCategory() {
        return Optional.of("§6§lMoney");
    }

    @Override
    public @NotNull String getText() {
        PlayerStats playerStats = StatsHandler.unpackStats(player);
        return playerStats.getMoney() + "pc";
    }

    @Override
    public @NotNull String getId() {
        return "money";
    }
}
