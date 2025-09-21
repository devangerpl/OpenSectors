package io.github.fajzu.nms.api.actionbar;

import org.bukkit.entity.Player;

public interface Actionbar {

    void sendActionBar(Player player,
                       String message);
}
