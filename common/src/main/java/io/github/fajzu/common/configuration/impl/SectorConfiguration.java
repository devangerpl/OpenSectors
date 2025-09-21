package io.github.fajzu.common.configuration.impl;

import io.github.fajzu.common.configuration.Configuration;

public class SectorConfiguration implements Configuration {

    private final String currentSector;

    public SectorConfiguration() {
        this.currentSector = "s1";
    }

    public String currentSector() {
        return this.currentSector;
    }

    @Override
    public String fileName() {
        return "config.json";
    }
}
