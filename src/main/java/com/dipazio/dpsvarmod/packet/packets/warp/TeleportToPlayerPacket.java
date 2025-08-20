package com.dipazio.dpsvarmod.packet.packets.warp;

import com.dipazio.dpsvarmod.item.warp.PlayerWarpPage;
import com.dipazio.dpsvarmod.packet.IPacketToServer;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.packet.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

// I hate packets
public record TeleportToPlayerPacket(String toPlayerUUID) implements IPacketToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportToPlayerPacket> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                StreamCodecs.STRING_CODEC, TeleportToPlayerPacket::toPlayerUUID,
                TeleportToPlayerPacket::new
        );
    }

    @Override
    public void onCall(ServerPlayer player) {
        PlayerWarpPage.teleportPlayer(player, toPlayerUUID);
    }

    @Override
    public String getPacketPath() {
        return PacketHandler.TELEPORT_PLAYER_WARP_PACKET_PATH;
    }
}