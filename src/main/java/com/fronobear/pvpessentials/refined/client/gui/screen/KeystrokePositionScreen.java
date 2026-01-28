package com.fronobear.pvpessentials.refined.client.gui.screen;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.fronobear.pvpessentials.refined.tiptap.KeystrokeOverlay;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.render.RenderTickCounter;

public class KeystrokePositionScreen extends Screen {

    private final Screen parent;
    private final KeystrokeOverlay overlay;
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public KeystrokePositionScreen(Screen parent) {
        super(Text.literal("Adjust Keystroke Position"));
        this.parent = parent;
        this.overlay = new KeystrokeOverlay();
    }

    @Override
    protected void init() {
        // Add a "Done" button to save and exit
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> {
            ConfigManager.save();
            if (this.client != null) {
                this.client.setScreen(parent);
            }
        })
        .dimensions(this.width / 2 - 100, this.height - 30, 200, 20)
        .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render a dark background to make it clear we are in edit mode
        context.fill(0, 0, this.width, this.height, 0xA0000000);
        
        // Render instructions
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Drag the Keystrokes to move"), this.width / 2, 40, 0xFFFFFF);

        // Render the overlay
        RenderTickCounter tickCounter = RenderTickCounter.ZERO;
        overlay.onHudRender(context, tickCounter);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.button() == 0) { // Left click
            ModConfig.Keystrokes config = ConfigManager.getConfig().keystrokes;
            double scale = this.client.getWindow().getScaleFactor();
            
            // Calculate width identically to KeystrokeOverlay
            int updatedRowWidth = (int) (KeystrokeOverlay.ROW_WIDTH * config.displayFactor);
            while ((updatedRowWidth - 2) % 3 != 0 || (updatedRowWidth - 1) % 2 != 0) {
                updatedRowWidth++;
            }
            
            // Calculate current overlay position (in scaled pixels)
            int currentX;
            if (config.alignRight) {
                 currentX = this.width - updatedRowWidth - (int) (config.horizontalSlider / scale);
            } else {
                 currentX = (int) (config.horizontalSlider / scale);
            }
            
            int currentY = (int) (config.verticalSlider / scale);
            
            // Height is variable, let's assume ~80 * factor (approximate)
            double height = 80 * config.displayFactor;

            if (click.x() >= currentX && click.x() <= currentX + updatedRowWidth &&
                click.y() >= currentY && click.y() <= currentY + height) {
                dragging = true;
                dragOffsetX = (int) (click.x() - currentX);
                dragOffsetY = (int) (click.y() - currentY);
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = false;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (dragging) {
            ModConfig.Keystrokes config = ConfigManager.getConfig().keystrokes;
            double scale = this.client.getWindow().getScaleFactor();

            // Calculate width identically to KeystrokeOverlay
            int updatedRowWidth = (int) (KeystrokeOverlay.ROW_WIDTH * config.displayFactor);
            while ((updatedRowWidth - 2) % 3 != 0 || (updatedRowWidth - 1) % 2 != 0) {
                updatedRowWidth++;
            }

            // Calculate new position in scaled pixels
            // Use click.x() and click.y() as current mouse position
            int newX = (int) (click.x() - dragOffsetX);
            int newY = (int) (click.y() - dragOffsetY);

            // Auto-align based on screen position
            // If the center of the widget is past the center of the screen, align to the right
            if (newX + updatedRowWidth / 2 > this.width / 2) {
                config.alignRight = true;
                int offsetFromRight = this.width - (newX + updatedRowWidth);
                config.horizontalSlider = (int) (offsetFromRight * scale);
            } else {
                config.alignRight = false;
                config.horizontalSlider = (int) (newX * scale);
            }
            
            config.verticalSlider = (int) (newY * scale);
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }
}
