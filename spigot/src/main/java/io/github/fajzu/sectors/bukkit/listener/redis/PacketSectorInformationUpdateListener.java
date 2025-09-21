package io.github.fajzu.sectors.bukkit.listener.redis;

import io.github.fajzu.common.network.packet.impl.SectorInformationUpdatePacket;
import io.github.fajzu.common.network.packet.PacketListener;
import io.github.fajzu.common.sector.Sector;
import io.github.fajzu.common.sector.service.SectorService;

public class PacketSectorInformationUpdateListener extends PacketListener<SectorInformationUpdatePacket> {

    private final SectorService sectorService;

    public PacketSectorInformationUpdateListener(SectorService sectorService) {
        super(SectorInformationUpdatePacket.class);

        this.sectorService = sectorService;
    }

    @Override
    public void handle(SectorInformationUpdatePacket packet) {
        Sector sector = this.sectorService.find(packet.sender());

        if (sector == null) return;

        sector.lastUpdate(System.currentTimeMillis());
        sector.players(packet.players());
        sector.tps(packet.tps());
    }
}
