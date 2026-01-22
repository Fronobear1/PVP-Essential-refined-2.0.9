package com.fronobear.pvpessentials.refined;

import com.fronobear.pvpessentials.refined.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvPEssentialsRefined implements ModInitializer {
    public static final String MOD_ID = "pvp-essentials-refined";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // Config accessed via ConfigManager.getConfig()

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing PvP Essentials: Refined");
        
        // Initialize Config
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }
}
