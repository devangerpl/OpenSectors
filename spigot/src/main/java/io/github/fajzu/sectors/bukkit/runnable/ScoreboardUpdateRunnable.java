package io.github.fajzu.sectors.bukkit.runnable;

import io.github.fajzu.sectors.bukkit.BukkitSectorPlugin;
import io.github.fajzu.sectors.bukkit.scoreboard.ScoreboardPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdateRunnable extends BukkitRunnable {

    private final BukkitSectorPlugin plugin;

    public ScoreboardUpdateRunnable(BukkitSectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (final ScoreboardPlayer scoreboardPlayer : this.plugin.scoreboardPlayerService().scoreboardPlayers().values()) {
            scoreboardPlayer.update();
        }
    }
}
