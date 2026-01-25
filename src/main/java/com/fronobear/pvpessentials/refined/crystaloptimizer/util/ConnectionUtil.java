package com.fronobear.pvpessentials.refined.crystaloptimizer.util;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class ConnectionUtil {
    public static @Nullable String currentServerKey(MinecraftClient client) {
        if (client.getCurrentServerEntry() == null) {
            return null;
        }

        return client.getCurrentServerEntry().address;
    }
}
