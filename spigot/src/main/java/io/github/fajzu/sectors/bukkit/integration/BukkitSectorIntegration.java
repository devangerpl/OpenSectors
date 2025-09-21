package io.github.fajzu.sectors.bukkit.integration;

import io.github.fajzu.common.network.NetworkService;
import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.user.User;
import io.github.fajzu.sectors.bukkit.user.service.UserService;
import io.github.fajzu.common.network.packet.Packet;
import io.github.fajzu.common.network.packet.PacketListener;
import io.github.fajzu.common.sector.Sector;
import io.github.fajzu.common.sector.service.SectorService;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitSectorIntegration {

    private final BukkitSectorPlugin instance;

    private BukkitSectorIntegration() {
        this.instance = BukkitSectorPlugin.getPlugin(BukkitSectorPlugin.class);
    }

    public static BukkitSectorIntegration create() {
        return new BukkitSectorIntegration();
    }

    public User findUserByUUID(UUID uuid) {
        return this.instance.userService().find(uuid);
    }

    public User findUserByName(String name) {
        return this.instance.userService().find(name);
    }

    public Sector findSectorByName(String name) {
        return this.instance.sectorService().find(name);
    }

    public Sector findSectorByLocation(Location location) {
        return this.instance.bukkitSectorRegionService().find(location);
    }

    public Sector currentSector() {
        return this.instance.sectorService().currentSector();
    }

    public List<Sector> allSectors() {
        return new ArrayList<>(this.instance.sectorService().sectors().values());
    }

    public List<User> allUsers() {
        return new ArrayList<>(this.instance.userService().users().values());
    }

    public void sendPacket(String channel,
                           Packet packet) {
        this.instance.networkService().publish(channel, packet);
    }

    public void subscribe(String channel,
                          PacketListener<?> listener) {
        this.instance.networkService().subscribe(channel, listener);
    }

    public SectorService sectorService() {
        return this.instance.sectorService();
    }

    public UserService userService() {
        return this.instance.userService();
    }

    public NetworkService messengerService() {
        return this.instance.networkService();
    }

    public BukkitSectorPlugin instance() {
        return this.instance;
    }

}