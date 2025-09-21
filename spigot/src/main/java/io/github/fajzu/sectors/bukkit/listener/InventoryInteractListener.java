package io.github.fajzu.sectors.bukkit.listener;

import io.github.fajzu.sectors.bukkit.inventory.api.holder.GuiHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryInteractListener implements Listener {

    @EventHandler
    void onInventoryClick(InventoryClickEvent event) {
        if (!this.isGuiWindow(event.getInventory())) return;

        GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
        event.setCancelled(true);
        holder.handleClick(event);
    }

    @EventHandler
    void onInventoryInteract(InventoryInteractEvent event) {
        if (!this.isGuiWindow(event.getInventory())) return;

        event.setCancelled(true);
    }

    private boolean isGuiWindow(Inventory inventory) {
        return inventory != null
                && inventory.getType() == InventoryType.CHEST
                && inventory.getHolder() instanceof GuiHolder;
    }
}