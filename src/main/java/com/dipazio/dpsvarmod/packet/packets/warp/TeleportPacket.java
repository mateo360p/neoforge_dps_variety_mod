package com.dipazio.dpsvarmod.packet.packets.warp;

import com.dipazio.dpsvarmod.packet.IPacketExtension;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.packet.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

// I hate packets
public record TeleportPacket(double x, double y, double z) implements IPacketExtension {
    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportPacket> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::x,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::y,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::z,
                TeleportPacket::new
        );
    }

    @Override
    public void onCall(ServerPlayer player) {
        player.teleportTo(x, y, z);
    }

    @Override
    public String getPacketPath() {
        return PacketHandler.TELEPORT_WARP_PACKET_PATH;
    }
}