package net.lightcode.bukkit.runnable;

import net.lightcode.bukkit.BukkitSectorPlugin;
import net.lightcode.bukkit.region.BukkitSectorRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SectorBorderUpdateRunnable extends BukkitRunnable {

    private final BukkitSectorPlugin plugin;

    public SectorBorderUpdateRunnable(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        final BukkitSectorRegion sector = this.plugin.bukkitSectorRegionService().currentSectorRegion();

        double minX = sector.minimumPoint().getX();
        double minZ = sector.minimumPoint().getZ();
        double maxX = sector.maximumPoint().getX();
        double maxZ = sector.maximumPoint().getZ();

        double xDiff = Math.abs(maxX - minX) + 1;
        double zDiff = Math.abs(maxZ - minZ) + 1;

        double centerX = (minX + maxX) / 2.0;
        double centerZ = (minZ + maxZ) / 2.0;

        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            final Location location = player.getLocation();

            double addX = xDiff > zDiff ? location.getX() - centerX : 0;
            double addZ = zDiff > xDiff ? location.getZ() - centerZ : 0;

            this.plugin.nmsService().border().sendWorldBorder(player,Math.min(xDiff, zDiff) + 0.4, centerX + 0.5 + addX, centerZ + 0.5 + addZ);
        }
    }
}