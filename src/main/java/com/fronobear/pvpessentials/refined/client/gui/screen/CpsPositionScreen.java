package com.fronobear.pvpessentials.refined.client.gui.screen;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.fronobear.pvpessentials.refined.hud.CpsDisplayRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.render.RenderTickCounter;

public class CpsPositionScreen extends Screen {

    private final Screen parent;
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public CpsPositionScreen(Screen parent) {
        super(Text.literal("Adjust CPS Position"));
        this.parent = parent;
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
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render a dark background to make it clear we are in edit mode
        context.fill(0, 0, this.width, this.height, 0xA0000000);
        
        // Render instructions
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Drag the CPS Counter to move"), this.width / 2, 40, 0xFFFFFF);

        // Render the HUD
        RenderTickCounter tickCounter = RenderTickCounter.ZERO;
        CpsDisplayRenderer.render(context, tickCounter);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.button() == 0) { // Left click
            ModConfig.Cps config = ConfigManager.getConfig().cps;
            
            String text = "[CPS : 0 | 0]"; // Dummy text for hit testing size
            int textWidth = this.client.textRenderer.getWidth(text);
            int textHeight = this.client.textRenderer.fontHeight;
            int padding = 2;
            int totalWidth = textWidth + padding * 2;
            int totalHeight = textHeight + padding * 2;

            int currentX;
            if (config.alignRight) {
                 currentX = this.width - totalWidth - config.x;
            } else {
                 currentX = config.x;
            }
            int currentY = config.y;
            
            if (click.x() >= currentX && click.x() <= currentX + totalWidth &&
                click.y() >= currentY && click.y() <= currentY + totalHeight) {
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
            ModConfig.Cps config = ConfigManager.getConfig().cps;
            
            String text = "[CPS : 0 | 0]";
            int textWidth = this.client.textRenderer.getWidth(text);
            int padding = 2;
            int totalWidth = textWidth + padding * 2;

            // Calculate new absolute position
            int newX = (int) (click.x() - dragOffsetX);
            int newY = (int) (click.y() - dragOffsetY);

            // Auto-align based on screen position
            // If the center of the widget is past the center of the screen, align to the right
            if (newX + totalWidth / 2 > this.width / 2) {
                config.alignRight = true;
                config.x = this.width - totalWidth - newX;
            } else {
                config.alignRight = false;
                config.x = newX;
            }
            
            config.y = newY;
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }
}
