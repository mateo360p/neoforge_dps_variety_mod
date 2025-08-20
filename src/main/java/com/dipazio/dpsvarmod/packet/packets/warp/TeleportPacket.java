package com.dipazio.dpsvarmod.packet.packets.warp;

import com.dipazio.dpsvarmod.item.warp.BoundWarpPage;
import com.dipazio.dpsvarmod.packet.IPacketToServer;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.packet.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public record TeleportPacket(double x, double y, double z, double xRot, double yRot, String dim) implements IPacketToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportPacket> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::x,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::y,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::z,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::xRot,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::yRot,
                StreamCodecs.STRING_CODEC, TeleportPacket::dim,
                TeleportPacket::new
        );
    }

    @Override
    public void onCall(ServerPlayer player) {
        BoundWarpPage.teleportPlayer(player, dim,
                x, y, z,
                xRot, yRot);
    }

    @Override
    public String getPacketPath() {
        return PacketHandler.TELEPORT_WARP_PACKET_PATH;
    }
}