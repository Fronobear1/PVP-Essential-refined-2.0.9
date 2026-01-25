package com.fronobear.pvpessentials.refined.crystaloptimizer.listener;

import com.fronobear.pvpessentials.refined.crystaloptimizer.CrystalOptimizerManager;
import com.fronobear.pvpessentials.refined.crystaloptimizer.cache.OptOutCache;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class DisconnectEventListener implements ClientPlayConnectionEvents.Disconnect {

    private final OptOutCache optOutCache;

    public DisconnectEventListener() {
        optOutCache = CrystalOptimizerManager.getInstance().getOptOutCache();
    }

    @Override
    public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        optOutCache.clearCurrentSession();
    }
}
