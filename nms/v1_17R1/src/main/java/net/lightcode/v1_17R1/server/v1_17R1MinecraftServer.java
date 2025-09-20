package net.lightcode.v1_17R1.server;

import net.lightcode.server.MinecraftServer;
import org.bukkit.Bukkit;

public class v1_17R1MinecraftServer implements MinecraftServer {
    @Override
    public double tps() {
        return Bukkit.getTPS()[0];
    }
}