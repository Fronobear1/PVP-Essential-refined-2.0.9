package com.fronobear.pvpessentials.refined.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;

import java.util.HashMap;
import java.util.Map;

public class PvpEssentialsConfigScreen {

    public static Screen create(Screen parent) {
        // Get the live config instance
        ModConfig config = ConfigManager.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("PvP Essentials Refined").formatted(Formatting.BOLD, Formatting.AQUA))
                .setSavingRunnable(ConfigManager::save);
        
        // Break chains for void/boolean return types
        builder.setGlobalized(true);
        builder.setTransparentBackground(true);

        ConfigEntryBuilder eb = builder.entryBuilder();

        /* =========================
           GENERAL SETTINGS (Animated Collapsible)
        ========================= */
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General").formatted(Formatting.AQUA, Formatting.BOLD));

        // Basics SubCategory
        SubCategoryBuilder basics = eb.startSubCategory(Text.literal("⚡ Basics").formatted(Formatting.GREEN));
        basics.add(eb.startBooleanToggle(Text.literal("Enable Mod"), config.general.modEnabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Toggle the entire mod on/off"))
                .setSaveConsumer(v -> config.general.modEnabled = v)
                .build());
        basics.add(eb.startBooleanToggle(Text.literal("Safe Compatibility Mode"), config.general.safeMode)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Enable safe mode for maximum compatibility"))
                .setSaveConsumer(v -> config.general.safeMode = v)
                .build());
        basics.setExpanded(true);
        general.addEntry(basics.build());


        /* =========================
           HUD & OVERLAY (Animated + Icons)
        ========================= */
        ConfigCategory hud = builder.getOrCreateCategory(Text.literal("HUD & Overlay").formatted(Formatting.GREEN, Formatting.BOLD));

        // HUD Group: Counters with icons
        SubCategoryBuilder hudCounters = eb.startSubCategory(Text.literal("📦 Item Counters").formatted(Formatting.LIGHT_PURPLE));
        
        hudCounters.add(eb.startEnumSelector(Text.literal("Slot 1 Item"), ModConfig.HUD.HudItem.class, config.hud.slot1)
                .setDefaultValue(ModConfig.HUD.HudItem.ENDER_PEARL)
                .setTooltip(Text.literal("Select item for Slot 1"))
                .setSaveConsumer(v -> config.hud.slot1 = v)
                .build());

        hudCounters.add(eb.startEnumSelector(Text.literal("Slot 2 Item"), ModConfig.HUD.HudItem.class, config.hud.slot2)
                .setDefaultValue(ModConfig.HUD.HudItem.GOLDEN_APPLE)
                .setTooltip(Text.literal("Select item for Slot 2"))
                .setSaveConsumer(v -> config.hud.slot2 = v)
                .build());

        hudCounters.add(eb.startEnumSelector(Text.literal("Slot 3 Item"), ModConfig.HUD.HudItem.class, config.hud.slot3)
                .setDefaultValue(ModConfig.HUD.HudItem.TOTEM)
                .setTooltip(Text.literal("Select item for Slot 3"))
                .setSaveConsumer(v -> config.hud.slot3 = v)
                .build());

        hudCounters.add(eb.startEnumSelector(Text.literal("Slot 4 Item"), ModConfig.HUD.HudItem.class, config.hud.slot4)
                .setDefaultValue(ModConfig.HUD.HudItem.ARROW)
                .setTooltip(Text.literal("Select item for Slot 4"))
                .setSaveConsumer(v -> config.hud.slot4 = v)
                .build());

        hudCounters.setExpanded(false);
        hud.addEntry(hudCounters.build());

        // FPS SubCategory
        SubCategoryBuilder fpsSettings = eb.startSubCategory(Text.literal("⚡ FPS Display").formatted(Formatting.RED));
        fpsSettings.add(eb.startBooleanToggle(Text.literal("Enable FPS Display"), config.fps.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Show real-time FPS counter"))
                .setSaveConsumer(v -> config.fps.enabled = v)
                .build());
        
        // Color Picker for FPS (using dropdown or suggestion if possible, but String field is requested in ModConfig)
        // Since ModConfig uses String for color, we use a string field with suggestions.
        fpsSettings.add(eb.startStrField(Text.literal("FPS Color"), config.fps.color)
                .setDefaultValue("white")
                .setTooltip(Text.literal("Color name (e.g. white, red, blue)"))
                .setSaveConsumer(v -> config.fps.color = v)
                .build());
        
        fpsSettings.setExpanded(true);
        hud.addEntry(fpsSettings.build());

        /* =========================
           CROSSHAIR (Colored Sliders + Preview)
        ========================= */
        ConfigCategory crosshair = builder.getOrCreateCategory(Text.literal("Crosshair").formatted(Formatting.YELLOW, Formatting.BOLD));

        crosshair.addEntry(
            eb.startBooleanToggle(Text.literal("Enable Highlight ✨"), config.crosshair.enableCrosshairHighlight)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Highlight crosshair when aiming"))
                .setSaveConsumer(v -> config.crosshair.enableCrosshairHighlight = v)
                .build()
        );

        crosshair.addEntry(
            eb.startAlphaColorField(Text.literal("Highlight Color"), config.crosshair.crosshairColor)
                .setDefaultValue(0xFFFF0000)
                .setTooltip(Text.literal("Pick crosshair highlight color"))
                .setSaveConsumer(v -> config.crosshair.crosshairColor = v)
                .build()
        );

        /* =========================
           HURT CAM (Animated)
        ========================= */
        ConfigCategory hurtCam = builder.getOrCreateCategory(Text.literal("Hurt Cam").formatted(Formatting.RED, Formatting.BOLD));

        hurtCam.addEntry(
            eb.startBooleanToggle(Text.literal("Enable Better Hurt Cam"), config.hurtcam.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Enable or disable the Better Hurt Cam features"))
                .setSaveConsumer(v -> config.hurtcam.enabled = v)
                .build()
        );

        hurtCam.addEntry(
            eb.startDoubleField(Text.literal("Intensity Multiplier"), config.hurtcam.multiplier)
                .setDefaultValue(0.0)
                .setTooltip(Text.literal("Control the intensity of the hurt camera effect"))
                .setSaveConsumer(v -> config.hurtcam.multiplier = v)
                .build()
        );

        hurtCam.addEntry(
            eb.startBooleanToggle(Text.literal("Heart Blink"), config.hurtcam.heartBlink)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Enable heart blinking on low health"))
                .setSaveConsumer(v -> config.hurtcam.heartBlink = v)
                .build()
        );

        hurtCam.addEntry(
            eb.startEnumSelector(Text.literal("Animation Type"), com.fronobear.pvpessentials.refined.config.HurtCamType.class, config.hurtcam.type)
                .setDefaultValue(com.fronobear.pvpessentials.refined.config.HurtCamType.YAW_BASED)
                .setTooltip(Text.literal("Select the hurt camera animation style"))
                .setSaveConsumer(v -> config.hurtcam.type = v)
                .build()
        );

        /* =========================
           CRYSTAL OPTIMIZER
        ========================= */
        ConfigCategory crystalOptimizer = builder.getOrCreateCategory(Text.literal("Crystal Optimizer").formatted(Formatting.AQUA, Formatting.BOLD));

        crystalOptimizer.addEntry(
            eb.startBooleanToggle(Text.literal("Enable Crystal Optimizer"), config.crystalOptimizer.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Enable or disable the Crystal Optimizer features"))
                .setSaveConsumer(v -> config.crystalOptimizer.enabled = v)
                .build()
        );

        /* =========================
           ANCHOR OPTIMIZER
        ========================= */
        ConfigCategory anchorOptimizer = builder.getOrCreateCategory(Text.literal("Anchor Optimizer").formatted(Formatting.GOLD, Formatting.BOLD));

        anchorOptimizer.addEntry(
            eb.startBooleanToggle(Text.literal("Enable Anchor Optimizer"), config.anchorOptimizer.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Optimizes respawn anchors by making them translucent when unsafe"))
                .setSaveConsumer(v -> config.anchorOptimizer.enabled = v)
                .build()
        );

        /* =========================
           ARMOR HUD
        ========================= */
        ConfigCategory armorHud = builder.getOrCreateCategory(Text.literal("Armor HUD").formatted(Formatting.BLUE, Formatting.BOLD));

        armorHud.addEntry(eb.startBooleanToggle(Text.literal("Enable Armor HUD"), config.armorHud.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.armorHud.enabled = v)
                .build());

        armorHud.addEntry(eb.startEnumSelector(Text.literal("Anchor Point"), ModConfig.ArmorHud.Anchor.class, config.armorHud.anchor)
                .setDefaultValue(ModConfig.ArmorHud.Anchor.HOTBAR)
                .setSaveConsumer(v -> config.armorHud.anchor = v)
                .build());

        armorHud.addEntry(eb.startEnumSelector(Text.literal("Side"), ModConfig.ArmorHud.Side.class, config.armorHud.side)
                .setDefaultValue(ModConfig.ArmorHud.Side.LEFT)
                .setSaveConsumer(v -> config.armorHud.side = v)
                .build());
        
        armorHud.addEntry(eb.startIntField(Text.literal("Offset X"), config.armorHud.offsetX)
                .setDefaultValue(0)
                .setSaveConsumer(v -> config.armorHud.offsetX = v)
                .build());

        armorHud.addEntry(eb.startIntField(Text.literal("Offset Y"), config.armorHud.offsetY)
                .setDefaultValue(0)
                .setSaveConsumer(v -> config.armorHud.offsetY = v)
                .build());

        armorHud.addEntry(eb.startEnumSelector(Text.literal("Orientation"), ModConfig.ArmorHud.Orientation.class, config.armorHud.orientation)
                .setDefaultValue(ModConfig.ArmorHud.Orientation.HORIZONTAL)
                .setSaveConsumer(v -> config.armorHud.orientation = v)
                .build());

        armorHud.addEntry(eb.startEnumSelector(Text.literal("Style"), ModConfig.ArmorHud.Style.class, config.armorHud.style)
                .setDefaultValue(ModConfig.ArmorHud.Style.STYLE_1_E)
                .setSaveConsumer(v -> config.armorHud.style = v)
                .build());

        armorHud.addEntry(eb.startEnumSelector(Text.literal("Widget Shown Condition"), ModConfig.ArmorHud.WidgetShown.class, config.armorHud.widgetShown)
                .setDefaultValue(ModConfig.ArmorHud.WidgetShown.NOT_EMPTY)
                .setSaveConsumer(v -> config.armorHud.widgetShown = v)
                .build());

        armorHud.addEntry(eb.startEnumSelector(Text.literal("Offhand Slot Behavior"), ModConfig.ArmorHud.OffhandSlotBehavior.class, config.armorHud.offhandSlotBehavior)
                .setDefaultValue(ModConfig.ArmorHud.OffhandSlotBehavior.ADHERE)
                .setSaveConsumer(v -> config.armorHud.offhandSlotBehavior = v)
                .build());

        armorHud.addEntry(eb.startBooleanToggle(Text.literal("Reverse Order"), config.armorHud.reversed)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.armorHud.reversed = v)
                .build());

        armorHud.addEntry(eb.startBooleanToggle(Text.literal("Show Empty Slot Icons"), config.armorHud.iconsShown)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.armorHud.iconsShown = v)
                .build());

        armorHud.addEntry(eb.startBooleanToggle(Text.literal("Show Durability Warning"), config.armorHud.warningShown)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.armorHud.warningShown = v)
                .build());

        armorHud.addEntry(eb.startIntField(Text.literal("Min Durability Value"), config.armorHud.minDurabilityValue)
                .setDefaultValue(5)
                .setSaveConsumer(v -> config.armorHud.minDurabilityValue = v)
                .build());

        armorHud.addEntry(eb.startDoubleField(Text.literal("Min Durability Percentage"), config.armorHud.minDurabilityPercentage)
                .setDefaultValue(0.05D)
                .setSaveConsumer(v -> config.armorHud.minDurabilityPercentage = v)
                .build());
        
        armorHud.addEntry(eb.startFloatField(Text.literal("Warning Bobbing Interval (ms)"), config.armorHud.warningIconBobbingIntervalMs)
                .setDefaultValue(2000.0F)
                .setSaveConsumer(v -> config.armorHud.warningIconBobbingIntervalMs = v)
                .build());

        /* =========================
           ADVANCED
        ========================= */
        ConfigCategory advanced = builder.getOrCreateCategory(Text.literal("Advanced").formatted(Formatting.DARK_GRAY, Formatting.BOLD));

        advanced.addEntry(
            eb.startBooleanToggle(Text.literal("Debug Mode 🐞"), config.advanced.debugMode)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Enable debug logs"))
                .setSaveConsumer(v -> config.advanced.debugMode = v)
                .build()
        );

        return builder.build();
    }
}
