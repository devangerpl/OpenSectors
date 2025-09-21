package io.github.fajzu.nms.v1_21R1.server;

import io.github.fajzu.nms.api.server.MinecraftServer;

public class v1_21R1MinecraftServer implements MinecraftServer {
    @Override
    public double tps() {
        return net.minecraft.server.MinecraftServer.getServer().tps1.getAverage();
    }
}