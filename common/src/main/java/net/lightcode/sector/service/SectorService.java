package net.lightcode.sector.service;

import net.lightcode.sector.Sector;
import net.lightcode.sector.type.SectorType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SectorService {

    private final Map<String, Sector> sectors = new ConcurrentHashMap<>();

    private final String currentSector;

    public SectorService(String currentSector) {
        this.currentSector = currentSector;
    }

    public Sector find(String id) {
        return this.sectors.get(id);
    }

    public Sector find(SectorType type) {
        return this.sectors
                .values()
                .stream()
                .filter(sector -> sector.sectorType() == type)
                .filter(Sector::isOnline)
                .findFirst()
                .orElse(null);
    }

    public void create(String id,
                       Sector sector) {
        this.sectors.put(id, sector);
    }

    public void load(Sector[] sectors) {
        for (Sector sector : sectors) {
            this.sectors.put(sector.id(), sector);
        }
    }

    public String currentSectorId() {
        return this.currentSector;
    }

    public Sector currentSector() {
        return this.sectors.get(currentSector);
    }

    public Map<String, Sector> sectors() {
        return this.sectors;
    }
}
