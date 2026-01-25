package com.fronobear.pvpessentials.refined.crystaloptimizer.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record VersionPacket(int major, int minor, int patch, boolean snapshot) implements CustomPayload {

    public static final CustomPayload.Id<VersionPacket> ID = new CustomPayload.Id<>(CrystalOptimizerPackets.id("version"));

    public static final PacketCodec<PacketByteBuf, VersionPacket> CODEC = PacketCodec.of(
        (VersionPacket value, PacketByteBuf buf) -> {
            buf.writeVarInt(value.major);
            buf.writeVarInt(value.minor);
            buf.writeVarInt(value.patch);
            buf.writeBoolean(value.snapshot);
        },
        buf -> new VersionPacket(
            buf.readVarInt(),
            buf.readVarInt(),
            buf.readVarInt(),
            buf.readBoolean()
        )
    );

    @Override
    public CustomPayload.Id<VersionPacket> getId() {
        return ID;
    }
}
