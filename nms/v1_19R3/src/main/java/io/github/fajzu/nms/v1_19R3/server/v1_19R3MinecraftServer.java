package io.github.fajzu.nms.v1_19R3.server;

import io.github.fajzu.nms.api.server.MinecraftServer;
import org.bukkit.Bukkit;

public class v1_19R3MinecraftServer implements MinecraftServer {
    @Override
    public double tps() {
        return Bukkit.getTPS()[0];
    }
}