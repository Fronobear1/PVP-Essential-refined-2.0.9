package com.fronobear.pvpessentials.refined.mixin;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.hit.HitResult;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class CrosshairMixin {

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!ConfigManager.getConfig().crosshair.enableCrosshairHighlight) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            // We are looking at an entity
            int color = ConfigManager.getConfig().crosshair.crosshairColor; 
            
            // Cancel vanilla crosshair
            ci.cancel();
            
            // Draw custom crosshair
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            int cx = screenWidth / 2;
            int cy = screenHeight / 2;
            
            // Draw simple crosshair (+)
            // Horizontal line (length 9px: -4 to +4)
            context.fill(cx - 4, cy, cx + 5, cy + 1, color);
            // Vertical line (length 9px: -4 to +4)
            context.fill(cx, cy - 4, cx + 1, cy + 5, color);
        }
    }

    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void afterRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        // No reset needed since we cancelled the render or didn't change state
    }
}
