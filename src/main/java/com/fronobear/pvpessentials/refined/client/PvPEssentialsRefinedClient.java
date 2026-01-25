package com.fronobear.pvpessentials.refined.client;

import com.fronobear.pvpessentials.refined.PvPEssentialsRefined;
import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.hud.CombatItemsHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class PvPEssentialsRefinedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PvPEssentialsRefined.LOGGER.info("Initializing PvP Essentials: Refined Client");

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (!ConfigManager.getConfig().general.modEnabled) return;
            CombatItemsHud.render(drawContext, tickCounter);
        });
    }
}
