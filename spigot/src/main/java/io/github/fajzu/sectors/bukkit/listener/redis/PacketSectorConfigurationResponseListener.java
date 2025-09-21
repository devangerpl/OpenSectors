package io.github.fajzu.sectors.bukkit.listener.redis;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.region.BukkitSectorRegion;
import io.github.fajzu.common.network.packet.impl.SectorConfigurationResponsePacket;
import io.github.fajzu.common.network.packet.PacketListener;
import io.github.fajzu.common.sector.Sector;

import java.util.List;

public class PacketSectorConfigurationResponseListener extends PacketListener<SectorConfigurationResponsePacket> {

    private final BukkitSectorPlugin plugin;

    public PacketSectorConfigurationResponseListener(BukkitSectorPlugin plugin) {
        super(SectorConfigurationResponsePacket.class);

        this.plugin = plugin;
    }

    @Override
    public void handle(SectorConfigurationResponsePacket packet) {
        if (packet.sectors() == null) {
            this.plugin.logger().severe("No found sector with name " + this.plugin.sectorService().currentSectorId());

            this.plugin.getServer().shutdown();
            return;
        }

        this.plugin.logger().log("Received SectorConfigurationResponsePacket with " + List.of(packet.sectors()).size() + " sectors.");

        this.plugin.sectorService().load(packet.sectors());

        for (Sector sector : packet.sectors()) {
            this.plugin.bukkitSectorRegionService().create(sector.id(), new BukkitSectorRegion(sector.region()));
            this.plugin.logger().log("Created BukkitSectorRegion for sector ID: " + sector.id());
        }

        this.plugin.registerSector();

        this.plugin.logger().log("Sectors registered.");
        this.plugin.logger().log("SectorConfigurationResponsePacket handling completed.");
    }
}
