package io.github.fajzu.sectors.bukkit.listener.redis;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.helper.ChatHelper;
import io.github.fajzu.common.network.packet.impl.PlayerSendMessagePacket;
import io.github.fajzu.common.network.packet.PacketListener;

public class PacketPlayerSendMessageListener extends PacketListener<PlayerSendMessagePacket> {

    private final BukkitSectorPlugin plugin;

    public PacketPlayerSendMessageListener(BukkitSectorPlugin plugin) {
        super(PlayerSendMessagePacket.class);

        this.plugin = plugin;
    }

    @Override
    public void handle(PlayerSendMessagePacket packet) {
        this.plugin.getServer().broadcastMessage(ChatHelper.colored("&7" + packet.name() + ": " + packet.message()));
    }
}
