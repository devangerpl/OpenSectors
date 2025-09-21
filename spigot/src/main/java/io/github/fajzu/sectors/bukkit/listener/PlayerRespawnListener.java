package io.github.fajzu.sectors.bukkit.listener;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.helper.ChatHelper;
import io.github.fajzu.sectors.bukkit.user.User;
import io.github.fajzu.common.sector.Sector;
import io.github.fajzu.common.sector.type.SectorType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final BukkitSectorPlugin plugin;

    public PlayerRespawnListener(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final User user = this.plugin.userService().find(player.getUniqueId());
        final Sector sector = this.plugin.sectorService().find(SectorType.SPAWN);

        if (user == null) {
            player.kickPlayer(this.plugin.messagesConfiguration().playerDataNotFoundMessage());
            return;
        }

        if (sector == null) {
            player.kickPlayer(ChatHelper.colored(this.plugin.messagesConfiguration().spawnSectorNotFoundMessage()));
            return;
        }

        event.setRespawnLocation(this.plugin.bukkitSectorRegionService().randomLocation(sector));

        this.plugin.transferService().connect(player, user, sector, false);
    }
}
