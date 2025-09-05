package net.lightcode.bukkit.listener;

import net.lightcode.bukkit.BukkitSectorPlugin;
import net.lightcode.bukkit.helper.ChatHelper;
import net.lightcode.bukkit.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private final BukkitSectorPlugin plugin;

    public PlayerLoginListener(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        this.plugin.logger().log("Player " + player.getName() + " is trying to log in.");
        if (this.plugin.sectorService().sectors().isEmpty()) {
            this.plugin.logger().warning("No sectors available. Kicking player " + player.getName());

            event.setKickMessage(ChatHelper.colored(this.plugin.messagesConfiguration().noSectorsAvailableMessage()));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            return;
        }

        final User user = this.plugin.userService().find(player.getUniqueId());

        if(user != null) {
            this.plugin.logger().log("User data found for player " + player.getName());
            return;
        }

        this.plugin.userService().create(player.getUniqueId(),player.getName());
    }
}
