package com.fronobear.pvpessentials.refined.crystaloptimizer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public class InteractHandler implements PlayerInteractEntityC2SPacket.Handler {

    private final MinecraftClient client;

    public InteractHandler(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void interact(Hand hand) {
    }

    @Override
    public void interactAt(Hand hand, Vec3d pos) {
    }

    @Override
    public void attack() {
        HitResult hitResult = client.crosshairTarget;
        if (!(hitResult instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity entity = entityHitResult.getEntity();
        if (!(entity instanceof EndCrystalEntity crystal)) {
            return;
        }

        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }

        if (canDestroyCrystal(player)) {
            destroyCrystal(crystal);
        }
    }

    private boolean canDestroyCrystal(ClientPlayerEntity player) {
        // In 1.21, attributes include base, item modifiers, and status effect modifiers (Strength/Weakness).
        // If the total damage is > 0, the crystal will break.
        return player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE) > 0.0D;
    }

    private void destroyCrystal(Entity crystal) {
        crystal.remove(Entity.RemovalReason.KILLED);
        crystal.emitGameEvent(GameEvent.ENTITY_DIE);
    }
}
