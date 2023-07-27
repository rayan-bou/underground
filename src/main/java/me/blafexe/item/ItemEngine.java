package me.blafexe.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * The ItemEngine connects all parts of the item system and provides methods for simple management of custom items.
 */
public record ItemEngine(ItemFactory itemFactory, ItemHandler itemHandler) {

    /**
     * Creates a new custom item from a given type, ready for use.
     * Unique items must be manually deleted, if not needed anymore.
     * @param type Desired type of the item.
     * @return An optional, containing the ready to use item stack if creation was successful.
     */
    public Optional<ItemStack> createItem(@NotNull String type) {

        try {
            BaseItem baseItem = itemFactory.createItemFromType(type).orElseThrow();
            ItemStack itemStack = itemFactory.createStackFromItem(baseItem).orElseThrow();
            itemHandler.items.put(baseItem.getReference(), baseItem);
            return Optional.of(itemStack);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public void deleteItem(@NotNull ItemStack itemStack) {

        getItem(itemStack).ifPresent(baseItem -> {
            if (baseItem instanceof UniqueItem) itemHandler.items.remove(baseItem.getReference());
        });

    }

    /**
     * Searches for the BaseItem referenced by the given ItemStack.
     * @param itemStack The item stack.
     * @return An optional, containing the base item if found, empty otherwise.
     */
    public Optional<BaseItem> getItem(@NotNull ItemStack itemStack) {

        try {
            String id = itemFactory.unpackReference(itemStack).orElseThrow();
            BaseItem baseItem = itemHandler.items.get(id);
            if (baseItem != null) return Optional.of(baseItem);
            else return Optional.empty();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

}
