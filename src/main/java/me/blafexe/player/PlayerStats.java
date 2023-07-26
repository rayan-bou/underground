package me.blafexe.player;

/**
 * Is responsible for holding and allowing easy access on custom player data/stats. Is stored using the <code>PersistentDataContainer</code>.
 */
public class PlayerStats {

    //Money
    private int money;
    //Level
    private double level;
    private boolean hasLeveledUp;

    /**
     * @param money a value for money.
     * @param level a value for level.<br>The integer places is the actual level, the decimal places are the progress to
     *              the next level.
     */
    public PlayerStats(int money, double level) {
        this.money = money;
        this.level = level;
    }

    /**
     * Adds or removes money from the player, depending on the delta value.
     * @param delta Delta money.
     */
    public void deltaMoney(int delta) {
        money += delta;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Adds a specified amount of levelProgress to a players level progress. If the progress reaches the required value
     * for level up (>= 1), the players level and progress will be adjusted accordingly.
     * @param levelProgress Amount of level points to add.
     */
    public void addLevelProgress(double levelProgress) {

        int oldFullLevel = getFullLevel();
        level += levelProgress;
        int newFullLevel = getFullLevel();

        //Set level up flag
        hasLeveledUp = oldFullLevel < newFullLevel;

    }

    /**
     * @return The current full level a player has reached.
     */
    public int getFullLevel() {
        return (int) Math.floor(level);
    }

    /**
     * @return The progress to the next full level. A full level is reached, when the progress is >= 1.
     */
    public double getLevelProgress() {
        return level - getFullLevel();
    }

    /**
     * @return A double, containing the full level and the level progress.
     */
    public double getLevel() {
        return level;
    }

    /**
     * @return The level up flag. True, if the last change to a players level progress triggered a level up.
     */
    public boolean hasLeveledUp() {
        return hasLeveledUp;
    }

}
