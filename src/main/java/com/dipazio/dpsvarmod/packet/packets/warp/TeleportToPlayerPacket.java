package com.dipazio.dpsvarmod.packet.packets.warp;

import com.dipazio.dpsvarmod.item.warp.BoundWarpPage;
import com.dipazio.dpsvarmod.item.warp.PlayerWarpPage;
import com.dipazio.dpsvarmod.item.warp.UnboundWarpPage;
import com.dipazio.dpsvarmod.packet.IPacketToServer;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.packet.StreamCodecs;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

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
        if (player.getStringUUID().equals(toPlayerUUID)) return;

        Vec3 oldPos = player.position();
        ServerPlayer toPlayer;

        try {
            toPlayer = player.getServer().getPlayerList().getPlayer(UUID.fromString(toPlayerUUID));
        } catch(Exception ignored) {
            return;
        }

        if (toPlayer == null) return;

        ResourceKey<Level> dimension = toPlayer.level().dimension();

        double x = toPlayer.getX();
        double y = toPlayer.getY();
        double z = toPlayer.getZ();

        // Leaves
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

        double traveledDistance = (!oldLevel.dimension().equals(dimension)) ?
                Double.POSITIVE_INFINITY :
                Math.sqrt(sqr(x - oldPos.x) + sqr(y - oldPos.y) + sqr(z - oldPos.z));

        // Teleport
        PlayerWarpPage.teleportPlayer(player, toPlayerUUID);
        player.causeFoodExhaustion((float) BoundWarpPage.calcWarpHungerExhaustion(traveledDistance, player.level().getDifficulty()));

        // Arrives
        Level newLevel = player.level();
        if (!newLevel.isClientSide()) {
            newLevel.getServer().execute(() -> {
                ((ServerLevel) newLevel).sendParticles(
                        ParticleTypes.LARGE_SMOKE,
                        true,
                        true,
                        x,
                        y + 1.0,
                        z,
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
                    x + 0.5,
                    y + 1.0,
                    z + 0.5,
                    120,
                    0.0,
                    1.0,
                    0.0,
                    1.0
            );
        }
    }

    // XD
    private double sqr(double x) {
        return x * x;
    }

    @Override
    public String getPacketPath() {
        return PacketHandler.TELEPORT_PLAYER_WARP_PACKET_PATH;
    }
}