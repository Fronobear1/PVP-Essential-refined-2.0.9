package com.fronobear.pvpessentials.refined.mixin.hurtcam;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"), index = 10)
    public boolean renderHearts(boolean blinking) {
        if (!ConfigManager.getConfig().hurtcam.enabled) return blinking;
        return blinking && ConfigManager.getConfig().hurtcam.heartBlink;
    }
}
