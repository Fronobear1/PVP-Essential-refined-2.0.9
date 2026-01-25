package com.fronobear.pvpessentials.refined.client.overlay;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.HashMap;
import java.util.Map;

public class FpsOverlay implements HudRenderCallback {

    private static final Map<String, Integer> COLORS = new HashMap<>() {{
        put("white", 0xFFFFFF);
        put("black", 0x000000);
        put("gray", 0x808080);
        put("red", 0xFF0000);
        put("green", 0x00FF00);
        put("blue", 0x0000FF);
        put("yellow", 0xFFFF00);
        put("purple", 0xFF00FF);
        put("cyan", 0x00FFFF);
        put("orange", 0xFFA500);
        put("brown", 0xA52A2A);
        put("lime", 0x32CD32);
        put("pink", 0xFFC0CB);
        put("light_gray", 0xD3D3D3);
        put("light_blue", 0xADD8E6);
        put("light_green", 0x90EE90);
    }};

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        ModConfig config = ConfigManager.getConfig();
        if (!config.fps.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        
        // Ensure we don't render on F3 debug screen to avoid clutter
        if (client.options.hudHidden || client.getDebugHud().shouldShowDebugHud()) return;

        int fps = client.getCurrentFps();
        String fpsText = String.format("FPS[%d]", fps);

        TextRenderer textRenderer = client.textRenderer;
        int textWidth = textRenderer.getWidth(fpsText);
        int textHeight = textRenderer.fontHeight;

        // Position: Top Left corner
        int x = 2;
        int y = 2;
        
        int padding = 2;
        int shadowOffset = 1;

        int color = COLORS.getOrDefault(config.fps.color.toLowerCase(), 0xFFFFFF);
        int fontColorHex = color | 0xFF000000; // Ensure full alpha

        // Fill background (semi-transparent black)
        drawContext.fill(
                x - padding,
                y - padding,
                x + textWidth + padding,
                y + textHeight + padding - shadowOffset,
                0x90000000
        );

        // Draw text
        drawContext.drawText(
                textRenderer,
                fpsText,
                x,
                y,
                fontColorHex,
                true
        );
    }
}
