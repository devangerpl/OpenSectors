package net.lightcode.bukkit.event;

import net.lightcode.bukkit.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSaveDataEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    private final Player player;
    private final User user;
    private boolean cancelled;

    public PlayerSaveDataEvent(Player player,
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
