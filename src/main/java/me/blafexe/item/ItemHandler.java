package me.blafexe.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class ItemHandler implements Listener {

    //ID - Item
    protected Map<String, BaseItem> items;
    private ItemEngine itemEngine;
    private Plugin plugin;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;

        if (event.hasItem()) {
            itemEngine.getItem(event.getItem()).ifPresent(baseItem -> {
                baseItem.onUse(event);
            });
        }

    }

    //Update Parent
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.isCancelled()) return;
        if (event.getClickedInventory() == null) return;

        ItemStack cursorItemStack = event.getCursor();

        itemEngine.getItem(cursorItemStack).ifPresent(cursorBaseItem -> {
            if (cursorBaseItem instanceof UniqueItem uniqueItem) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {

                    ItemStack newParentItemStack = event.getClickedInventory().getItem(event.getSlot());
                    BaseItem backtracedBaseItem = itemRegistry.getItem(newParentItemStack);
                    if (backtracedBaseItem == null) return;
                    if (!backtracedBaseItem.equals(baseItem)) return;
                    baseItem.setParentItemStack(newParentItemStack);

                }, 1);
            }
        });


    }

}
