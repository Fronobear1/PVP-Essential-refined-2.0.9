package com.fronobear.pvpessentials.refined.client;

import com.fronobear.pvpessentials.refined.PvPEssentialsRefined;
import com.fronobear.pvpessentials.refined.client.anchor.AnchorOptimizer;
import com.fronobear.pvpessentials.refined.client.overlay.CoordinateOverlay;
import com.fronobear.pvpessentials.refined.client.overlay.FpsOverlay;
import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.crystaloptimizer.listener.ConnectEventListener;
import com.fronobear.pvpessentials.refined.crystaloptimizer.listener.DisconnectEventListener;
import com.fronobear.pvpessentials.refined.crystaloptimizer.listener.OptOutPacketListener;
import com.fronobear.pvpessentials.refined.crystaloptimizer.packet.OptOutAckPacket;
import com.fronobear.pvpessentials.refined.crystaloptimizer.packet.OptOutPacket;
import com.fronobear.pvpessentials.refined.crystaloptimizer.packet.VersionPacket;
import com.fronobear.pvpessentials.refined.hud.ArmorHudRenderer;
import com.fronobear.pvpessentials.refined.hud.CombatItemsHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PvPEssentialsRefinedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PvPEssentialsRefined.LOGGER.info("Initializing PvP Essentials: Refined Client");

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (!ConfigManager.getConfig().general.modEnabled) return;
            CombatItemsHud.render(drawContext, tickCounter);
            ArmorHudRenderer.render(drawContext, tickCounter);
        });
        
        if (ConfigManager.getConfig().coordinates.enabled) {
            HudRenderCallback.EVENT.register(new CoordinateOverlay());
        }

        if (ConfigManager.getConfig().fps.enabled) {
            HudRenderCallback.EVENT.register(new FpsOverlay());
        }

        initializeCrystalOptimizer();
        AnchorOptimizer.initialize();
    }

    private void initializeCrystalOptimizer() {
        PayloadTypeRegistry.playS2C().register(OptOutPacket.ID, OptOutPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(OptOutAckPacket.ID, OptOutAckPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(VersionPacket.ID, VersionPacket.CODEC);

        ClientPlayConnectionEvents.JOIN.register(new ConnectEventListener());
        ClientPlayConnectionEvents.DISCONNECT.register(new DisconnectEventListener());
        OptOutPacketListener.register();
    }
}
