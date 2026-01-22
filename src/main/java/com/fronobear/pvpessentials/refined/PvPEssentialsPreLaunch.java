package com.fronobear.pvpessentials.refined;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PvPEssentialsPreLaunch implements PreLaunchEntrypoint {
    public static final Logger LOGGER = LoggerFactory.getLogger("pvp-essentials-refined");

    @Override
    public void onPreLaunch() {
        copyConfigIfMissing("ukulib.toml");
        copyConfigIfMissing("ukus-armor-hud.toml");
    }

    private void copyConfigIfMissing(String fileName) {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path configFile = configDir.resolve(fileName);

        if (!Files.exists(configFile)) {
            try (InputStream in = PvPEssentialsPreLaunch.class.getResourceAsStream("/default_configs/" + fileName)) {
                if (in != null) {
                    Files.copy(in, configFile, StandardCopyOption.REPLACE_EXISTING);
                    LOGGER.info("Created default configuration for {}", fileName);
                } else {
                    LOGGER.warn("Default configuration for {} not found in resources", fileName);
                }
            } catch (IOException e) {
                LOGGER.error("Failed to create default configuration: {}", fileName, e);
            }
        }
    }
}
