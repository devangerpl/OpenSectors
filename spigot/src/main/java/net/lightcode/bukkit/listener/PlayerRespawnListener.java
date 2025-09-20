package net.lightcode.bukkit.listener;

import net.lightcode.bukkit.BukkitSectorPlugin;
import net.lightcode.bukkit.helper.ChatHelper;
import net.lightcode.bukkit.user.User;
import net.lightcode.sector.Sector;
import net.lightcode.sector.type.SectorType;
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
