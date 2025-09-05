package net.lightcode.bukkit.listener;

import net.lightcode.bukkit.user.User;
import net.lightcode.configuration.impl.MessagesConfiguration;
import net.lightcode.bukkit.helper.ChatHelper;
import net.lightcode.bukkit.region.service.BukkitSectorRegionService;
import net.lightcode.sector.Sector;
import net.lightcode.sector.service.SectorService;
import net.lightcode.sector.type.SectorType;
import net.lightcode.bukkit.transfer.PlayerTransferService;
import net.lightcode.bukkit.user.service.UserService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final UserService userService;

    private final SectorService sectorService;

    private final BukkitSectorRegionService bukkitSectorRegionService;

    private final PlayerTransferService transferService;

    private final MessagesConfiguration messagesConfiguration;

    public PlayerMoveListener(SectorService sectorService, UserService userService, BukkitSectorRegionService bukkitSectorRegionService, PlayerTransferService transferService, MessagesConfiguration messagesConfiguration) {
        this.sectorService = sectorService;
        this.userService = userService;
        this.bukkitSectorRegionService = bukkitSectorRegionService;
        this.transferService = transferService;
        this.messagesConfiguration = messagesConfiguration;
    }

    @EventHandler
    void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location location = event.getTo();
        final User user = this.userService.find(player.getUniqueId());

        Sector sector = this.bukkitSectorRegionService.find(location);
        if (user == null || sector == null) return;

        if (sector.sectorType() == SectorType.SPAWN && this.sectorService.find(SectorType.SPAWN) != null) {
            sector = this.sectorService.find(SectorType.SPAWN);
        }

        if (!sector.isOnline()) {
            this.bukkitSectorRegionService.knock(player);

            player.sendMessage(ChatHelper.colored(this.messagesConfiguration.sectorIsOfflineMessage()));
            return;
        }

        if (user.isTransferCooldown()) {
            this.bukkitSectorRegionService.knock(player);

            player.sendMessage(ChatHelper.colored(this.messagesConfiguration.playerSectorTransferCooldownMessage()));
            return;
        }

        if (user.isRedirecting()) return;

        user.setRedirecting(true);

        this.transferService.connect(player, user, sector,false);
    }
}
