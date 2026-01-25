package com.fronobear.pvpessentials.refined.crystaloptimizer.listener;

import com.fronobear.pvpessentials.refined.crystaloptimizer.CrystalOptimizerManager;
import com.fronobear.pvpessentials.refined.crystaloptimizer.cache.OptOutCache;
import com.fronobear.pvpessentials.refined.crystaloptimizer.packet.OptOutAckPacket;
import com.fronobear.pvpessentials.refined.crystaloptimizer.packet.OptOutPacket;
import com.fronobear.pvpessentials.refined.crystaloptimizer.util.ConnectionUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class OptOutPacketListener {

    private static final Text PREFIX = Text.literal("[").formatted(Formatting.GRAY)
            .append(Text.literal("Crystal Optimizer").formatted(Formatting.AQUA))
            .append(Text.literal("] ").formatted(Formatting.GRAY));

    private OptOutPacketListener() {
    }

    private static Text optimizerDisabledMessage() {
        Text hover = Text.empty()
                .append(Text.literal("Why is this disabled?\n").formatted(Formatting.AQUA))
                .append(Text.literal("• This server has requested Crystal Optimizer to be disabled.\n").formatted(Formatting.GRAY))
                .append(Text.literal("• This may be to enforce server rules or avoid compatibility issues.\n").formatted(Formatting.GRAY))
                .append(Text.literal("\nThis only applies while you are connected to this server.").formatted(Formatting.DARK_GRAY));

        Style hoverStyle = Style.EMPTY; // TODO: Fix HoverEvent in 1.21.11
        Text message = Text.literal("Optimizer disabled on this server.")
                .setStyle(hoverStyle.withColor(Formatting.RED));

        return PREFIX.copy()
                .setStyle(hoverStyle)
                .append(message);
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(OptOutPacket.ID, (payload, context) -> {
            MinecraftClient client = context.client();
            OptOutCache cache = CrystalOptimizerManager.getInstance().getOptOutCache();

            String key = ConnectionUtil.currentServerKey(client);

            if (key != null) {
                cache.markOptedOut(key);
            } else {
                cache.setOptedOut(true);
            }

            if (!cache.hasNotified(key)) {
                CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS, Util.getMainWorkerExecutor()).execute(() -> client.execute(() -> {
                    if (client.player == null) return;
                    if (cache.hasNotified(key)) return;

                    cache.markNotified(key);
                    client.player.sendMessage(optimizerDisabledMessage(), false);
                }));
            }

            context.responseSender().sendPacket(new OptOutAckPacket());
        });
    }
}
