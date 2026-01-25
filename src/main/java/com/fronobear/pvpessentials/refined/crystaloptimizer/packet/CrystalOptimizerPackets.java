package com.fronobear.pvpessentials.refined.crystaloptimizer.packet;

import com.fronobear.pvpessentials.refined.PvPEssentialsRefined;
import net.minecraft.util.Identifier;

public class CrystalOptimizerPackets {
    // We use the original mod ID "marlowcrystal" for compatibility with servers that support it!
    public static final String COMPAT_MOD_ID = "marlowcrystal";

    public static Identifier id(String path) {
        return Identifier.of(COMPAT_MOD_ID, path);
    }
}
