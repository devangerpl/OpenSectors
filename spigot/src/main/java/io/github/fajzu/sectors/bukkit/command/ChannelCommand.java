package io.github.fajzu.sectors.bukkit.command;

import io.github.fajzu.sectors.bukkit.inventory.ChannelInventory;
import io.github.fajzu.sectors.bukkit.transfer.PlayerTransferService;
import io.github.fajzu.sectors.bukkit.user.service.UserService;
import io.github.fajzu.common.configuration.impl.MessagesConfiguration;
import io.github.fajzu.common.sector.service.SectorService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChannelCommand implements CommandExecutor {

    private final UserService userService;

    private final SectorService sectorService;

    private final PlayerTransferService transferService;

    private final MessagesConfiguration messagesConfiguration;

    public ChannelCommand(UserService userService,
                          SectorService sectorService,
                          PlayerTransferService transferService,
                          MessagesConfiguration messagesConfiguration) {
        this.userService = userService;
        this.sectorService = sectorService;
        this.transferService = transferService;
        this.messagesConfiguration = messagesConfiguration;
    }

    @Override
    public boolean onCommand(CommandSender commandSender,
                             Command command,
                             String s,
                             String[] strings) {
        Player player = (Player) commandSender;

        ChannelInventory channelInventory = new ChannelInventory(player, this.sectorService, this.userService, this.messagesConfiguration, this.transferService);

        channelInventory.openInventory();
        return true;
    }
}
