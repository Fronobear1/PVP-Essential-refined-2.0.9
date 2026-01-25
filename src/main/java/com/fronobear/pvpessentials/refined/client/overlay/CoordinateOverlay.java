package com.fronobear.pvpessentials.refined.client.overlay;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class CoordinateOverlay implements HudRenderCallback {

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
        if (!config.coordinates.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player != null) {
            int x = player.getBlockPos().getX();
            int y = player.getBlockPos().getY();
            int z = player.getBlockPos().getZ();
            String coordinates = String.format("Position: %d, %d, %d", x, y, z);

            int h = client.getWindow().getScaledHeight();
            // Original mod used h / 6. User asked for "top left corner but bit downside".
            // We can use the config offset, or default to h/6 if offset is 0?
            // Let's use the config offset.
            int posY = config.coordinates.yOffset; 
            
            // If the user wants it like the original mod, they might expect h/6.
            // But a fixed offset is more predictable.
            // I'll stick to posY as the start Y.

            int padding = 2;
            int shadowOffset = 1;
            int offset = 5; // X offset
            
            TextRenderer textRenderer = client.textRenderer;
            int textWidth = textRenderer.getWidth(coordinates);
            int textHeight = textRenderer.fontHeight;
            
            int color = COLORS.getOrDefault(config.coordinates.fontColor.toLowerCase(), 0xFFFFFF);
            int fontColorHex = color | 0xFF000000; // Ensure full alpha

            // Fill background
            // 0x90000000 is semi-transparent black
            drawContext.fill(
                    0,
                    posY - padding,
                    offset + textWidth + padding,
                    posY + textHeight + padding - shadowOffset,
                    0x90000000
            );

            // Draw text
            drawContext.drawText(
                    textRenderer,
                    coordinates,
                    offset,
                    posY,
                    fontColorHex,
                    true
            );
        }
    }
}
