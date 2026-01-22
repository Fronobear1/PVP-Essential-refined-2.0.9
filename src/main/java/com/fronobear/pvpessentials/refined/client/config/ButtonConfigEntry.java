package com.fronobear.pvpessentials.refined.client.config;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ButtonConfigEntry extends AbstractConfigListEntry<Object> {
    private final ButtonWidget button;
    private final Text fieldName;

    public ButtonConfigEntry(Text fieldName, Text buttonText, ButtonWidget.PressAction onPress) {
        super(fieldName, false);
        this.fieldName = fieldName;
        this.button = ButtonWidget.builder(buttonText, onPress)
                .dimensions(0, 0, 150, 20)
                .build();
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Optional<Object> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public void save() {
        // No-op
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        
        // Render label
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.fieldName, x, y + 6, 0xFFFFFF);
        
        // Render button
        this.button.setX(x + entryWidth - 150);
        this.button.setY(y);
        this.button.setWidth(150);
        this.button.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return Collections.singletonList(button);
    }

    @Override
    public List<? extends Selectable> narratables() {
        return Collections.singletonList(button);
    }
}
