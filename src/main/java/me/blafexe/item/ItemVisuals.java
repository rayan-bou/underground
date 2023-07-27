package me.blafexe.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Determines the visuals of an item. Uses the Minecraft server api.
 */
public class ItemVisuals {

    private Material material;
    private Component name;
    private int customModelData;

    public ItemStack createStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(name);
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
