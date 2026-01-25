package com.fronobear.pvpessentials.refined.mixin.hurtcam;

import com.fronobear.pvpessentials.refined.config.ConfigManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"), method = "tiltViewWhenHurt")
    public float changeBobIntensity(float value) {
        if (ConfigManager.getConfig().hurtcam.enabled) {
            return (float) (ConfigManager.getConfig().hurtcam.multiplier * value);
        }
        return value;
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void disableHurtCam(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!ConfigManager.getConfig().hurtcam.enabled) ci.cancel();
    }

    @Redirect(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageTiltYaw()F"))
    public float changeHurtCamType(LivingEntity instance) {
        if (!ConfigManager.getConfig().hurtcam.enabled) return instance.getDamageTiltYaw();
        
        return switch (ConfigManager.getConfig().hurtcam.type) {
            case OLD -> 0;
            case YAW_BASED -> instance.getDamageTiltYaw();
        };
    }
}
