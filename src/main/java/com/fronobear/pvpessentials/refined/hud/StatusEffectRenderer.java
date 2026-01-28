package com.fronobear.pvpessentials.refined.hud;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatusEffectRenderer {

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        ModConfig.StatusEffects config = ConfigManager.getConfig().statusEffects;
        if (!config.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        // Don't render if HUD is hidden (F1)
        if (client.options.hudHidden) return;

        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        if (effects.isEmpty()) return;

        // Sort effects by duration (shortest first? or longest?)
        // Let's do duration descending so they don't jump around as much?
        // Or alphabetical? Vanilla uses some order.
        // Let's stick to duration descending for now.
        List<StatusEffectInstance> sortedEffects = effects.stream()
                .sorted(Comparator.comparingInt(StatusEffectInstance::getDuration).reversed())
                .collect(Collectors.toList());

        int xOffset = config.xOffset;
        int yOffset = config.yOffset;
        int iconSize = 18;
        int spacing = 4; // Spacing between effects

        int screenWidth = context.getScaledWindowWidth();
        int currentRightX = screenWidth - xOffset;
        int fixedY = yOffset;

        for (StatusEffectInstance effect : sortedEffects) {
            Identifier effectId = Registries.STATUS_EFFECT.getId(effect.getEffectType().value());
            if (effectId == null) continue;
            
            // Use drawGuiTexture with sprite location
            // Standard location in 1.21 is "mob_effect/name" in the gui atlas
            Identifier spriteId = Identifier.of(effectId.getNamespace(), "mob_effect/" + effectId.getPath());

            // Blinking logic (5 seconds = 100 ticks)
            boolean shouldBlink = !effect.isInfinite() && effect.getDuration() <= 100 && (effect.getDuration() % 20 < 10);
            
            // Layout Calculation
            int textPadding = 4;
            // Prepare Text
            // Amplifier (Level)
            int amplifier = effect.getAmplifier() + 1;
            String levelText = String.valueOf(amplifier);
            // Duration
            String durationText = formatDuration(effect.getDuration());
            
            int levelWidth = client.textRenderer.getWidth(levelText);
            int durationWidth = client.textRenderer.getWidth(durationText);
            
            int maxWidth = Math.max(levelWidth, durationWidth);
            int totalWidth = maxWidth + textPadding + iconSize;
            int totalHeight = iconSize + 2; // Add some vertical padding
            
            // Calculate Position (Horizontal Right-to-Left)
            int entryX = currentRightX - totalWidth;
            int entryY = fixedY;
            
            // Draw Background Box (Tooltip Style)
            int bgX = entryX - 2;
            int bgY = entryY - 2;
            int bgW = totalWidth + 4;
            int bgH = totalHeight + 4;
            int zOffset = 0; // DrawContext doesn't expose zOffset directly in fill, but order matters
            
            // Background fill
            context.fill(bgX + 1, bgY, bgX + bgW - 1, bgY + bgH, 0xF0100010);
            context.fill(bgX, bgY + 1, bgX + bgW, bgY + bgH - 1, 0xF0100010);
            
            // Border (Gradient-like solid colors for now to keep it simple and consistent)
            int borderColorStart = 0x505000FF;
            int borderColorEnd = 0x5028007F;
            
            // Top and Bottom border
            context.fill(bgX + 1, bgY, bgX + bgW - 1, bgY + 1, borderColorStart);
            context.fill(bgX + 1, bgY + bgH - 1, bgX + bgW - 1, bgY + bgH, borderColorEnd);
            
            // Left and Right border
            context.fill(bgX, bgY + 1, bgX + 1, bgY + bgH - 1, borderColorStart);
            context.fill(bgX + bgW - 1, bgY + 1, bgX + bgW, bgY + bgH - 1, borderColorEnd);

            // Icon position (relative to entry)
            // Icon on the right
            int iconX = entryX + maxWidth + textPadding;
            int iconY = entryY + 1;

            // Draw Icon (if not blinking out)
            if (!shouldBlink) {
                // drawGuiTexture handles the atlas lookup
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, spriteId, iconX, iconY, iconSize, iconSize);
            }

            // Draw Level (Top line) - Centered in text area
            int levelColor = 0xFFFFAA00; // Gold
            int levelX = entryX + (maxWidth - levelWidth) / 2; // Center horizontally in text area
            context.drawText(client.textRenderer, levelText, levelX, iconY, levelColor, true);
            
            // Draw Duration (Bottom line)
            int durationColor = 0xFFCCCCCC; // Opaque Gray
            if (effect.getDuration() <= 200) { // 10 seconds warning
                durationColor = 0xFFFF5555; // Opaque Red
            }
            
            // Apply blinking to duration text
            if (shouldBlink) {
                 // Skip drawing text if blinking out
            } else {
                 int durationX = entryX + (maxWidth - durationWidth) / 2; // Center horizontally in text area
                 context.drawText(client.textRenderer, durationText, durationX, iconY + 9, durationColor, true);
            }
            
            // Move left for next entry
            currentRightX -= (totalWidth + spacing + 4); // +4 for background padding
        }
    }
    
    private static String formatDuration(int ticks) {
        if (ticks == -1) return "**"; // Infinite
        
        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        
        if (minutes > 0) {
            // "1m 30s" or "1:30"
            // User example: "1m"
            if (seconds == 0) return minutes + "m";
            return String.format("%d:%02d", minutes, seconds);
        } else {
            return seconds + "s";
        }
    }
}
