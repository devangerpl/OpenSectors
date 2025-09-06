package net.lightcode.bridge.listener.redis;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.lightcode.bridge.BridgePlugin;
import net.lightcode.packet.impl.PlayerConnectSectorPacket;
import net.lightcode.redis.PacketListener;

import java.util.Optional;

public class PacketPlayerConnectSectorListener extends PacketListener<PlayerConnectSectorPacket> {

    private final BridgePlugin plugin;

    public PacketPlayerConnectSectorListener(BridgePlugin plugin) {
        super(PlayerConnectSectorPacket.class);

        this.plugin = plugin;
    }

    @Override
    public void handle(PlayerConnectSectorPacket packet) {
        Optional<Player> playerOptional = this.plugin.server().getPlayer(packet.name());
        if (playerOptional.isEmpty()) {
            this.plugin.logger().warning("Player not found: {}", packet.name());
            return;
        }

        Player player = playerOptional.get();
        this.plugin.logger().log("Found player: {}, trying to connect to sector: {}", player.getUsername(), packet.sectorName());

        Optional<RegisteredServer> serverOptional = this.plugin.server().getServer(packet.sectorName());
        if (serverOptional.isEmpty()) {
            this.plugin.logger().warning("Registered server not found: {}", packet.sectorName());
            return;
        }

        RegisteredServer registeredServer = serverOptional.get();
        this.plugin.logger().log("Found registered server: {}, sending connection request...", registeredServer.getServerInfo().getName());

        player.createConnectionRequest(registeredServer).connect();
        this.plugin.logger().log("Connection request sent to player: {}", player.getUsername());
    }
}