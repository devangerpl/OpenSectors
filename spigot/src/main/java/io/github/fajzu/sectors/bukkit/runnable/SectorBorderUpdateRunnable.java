package io.github.fajzu.sectors.bukkit.runnable;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.region.BukkitSectorRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SectorBorderUpdateRunnable extends BukkitRunnable {

    private final BukkitSectorPlugin plugin;

    public SectorBorderUpdateRunnable(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        final BukkitSectorRegion sector = this.plugin.bukkitSectorRegionService().currentSectorRegion();

        final double minX = sector.minimumPoint().getX(), minZ = sector.minimumPoint().getZ(), maxX = sector.maximumPoint().getX(), maxZ = sector.maximumPoint().getZ();

        final double xDiff = Math.abs(maxX - minX) + 1, zDiff = Math.abs(maxZ - minZ) + 1;

        final double centerX = (minX + maxX) / 2.0, centerZ = (minZ + maxZ) / 2.0;

        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            final Location location = player.getLocation();

            final double addX = xDiff > zDiff ? location.getX() - centerX : 0, addZ = zDiff > xDiff ? location.getZ() - centerZ : 0;

            this.plugin.nmsService().border().sendWorldBorder(player, Math.min(xDiff, zDiff) + 0.4, centerX + 0.5 + addX, centerZ + 0.5 + addZ);
        }
    }
}