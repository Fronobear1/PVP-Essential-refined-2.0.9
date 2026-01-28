package com.fronobear.pvpessentials.refined.hud;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.fronobear.pvpessentials.refined.tiptap.ClickCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class CpsDisplayRenderer {

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        ModConfig config = ConfigManager.getConfig();
        if (!config.cps.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) return;

        int leftCps = ClickCounter.getLeftCps();
        int rightCps = ClickCounter.getRightCps();
        String text = String.format("[CPS : %d | %d]", leftCps, rightCps);

        int textWidth = client.textRenderer.getWidth(text);
        int textHeight = client.textRenderer.fontHeight;
        int padding = 2;
        int totalWidth = textWidth + padding * 2;
        int totalHeight = textHeight + padding * 2;

        int x;
        if (config.cps.alignRight) {
             x = context.getScaledWindowWidth() - totalWidth - config.cps.x;
        } else {
             x = config.cps.x;
        }
        int y = config.cps.y;
        
        // If Y is 0 (default), try to align with hotbar similar to armor hud logic if needed, 
        // but for now user has drag and drop control. 
        // If it's the first run and y is 0, maybe we should set a default?
        // Let's rely on the user positioning it or the default 0,0 being top-left/top-right.
        // User asked for "right side of the hot bar", so let's default it there if x/y are 0?
        // Actually, let's just render it where the config says. 
        // The user can move it with the editor.

        context.fill(x, y, x + totalWidth, y + totalHeight, config.cps.backgroundColor);
        context.drawText(client.textRenderer, text, x + padding, y + padding, config.cps.textColor, true);
    }
}
