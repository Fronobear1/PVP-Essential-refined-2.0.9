package com.fronobear.pvpessentials.refined.hud;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.render.RenderTickCounter;

public class CombatItemsHud {

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.player.isSpectator()) return;

        // Fetch latest config
        ModConfig config = ConfigManager.getConfig();

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        
        // Position: Mid-left (anchored to screen center-left)
        int x = 5;
        int y = height / 2 - 30; 

        // Apply Scale
        float scale = 1.0f;
        
        // Apply Opacity
        float alpha = 1.0f;

        int yOffset = 0;
        int spacing = (int)(24 * scale); 

        // Render Slot 1
        yOffset = renderSlot(context, client, config.hud.slot1, x, y, yOffset, spacing, scale, alpha);
        // Render Slot 2
        yOffset = renderSlot(context, client, config.hud.slot2, x, y, yOffset, spacing, scale, alpha);
        // Render Slot 3
        yOffset = renderSlot(context, client, config.hud.slot3, x, y, yOffset, spacing, scale, alpha);
        // Render Slot 4
        yOffset = renderSlot(context, client, config.hud.slot4, x, y, yOffset, spacing, scale, alpha);
    }

    private static int renderSlot(DrawContext context, MinecraftClient client, ModConfig.HUD.HudItem itemType, int x, int y, int yOffset, int spacing, float scale, float alpha) {
        if (itemType == ModConfig.HUD.HudItem.NONE) return yOffset;

        ItemStack displayStack = ItemStack.EMPTY;
        int count = 0;

        switch (itemType) {
            case ENDER_PEARL:
                displayStack = new ItemStack(Items.ENDER_PEARL);
                count = countItem(client.player.getInventory(), Items.ENDER_PEARL);
                break;
            case GOLDEN_APPLE:
                displayStack = new ItemStack(Items.GOLDEN_APPLE);
                count = countItem(client.player.getInventory(), Items.GOLDEN_APPLE) + countItem(client.player.getInventory(), Items.ENCHANTED_GOLDEN_APPLE);
                break;
            case ARROW:
                displayStack = new ItemStack(Items.ARROW);
                count = countItem(client.player.getInventory(), Items.ARROW) + countItem(client.player.getInventory(), Items.SPECTRAL_ARROW) + countItem(client.player.getInventory(), Items.TIPPED_ARROW);
                break;
            case TOTEM:
                displayStack = new ItemStack(Items.TOTEM_OF_UNDYING);
                count = countItem(client.player.getInventory(), Items.TOTEM_OF_UNDYING);
                break;
            case CRYSTAL:
                displayStack = new ItemStack(Items.END_CRYSTAL);
                count = countItem(client.player.getInventory(), Items.END_CRYSTAL);
                break;
            case OBSIDIAN:
                displayStack = new ItemStack(Items.OBSIDIAN);
                count = countItem(client.player.getInventory(), Items.OBSIDIAN);
                break;
            case ANCHOR:
                displayStack = new ItemStack(Items.RESPAWN_ANCHOR);
                count = countItem(client.player.getInventory(), Items.RESPAWN_ANCHOR);
                break;
            case GLOWSTONE:
                displayStack = new ItemStack(Items.GLOWSTONE);
                count = countItem(client.player.getInventory(), Items.GLOWSTONE);
                break;
            case EXP_BOTTLE:
                displayStack = new ItemStack(Items.EXPERIENCE_BOTTLE);
                count = countItem(client.player.getInventory(), Items.EXPERIENCE_BOTTLE);
                break;
            default:
                return yOffset;
        }

        renderScaledItem(context, client, displayStack, count, x, y + yOffset, scale, alpha);
        return yOffset + spacing;
    }

    private static void renderScaledItem(DrawContext context, MinecraftClient client, ItemStack displayStack, int count, int x, int y, float scale, float alpha) {
        var matrices = context.getMatrices();
        
        matrices.pushMatrix();
        // Translate to the element's position
        matrices.translate((float)x, (float)y);
        // Scale locally
        matrices.scale(scale, scale);
        
        // Draw at (0,0) relative to the pushed matrix
        context.drawItem(displayStack, 0, 0);
        
        // Draw count
        String text = String.valueOf(count);
        int alphaInt = (int)(alpha * 255) << 24;
        int color = 0x00FFFFFF | alphaInt; 
        
        context.drawTextWithShadow(client.textRenderer, text, 18, 5, color);
        
        matrices.popMatrix();
    }

    private static int countItem(PlayerInventory inventory, Item item) {
        int count = 0;
        // Count main inventory
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }
}
