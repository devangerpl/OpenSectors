package io.github.fajzu.sectors.bukkit.runnable;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.common.network.packet.impl.SectorInformationUpdatePacket;
import org.bukkit.scheduler.BukkitRunnable;

public class SectorInformationUpdateRunnable extends BukkitRunnable {

    private final BukkitSectorPlugin plugin;

    public SectorInformationUpdateRunnable(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.networkService().publish("sectors", new SectorInformationUpdatePacket(this.plugin.getServer().getOnlinePlayers().size(), this.plugin.nmsService().minecraftServer().tps()));
    }
}
