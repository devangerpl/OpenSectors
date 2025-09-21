package io.github.fajzu.common.network.packet.impl;

import io.github.fajzu.common.network.packet.Packet;
import io.github.fajzu.common.sector.Sector;

public class SectorConfigurationResponsePacket extends Packet {

    private final Sector[] sectors;

    public SectorConfigurationResponsePacket() {
        this(null);
    }

    public SectorConfigurationResponsePacket(Sector[] sectors) {
        this.sectors = sectors;
    }

    public Sector[] sectors() {
        return this.sectors;
    }
}
