package net.lightcode.v1_21R1.server;

import net.lightcode.server.MinecraftServer;

public class v1_21R1MinecraftServer implements MinecraftServer {
    @Override
    public double tps() {
        return net.minecraft.server.MinecraftServer.getServer().tps1.getAverage();
    }
}