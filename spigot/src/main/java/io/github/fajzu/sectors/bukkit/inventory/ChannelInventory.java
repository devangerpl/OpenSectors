package io.github.fajzu.sectors.bukkit.inventory;

import io.github.fajzu.sectors.bukkit.helper.ChatHelper;
import io.github.fajzu.sectors.bukkit.helper.CustomHeadHelper;
import io.github.fajzu.sectors.bukkit.inventory.api.GuiWindow;
import io.github.fajzu.sectors.bukkit.inventory.api.builder.ItemBuilder;
import io.github.fajzu.sectors.bukkit.transfer.PlayerTransferService;
import io.github.fajzu.sectors.bukkit.user.User;
import io.github.fajzu.sectors.bukkit.user.service.UserService;
import io.github.fajzu.common.configuration.impl.MessagesConfiguration;
import io.github.fajzu.common.sector.Sector;
import io.github.fajzu.common.sector.service.SectorService;
import io.github.fajzu.common.sector.type.SectorType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelInventory {

    private final Player player;
    private final GuiWindow guiWindow;

    public ChannelInventory(Player player,
                            SectorService sectorService,
                            UserService userService,
                            MessagesConfiguration messagesConfiguration,
                            PlayerTransferService playerTransferService) {
        this.player = player;

        this.guiWindow = new GuiWindow("&7Kanaly...", 1);

        int i = 0;

        for (Sector sector : sectorService
                .sectors()
                .values()
                .stream()
                .filter(sector -> sector.sectorType().equals(SectorType.SPAWN))
                .collect(Collectors.toList())) {

            ItemStack itemStack = (sector.isOnline() ?
                    CustomHeadHelper.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGEyZjljNzYxZmMxMzFkYmViZDA3M2IwYjFkZDdkMWJhZWExOTFjZTlkMzNjNDljM2FjYTk0NDhiMWI2YjY4NCJ9fX0=")
                    :
                    CustomHeadHelper.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIwZTA3NjMyMmZjOWFmNzk1OTJlYjg1MmNhOGM3YzQ1YmIyYzNjZWFiYzNjMGU4YTZhMWUwNGI0Y2UzZDM0YiJ9fX0="));
            ItemBuilder sectorItem = new ItemBuilder(itemStack)
                    .name(
                            "&7Sektor &a" + sector.id()
                    );

            sectorItem.lore(sector.isOnline() ?
                    List.of(
                            "",
                            "&7Online: &a" + sector.players(),
                            "&7TPS: &a" + ChatHelper.formatTps(sector.tps()),
                            "")
                    :
                    List.of(
                            "",
                            "&cSektor jest offline",
                            ""
                    ));

            sectorItem.lore(sector.id().equals(sectorService.currentSectorId()) ? "&eZnajdujesz sie na tym kanale" : "&eKliknij aby polaczyc sie z kanalem");

            this.guiWindow.setItem(i, sectorItem.build(), (event -> {
                if (sectorService.currentSector().id().equals(sector.id())) {
                    player.sendMessage(ChatHelper.colored(messagesConfiguration.playerAlreadyConnectedMessage()));
                    return;
                }

                if (!sector.isOnline()) {
                    player.sendMessage(ChatHelper.colored(messagesConfiguration.sectorIsOfflineMessage()));
                    return;
                }

                final User user = userService.find(player.getUniqueId());

                if (user == null) {
                    player.kickPlayer(ChatHelper.colored(messagesConfiguration.playerDataNotFoundMessage()));
                    return;
                }

                playerTransferService.connect(player, user, sector, true);
            }));

            i++;
        }

    }

    public void openInventory() {
        this.guiWindow.open(this.player);
    }

}