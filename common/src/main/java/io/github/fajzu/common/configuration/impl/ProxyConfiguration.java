package io.github.fajzu.common.configuration.impl;

import io.github.fajzu.common.configuration.wrapper.SectorWrapper;
import io.github.fajzu.common.sector.type.SectorType;
import io.github.fajzu.common.configuration.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyConfiguration implements Configuration {

    private final Map<String, SectorWrapper> sectors;

    public ProxyConfiguration() {
        this.sectors = new ConcurrentHashMap<>();

        this.sectors.put("s1", new SectorWrapper("s1", SectorType.NORMAL, 250, 2000, -250, 2000));
        this.sectors.put("spawn_1", new SectorWrapper("spawn_1", SectorType.SPAWN, -250, 250, -250, 250));
    }

    public Map<String, SectorWrapper> sectors() {
        return this.sectors;
    }

    public String fileName() {
        return "sectors.json";
    }
}
