package com.dipazio.dpsvarmod.packet.packets.warp;

import com.dipazio.dpsvarmod.packet.IPacketToServer;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.packet.StreamCodecs;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

// I hate packets
public record TeleportPacket(double x, double y, double z) implements IPacketToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportPacket> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::x,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::y,
                StreamCodecs.DOUBLE_CODEC, TeleportPacket::z,
                TeleportPacket::new
        );
    }

    // I just remembered that the nether and the end EXISTS
    // So uhhh, I guess I have to add more vars now
    @Override
    public void onCall(ServerPlayer player) {
        Level oldLevel = player.level();
        if (!oldLevel.isClientSide()) {
            ((ServerLevel) oldLevel).sendParticles(
                    ParticleTypes.PORTAL,
                    true,
                    true,
                    player.getX() + 0.5,
                    player.getY() + 1.0,
                    player.getZ() + 0.5,
                    150,
                    0.5,
                    0.25,
                    0.5,
                    1.5
            );
        }

        player.teleportTo(x, y, z);

        Level newLevel = player.level();
        if (!newLevel.isClientSide()) {
            newLevel.getServer().execute(() -> {
                ((ServerLevel) newLevel).sendParticles(
                        ParticleTypes.LARGE_SMOKE,
                        true,
                        true,
                        this.x,
                        this.y + 1.0,
                        this.z,
                        225,
                        0.5,
                        1.0,
                        0.5,
                        0.1
                );
            });
            ((ServerLevel) newLevel).sendParticles(
                    ParticleTypes.PORTAL,
                    true,
                    true,
                    this.x + 0.5,
                    this.y + 1.0,
                    this.z + 0.5,
                    120,
                    0.0,
                    1.0,
                    0.0,
                    1.0
            );
        }
    }

    @Override
    public String getPacketPath() {
        return PacketHandler.TELEPORT_WARP_PACKET_PATH;
    }
}