package com.fronobear.pvpessentials.refined.compat;

import com.fronobear.pvpessentials.refined.client.config.PvpEssentialsConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PvpEssentialsConfigScreen::create;
    }
}
