package me.blafexe.player;

import me.blafexe.infoview.InfoviewElement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class Stats {

    private final StatsHandler statsHandler;

    public Stats(StatsHandler statsHandler) {
        this.statsHandler = statsHandler;
    }

    private int money = new Random().nextInt();
    private int level = new Random().nextInt();
    private float levelProgress = new Random().nextInt() + new Random().nextFloat();

    public StatsHandler getStatsHandler() {
        return statsHandler;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        statsHandler.reportUpdate(this);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        statsHandler.reportUpdate(this);
    }

    public float getLevelProgress() {
        return levelProgress;
    }

    public void setLevelProgress(float levelProgress) {
        this.levelProgress = levelProgress;
        statsHandler.reportUpdate(this);
    }

    public InfoviewElement getLevelView() {
        return new InfoviewElement() {
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
                int widthHalf = 15;
                float progress = Math.min(1, levelProgress);
                int colorEnd = Math.round(progress * widthHalf * 2);

                String bars = "§a" + "|".repeat(colorEnd) + "§7" + "|".repeat(widthHalf * 2 - colorEnd);
                int barsCenterIndex = bars.length() / 2;
                return "§8[" + bars.substring(0, barsCenterIndex) + "§f§l " + level + (progress > 0.5 ? " §a" : " §7") +
                        bars.substring(barsCenterIndex) + "§8]";
            }

            @Override
            public @NotNull String getId() {
                return "player_level";
            }
        };
    }

    public InfoviewElement getMoneyView() {
        return new InfoviewElement() {
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
                return money + "pc";
            }

            @Override
            public @NotNull String getId() {
                return "player_money";
            }
        };
    }

}
