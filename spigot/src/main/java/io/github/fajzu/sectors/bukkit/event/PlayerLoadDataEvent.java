package io.github.fajzu.sectors.bukkit.event;

import io.github.fajzu.sectors.bukkit.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLoadDataEvent extends Event {
    private final HandlerList handlers = new HandlerList();
    private final Player player;
    private final User user;
    private boolean cancelled;

    public PlayerLoadDataEvent(Player player,
                               User user) {
        this.cancelled = false;
        this.player = player;
        this.user = user;
    }

    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player player() {
        return this.player;
    }

    public User user() {
        return this.user;
    }
}
