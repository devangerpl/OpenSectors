package net.lightcode.bridge;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.lightcode.NetworkService;
import net.lightcode.bridge.listener.PlayerServerConnectListener;
import net.lightcode.bridge.listener.redis.PacketPlayerConnectSectorListener;
import net.lightcode.bridge.listener.redis.PacketSectorConfigurationRequestListener;
import net.lightcode.configuration.impl.DatabaseConfiguration;
import net.lightcode.configuration.impl.ProxyConfiguration;
import net.lightcode.configuration.service.ConfigurationService;
import net.lightcode.sector.Sector;
import net.lightcode.sector.service.SectorService;
import net.lightcode.updater.UpdaterService;
import org.slf4j.Logger;

import java.util.List;


@Plugin(
        id = "opensectors-bridge",
        name = "opensectors-bridge",
        version = "3.0",
        authors = "fajzu"
)
public class BridgePlugin {

    private final ProxyServer server;
    private final BridgeLogger logger;

    private NetworkService networkService;
    private SectorService sectorService;
    private ConfigurationService configurationService;

    @Inject
    public BridgePlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = new BridgeLogger(logger);

        String text = "\n\n" +
                " ██████╗ ██████╗ ███████╗███╗   ██╗███████╗███████╗ ██████╗████████╗ ██████╗ ██████╗ ███████╗\n" +
                "██╔═══██╗██╔══██╗██╔════╝████╗  ██║██╔════╝██╔════╝██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗██╔════╝\n" +
                "██║   ██║██████╔╝█████╗  ██╔██╗ ██║███████╗█████╗  ██║        ██║   ██║   ██║██████╔╝███████╗\n" +
                "██║   ██║██╔═══╝ ██╔══╝  ██║╚██╗██║╚════██║██╔══╝  ██║        ██║   ██║   ██║██╔══██╗╚════██║\n" +
                "╚██████╔╝██║     ███████╗██║ ╚████║███████║███████╗╚██████╗   ██║   ╚██████╔╝██║  ██║███████║\n" +
                " ╚═════╝ ╚═╝     ╚══════╝╚═╝  ╚═══╝╚══════╝╚══════╝ ╚═════╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝\n";
        this.logger.log(text);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.logger.log("Starting bridge initialization...");

        this.configurationService = new ConfigurationService("plugins/opensectors-bridge");
        this.logger.log("ConfigurationService initialized with path 'plugins/opensectors-bridge");

        ProxyConfiguration proxyConfiguration = this.configurationService.loadConfiguration(ProxyConfiguration.class);
        this.logger.log("Loaded ProxyConfiguration: sectors count " + proxyConfiguration.sectors().size());

        DatabaseConfiguration databaseConfiguration = this.configurationService.loadConfiguration(DatabaseConfiguration.class);

        this.sectorService = new SectorService("bridge");
        this.logger.log("SectorService initialized with namespace 'bridge'");

        proxyConfiguration.sectors().forEach((id, sectorWrapper) -> {
            this.logger.log("Creating sector: id={}, name={}, type={}, bounds: X({}-{}), Z({}-{})", id, sectorWrapper.name(), sectorWrapper.sectorType(), sectorWrapper.minX(), sectorWrapper.maxX(), sectorWrapper.minZ(), sectorWrapper.maxZ());

            this.sectorService.create(id, new Sector(sectorWrapper.name(), sectorWrapper.sectorType(), sectorWrapper.minX(), sectorWrapper.maxX(), sectorWrapper.minZ(), sectorWrapper.maxZ()));
        });

        this.networkService = new NetworkService(databaseConfiguration.redisHost(), databaseConfiguration.redisPort(), databaseConfiguration.redisPassword());
        this.networkService.setPacketSender("bridge");
        this.logger.log("Initialized with Redis");

        this.networkService.subscribe("bridge", new PacketSectorConfigurationRequestListener(this.sectorService, this.networkService, this.logger));
        this.networkService.subscribe("bridge", new PacketPlayerConnectSectorListener(this));

        this.server.getEventManager().register(this, new PlayerServerConnectListener(this));
        this.logger.log("Bridge listeners registered");

        this.checkForUpdates();

        this.logger.log("Bridge initialization complete!");
    }

    private void checkForUpdates() {
        UpdaterService updaterService = new UpdaterService("3.5", java.util.logging.Logger.getAnonymousLogger());

        updaterService.check(newestVersion -> this.logger.warning(List.of(
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",
                "⚠️ A new version of OpenSectors! ⚠️",
                "Current version: 3.5",
                "Available version: " + newestVersion,
                "",
                "Download at: https://github.com/fajzu1/OpenSectors",
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        )));
    }

    public SectorService sectorService() {
        return this.sectorService;
    }

    public ProxyServer server() {
        return this.server;
    }

    public BridgeLogger logger() {
        return this.logger;
    }
}
