package me.blafexe.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ItemFactory {

    private final NamespacedKey ITEM_REFERENCE = new NamespacedKey("underground", "reference");

    public Optional<String> unpackReference(ItemStack itemStack) {

        if (!itemStack.hasItemMeta()) return Optional.empty();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (!container.has(ITEM_REFERENCE)) return Optional.empty();
        return Optional.ofNullable(container.get(ITEM_REFERENCE, PersistentDataType.STRING));

    }

    public boolean packReference(@NotNull ItemStack itemStack, @NotNull String reference) {

        if (!itemStack.hasItemMeta()) return false;

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(ITEM_REFERENCE, PersistentDataType.STRING, reference);

        itemStack.setItemMeta(itemMeta);
        return true;

    }

    //List of presets, if unique, preset must not exist within the world
    private Map<String, BaseItem> presets;

    /**
     * Creates an <code>ItemStack</code> from an existing BaseItem and its visual property. If the item is an instance of
     * <code>BaseItem</code> the stack will hold a generic reference (type), if the item is an instance of
     * <code>UniqueItem</code> the stack will hold a unique reference (id). The unique item will also automatically be
     * given a reference to the created stack (parent stack), if creation was successful.
     * This method <b>will not check</b>, if there is already an <code>ItemStack</code> around, holding a unique reference.
     * In case of doubt there will be two ItemStacks pointing to a single unique item.
     *
     * @param baseItem Item for which a stack should be created.
     * @return An optional, containing an ItemStack if creation was successful.
     */
    public Optional<ItemStack> createStackFromItem(@NotNull BaseItem baseItem) {

        //Get correct reference, depending on item type
        String reference = baseItem.getReference();

        //Create stack and link reference
        ItemStack itemStack = baseItem.getVisuals().createStack();
        if (packReference(itemStack, reference)) {
            if (baseItem instanceof UniqueItem uniqueItem) uniqueItem.setParentStack(itemStack);
            return Optional.of(itemStack);
        }
        return Optional.empty();

    }

    /**
     * Provides an instance of <code>BaseItem</code>, depending on the specified type.
     * If the preset defined by the type is generic (is or extends class <code>BaseItem</code>), then the preset itself
     * will be returned.<br>
     * If the defined preset for that type is unique (is or extends class <code>UniqueItem</code>), a new instance of that
     * (sub-)class will be created and returned.
     *
     * @param type Desired type of the item.
     * @return An optional containing the provided instance, empty if type is invalid or creation could not be completed.
     */
    public Optional<BaseItem> createItemFromType(@NotNull String type) {

        //Check for valid type
        BaseItem baseItem = presets.get(type);
        if (baseItem == null) return Optional.empty();

        //Create instance if necessary
        if (baseItem instanceof UniqueItem uniqueItem) {
            try {
                UniqueItem clone = uniqueItem.clone();
                clone.setId(Integer.toHexString(ThreadLocalRandom.current().nextInt(0x1000000)));
                return Optional.of(clone);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        } else {
            return Optional.of(baseItem);
        }
    }

}
