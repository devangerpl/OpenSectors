package net.lightcode.bukkit.region.service;

import net.lightcode.bukkit.region.BukkitSectorRegion;
import net.lightcode.bukkit.BukkitSectorPlugin;
import net.lightcode.sector.Sector;
import net.lightcode.sector.type.SectorType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitSectorRegionService {

    private final Map<String, BukkitSectorRegion> sectorRegions = new ConcurrentHashMap<>();

    private final BukkitSectorPlugin plugin;

    public BukkitSectorRegionService(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    public void create(String name, BukkitSectorRegion bukkitSectorRegion) {
        this.sectorRegions.put(name, bukkitSectorRegion);
    }

    public BukkitSectorRegion find(String name) {
        return this.sectorRegions.get(name);
    }

    public boolean isInsideBorder(Location location) {
        final BukkitSectorRegion region = this.currentSectorRegion();

        final double xDiff = Math.abs(region.maximumPoint().getX() - region.minimumPoint().getX()) + 1;
        final double zDiff = Math.abs(region.maximumPoint().getZ() - region.minimumPoint().getZ()) + 1;

        final double borderSize = Math.min(xDiff, zDiff);

        return location.getBlockX() >= borderSize || location.getBlockX() <= -borderSize
                || location.getBlockZ() >= borderSize || location.getBlockZ() <= -borderSize;
    }

    public Sector find(Location location) {
        return this.plugin.sectorService().sectors().values().stream()
                .filter(sector -> {
                    final BukkitSectorRegion region = this.sectorRegions.get(sector.id());
                    return region != null && region.isInside(location);
                })
                .filter(sector -> !sector.equals(this.plugin.sectorService().currentSector()))
                .findFirst()
                .orElse(null);
    }

    public Location randomLocation(Sector sector) {
        final double x = sector.region().minX() + Math.random() * (sector.region().maxX() - sector.region().minX());
        final double z = sector.region().minZ() + Math.random() * (sector.region().maxZ() - sector.region().minZ());

        World world = Bukkit.getWorld("world");

        return new Location(world, x, world.getHighestBlockYAt((int) x, (int) z), z);
    }

    public void knock(Player player) {
        final BukkitSectorRegion sectorRegion = this.currentSectorRegion();

        if(sectorRegion == null) return;

        final Location location = new Location(player.getLocation().getWorld(),
                sectorRegion.center().getX(),
                player.getLocation().getY(),
                sectorRegion.center().getZ());

        player.setVelocity(location.toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.2D));
    }

    public double distance(Location location) {
        final BukkitSectorRegion sectorRegion = this.currentSectorRegion();

        if (sectorRegion == null) return 0.0;

        final Vector point = location.toVector();

        return Math.min(
                Math.min(Math.abs(point.getX() - sectorRegion.minimumPoint().getX()), Math.abs(sectorRegion.maximumPoint().getX() - point.getX())),
                Math.min(Math.abs(point.getZ() - sectorRegion.maximumPoint().getZ()), Math.abs(sectorRegion.maximumPoint().getZ() - point.getZ()))
        );
    }

    public Map<String, BukkitSectorRegion> regions() {
        return this.sectorRegions;
    }

    public BukkitSectorRegion currentSectorRegion() {
        return this.sectorRegions.get(this.plugin.sectorService().currentSectorId());
    }
}
