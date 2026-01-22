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
        hudCounters.add(eb.startBooleanToggle(Text.literal("Ender Pearls 🧿"), config.hud.showEnderPearls)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Show Ender Pearl counter"))
                .setSaveConsumer(v -> config.hud.showEnderPearls = v)
                .build());
        hudCounters.add(eb.startBooleanToggle(Text.literal("Golden Apples 🍎"), config.hud.showGoldenApples)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Show Golden Apple counter"))
                .setSaveConsumer(v -> config.hud.showGoldenApples = v)
                .build());
        hudCounters.add(eb.startBooleanToggle(Text.literal("Arrows 🏹"), config.hud.showArrows)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Show Arrow counter"))
                .setSaveConsumer(v -> config.hud.showArrows = v)
                .build());
        hudCounters.setExpanded(false);
        hud.addEntry(hudCounters.build());

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
