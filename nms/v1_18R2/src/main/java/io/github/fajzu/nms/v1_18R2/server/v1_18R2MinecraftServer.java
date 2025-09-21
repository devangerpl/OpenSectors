package io.github.fajzu.nms.v1_18R2.server;

import io.github.fajzu.nms.api.server.MinecraftServer;
import org.bukkit.Bukkit;

public class v1_18R2MinecraftServer implements MinecraftServer {
    @Override
    public double tps() {
        return Bukkit.getTPS()[0];
    }
}