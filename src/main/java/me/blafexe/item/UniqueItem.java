package me.blafexe.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a custom item with own data and behavior. This item can be linked to any <code>ItemStack</code>, through
 * which its behaviors can be triggered. This item is unique, any data it holds will only be used by one single
 * <code>ItemStack</code> that is referencing the item.
 * Useful for items such as firearms.
 */
public class UniqueItem extends BaseItem implements Cloneable {

    private String id;
    private ItemStack parentStack;

    public UniqueItem(String type, ItemVisuals visuals) {
        super(type, visuals);
    }

    public String getId() {
        return id;
    }

    @Override
    public String getReference() {
        return id;
    }

    protected void setId(@NotNull String id) {
        this.id = id;
    }

    protected void setParentStack(@NotNull ItemStack parentStack) {
        this.parentStack = parentStack;
    }

    @Override
    public UniqueItem clone() throws CloneNotSupportedException {
        UniqueItem clone = (UniqueItem) super.clone();
        // TODO: copy mutable state here, so the clone can't change the internals of the original
        return clone;
    }
}
