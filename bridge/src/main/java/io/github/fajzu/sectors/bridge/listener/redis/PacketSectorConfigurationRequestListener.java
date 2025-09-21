package io.github.fajzu.sectors.bridge.listener.redis;

import io.github.fajzu.common.network.NetworkService;
import io.github.fajzu.sectors.bridge.BridgeLogger;
import io.github.fajzu.common.network.packet.impl.SectorConfigurationRequestPacket;
import io.github.fajzu.common.network.packet.impl.SectorConfigurationResponsePacket;
import io.github.fajzu.common.network.packet.PacketListener;
import io.github.fajzu.common.sector.Sector;
import io.github.fajzu.common.sector.service.SectorService;

public class PacketSectorConfigurationRequestListener extends PacketListener<SectorConfigurationRequestPacket> {

    private final SectorService sectorService;

    private final NetworkService networkService;

    private final BridgeLogger logger;

    public PacketSectorConfigurationRequestListener(SectorService sectorService,
                                                    NetworkService networkService,
                                                    BridgeLogger logger) {
        super(SectorConfigurationRequestPacket.class);

        this.sectorService = sectorService;
        this.networkService = networkService;
        this.logger = logger;
    }

    @Override
    public void handle(SectorConfigurationRequestPacket packet) {
        this.logger.log("Received SectorConfigurationRequestPacket from sender: {}", packet.sender());

        if (this.sectorService.find(packet.sender()) == null) {
            this.networkService.publish(
                    packet.sender(),
                    new SectorConfigurationResponsePacket(null)
            );

            this.logger.warning("No sector found for sender: {}", packet.sender());
            return;
        }

        this.logger.log("Sector found for sender: {}. Sending sector configuration response.", packet.sender());

        this.networkService.publish(
                packet.sender(),
                new SectorConfigurationResponsePacket(this.sectorService.sectors().values().toArray(new Sector[0]))
        );

        this.logger.warning("SectorConfigurationResponsePacket sent to sender: {}", packet.sender());
    }
}