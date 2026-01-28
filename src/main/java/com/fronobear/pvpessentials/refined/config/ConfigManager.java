package com.fronobear.pvpessentials.refined.config;

import me.shedaniel.autoconfig.AutoConfig;

public class ConfigManager {

    // Dynamic getter to ensure we always have the latest config instance
    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static void save() {
        try {
            AutoConfig.getConfigHolder(ModConfig.class).save();
        } catch (Throwable e) {
            com.fronobear.pvpessentials.refined.PvPEssentialsRefined.LOGGER.error("Failed to save config", e);
        }
    }
}
