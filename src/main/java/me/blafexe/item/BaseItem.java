package me.blafexe.item;

import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Represents a custom item with own data and behavior. This item can be linked to any <code>ItemStack</code>, through
 * which its behaviors can be triggered. This item is generic, any data it hold will be shared between all referencing
 * <code>ItemStacks</code>.
 * Useful for items such as consumables.
 */
public class BaseItem {

    private final String type;
    private final ItemVisuals visuals;

    public BaseItem(String type, ItemVisuals visuals) {
        this.type = type;
        this.visuals = visuals;
    }

    /**
     * Is called, whenever a player interacts with the ItemStack referencing this item (parentStack).
     * @param event A PlayerInteractEvent.
     */
    public void onUse(PlayerInteractEvent event) {}

    public String getType() {
        return type;
    }

    public String getReference() {
        return type;
    }

    public ItemVisuals getVisuals() {
        return visuals;
    }

}
