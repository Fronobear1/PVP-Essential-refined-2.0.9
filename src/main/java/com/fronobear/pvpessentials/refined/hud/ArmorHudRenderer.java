package com.fronobear.pvpessentials.refined.hud;


import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class ArmorHudRenderer {
    private static final Identifier WIDGETS_TEXTURE = Identifier.ofVanilla("textures/gui/widgets.png");
    private static final Identifier EMPTY_HELMET_SLOT_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_helmet");
    private static final Identifier EMPTY_CHESTPLATE_SLOT_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_chestplate");
    private static final Identifier EMPTY_LEGGINGS_SLOT_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_leggings");
    private static final Identifier EMPTY_BOOTS_SLOT_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_boots");
    private static final Identifier BLOCK_ATLAS_TEXTURE = Identifier.ofVanilla("textures/atlas/blocks.png");

    private static final int step = 20;
    private static final int width = 22;
    private static final int height = 22;
    private static final int defaultHotbarOffset = 98;
    private static final int defaultOffhandSlotOffset = 29;
    private static final int defaultHotbarAttackIndicatorOffset = 23;
    private static final int minWarningHeight = 2;
    private static final int maxWarningHeight = 7;
    private static final int warningHorizontalOffset = 7;

    private static long lastMeasuredTime;
    private static long measuredTime;
    private static float[] cycleProgress = null;
    private static final List<ItemStack> armorItems = new ArrayList<>(4);
    private static final List<Integer> armorItemIndexes = new ArrayList<>(4);
    private static final Random random = Random.create();

    private static final Identifier[] EMPTY_SLOT_TEXTURES = new Identifier[]{
        EMPTY_BOOTS_SLOT_TEXTURE,
        EMPTY_LEGGINGS_SLOT_TEXTURE,
        EMPTY_CHESTPLATE_SLOT_TEXTURE,
        EMPTY_HELMET_SLOT_TEXTURE
    };

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = ConfigManager.getConfig();

        if (client.player == null || client.player.isSpectator()) return;
        if (!config.armorHud.enabled) return;

        // update measuring time
        lastMeasuredTime = measuredTime;
        measuredTime = Util.getMeasuringTimeMs();

        PlayerEntity playerEntity = client.player;
        int amount = 0;

        // Count items
        armorItems.clear();
        armorItemIndexes.clear();
        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = playerEntity.getInventory().getStack(36 + i);
            if (!itemStack.isEmpty())
                amount++;
            if (!itemStack.isEmpty() || config.armorHud.widgetShown != ModConfig.ArmorHud.WidgetShown.NOT_EMPTY) {
                armorItems.add(itemStack);
                armorItemIndexes.add(i);
            }
        }

        if (amount > 0 || config.armorHud.widgetShown == ModConfig.ArmorHud.WidgetShown.ALWAYS) {
            int scaledWidth = client.getWindow().getScaledWidth();
            int scaledHeight = client.getWindow().getScaledHeight();

            final int armorWidgetY;
            final int armorWidgetX;
            final int sideMultiplier;
            final int sideOffsetMultiplier;
            final int verticalMultiplier;
            final int verticalOffsetMultiplier;
            final int slots;
            
            // Layout dimensions
            final int widgetLength = width + (( (config.armorHud.widgetShown == ModConfig.ArmorHud.WidgetShown.NOT_EMPTY ? amount : 4) - 1) * step);
            final int layoutWidth;
            final int layoutHeight;
            
            if (config.armorHud.orientation == ModConfig.ArmorHud.Orientation.HORIZONTAL) {
                layoutWidth = widgetLength;
                layoutHeight = height;
            } else {
                layoutWidth = height; // 22
                layoutHeight = widgetLength;
            }

            var matrices = context.getMatrices();
            matrices.pushMatrix();

            // Calculate position
            if ((config.armorHud.anchor == ModConfig.ArmorHud.Anchor.HOTBAR && config.armorHud.side == ModConfig.ArmorHud.Side.LEFT) || 
                (config.armorHud.anchor != ModConfig.ArmorHud.Anchor.HOTBAR && config.armorHud.side == ModConfig.ArmorHud.Side.RIGHT)) {
                sideMultiplier = -1;
                sideOffsetMultiplier = -1;
            } else {
                sideMultiplier = 1;
                sideOffsetMultiplier = 0;
            }

            switch (config.armorHud.anchor) {
                case TOP:
                case TOP_CENTER:
                    verticalMultiplier = 1;
                    verticalOffsetMultiplier = 0;
                    break;
                case HOTBAR:
                case BOTTOM:
                    verticalMultiplier = -1;
                    verticalOffsetMultiplier = -1;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + config.armorHud.anchor);
            }

            int addedHotbarOffset;
            switch (config.armorHud.offhandSlotBehavior) {
                case ALWAYS_IGNORE:
                    addedHotbarOffset = 0;
                    break;
                case ALWAYS_LEAVE_SPACE:
                    addedHotbarOffset = Math.max(defaultOffhandSlotOffset, defaultHotbarAttackIndicatorOffset);
                    break;
                case ADHERE:
                    if (
                            (
                                    playerEntity.getMainArm().getOpposite() == Arm.LEFT && config.armorHud.side == ModConfig.ArmorHud.Side.LEFT
                                            || playerEntity.getMainArm().getOpposite() == Arm.RIGHT && config.armorHud.side == ModConfig.ArmorHud.Side.RIGHT
                            ) && !playerEntity.getOffHandStack().isEmpty()
                    )
                        addedHotbarOffset = defaultOffhandSlotOffset;
                    else if (
                            (
                                    playerEntity.getMainArm() == Arm.LEFT && config.armorHud.side == ModConfig.ArmorHud.Side.LEFT
                                            || playerEntity.getMainArm() == Arm.RIGHT && config.armorHud.side == ModConfig.ArmorHud.Side.RIGHT
                            ) && client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR
                    )
                        addedHotbarOffset = defaultHotbarAttackIndicatorOffset;
                    else
                        addedHotbarOffset = 0;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + config.armorHud.offhandSlotBehavior);
            }

            int armorWidgetY1;
            switch (config.armorHud.anchor) {
                case BOTTOM:
                case HOTBAR:
                    armorWidgetY1 = scaledHeight - layoutHeight;
                    break;
                case TOP:
                case TOP_CENTER:
                    armorWidgetY1 = 0;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + config.armorHud.anchor);
            }

            slots = config.armorHud.widgetShown == ModConfig.ArmorHud.WidgetShown.NOT_EMPTY ? amount : 4;
            
            int armorWidgetX1;
            switch (config.armorHud.anchor) {
                case TOP_CENTER:
                    armorWidgetX1 = scaledWidth / 2 - (layoutWidth / 2);
                    break;
                case TOP:
                case BOTTOM:
                    armorWidgetX1 = (layoutWidth - scaledWidth) * sideOffsetMultiplier;
                    break;
                case HOTBAR:
                    armorWidgetX1 = scaledWidth / 2 + ((defaultHotbarOffset + addedHotbarOffset) * sideMultiplier) + (layoutWidth * sideOffsetMultiplier);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + config.armorHud.anchor);
            }

            armorWidgetX1 += config.armorHud.offsetX * sideMultiplier;
            armorWidgetY1 += config.armorHud.offsetY * verticalMultiplier;

            armorWidgetY = armorWidgetY1;
            armorWidgetX = armorWidgetX1;

            // Draw Background Slots
            matrices.pushMatrix();
            matrices.translate((float)armorWidgetX, (float)armorWidgetY);
            
            if (config.armorHud.orientation == ModConfig.ArmorHud.Orientation.VERTICAL) {
                matrices.rotate(MathHelper.HALF_PI);
                matrices.translate(0.0f, -(float)height);
            }

            // Draw relative to 0,0 since we translated
            switch (config.armorHud.style) {
                case HOTBAR:
                    // Left cap
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, 0, 0, 0, 0, 1, 22, 256, 256);
                    // Slots
                    for (int i = 0; i < slots; i++) {
                        context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, 1 + i * 20, 0, 1, 0, 20, 22, 256, 256);
                    }
                    // Right cap
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, widgetLength - 1, 0, 181, 0, 1, 22, 256, 256);
                    break;
                case MODERN:
                    // Modern style: Semi-transparent black background with a subtle border
                    int modernColor = 0x80000000; // Semi-transparent black
                    int borderColor = 0x40FFFFFF; // Subtle white border
                    
                    context.fill(0, 0, widgetLength, height, modernColor);
                    // Top border
                    context.fill(0, 0, widgetLength, 1, borderColor);
                    // Bottom border
                    context.fill(0, height - 1, widgetLength, height, borderColor);
                    // Left border
                    context.fill(0, 0, 1, height, borderColor);
                    // Right border
                    context.fill(widgetLength - 1, 0, widgetLength, height, borderColor);
                    break;
            }
            matrices.popMatrix(); // End background drawing

            // Draw Items and Overlays
            for (int i = 0; i < armorItems.size(); i++) {
                int iReversed = config.armorHud.reversed ? (armorItems.size() - i - 1) : i;
                
                int xOffset = config.armorHud.orientation == ModConfig.ArmorHud.Orientation.HORIZONTAL ? (step * iReversed) : 0;
                int yOffset = config.armorHud.orientation == ModConfig.ArmorHud.Orientation.VERTICAL ? (step * iReversed) : 0;
                
                int x = armorWidgetX + xOffset + 3;
                int y = armorWidgetY + yOffset + 3;

                ItemStack stack = armorItems.get(i);
                
                // Draw Slot Icons (Empty)
                if (stack.isEmpty() && config.armorHud.iconsShown && config.armorHud.widgetShown == ModConfig.ArmorHud.WidgetShown.ALWAYS) {
                    int slotIndex = armorItemIndexes.get(i); // 0=Boots, 1=Leggings, 2=Chest, 3=Helmet
                    if (slotIndex >= 0 && slotIndex < EMPTY_SLOT_TEXTURES.length) {
                        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, EMPTY_SLOT_TEXTURES[slotIndex], x, y, 16, 16);
                    }
                }

                if (!stack.isEmpty()) {
                    // Draw Item
                    context.drawItem(stack, x, y);
                    
                    // Draw Durability Bar (Standard) - Only if enabled
                    if (config.armorHud.durabilityDisplay == ModConfig.ArmorHud.DurabilityDisplay.BAR ||
                        config.armorHud.durabilityDisplay == ModConfig.ArmorHud.DurabilityDisplay.BAR_AND_VALUES ||
                        config.armorHud.durabilityDisplay == ModConfig.ArmorHud.DurabilityDisplay.BAR_AND_PERCENTAGE) {
                        context.drawStackOverlay(client.textRenderer, stack, x, y);
                    }
                    
                    // Draw Custom Durability Text (Value/Percentage)
                    if (config.armorHud.durabilityDisplay != ModConfig.ArmorHud.DurabilityDisplay.BAR && stack.isDamageable()) {
                        String text = "";
                        int damage = stack.getDamage();
                        int maxDamage = stack.getMaxDamage();
                        int current = maxDamage - damage;
                        
                        if (config.armorHud.durabilityDisplay == ModConfig.ArmorHud.DurabilityDisplay.NUMERIC ||
                            config.armorHud.durabilityDisplay == ModConfig.ArmorHud.DurabilityDisplay.BAR_AND_VALUES) {
                            text = String.valueOf(current);
                        } else if (config.armorHud.durabilityDisplay == ModConfig.ArmorHud.DurabilityDisplay.PERCENTAGE ||
                                   config.armorHud.durabilityDisplay == ModConfig.ArmorHud.DurabilityDisplay.BAR_AND_PERCENTAGE) {
                            int percent = (int) Math.round(((double) current / maxDamage) * 100);
                            text = percent + "%";
                        }
                        
                        if (!text.isEmpty()) {
                            matrices.pushMatrix();
                            float scale = 0.75f; // Slightly larger text for readability
                            matrices.scale(scale, scale); // 2D scale
                            matrices.translate(0.0f, 0.0f); // 2D translate
                            
                            int textWidth = client.textRenderer.getWidth(text);
                            int textHeight = client.textRenderer.fontHeight;
                            
                            // Color Logic
                            // Green (Full/High) -> Yellow (Half) -> Red (Low/Critical)
                            int color = 0xFF55FF55; // Green
                            double damageRatio = (double) current / maxDamage;
                            
                            if (current <= config.armorHud.minDurabilityValue || damageRatio <= config.armorHud.minDurabilityPercentage) {
                                color = 0xFFFF5555; // Red
                            } else if (damageRatio <= 0.5) {
                                color = 0xFFFFFF55; // Yellow
                            }
                            
                            // Position Logic
                            float drawX;
                            float drawY;

                            if (config.armorHud.orientation == ModConfig.ArmorHud.Orientation.VERTICAL) {
                                // Right side of the slot, vertically centered
                                // x + 19 is the right edge of the slot background
                                drawX = (x + 21) / scale;
                                drawY = (y + 8) / scale - textHeight / 2.0f;
                            } else {
                                // Horizontal: Bottom-Right corner inside the slot
                                drawX = (x + 19) / scale - textWidth;
                                drawY = (y + 19) / scale - textHeight;
                            }
                            
                            // Draw text with shadow (last parameter true) which helps it pop against items
                            // Note: We rely on draw order for Z-layering since Matrix3x2fStack is 2D
                            context.drawText(client.textRenderer, text, (int)drawX, (int)drawY, color, true);
                            matrices.popMatrix();
                        }   
                    }
                    
                    // Draw Warning
                    if (config.armorHud.warningShown && stack.isDamageable() && stack.getDamage() > 0) {
                        int damage = stack.getDamage();
                        int maxDamage = stack.getMaxDamage();
                        if ((1.0F - ((float) damage) / ((float) maxDamage) <= config.armorHud.minDurabilityPercentage) || (maxDamage - damage <= config.armorHud.minDurabilityValue)) {
                             matrices.pushMatrix();
                             
                             float bobbing = getCycleProgress(i, config); // 0.0 to 1.0
                             // Simple bobbing offset
                             int bobOffset = (int)(Math.sin(bobbing * Math.PI * 2) * 2);
                             
                             int warnX = x + 6; // Center-ish
                             int warnY = y;
                             
                             if (config.armorHud.orientation == ModConfig.ArmorHud.Orientation.HORIZONTAL) {
                                 if (config.armorHud.anchor == ModConfig.ArmorHud.Anchor.TOP || config.armorHud.anchor == ModConfig.ArmorHud.Anchor.TOP_CENTER) {
                                     warnY = y + 16 + bobOffset;
                                 } else {
                                     warnY = y - 8 + bobOffset;
                                 }
                             } else {
                                 boolean onRight = (armorWidgetX > client.getWindow().getScaledWidth() / 2);
                                 if (onRight) {
                                     warnX = x - 8;
                                 } else {
                                     warnX = x + 16;
                                 }
                                 warnY = y + 4 + bobOffset;
                             }
 
                             
                             context.drawText(client.textRenderer, "!", warnX, warnY, 0xFFFF0000, true);
                             matrices.popMatrix();
                        }
                    }
                }
            }

            matrices.popMatrix();
        }
    }

    private static void drawSlots1(DrawContext context, int armorWidgetY, int armorWidgetX, int widgetWidth, int endPieceLength) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, armorWidgetX, armorWidgetY, 0, 0, 0, widgetWidth - endPieceLength, height, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, armorWidgetX + widgetWidth - endPieceLength, armorWidgetY, 0, 182 - endPieceLength, 0, endPieceLength, height, 256, 256);
    }

    private static void drawSlots2(DrawContext context, int armorWidgetY, int armorWidgetX, int widgetWidth, int endPieceLength) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, armorWidgetX, armorWidgetY, 0, 24, 23, endPieceLength, height, 256, 256);

        if (widgetWidth > endPieceLength * 2) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, armorWidgetX + endPieceLength, armorWidgetY, 0, endPieceLength, 0, widgetWidth - 2 * endPieceLength, height, 256, 256);
        }
        if (widgetWidth - endPieceLength < endPieceLength)
            endPieceLength = widgetWidth - endPieceLength;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, armorWidgetX + widgetWidth - endPieceLength, armorWidgetY, 0, 24 + width - endPieceLength, 23, endPieceLength, height, 256, 256);
    }

    private static float getCycleProgress(int index, ModConfig config) {
        if (cycleProgress == null) {
            cycleProgress = new float[]{random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()};
        }

        if (config.armorHud.warningIconBobbingIntervalMs == 0.0F) {
            return 0.5F;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        boolean isPreview = false; 

        if (!client.isPaused() || isPreview) {
            cycleProgress[index] += (measuredTime - lastMeasuredTime) / config.armorHud.warningIconBobbingIntervalMs;
            cycleProgress[index] %= 1.0F;

            if (cycleProgress[index] < 0 || Float.isNaN(cycleProgress[index]))
                cycleProgress[index] = random.nextFloat();
        }

        return cycleProgress[index];
    }
}
