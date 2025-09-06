package net.lightcode.packet.impl;

import net.lightcode.packet.Packet;

public class PlayerTransferRequestPacket extends Packet {

    private final String name;

    public PlayerTransferRequestPacket() {
        this(null);
    }

    public PlayerTransferRequestPacket(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }
}
