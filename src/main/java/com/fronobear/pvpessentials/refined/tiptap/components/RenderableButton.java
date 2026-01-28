package com.fronobear.pvpessentials.refined.tiptap.components;

import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.fronobear.pvpessentials.refined.tiptap.ClickCounter;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class RenderableButton {
    private final int x;
    public int y;
    private final int width;
    private final int height;

    private final KeyBinding key;
    private final String displayText;

    private static final Click leftClick = new Click(0, 0, new MouseInput(0, 0));
    private static final Click rightClick = new Click(0, 0, new MouseInput(1, 0));

    private static final Map<String, Integer> cachedRainbowColors = new HashMap<>();

    private static long lastUsedRainbowMillis = 0;
    private static int rainbowFramesSkipped = 0;

    public RenderableButton(int x, int y, int width, int height, KeyBinding key) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = key;
        this.displayText = getDisplayText(key);
    }

    private ModConfig.Keystrokes getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig().keystrokes;
    }

    public void render(DrawContext context) {
        ModConfig.Keystrokes config = getConfig();
        boolean isPressed = key.isPressed();
        int fillColor = isPressed ? config.pressedBackgroundColor : config.backgroundColor;
        int textColor;
        if (config.rainbowMode) {
            maybeClearRainbowCache(config);
            textColor = getRainbowColor(this.x, config);
        } else if (isPressed) {
            textColor = config.pressedKeyColor;
        } else {
            textColor = config.keyColor;
        }

        context.fill(x, y, x + width, y + height, fillColor);

        if (displayText.equals("{jumpKey}")) {
            renderJump(context, this, textColor);
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (displayText.contains("\n")) {
            String[] splitText = displayText.split("\n", 2);
            String firstLine = splitText[0];
            String secondLine = splitText[1];
            float firstLineX = width / 2F + x - client.textRenderer.getWidth(firstLine) / 2F;
            float secondLineX = width / 2F + x - client.textRenderer.getWidth(secondLine) / 2F;
            float lineY = height / 2F + y - client.textRenderer.fontHeight * 2 / 2F;

            renderText(context, firstLine, firstLineX, lineY, textColor, config);
            renderText(context, secondLine, secondLineX, lineY + client.textRenderer.fontHeight, textColor, config);
            return;
        }

        int letterWidth = client.textRenderer.getWidth(displayText);
        float lineX = width / 2F + x - letterWidth / 2F;
        float lineY = height / 2F + y - client.textRenderer.fontHeight / 2F;

        renderText(context, displayText, lineX, lineY, textColor, config);
    }

    private static void renderJump(DrawContext context, RenderableButton button, int textColor) {
        int letterWidth = (int) (button.width * 0.6);
        float lineX = button.width / 2F + button.x;
        float lineY = button.height / 2F + button.y;
        context.fill(Math.round(lineX - letterWidth / 2F), (int) lineY - 1, Math.round(lineX + letterWidth / 2F), (int) lineY, textColor);
    }

    private static void renderText(DrawContext context, String text, float lineX, float lineY, int textColor, ModConfig.Keystrokes config) {
        if (config.keyShadow) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.of(text), (int) lineX, (int) lineY, textColor);
            return;
        }
        context.drawText(MinecraftClient.getInstance().textRenderer, text, (int) lineX, (int) lineY, textColor, false);
    }

    private String getDisplayText(KeyBinding key) {
        if (key.equals(MinecraftClient.getInstance().options.attackKey)) {
            return getDisplayTextForUseOrAttackKey(key, ClickCounter.getLeftCps(), "LMB");
        } else if (key.equals(MinecraftClient.getInstance().options.useKey)) {
            return getDisplayTextForUseOrAttackKey(key, ClickCounter.getRightCps(), "RMB");
        } else if (key.equals(MinecraftClient.getInstance().options.jumpKey)) {
            return "{jumpKey}";
        } else {
            return key.getBoundKeyLocalizedText().getString().toUpperCase();
        }
    }

    private String getDisplayTextForUseOrAttackKey(KeyBinding key, int clicks, String label) {
        ModConfig.Keystrokes config = getConfig();
        String lmbString = "LMB";
        String rmbString = "RMB";
        
        if (!shouldRenderCps(clicks, config)) {
            if (key.isDefault()) {
                return label;
            } else {
                if (key.matchesMouse(leftClick)) {
                    return lmbString;
                } else if (key.matchesMouse(rightClick)) {
                    return rmbString;
                }
                return key.getBoundKeyLocalizedText().getString().toUpperCase();
            }
        } else {
            if (key.isDefault()) {
                return label + "\n" + clicks + " CPS";
            } else {
                if (key.matchesMouse(leftClick)) {
                    return lmbString + "\n" + clicks + " CPS";
                } else if (key.matchesMouse(rightClick)) {
                    return rmbString + "\n" + clicks + " CPS";
                }
                return key.getBoundKeyLocalizedText().getString().toUpperCase() + "\n" + clicks + " CPS";
            }
        }
    }

    private static boolean shouldRenderCps(int clicks, ModConfig.Keystrokes config) {
        return config.cpsType == ModConfig.Keystrokes.CpsType.ALWAYS || (config.cpsType == ModConfig.Keystrokes.CpsType.ON_CLICK && clicks != 0);
    }

    public int getRainbowColor(double offset, ModConfig.Keystrokes config) {
        if (cachedRainbowColors.containsKey(this.displayText)) {
            return cachedRainbowColors.get(this.displayText);
        }

        float hue = (float) (lastUsedRainbowMillis % 1000L / 1000.0) + (float) (this.width + offset / this.width * (config.rainbowOffset / 10.0));
        int newColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        cachedRainbowColors.put(this.displayText, newColor);
        return newColor;
    }

    public void maybeClearRainbowCache(ModConfig.Keystrokes config) {
        int framesToSkip = (5 + 1 - config.rainbowSpeed) * 7;
        if (framesToSkip <= rainbowFramesSkipped) {
            cachedRainbowColors.clear();
            lastUsedRainbowMillis += 1000 / Math.max(MinecraftClient.getInstance().getCurrentFps(), 60);
            rainbowFramesSkipped = 0;
        }
        rainbowFramesSkipped++;
    }
}
