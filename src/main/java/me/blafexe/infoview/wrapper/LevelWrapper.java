package me.blafexe.infoview.wrapper;

import me.blafexe.infoview.InfoviewElement;
import me.blafexe.player.PlayerStats;
import me.blafexe.player.StatsHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LevelWrapper implements InfoviewElement {

    private final Player player;

    public LevelWrapper(Player player) {
        this.player = player;
    }

    private String generateText(PlayerStats playerStats) {

        String formatOn = "§a";
        String formatOff = "§7";
        String formatNumber = "§f";
        char barChar = '|';
        int barSize = 24;
        float progress = (float) Math.min(1, playerStats.getLevelProgress());
        int barOnEnd = Math.round(progress * barSize);

        String s = "A".repeat(barOnEnd) + "B".repeat(barSize - barOnEnd);
        s = s.substring(0, s.length() / 2) + "C" + s.substring(s.length() / 2);

        StringBuilder stringBuilder = new StringBuilder();
        char lastChar = '0';
        for (char c : s.toCharArray()) {
            //Check if format needs to be applied
            if (lastChar != c) {
                stringBuilder.append(switch (c) {
                   case 'A' -> formatOn;
                   case 'B' -> formatOff;
                   case 'C' -> formatNumber;
                   default -> "";
                });
            }

            //Apply value
            switch (c) {
                case 'A', 'B' -> stringBuilder.append(barChar);
                case 'C' -> stringBuilder.append(playerStats.getFullLevel());
            }
        }

        return "§8[" + stringBuilder + "§8]";

    }

    @Override
    public int getImportance() {
        return 10;
    }

    @Override
    public Optional<String> getCategory() {
        return Optional.of("§e§lLevel");
    }

    @Override
    public @NotNull String getText() {
        PlayerStats playerStats = StatsHandler.unpackStats(player);
        return generateText(playerStats);
    }

    @Override
    public @NotNull String getId() {
        return "level";
    }
}
