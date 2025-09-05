package net.lightcode.bukkit.listener;

import com.google.common.base.Stopwatch;
import net.lightcode.bukkit.BukkitSectorPlugin;
import net.lightcode.bukkit.helper.ChatHelper;
import net.lightcode.bukkit.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PlayerJoinListener implements Listener {

    private final BukkitSectorPlugin plugin;

    public PlayerJoinListener(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        final Stopwatch stopwatch = Stopwatch.createStarted();
        final Player player = event.getPlayer();
        final User user = this.plugin.userService().find(player.getUniqueId());

        if(user == null) {
            player.kickPlayer(ChatHelper.colored(this.plugin.messagesConfiguration().playerDataNotFoundMessage()));
            return;
        }

        if(user.isRedirecting()) {
            user.loadData(player,this.plugin);

            user.setRedirecting(false);
            user.setTransferCooldown(true);

            player.sendMessage(ChatHelper.colored(this.plugin.messagesConfiguration().playerDataLoadedMessage()).replace("{TIME}",String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS))));
        }

        player.sendTitle(ChatHelper.colored(this.plugin.messagesConfiguration().connectedInfoTitle()), ChatHelper.colored(this.plugin.messagesConfiguration().connectedInfoSubTitle()).replace("{SECTOR}", this.plugin.sectorService().currentSectorId()));
    }

}
