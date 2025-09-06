package net.lightcode.bukkit.listener.redis;

import net.lightcode.bukkit.BukkitSectorPlugin;
import net.lightcode.packet.impl.PlayerConnectSectorPacket;
import net.lightcode.packet.impl.PlayerTransferRequestPacket;
import net.lightcode.redis.PacketListener;
import net.lightcode.bukkit.user.User;

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
