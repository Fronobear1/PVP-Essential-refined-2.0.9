package com.fronobear.pvpessentials.refined.crystaloptimizer;

import com.fronobear.pvpessentials.refined.crystaloptimizer.cache.OptOutCache;
import com.fronobear.pvpessentials.refined.crystaloptimizer.packet.VersionPacket;
import com.fronobear.pvpessentials.refined.crystaloptimizer.util.VersionUtil;

public class CrystalOptimizerManager {
    private static CrystalOptimizerManager instance;

    private final OptOutCache optOutCache;
    private final VersionPacket versionPacket;

    public CrystalOptimizerManager() {
        this.optOutCache = new OptOutCache();
        this.versionPacket = VersionUtil.createVersionPacket();
    }

    public static CrystalOptimizerManager getInstance() {
        if (instance == null) {
            instance = new CrystalOptimizerManager();
        }
        return instance;
    }

    public OptOutCache getOptOutCache() {
        return optOutCache;
    }

    public VersionPacket getVersionPacket() {
        return versionPacket;
    }
}
