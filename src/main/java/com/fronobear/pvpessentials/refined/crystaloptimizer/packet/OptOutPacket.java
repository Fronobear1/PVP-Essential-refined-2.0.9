package com.fronobear.pvpessentials.refined.crystaloptimizer.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record OptOutPacket() implements CustomPayload {
    public static final CustomPayload.Id<OptOutPacket> ID = new CustomPayload.Id<>(CrystalOptimizerPackets.id("opt_out"));
    public static final PacketCodec<PacketByteBuf, OptOutPacket> CODEC = PacketCodec.unit(new OptOutPacket());

    @Override
    public CustomPayload.Id<OptOutPacket> getId() {
        return ID;
    }
}
