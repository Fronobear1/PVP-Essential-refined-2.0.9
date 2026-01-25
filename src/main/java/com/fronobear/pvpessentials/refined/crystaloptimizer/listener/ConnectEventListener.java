package com.fronobear.pvpessentials.refined.crystaloptimizer.listener;

import com.fronobear.pvpessentials.refined.crystaloptimizer.CrystalOptimizerManager;
import com.fronobear.pvpessentials.refined.crystaloptimizer.cache.OptOutCache;
import com.fronobear.pvpessentials.refined.crystaloptimizer.util.ConnectionUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class ConnectEventListener implements ClientPlayConnectionEvents.Join {

    private final OptOutCache optOutCache;

    public ConnectEventListener() {
        this.optOutCache = CrystalOptimizerManager.getInstance().getOptOutCache();
    }

    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        if (client.isIntegratedServerRunning()) return;
        if (!com.fronobear.pvpessentials.refined.config.ConfigManager.getConfig().crystalOptimizer.enabled) return;

        sender.sendPacket(CrystalOptimizerManager.getInstance().getVersionPacket());

        String key = ConnectionUtil.currentServerKey(client);
        boolean shouldOptOut = optOutCache.isServerOptedOut(key);

        if (shouldOptOut) {
            optOutCache.setOptedOut(true);
        }
    }
}
