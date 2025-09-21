package io.github.fajzu.nms.v1_21R1.border;

import io.github.fajzu.nms.api.border.Border;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class v1_21R1Border implements Border {
    @Override
    public void sendWorldBorder(Player player,
                                double size,
                                double centerX,
                                double centerZ) {
        WorldBorder worldBorder = Bukkit.createWorldBorder();

        worldBorder.setWarningDistance(0);

        worldBorder.setSize(size);

        worldBorder.setCenter(centerX, centerZ);

        player.setWorldBorder(worldBorder);
    }
}