package com.fronobear.pvpessentials.refined.config;

import me.shedaniel.autoconfig.AutoConfig;

public class ConfigManager {

    // Dynamic getter to ensure we always have the latest config instance
    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
