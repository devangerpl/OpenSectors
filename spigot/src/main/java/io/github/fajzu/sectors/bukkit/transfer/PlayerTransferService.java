package io.github.fajzu.sectors.bukkit.transfer;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.event.PlayerSectorChangeEvent;
import io.github.fajzu.sectors.bukkit.user.User;
import io.github.fajzu.common.network.packet.impl.PlayerTransferRequestPacket;
import io.github.fajzu.common.sector.Sector;
import io.github.fajzu.common.sector.type.SectorType;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerTransferService {

    private final BukkitSectorPlugin plugin;

    public PlayerTransferService(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect(final Player player,
                        final User user,
                        final Sector sector,
                        boolean transferCheck) {
        if (sector.sectorType() == SectorType.SPAWN
                && this.plugin.sectorService().currentSector().sectorType() == SectorType.SPAWN
                && !transferCheck) return;

        this.plugin.getLogger().info("Starting connection process for player " + player.getName() + " to sector " + sector.id());

        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            PlayerSectorChangeEvent sectorChangeEvent = new PlayerSectorChangeEvent(player, this.plugin.sectorService().currentSector(), sector);
            this.plugin.getServer().getPluginManager().callEvent(sectorChangeEvent);

            if (sectorChangeEvent.isCancelled()) {
                this.plugin.getLogger().info("Connection cancelled by event for player " + player.getName());
                return;
            }

            if (player.isInsideVehicle()) {
                player.leaveVehicle();
            }

            this.plugin.getLogger().info("Saving user data for player " + player.getName());

            user.saveData(player, this.plugin);

            CompletableFuture.runAsync(() -> this.plugin.userService().userRepository().update(user)).thenAccept(unused -> {
                this.plugin.networkService().publish(sector.id(), new PlayerTransferRequestPacket(player.getName()));

                this.plugin.getLogger().info("Connection process finished for player " + player.getName());
            });
        });
    }
}

