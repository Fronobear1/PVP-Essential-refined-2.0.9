package com.fronobear.pvpessentials.refined.mixin.crystaloptimizer;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.crystaloptimizer.CrystalOptimizerManager;
import com.fronobear.pvpessentials.refined.crystaloptimizer.InteractHandler;
import com.fronobear.pvpessentials.refined.crystaloptimizer.cache.OptOutCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Unique
    private OptOutCache optOutCache;

    @Unique
    private InteractHandler cachedHandler;

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void onPacketSend(Packet<?> packet, CallbackInfo ci) {
        if (!ConfigManager.getConfig().crystalOptimizer.enabled) return;

        if (packet instanceof PlayerInteractEntityC2SPacket interactionPacket) {
            if (optOutCache == null) {
                optOutCache = CrystalOptimizerManager.getInstance().getOptOutCache();
            }
            if (optOutCache.isOptedOut()) return;

            if (cachedHandler == null) {
                cachedHandler = new InteractHandler(MinecraftClient.getInstance());
            }
            interactionPacket.handle(cachedHandler);
        }
    }
}
