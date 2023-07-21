package me.blafexe.job.courier;

import me.blafexe.job.Job;
import me.blafexe.job.JobEngine;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class CourierJob implements Job {

    private final int id;
    private ItemStack item;
    private Location targetLocation;
    private double targetRadius;

    public CourierJob(Location targetLocation) {
        this.targetLocation = targetLocation;
        targetRadius = 2;
        item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Job Item"));
        item.setItemMeta(meta);
        id = new Random().nextInt();
    }

    @Override
    public void start(Player player) {
        //Item an den Spieler geben
        player.getInventory().addItem(item);
    }

    @Override
    public void end(Player player) {
        player.getInventory().removeItemAnySlot(item);
    }

    @Override
    public void handleEvent(Event event, JobEngine jobEngine) {

        if (event instanceof PlayerDropItemEvent dropItemEvent) {

            ItemStack itemStack = dropItemEvent.getItemDrop().getItemStack();
            if (itemStack.isSimilar(item)) {

                if (dropItemEvent.getItemDrop().getLocation().distanceSquared(targetLocation) <= targetRadius * targetRadius) {
                    dropItemEvent.getItemDrop().remove();
                    jobEngine.finishJob(((PlayerDropItemEvent) event).getPlayer(), this);
                }

            }

        }

    }

    @Override
    public String getCategory() {
        return "Job";
    }

    @Override
    public String getId() {
        return "job_" + id;
    }

    @Override
    public String getText() {
        return "Ziel " + targetLocation.getX() + ", " + targetLocation.getY() + ", " + targetLocation.getZ();
    }
}
