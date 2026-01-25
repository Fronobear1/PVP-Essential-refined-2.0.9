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

    @ConfigEntry.Category("hurtcam")
    @ConfigEntry.Gui.TransitiveObject
    public HurtCam hurtcam = new HurtCam();

    @ConfigEntry.Category("coordinates")
    @ConfigEntry.Gui.TransitiveObject
    public Coordinates coordinates = new Coordinates();

    @ConfigEntry.Category("crystal_optimizer")
    @ConfigEntry.Gui.TransitiveObject
    public CrystalOptimizer crystalOptimizer = new CrystalOptimizer();

    @ConfigEntry.Category("anchor_optimizer")
    @ConfigEntry.Gui.TransitiveObject
    public AnchorOptimizer anchorOptimizer = new AnchorOptimizer();

    @ConfigEntry.Category("armor_hud")
    @ConfigEntry.Gui.TransitiveObject
    public ArmorHud armorHud = new ArmorHud();

    @ConfigEntry.Category("simple_hud")
    @ConfigEntry.Gui.TransitiveObject
    public Advanced advanced = new Advanced();

    // Optional FPS HUD (if you want it later)
    @ConfigEntry.Category("fps")
    @ConfigEntry.Gui.TransitiveObject
    public FPS fps = new FPS();

    // ==================== INNER CLASSES ====================

    public static class FPS {
        @Comment("Enable FPS Display")
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @Comment("FPS Display Color (e.g. white, red, blue)")
        @ConfigEntry.Gui.Tooltip
        public String color = "white";
    }

    public static class AnchorOptimizer {
        @Comment("Enable Anchor Optimizer")
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;
    }

    public static class ArmorHud {
        @Comment("Enable Armor HUD")
        public boolean enabled = true;

        @Comment("Anchor Point")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Anchor anchor = Anchor.HOTBAR;

        @Comment("Side")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Side side = Side.LEFT;

        @Comment("Offset X")
        public int offsetX = 0;

        @Comment("Offset Y")
        public int offsetY = 0;

        @Comment("Style")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Style style = Style.STYLE_1_E;

        @Comment("Widget Shown Condition")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public WidgetShown widgetShown = WidgetShown.NOT_EMPTY;

        @Comment("Offhand Slot Behavior")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public OffhandSlotBehavior offhandSlotBehavior = OffhandSlotBehavior.ADHERE;

        @Comment("Reverse Order")
        public boolean reversed = true;

        @Comment("Show Empty Slot Icons")
        public boolean iconsShown = true;

        @Comment("Show Durability Warning")
        public boolean warningShown = true;

        @Comment("Min Durability Value for Warning")
        public int minDurabilityValue = 5;

        @Comment("Min Durability Percentage for Warning")
        public double minDurabilityPercentage = 0.05D;

        @Comment("Warning Icon Bobbing Interval (ms)")
        public float warningIconBobbingIntervalMs = 2000.0F;

        @Comment("Orientation")
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Orientation orientation = Orientation.HORIZONTAL;

        public enum Anchor { TOP_CENTER, TOP, BOTTOM, HOTBAR }
        public enum Side { RIGHT, LEFT }
        public enum OffhandSlotBehavior { ALWAYS_IGNORE, ADHERE, ALWAYS_LEAVE_SPACE }
        public enum WidgetShown { ALWAYS, IF_ANY_PRESENT, NOT_EMPTY }
        public enum Style { STYLE_1_E, STYLE_1_H, STYLE_1_S, STYLE_2_E, STYLE_2_H, STYLE_2_S, STYLE_3 }
        public enum Orientation { HORIZONTAL, VERTICAL }
    }

    public static class General {
        @Comment("Enable or disable the entire mod")
        @ConfigEntry.Gui.Tooltip
        public boolean modEnabled = true;

        @Comment("Enable safe mode for maximum compatibility")
        @ConfigEntry.Gui.Tooltip
        public boolean safeMode = true;
    }

    public static class HUD {
        @Comment("Slot 1 Item")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public HudItem slot1 = HudItem.ENDER_PEARL;

        @Comment("Slot 2 Item")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public HudItem slot2 = HudItem.GOLDEN_APPLE;

        @Comment("Slot 3 Item")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public HudItem slot3 = HudItem.TOTEM;

        @Comment("Slot 4 Item")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public HudItem slot4 = HudItem.ARROW;

        public enum HudItem {
            NONE,
            ENDER_PEARL,
            GOLDEN_APPLE,
            ARROW,
            TOTEM,
            CRYSTAL,
            OBSIDIAN,
            ANCHOR,
            GLOWSTONE,
            EXP_BOTTLE
        }
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

    public static class HurtCam {
        @Comment("Enable Better Hurt Cam")
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @Comment("Hurt cam intensity multiplier")
        @ConfigEntry.Gui.Tooltip
        public double multiplier = 0.0;

        @Comment("Enable heart blinking on low health")
        @ConfigEntry.Gui.Tooltip
        public boolean heartBlink = true;

        @Comment("Hurt camera animation type")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public HurtCamType type = HurtCamType.YAW_BASED;
    }

    public static class Coordinates {
        @Comment("Enable Coordinate Overlay")
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @Comment("Font Color (white, red, green, blue, etc.)")
        @ConfigEntry.Gui.Tooltip
        public String fontColor = "white";

        @Comment("Y Offset (Downside position)")
        @ConfigEntry.Gui.Tooltip
        public int yOffset = 60;
    }

    public static class CrystalOptimizer {
        @Comment("Enable Crystal Optimizer")
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;
    }

    public static class Advanced {
        @Comment("Enable debug logs")
        @ConfigEntry.Gui.Tooltip
        public boolean debugMode = false;
    }
}
