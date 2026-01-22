package com.fronobear.pvpessentials.refined.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "pvp-essentials-refined")
public class ModConfig implements ConfigData {

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public General general = new General();

    @ConfigEntry.Category("hud")
    @ConfigEntry.Gui.TransitiveObject
    public HUD hud = new HUD();

    @ConfigEntry.Category("crosshair")
    @ConfigEntry.Gui.TransitiveObject
    public Crosshair crosshair = new Crosshair();

    @ConfigEntry.Category("advanced")
    @ConfigEntry.Gui.TransitiveObject
    public Advanced advanced = new Advanced();

    public static class General {
        @Comment("Enable or disable the entire mod")
        @ConfigEntry.Gui.Tooltip
        public boolean modEnabled = true;

        @Comment("Enable safe mode for maximum compatibility")
        @ConfigEntry.Gui.Tooltip
        public boolean safeMode = true;
    }

    public static class HUD {
        @Comment("Show Ender Pearl count")
        @ConfigEntry.Gui.Tooltip
        public boolean showEnderPearls = true;

        @Comment("Show Arrow count")
        @ConfigEntry.Gui.Tooltip
        public boolean showArrows = true;

        @Comment("Show Golden Apple count")
        @ConfigEntry.Gui.Tooltip
        public boolean showGoldenApples = true;
    }

    public static class Crosshair {
        @Comment("Highlight crosshair when aiming at entities")
        @ConfigEntry.Gui.Tooltip
        public boolean enableCrosshairHighlight = true;

        @Comment("Color of the crosshair highlight")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int crosshairColor = 0xFFFF0000; // Red (Opaque)
    }

    public static class Advanced {
        @Comment("Enable debug logs")
        @ConfigEntry.Gui.Tooltip
        public boolean debugMode = false;
    }
}
