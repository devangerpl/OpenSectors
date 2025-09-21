package io.github.fajzu.sectors.bukkit.scoreboard;

import io.github.fajzu.sectors.bukkit.helper.ChatHelper;
import io.github.fajzu.common.configuration.impl.MessagesConfiguration;
import io.github.fajzu.common.sector.service.SectorService;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnScoreboard implements ScoreboardProfileFacade {

    private final SectorService sectorService;

    private final MessagesConfiguration messagesConfiguration;

    public SpawnScoreboard(SectorService sectorService,
                           MessagesConfiguration messagesConfiguration) {
        this.sectorService = sectorService;
        this.messagesConfiguration = messagesConfiguration;
    }

    @Override
    public String title(Player player) {
        return ChatHelper.colored(this.messagesConfiguration.scoreboardTitle());
    }

    @Override
    public List<String> lines(Player player) {
        List<String> lines = new ArrayList<>();

        for (String scoreboardLines : this.messagesConfiguration.scoreboardLines()) {
            lines.add(ChatHelper.colored(scoreboardLines
                    .replace("{SECTOR}", this.sectorService.currentSectorId())
                    .replace("{ONLINE}", String.valueOf(this.sectorService.currentSector().players()))
                    .replace("{TPS}", ChatHelper.formatTps(this.sectorService.currentSector().tps()))));
        }

        return lines;
    }

}
