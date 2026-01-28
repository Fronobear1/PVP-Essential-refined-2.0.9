package com.fronobear.pvpessentials.refined.tiptap;

import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.fronobear.pvpessentials.refined.tiptap.components.Row;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;

public class KeystrokeOverlay {
    public static final int ROW_SEPARATOR_SIZE = 1;
    public static final int ROW_WIDTH = 77;
    public static final int ROW_HEIGHT_NORMAL = 25;
    public static final int ROW_HEIGHT_SMALL = 20;

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        ModConfig.Keystrokes config = com.fronobear.pvpessentials.refined.config.ConfigManager.getConfig().keystrokes;
        if (!config.enabled) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }

        if (client.options.hudHidden) {
            return;
        }

        int currentWidth = context.getScaledWindowWidth();
        int threshold = config.smallScreenWidth;
        if (threshold <= 0) {
            threshold = 800;
        } else if (threshold == 400) {
            threshold = 800;
        }

        if (config.hideOnSmallScreen && currentWidth <= threshold) {
            return;
        }

        ArrayList<Row> unfinishedRows = new ArrayList<>();
        // TipTapShow divides by scale factor to handle positioning independent of GUI scale
        
        int updatedRowWidth = (int) (ROW_WIDTH * config.displayFactor);
        int updatedRowHeightNormal = (int) (ROW_HEIGHT_NORMAL * config.displayFactor);
        int updatedRowHeightSmall = (int) (ROW_HEIGHT_SMALL * config.displayFactor);
        while ((updatedRowWidth - 2) % 3 != 0 || (updatedRowWidth - 1) % 2 != 0) {
            updatedRowWidth++;
        }
        
        double scale = client.getWindow().getScaleFactor();
        int x;
        if (config.alignRight) {
            x = context.getScaledWindowWidth() - updatedRowWidth - (int) (config.horizontalSlider / scale);
        } else {
            x = (int) (config.horizontalSlider / scale);
        }
        
        if (config.showMovement) {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyBinding[]{null, client.options.forwardKey, null}));
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyBinding[]{client.options.leftKey, client.options.backKey, client.options.rightKey}));
        }
        if (config.showClick) {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightNormal, new KeyBinding[]{client.options.attackKey, client.options.useKey}));
        }
        if (config.showJump) {
            unfinishedRows.add(new Row(x, 0, updatedRowWidth, updatedRowHeightSmall, new KeyBinding[]{client.options.jumpKey}));
        }

        ArrayList<Row> rows = new ArrayList<>();
        //Height calculations
        for (int i = 0; i < unfinishedRows.size(); i++) {
            Row row = unfinishedRows.get(i);
            int height = (int) (config.verticalSlider / client.getWindow().getScaleFactor()) + ROW_SEPARATOR_SIZE * i;
            for (int j = 0; j < i; j++) {
                height += rows.get(j).getHeight();
            }
            row.setY(height);
            rows.add(row);
        }
        for (Row row : rows) {
            row.render(context);
        }
    }
}
