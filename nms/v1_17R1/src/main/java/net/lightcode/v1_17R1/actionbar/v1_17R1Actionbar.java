package net.lightcode.v1_17R1.actionbar;

import net.kyori.adventure.text.Component;
import net.lightcode.actionbar.Actionbar;
import org.bukkit.entity.Player;

public class v1_17R1Actionbar implements Actionbar {
    @Override
    public void sendActionBar(Player player,
                              String message) {
        player.sendActionBar(Component.text(message));
    }
}
