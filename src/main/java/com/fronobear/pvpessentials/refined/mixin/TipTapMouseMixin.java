package com.fronobear.pvpessentials.refined.mixin;

import com.fronobear.pvpessentials.refined.tiptap.ClickCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class TipTapMouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    public void pvpessentials$onMouseButton(long window, MouseInput input, int action, CallbackInfo ci) {
        if (action != GLFW.GLFW_PRESS) {
            return;
        }
        if (MinecraftClient.getInstance().currentScreen != null) {
            return;
        }

        if (input.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            ClickCounter.registerRightClick();
        }
        if (input.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                ClickCounter.registerLeftClick();
            } else if (input.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                ClickCounter.registerRightClick();
            }
    }
}
