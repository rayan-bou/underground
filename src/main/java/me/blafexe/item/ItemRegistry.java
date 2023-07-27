package me.blafexe.item;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Holds BaseItems by their respective references.
 */
public class ItemRegistry {

    private final Map<String, BaseItem> map;

    public ItemRegistry() {
        map = new HashMap<>();
    }

    /**
     * Adds an item to the registry using its <code>.getReference()</code>-method for the key.<br>
     * If an item with the same key already exists, the item <b>will not be</b> added.
     * @param baseItem The base item.
     */
    public void addItem(@NotNull BaseItem baseItem) {
        map.putIfAbsent(baseItem.getReference(), baseItem);
    }

    /**
     * Removes an item from the registry. If the item is generic, the item will not be removed. To remove generic items,
     * use the <code>forceRemoveItem</code>-method.
     * @param baseItem The base item to remove.
     */
    public void removeItem(@NotNull BaseItem baseItem) {
        if (baseItem instanceof UniqueItem) map.remove(baseItem.getReference());
    }

    /**
     * Removes an item from the registry, no matter if unique or generic.
     * @param baseItem The base item to remove.
     */
    public void forceRemoveItem(@NotNull BaseItem baseItem) {
        map.remove(baseItem.getReference());
    }

    /**
     * Retrieves an item from the registry using its reference.
     * @param reference The item's reference.
     * @return An optional, containing the item if present in the registry.
     */
    public Optional<BaseItem> getItem(@NotNull String reference) {
        return Optional.ofNullable(map.get(reference));
    }

}
