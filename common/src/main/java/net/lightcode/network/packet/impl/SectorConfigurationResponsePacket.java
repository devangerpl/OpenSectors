package net.lightcode.network.packet.impl;

import net.lightcode.network.packet.Packet;
import net.lightcode.sector.Sector;

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
