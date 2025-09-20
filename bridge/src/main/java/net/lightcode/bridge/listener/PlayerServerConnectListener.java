package net.lightcode.bridge.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.lightcode.bridge.BridgePlugin;
import net.lightcode.sector.Sector;
import net.lightcode.sector.type.SectorType;

public class PlayerServerConnectListener {

    private final BridgePlugin plugin;

    public PlayerServerConnectListener(BridgePlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        ;

        Sector sector = this.plugin.sectorService().find(
                this.plugin.networkService().databaseConnection().sync().get(player.getUsername())
        );

        if (sector == null || sector.sectorType() == SectorType.SPAWN) {
            sector = this.plugin.sectorService().find(SectorType.SPAWN);
        }

        if (sector == null) {
            player.disconnect(Component.text("Brak dostępnych serwerów."));
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
            return;
        }

        this.plugin.server().getServer(sector.id()).ifPresentOrElse(server ->
                        event.setResult(ServerPreConnectEvent.ServerResult.allowed(server)),
                () -> {
                    player.disconnect(Component.text("Brak dostępnych serwerów."));
                    event.setResult(ServerPreConnectEvent.ServerResult.denied());
                }
        );
    }
}
