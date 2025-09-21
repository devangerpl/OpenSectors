package net.lightcode.bukkit.listener.redis;

import net.lightcode.network.packet.impl.SectorInformationUpdatePacket;
import net.lightcode.network.packet.PacketListener;
import net.lightcode.sector.Sector;
import net.lightcode.sector.service.SectorService;

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
