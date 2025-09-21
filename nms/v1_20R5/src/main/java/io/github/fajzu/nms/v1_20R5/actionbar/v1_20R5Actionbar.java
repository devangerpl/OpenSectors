package io.github.fajzu.nms.v1_20R5.actionbar;

import io.github.fajzu.nms.api.actionbar.Actionbar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class v1_20R5Actionbar implements Actionbar {
    @Override
    public void sendActionBar(Player player,
                              String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
