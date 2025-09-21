package io.github.fajzu.sectors.bukkit.listener.redis;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.user.User;
import io.github.fajzu.common.network.packet.impl.PlayerConnectSectorPacket;
import io.github.fajzu.common.network.packet.impl.PlayerTransferRequestPacket;
import io.github.fajzu.common.network.packet.PacketListener;

public class PacketPlayerTransferRequestListener extends PacketListener<PlayerTransferRequestPacket> {

    private final BukkitSectorPlugin plugin;

    public PacketPlayerTransferRequestListener(BukkitSectorPlugin plugin) {
        super(PlayerTransferRequestPacket.class);

        this.plugin = plugin;
    }

    @Override
    public void handle(PlayerTransferRequestPacket packet) {
        User user = this.plugin.userService().fetch(packet.name());

        if (user == null) {
            this.plugin.getLogger().warning("User not found for transfer: " + packet.name());
            return;
        }

        this.plugin.userService().users().put(user.uuid(), user);
        this.plugin.networkService().publish("bridge", new PlayerConnectSectorPacket(user.name(), this.plugin.sectorService().currentSectorId()));
    }
}
