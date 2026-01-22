package com.fronobear.pvpessentials.refined.hud;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import com.fronobear.pvpessentials.refined.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.entity.player.PlayerInventory;

import net.minecraft.client.render.RenderTickCounter;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.entity.player.PlayerInventory;

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

        // Prepare matrices
        var matrices = context.getMatrices();

        // Use manual positioning ("numbers") with local scaling for each element
        // This avoids global matrix scaling issues and satisfies "set that in numbers not scales"

        int yOffset = 0;
        int spacing = (int)(24 * scale); // Scale the spacing manually

        if (config.hud.showEnderPearls) {
            int count = countItem(client.player.getInventory(), Items.ENDER_PEARL);
            renderScaledItem(context, client, new ItemStack(Items.ENDER_PEARL), count, x, y + yOffset, scale, alpha);
            yOffset += spacing; 
        }

        if (config.hud.showArrows) {
            int count = countItem(client.player.getInventory(), Items.ARROW) + countItem(client.player.getInventory(), Items.SPECTRAL_ARROW) + countItem(client.player.getInventory(), Items.TIPPED_ARROW);
            renderScaledItem(context, client, new ItemStack(Items.ARROW), count, x, y + yOffset, scale, alpha);
            yOffset += spacing;
        }

        if (config.hud.showGoldenApples) {
            int count = countItem(client.player.getInventory(), Items.GOLDEN_APPLE) + countItem(client.player.getInventory(), Items.ENCHANTED_GOLDEN_APPLE);
            renderScaledItem(context, client, new ItemStack(Items.GOLDEN_APPLE), count, x, y + yOffset, scale, alpha);
            yOffset += spacing;
        }
    }

    private static void renderScaledItem(DrawContext context, MinecraftClient client, ItemStack displayStack, int count, int x, int y, float scale, float alpha) {
        var matrices = context.getMatrices();
        
        matrices.pushMatrix();
        // Translate to the element's position
        matrices.translate((float)x, (float)y);
        // Scale locally
        matrices.scale(scale, scale);
        
        // Draw at (0,0) relative to the pushed matrix
        // Note: Item opacity is currently not supported due to API limitations
        context.drawItem(displayStack, 0, 0);
        
        // Draw count
        String text = String.valueOf(count);
        int alphaInt = (int)(alpha * 255) << 24;
        int color = 0x00FFFFFF | alphaInt; 
        
        context.drawTextWithShadow(client.textRenderer, text, 18, 5, color);
        
        matrices.popMatrix();
    }

    private static int countItem(PlayerInventory inventory, net.minecraft.item.Item item) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }
}
