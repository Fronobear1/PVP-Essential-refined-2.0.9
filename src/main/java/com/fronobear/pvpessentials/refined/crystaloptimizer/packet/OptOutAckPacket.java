package com.fronobear.pvpessentials.refined.crystaloptimizer.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record OptOutAckPacket() implements CustomPayload {
    public static final CustomPayload.Id<OptOutAckPacket> ID = new CustomPayload.Id<>(CrystalOptimizerPackets.id("opt_out_ack"));
    public static final PacketCodec<PacketByteBuf, OptOutAckPacket> CODEC = PacketCodec.unit(new OptOutAckPacket());

    @Override
    public CustomPayload.Id<OptOutAckPacket> getId() {
        return ID;
    }
}
