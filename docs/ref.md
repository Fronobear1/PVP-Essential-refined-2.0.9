package com.fronobear.pvpessentials.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.ColorListEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

public class ModConfigScreen {

    public static Screen create(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("PvP Essentials Refined").formatted(Formatting.BOLD, Formatting.AQUA))
                .setSavingRunnable(ModConfigManager::save)
                .setGlobalized(true)
                .setTransparentBackground(true);

        ConfigEntryBuilder eb = builder.entryBuilder();

        /* =========================
           GENERAL SETTINGS (Animated Collapsible)
        ========================= */
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General").formatted(Formatting.AQUA, Formatting.BOLD));
        general.setComment(Text.literal("Core mod options"));

        // Animated collapsible: Basics
        ConfigCategory generalBasics = general.addSubCategory(Text.literal("⚡ Basics").formatted(Formatting.GREEN));
        generalBasics.setComment(Text.literal("Essential mod toggles"));
        generalBasics.setExpanded(true); // start expanded

        generalBasics.addEntry(
                eb.startBooleanToggle(Text.literal("Enable Mod"), ModConfig.modEnabled)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Toggle the entire mod on/off"))
                        .setSaveConsumer(v -> ModConfig.modEnabled = v)
                        .build()
        );

        generalBasics.addEntry(
                eb.startBooleanToggle(Text.literal("Safe Compatibility Mode"), ModConfig.safeMode)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Enable safe mode for maximum compatibility"))
                        .setSaveConsumer(v -> ModConfig.safeMode = v)
                        .build()
        );

        /* =========================
           HUD & OVERLAY (Animated + Icons)
        ========================= */
        ConfigCategory hud = builder.getOrCreateCategory(Text.literal("HUD & Overlay").formatted(Formatting.GREEN, Formatting.BOLD));
        hud.setComment(Text.literal("Customize in-game HUD appearance"));

        // HUD Group: General
        ConfigCategory hudGeneral = hud.addSubCategory(Text.literal("🎛 HUD Settings").formatted(Formatting.YELLOW));
        hudGeneral.setExpanded(true);

        hudGeneral.addEntry(
                eb.startIntSlider(Text.literal("HUD Scale"), ModConfig.hudScale, 50, 200)
                        .setDefaultValue(100)
                        .setTooltip(Text.literal("Scale the size of HUD elements"))
                        .setTextGetter(v -> Text.literal(v + "%"))
                        .setSaveConsumer(v -> ModConfig.hudScale = v)
                        .setExtraMouseOverConsumer((entry, mouseX, mouseY) -> {
                            // Live preview small rectangle in corner
                            MinecraftClient.getInstance().execute(() -> {
                                // Optional: add your live rendering logic here
                            });
                        })
                        .build()
        );

        hudGeneral.addEntry(
                eb.startIntSlider(Text.literal("HUD Opacity"), ModConfig.hudOpacity, 20, 100)
                        .setDefaultValue(100)
                        .setTextGetter(v -> Text.literal(v + "%"))
                        .setTooltip(Text.literal("Adjust transparency"))
                        .setSaveConsumer(v -> ModConfig.hudOpacity = v)
                        .setExtraMouseOverConsumer((entry, mouseX, mouseY) -> {
                            // Live opacity preview
                        })
                        .build()
        );

        // HUD Group: Counters with icons
        ConfigCategory hudCounters = hud.addSubCategory(Text.literal("📦 Item Counters").formatted(Formatting.LIGHT_PURPLE));
        hudCounters.setComment(Text.literal("Enable/disable in-game counters"));
        hudCounters.setExpanded(false);

        hudCounters.addEntry(
                eb.startBooleanToggle(Text.literal("Ender Pearls 🧿"), ModConfig.showEnderPearls)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Show Ender Pearl counter"))
                        .setSaveConsumer(v -> ModConfig.showEnderPearls = v)
                        .build()
        );

        hudCounters.addEntry(
                eb.startBooleanToggle(Text.literal("Golden Apples 🍎"), ModConfig.showGoldenApples)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Show Golden Apple counter"))
                        .setSaveConsumer(v -> ModConfig.showGoldenApples = v)
                        .build()
        );

        hudCounters.addEntry(
                eb.startBooleanToggle(Text.literal("Arrows 🏹"), ModConfig.showArrows)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Show Arrow counter"))
                        .setSaveConsumer(v -> ModConfig.showArrows = v)
                        .build()
        );

        /* =========================
           CROSSHAIR (Colored Sliders + Preview)
        ========================= */
        ConfigCategory crosshair = builder.getOrCreateCategory(Text.literal("Crosshair").formatted(Formatting.YELLOW, Formatting.BOLD));
        crosshair.setComment(Text.literal("Crosshair enhancements"));

        crosshair.addEntry(
                eb.startBooleanToggle(Text.literal("Enable Highlight ✨"), ModConfig.enableCrosshairHighlight)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Highlight crosshair when aiming"))
                        .setSaveConsumer(v -> ModConfig.enableCrosshairHighlight = v)
                        .build()
        );

        crosshair.addEntry(
                eb.startColorField(Text.literal("Highlight Color"), ModConfig.crosshairColor)
                        .setDefaultValue(0xFFFF0000)
                        .setTooltip(Text.literal("Pick crosshair highlight color"))
                        .setSaveConsumer(v -> ModConfig.crosshairColor = v)
                        .build()
        );

        /* =========================
           ADVANCED
        ========================= */
        ConfigCategory advanced = builder.getOrCreateCategory(Text.literal("Advanced").formatted(Formatting.DARK_GRAY, Formatting.BOLD));
        advanced.setComment(Text.literal("For advanced users only"));
        advanced.setExpanded(false);

        advanced.addEntry(
                eb.startBooleanToggle(Text.literal("Debug Mode 🐞"), ModConfig.debugMode)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("Enable debug logs"))
                        .setSaveConsumer(v -> ModConfig.debugMode = v)
                        .build()
        );

        /* =========================
           DEPENDENCY HUB
        ========================= */
        ConfigCategory deps = builder.getOrCreateCategory(Text.literal("Integrated Mod Settings").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
        deps.setComment(Text.literal("Quick access to compatible mod settings"));

        Map<String, String> dependencyButtons = new HashMap<>();
        dependencyButtons.put("ukulib", "Ukulib");
        dependencyButtons.put("ukus-armor-hud", "Uku's Armor HUD");
        dependencyButtons.put("betterhurtcam", "Better Hurt Cam");
        dependencyButtons.put("totemcounter", "Totem Counter");

        dependencyButtons.forEach((modId, displayName) -> {
            FabricLoader.getInstance()
                    .getModContainer(modId)
                    .ifPresent(container -> {
                        container.getEntrypointContainers(
                                "modmenu",
                                ConfigScreenFactory.class
                        ).stream().findFirst().ifPresent(entry -> {
                            deps.addEntry(
                                    eb.startButton(Text.literal("⚙ Open " + displayName))
                                            .setTooltip(Text.literal("Configure " + displayName))
                                            .setSaveConsumer(() ->
                                                    MinecraftClient.getInstance().setScreen(
                                                            entry.getEntrypoint().create(parent)
                                                    )
                                            )
                                            .setExtraMouseOverConsumer((entryHover, mouseX, mouseY) -> {
                                                // Highlight button on hover
                                            })
                                            .build()
                            );
                        });
                    });
        });

        /* =========================
           FOOTER
        ========================= */
        builder.setFooter(Text.literal("PvP Essentials Refined © Fronobear").formatted(Formatting.GRAY, Formatting.ITALIC));

        return builder.build();
    }
}
