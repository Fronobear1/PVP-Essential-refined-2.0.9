package com.fronobear.pvpessentials.refined.client.render;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.fronobear.pvpessentials.refined.mixin.ping.PlayerListHudInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.MathHelper;

public class PingDisplayRenderer {
    private static final int PING_TEXT_RENDER_OFFSET = -13;
    private static final int PING_BARS_WIDTH = 11;

    public static void renderPingDisplay(MinecraftClient client, PlayerListHud hud, DrawContext context, int width, int x, int y, PlayerListEntry player) {
        ModConfig.BetterPingDisplay config = ConfigManager.getConfig().betterPingDisplay;

        String pingString = String.format(config.pingTextFormatString, player.getLatency());
        int pingStringWidth = client.textRenderer.getWidth(pingString);
        
        int pingTextColor;
        if (config.autoColorPingText) {
            pingTextColor = PingColors.getColor(player.getLatency());
        } else {
            pingTextColor = config.pingTextColor | 0xFF000000;
        }

        int textX = width + x - pingStringWidth + PING_TEXT_RENDER_OFFSET;

        if (!config.renderPingBars) {
            textX += PING_BARS_WIDTH;
        }

        // Draw the ping text
        context.drawTextWithShadow(client.textRenderer, pingString, textX, y, pingTextColor);

        if (config.renderPingBars) {
            ((PlayerListHudInvoker) hud).invokeRenderLatencyIcon(context, width, x, y, player);
        }
    }

    private static class PingColors {
        public static final int PING_START = 0;
        public static final int PING_MID = 150;
        public static final int PING_END = 300;
        public static final int ALPHA_MASK = 0xFF000000;
        public static final int COLOR_GREY = 0x535353;
        public static final int COLOR_START = 0x00E676;
        public static final int COLOR_MID = 0xD6CD30;
        public static final int COLOR_END = 0xE53935;

        public static int getColor(int ping) {
            return getColorInternal(ping) | ALPHA_MASK;
        }

        private static int getColorInternal(int ping) {
            if (ping < PING_START) return COLOR_GREY;
            if (ping < PING_MID) {
                return ColorUtil.interpolate(COLOR_START, COLOR_MID, computeOffset(PING_START, PING_MID, ping));
            }
            return ColorUtil.interpolate(COLOR_MID, COLOR_END, computeOffset(PING_MID, PING_END, Math.min(ping, PING_END)));
        }

        private static float computeOffset(int start, int end, int value) {
            float offset = (value - start) / (float) (end - start);
            return MathHelper.clamp(offset, 0.0F, 1.0F);
        }
    }

    private static class ColorUtil {
        public static int interpolate(int colorStart, int colorEnd, float offset) {
            if (offset < 0 || offset > 1) {
                offset = MathHelper.clamp(offset, 0.0f, 1.0f);
            }
            int redDiff = getRed(colorEnd) - getRed(colorStart);
            int greenDiff = getGreen(colorEnd) - getGreen(colorStart);
            int blueDiff = getBlue(colorEnd) - getBlue(colorStart);

            int newRed = Math.round(getRed(colorStart) + (redDiff * offset));
            int newGreen = Math.round(getGreen(colorStart) + (greenDiff * offset));
            int newBlue = Math.round(getBlue(colorStart) + (blueDiff * offset));

            return (newRed << 16) | (newGreen << 8) | newBlue;
        }

        static int getRed(int color) { return (color >> 16) & 0xFF; }
        static int getGreen(int color) { return (color >> 8) & 0xFF; }
        static int getBlue(int color) { return color & 0xFF; }
    }
}
