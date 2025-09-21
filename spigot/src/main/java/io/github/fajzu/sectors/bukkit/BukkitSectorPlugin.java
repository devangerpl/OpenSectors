package io.github.fajzu.sectors.bukkit;

import io.github.fajzu.common.network.NetworkService;
import io.github.fajzu.sectors.bukkit.listener.*;
import io.github.fajzu.sectors.bukkit.listener.redis.PacketPlayerSendMessageListener;
import io.github.fajzu.sectors.bukkit.listener.redis.PacketSectorConfigurationResponseListener;
import io.github.fajzu.sectors.bukkit.listener.redis.PacketSectorInformationUpdateListener;
import io.github.fajzu.sectors.bukkit.listener.redis.PacketPlayerTransferRequestListener;
import io.github.fajzu.sectors.bukkit.runnable.SectorInformationUpdateRunnable;
import io.github.fajzu.sectors.bukkit.user.User;
import io.github.fajzu.sectors.bukkit.user.service.UserService;
import io.github.fajzu.nms.api.NmsService;
import io.github.fajzu.sectors.bukkit.command.ChannelCommand;
import io.github.fajzu.common.configuration.impl.DatabaseConfiguration;
import io.github.fajzu.common.configuration.impl.MessagesConfiguration;
import io.github.fajzu.common.configuration.impl.SectorConfiguration;
import io.github.fajzu.common.configuration.service.ConfigurationService;
import io.github.fajzu.sectors.bukkit.helper.ChatHelper;
import io.github.fajzu.sectors.bukkit.helper.NmsHelper;
import io.github.fajzu.common.network.packet.impl.SectorConfigurationRequestPacket;
import io.github.fajzu.sectors.bukkit.runnable.ActionBarNotificationRunnable;
import io.github.fajzu.sectors.bukkit.runnable.ScoreboardUpdateRunnable;
import io.github.fajzu.sectors.bukkit.runnable.SectorBorderUpdateRunnable;
import io.github.fajzu.sectors.bukkit.scoreboard.ScoreboardPlayerService;
import io.github.fajzu.sectors.bukkit.scoreboard.SpawnScoreboard;
import io.github.fajzu.common.sector.Sector;
import io.github.fajzu.common.sector.service.SectorService;
import io.github.fajzu.common.sector.type.SectorType;
import io.github.fajzu.sectors.bukkit.transfer.PlayerTransferService;
import io.github.fajzu.common.updater.UpdaterService;
import io.github.fajzu.sectors.bukkit.region.service.BukkitSectorRegionService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class BukkitSectorPlugin extends JavaPlugin {

    private BukkitLogger logger;

    private NetworkService networkService;
    private PlayerTransferService transferService;

    private SectorService sectorService;
    private UserService userService;
    private ScoreboardPlayerService scoreboardPlayerService;
    private BukkitSectorRegionService bukkitSectorRegionService;

    private MessagesConfiguration messagesConfiguration;
    private DatabaseConfiguration databaseConfiguration;
    private SectorConfiguration sectorConfiguration;

    private NmsService nmsService;

    @Override
    public void onLoad() {
        this.logger = new BukkitLogger(Logger.getLogger("opensectors-spigot"));

        ConfigurationService configurationService = new ConfigurationService(this.getDataFolder().getAbsolutePath());

        this.messagesConfiguration = configurationService.loadConfiguration(MessagesConfiguration.class);
        this.sectorConfiguration = configurationService.loadConfiguration(SectorConfiguration.class);
        this.databaseConfiguration = configurationService.loadConfiguration(DatabaseConfiguration.class);
        this.logger.fine("Configurations loaded: Sector, Database, Messages.");

        this.logger.log("OpenSectors loading...");
    }

    @Override
    public void onEnable() {
        String text = "\n\n" +
                " ██████╗ ██████╗ ███████╗███╗   ██╗███████╗███████╗ ██████╗████████╗ ██████╗ ██████╗ ███████╗\n" +
                "██╔═══██╗██╔══██╗██╔════╝████╗  ██║██╔════╝██╔════╝██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗██╔════╝\n" +
                "██║   ██║██████╔╝█████╗  ██╔██╗ ██║███████╗█████╗  ██║        ██║   ██║   ██║██████╔╝███████╗\n" +
                "██║   ██║██╔═══╝ ██╔══╝  ██║╚██╗██║╚════██║██╔══╝  ██║        ██║   ██║   ██║██╔══██╗╚════██║\n" +
                "╚██████╔╝██║     ███████╗██║ ╚████║███████║███████╗╚██████╗   ██║   ╚██████╔╝██║  ██║███████║\n" +
                " ╚═════╝ ╚═╝     ╚══════╝╚═╝  ╚═══╝╚══════╝╚══════╝ ╚═════╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝\n";
        this.logger.log(text);

        this.nmsService = NmsHelper.findNmsService(this);
        this.logger.log("NMS service found: " + (this.nmsService != null));

        if (this.nmsService == null) {
            this.getServer().shutdown();
            return;
        }

        this.sectorService = new SectorService(sectorConfiguration.currentSector());
        this.bukkitSectorRegionService = new BukkitSectorRegionService(this);
        this.transferService = new PlayerTransferService(this);

        this.networkService = new NetworkService(
                databaseConfiguration.redisHost(),
                databaseConfiguration.redisPort(),
                databaseConfiguration.redisPassword(),
                this.sectorService.currentSectorId()
        );

        this.logger.log("NetworkService started with sender: " + this.sectorService.currentSectorId());

        this.userService = new UserService(this.networkService);

        this.networkService.subscribe(this.sectorService.currentSectorId(), new PacketSectorConfigurationResponseListener(this));
        this.networkService.subscribe(this.sectorService.currentSectorId(), new PacketPlayerTransferRequestListener(this));
        this.networkService.subscribe("sectors", new PacketSectorInformationUpdateListener(this.sectorService));
        this.networkService.subscribe("sectors", new PacketPlayerSendMessageListener(this));
        this.logger.log("Messenger subscriptions done.");

        this.checkForUpdates();

        this.initListeners();
        this.logger.fine("Bukkit listeners registered");

        new SectorInformationUpdateRunnable(this).runTaskTimerAsynchronously(this, 50L, 50L);
        new ActionBarNotificationRunnable(this).runTaskTimer(this, 1L, 1L);
        new SectorBorderUpdateRunnable(this).runTaskTimer(this, 65L, 65L);

        this.networkService.publish("bridge", new SectorConfigurationRequestPacket());
        this.logger.log("Sector configuration request packet published to bridge.");

        this.logger.fine("Plugin enabled successfully.");
    }

    @Override
    public void onDisable() {
        this.logger.log("Plugin disabling...");

        if(this.sectorService == null) return;

        if (this.sectorService.currentSector().sectorType() == SectorType.SPAWN) {
            this.logger.log("Current sector is SPAWN. Handling player transfer or kick.");

            final Sector sector = this.sectorService.find(SectorType.SPAWN);

            if (sector == null) {
                this.logger.warning("No available spawn sector found! Kicking all players.");

                for (final Player player : this.getServer().getOnlinePlayers()) {
                    player.kickPlayer(ChatHelper.colored(this.messagesConfiguration.spawnSectorNotFoundMessage()));
                }
                return;
            }

            this.logger.fine("Available spawn sector found: " + sector.id());

            for (final Player player : this.getServer().getOnlinePlayers()) {
                final User user = this.userService.find(player.getUniqueId());

                this.transferService.connect(player, user, sector, true);
            }
        }

        if (this.networkService == null) return;

        this.networkService.shutdown();

        this.logger.fine("NetworkService shutdown completed.");
        this.logger.fine("Plugin disabled successfully.");
    }

    public void registerSector() {
        this.logger.log("Registering sector...");

        if (this.sectorService.currentSector() == null) {
            this.logger.severe("Current sector is null! Shutting down server.");

            this.getServer().shutdown();
            return;
        }

        if (this.sectorService.currentSector().sectorType() != SectorType.SPAWN) {
            this.logger.log("Current sector is not SPAWN. Skipping scoreboard setup.");
            return;
        }

        this.scoreboardPlayerService = new ScoreboardPlayerService(new SpawnScoreboard(this.sectorService, this.messagesConfiguration));

        this.getServer().getPluginManager().registerEvents(new PlayerScoreboardListener(this.scoreboardPlayerService), this);

        new ScoreboardUpdateRunnable(this).runTaskTimer(this, 50L, 50L);

        this.logger.fine("Scoreboard system successfully initialized");

        this.getCommand("channel").setExecutor(new ChannelCommand(this.userService, this.sectorService, this.transferService, this.messagesConfiguration));

        this.logger.fine("OpenSectors successfully initialized and ready to use.");
    }

    private void checkForUpdates() {
        UpdaterService updaterService = new UpdaterService(this.getDescription().getVersion(), this.getLogger());

        updaterService.check(newestVersion -> this.logger.log(Level.WARNING, List.of(
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",
                "⚠️ A new version of OpenSectors! ⚠️",
                "Current version: " + this.getDescription().getVersion(),
                "Available version: " + newestVersion,
                "",
                "Download at: https://github.com/fajzu1/OpenSectors",
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        )));
    }

    private void initListeners() {
        Stream.of(
                new InventoryInteractListener(),
                new PlayerJoinListener(this),
                new PlayerRespawnListener(this),
                new PlayerDeathListener(this),
                new PlayerSectorInteractListener(this),
                new PlayerLoginListener(this),
                new PlayerChatListener(this.networkService),
                new PlayerQuitListener(this.userService),
                new PlayerMoveListener(this.sectorService, this.userService, this.bukkitSectorRegionService, this.transferService, this.messagesConfiguration),
                new PlayerTeleportListener(this.sectorService, this.userService, this.bukkitSectorRegionService, this.transferService, this.messagesConfiguration)
        ).forEach(listener -> {
            this.getServer().getPluginManager().registerEvents(listener, this);
            this.logger.fine("Registered listener: " + listener.getClass().getSimpleName());
        });

        this.logger.log("All event listeners initialized.");
    }

    public PlayerTransferService transferService() {
        return this.transferService;
    }

    public UserService userService() {
        return this.userService;
    }

    public NetworkService networkService() {
        return this.networkService;
    }

    public SectorService sectorService() {
        return this.sectorService;
    }

    public BukkitSectorRegionService bukkitSectorRegionService() {
        return this.bukkitSectorRegionService;
    }

    public MessagesConfiguration messagesConfiguration() {
        return this.messagesConfiguration;
    }

    public NmsService nmsService() {
        return this.nmsService;
    }

    public ScoreboardPlayerService scoreboardPlayerService() {
        return this.scoreboardPlayerService;
    }

    public BukkitLogger logger() {
        return this.logger;
    }
}
