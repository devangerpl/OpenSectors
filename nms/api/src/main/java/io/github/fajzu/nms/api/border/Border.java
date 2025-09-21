package io.github.fajzu.nms.api.border;

import org.bukkit.entity.Player;

public interface Border {
    void sendWorldBorder(Player player,
                         double size,
                         double centerX,
                         double centerZ);
}
